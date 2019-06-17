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
import Software.Schedulers.FCFSScheduler;
import Software.Schedulers.RoundRobinScheduler;
import Software.Timer;
import Software.Swapper;

import Software.Notification.MonitorDispatcherSwapper;
import Software.Notification.MonitorDispatcherTimer;
import Software.Notification.MonitorFCFSSwapper;
import Software.Notification.MonitorRoundRobinDispatcher;
import Software.Notification.MonitorRoundRobinTimer;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;


public class Start {

	private static FCFSScheduler fCFSScheduler;
	private static RoundRobinScheduler roundRobinScheduler;

	private static Dispatcher dispatcher;
	private static ProcessCreator processCreator;
	private static Swapper swapper;
	private static Timer timer;

	private static MonitorDispatcherSwapper monitorDS;
	private static MonitorDispatcherTimer monitorDT;
	private static MonitorFCFSSwapper monitorFS;
	private static MonitorRoundRobinDispatcher monitorRD;
	private static MonitorRoundRobinTimer monitorRT;

	private static CPU cPU;
	private static Disk disk;
	private static Memory memory;

	public static void main(String[] args) {

		// --------------------- Capturando configuraçoes gerais ---------------------

		int tamanhoDaMemoria,
				numeroDeProcessos,
				timeQuantum;

		tamanhoDaMemoria = Integer.parseInt(args[0]);
		numeroDeProcessos = Integer.parseInt(args[1]);
		timeQuantum = Integer.parseInt(args[2]);


		// --------------------- Capturando processos ---------------------

		int identificacaoProcesso,
				tempoDoProcesso,
				tempoDeChegada,
				tempoDeBurst;

		int args_count = 3;
		Collection<Process> processos  = new Vector<>();
		while (numeroDeProcessos > 0) {

			identificacaoProcesso = Integer.parseInt(args[args_count++]);
			tempoDoProcesso = Integer.parseInt(args[args_count++]);
			tempoDeChegada = Integer.parseInt(args[args_count++]);
			tempoDeBurst = Integer.parseInt(args[args_count++]);

			Process processo = new Process(identificacaoProcesso, tempoDoProcesso, tempoDeChegada, tempoDeBurst);
			processos.add(processo);

			numeroDeProcessos--;

		}


		// --------------------- Inicializando monitores ---------------------

		monitorDS = new MonitorDispatcherSwapper();
		monitorDT = new Notification.MonitorDispatcherTimer();
		monitorFS = new MonitorFCFSSwapper();
		monitorRD = new MonitorRoundRobinDispatcher();
		monitorRT = new MonitorRoundRobinTimer();


		// --------------------- Inicializando componentes ---------------------

		cPU = new CPU();
		memory = new Memory(tamanhoDaMemoria);
		disk = new Disk();

		fCFSScheduler = new FCFSScheduler(monitorFS);
		roundRobinScheduler = new RoundRobinScheduler(timeQuantum, monitorRD, monitorRT);

		dispatcher = new Dispatcher(roundRobinScheduler, monitorRD, monitorDT, monitorDS);
		processCreator = new ProcessCreator(processos);
		swapper = new Swapper(fCFSScheduler, monitorFS, monitorDS);
		timer = new Timer(roundRobinScheduler, monitorRT, monitorDT);


		// --------------------- Configurando componentes ---------------------

		cPU.setMemory(memory);
		disk.setMemory(memory);
		memory.setDisk(disk);

		roundRobinScheduler.setDispatcher(dispatcher);
		roundRobinScheduler.setTimer(timer);

		fCFSScheduler.setSwapper(swapper);

		dispatcher.setcPU(cPU);
		dispatcher.setMemory(memory);
		dispatcher.setSwapper(swapper);
		dispatcher.setTimer(timer);

		swapper.setDisk(disk);
		swapper.setMemory(memory);

		timer.setcPU(cPU);
		timer.setDispatcher(dispatcher);


		// --------------------- Inicializando Threads ---------------------

		System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date())
				+ ".	Início da observação");

		dispatcher.start();
		fCFSScheduler.start();
		processCreator.start();
		roundRobinScheduler.start();
		swapper.start();
		timer.start();


		// --------------------- Esperando conclusao de Threads ---------------------

		try {

			processCreator.join();
			fCFSScheduler.setStatusProcessCreator("concluded");

			fCFSScheduler.join();
			roundRobinScheduler.setStatusFCFSScheduler("concluded");

			dispatcher.join();
			roundRobinScheduler.join();
			swapper.join();
			timer.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

		System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date())
				+ ".	Término da observação");

	}

}









