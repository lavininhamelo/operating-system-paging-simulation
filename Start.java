/*

Alunos:

Bianca Ferreira da Frota Barreto - 2017.1906.053-3
Gabriel Cansanção da Silva Monteiro - 2017.1906.071-1
Lavinia da Silva Costa Melo - 2017.1906.056-8


*/


import Hardware.CPU;
import Hardware.Disk;
import Hardware.Memory;

import Software.Dispatcher;
import Software.Notification;
import Software.Process;
import Software.ProcessCreator;
import Software.Schedulers.RoundRobinScheduler;
import Software.Timer;
import Software.Pager;

import Software.Notification.MonitorDispatcherPager;
import Software.Notification.MonitorDispatcherTimer;
import Software.Notification.MonitorRoundRobinDispatcher;
import Software.Notification.MonitorRoundRobinTimer;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;


public class Start {

	private static RoundRobinScheduler roundRobinScheduler;

	private static Dispatcher dispatcher;
	private static ProcessCreator processCreator;
	private static Pager pager;
	private static Timer timer;

	private static MonitorDispatcherPager monitorDP;
	private static MonitorDispatcherTimer monitorDT;
	private static MonitorRoundRobinDispatcher monitorRD;
	private static MonitorRoundRobinTimer monitorRT;

	private static CPU cPU;
	private static Disk disk;
	private static Memory memory;

	public static void main(String[] args) {

		// --------------------- Capturando configuraçoes gerais ---------------------

		int seed,
				numeroDeFrames,
				numeroDeProcessos,
				timeQuantum;

		seed = Integer.parseInt(args[0]);
		numeroDeFrames = Integer.parseInt(args[1]);
		numeroDeProcessos = Integer.parseInt(args[2]);
		timeQuantum = Integer.parseInt(args[3]);


		// --------------------- Capturando processos ---------------------

		int identificacaoProcesso,
				numeroDePaginas,
				tempoDeChegada,
				tempoDeBurst;

		int args_count = 4;
		Collection<Process> processos  = new Vector<>();
		while (numeroDeProcessos > 0) {

			identificacaoProcesso = Integer.parseInt(args[args_count++]);
			numeroDePaginas = Integer.parseInt(args[args_count++]);
			tempoDeChegada = Integer.parseInt(args[args_count++]);
			tempoDeBurst = Integer.parseInt(args[args_count++]);

			Process processo = new Process(identificacaoProcesso, numeroDePaginas, tempoDeChegada, tempoDeBurst);
			processos.add(processo);

			numeroDeProcessos--;

		}


		// --------------------- Inicializando monitores ---------------------

		monitorDP = new MonitorDispatcherPager();
		monitorDT = new Notification.MonitorDispatcherTimer();
		monitorRD = new MonitorRoundRobinDispatcher();
		monitorRT = new MonitorRoundRobinTimer();


		// --------------------- Inicializando componentes ---------------------

		cPU = new CPU();
		memory = new Memory(seed);
		disk = new Disk();

		roundRobinScheduler = new RoundRobinScheduler(timeQuantum, monitorRD, monitorRT);

		dispatcher = new Dispatcher(seed, roundRobinScheduler, monitorRD, monitorDT, monitorDP);
		processCreator = new ProcessCreator(processos);
		pager = new Pager(monitorDP);
		timer = new Timer(roundRobinScheduler, monitorRT, monitorDT);


		// --------------------- Configurando componentes ---------------------

		cPU.setMemory(memory);
		disk.setMemory(memory);
		memory.setDisk(disk);

		roundRobinScheduler.setDispatcher(dispatcher);
		roundRobinScheduler.setTimer(timer);


		dispatcher.setcPU(cPU);
		dispatcher.setMemory(memory);
		dispatcher.setPager(pager);
		dispatcher.setTimer(timer);

		pager.setDisk(disk);
		pager.setMemory(memory);

		timer.setcPU(cPU);
		timer.setDispatcher(dispatcher);


		// --------------------- Inicializando Threads ---------------------

		System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date())
				+ ".	Início da observação");

		dispatcher.start();
		processCreator.start();
		roundRobinScheduler.start();
		pager.start();
		timer.start();


		// --------------------- Esperando conclusao de Threads ---------------------

		try {

			processCreator.join();
			roundRobinScheduler.setStatusProcess("concluded");

			dispatcher.join();
			roundRobinScheduler.join();
			pager.join();
			timer.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

		System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date())
				+ ".	Término da observação");

	}

}









