
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
import java.util.Scanner;

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

	public static final String reset = "\u001B[0m";
	public static final String red = "\u001B[31m";
	public static final String blue = "\u001B[34m";
	public static final String green = "\u001B[32m";

	public static void main(String[] args) {

		// --------------------- Capturando configuraçoes gerais ---------------------

		Scanner in = new Scanner(System.in);
		int seed, numeroDeFrames, numeroDeProcessos, timeQuantum;

		seed = in.nextInt();
		numeroDeFrames = in.nextInt();
		numeroDeProcessos = in.nextInt();
		timeQuantum = in.nextInt();

		// --------------------- Capturando processos ---------------------

		int identificacaoProcesso, numeroDePaginas, tempoDeChegada, tempoDeBurst;

		Collection<Process> processos = new Vector<>();
		while (numeroDeProcessos > 0) {

			identificacaoProcesso = in.nextInt();
			numeroDePaginas = in.nextInt();
			tempoDeChegada = in.nextInt();
			tempoDeBurst = in.nextInt();

			Process processo = new Process(identificacaoProcesso, numeroDePaginas, tempoDeChegada, tempoDeBurst);
			processos.add(processo);

			numeroDeProcessos--;
		}

		in.close();

		// --------------------- Inicializando monitores ---------------------

		monitorDP = new MonitorDispatcherPager();
		monitorDT = new Notification.MonitorDispatcherTimer();
		monitorRD = new MonitorRoundRobinDispatcher();
		monitorRT = new MonitorRoundRobinTimer();

		// --------------------- Inicializando componentes ---------------------

		cPU = new CPU();
		memory = new Memory(numeroDeFrames);
		disk = new Disk();

		roundRobinScheduler = new RoundRobinScheduler(timeQuantum, monitorRD, monitorRT);

		dispatcher = new Dispatcher(seed, roundRobinScheduler, monitorRD, monitorDT, monitorDP);
		processCreator = new ProcessCreator(processos);
		pager = new Pager(monitorDP);
		timer = new Timer(roundRobinScheduler, monitorRT, monitorDT);

		// --------------------- Configurando componentes ---------------------

		processCreator.setDisk(disk);

		cPU.setMemory(memory);
		disk.setMemory(memory);
		memory.setDisk(disk);

		roundRobinScheduler.setDispatcher(dispatcher);
		roundRobinScheduler.setMemory(memory);
		roundRobinScheduler.setTimer(timer);
		roundRobinScheduler.setDisk(disk);

		dispatcher.setcPU(cPU);
		dispatcher.setMemory(memory);
		dispatcher.setPager(pager);
		dispatcher.setTimer(timer);

		pager.setDisk(disk);
		pager.setMemory(memory);

		timer.setcPU(cPU);
		timer.setDispatcher(dispatcher);

		// --------------------- Inicializando Threads ---------------------

		System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()) + ".	Início da observação");

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

		memory.printPageFaults();
		System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()) + ".	Término da observação");
	}
}
