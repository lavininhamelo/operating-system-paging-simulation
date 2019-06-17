package Software;

import Software.Schedulers.RoundRobinScheduler;
import Hardware.CPU;
import Hardware.Memory;

import Software.Notification.MonitorDispatcherSwapper;
import Software.Notification.MonitorRoundRobinDispatcher;
import Software.Notification.MonitorDispatcherTimer;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Responsável por alocar o processo selecionado pelo scheduler na CPU.
 */
public class Dispatcher extends Thread {

	/**
	 * Comunicação com o RoundRobinScheduler.
	 */
	private RoundRobinScheduler roundRobinScheduler;

	/**
	 * Comunicação com o Swapper.
	 */
	private Swapper swapper;

	/**
	 * Comunicação com o Timer.
	 */
	private Timer timer;

	/**
	 * Comunicação com a CPU.
	 */
	private CPU cPU;

	/**
	 * Comunicação com a Memória.
	 */
	private Memory memory;

	/**
	 * Processo enviado pelo RoundRobinScheduler para despache.
	 */
	private Process process;

	/**
	 * Monitor entre RoundRobinScheduler e Dispatcher.
	 */
	private MonitorRoundRobinDispatcher monitorRoundRobin;

	/**
	 * Monitor entre Dispacther e Timer.
	 */
	private MonitorDispatcherTimer monitorTimer;

	/**
	 * Monitor entre Dispatcher e Swapper.
	 */
	private MonitorDispatcherSwapper monitorSwapper;

	/**
	 * Monitoramento de status do Criador de Processos.
	 */
	private String statusRoundRobinScheduler;

	public Dispatcher(RoundRobinScheduler roundRobinScheduler, MonitorRoundRobinDispatcher monitorRoundRobin, MonitorDispatcherTimer monitorTimer, MonitorDispatcherSwapper monitorSwapper) {
		
		this.roundRobinScheduler = roundRobinScheduler;
		this.monitorRoundRobin = monitorRoundRobin;
		this.monitorTimer = monitorTimer;
		this.monitorSwapper = monitorSwapper;
		this.statusRoundRobinScheduler = "";

	}

	public void setMemory(Memory memory) {
		this.memory = memory;
	}

	public void setcPU(CPU cPU) {
		this.cPU = cPU;
	}

	public void setProcess(Process process) {
		this.process = process;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	public void setSwapper(Swapper swapper) {
		this.swapper = swapper;
	}

	public void setStatusRoundRobinScheduler(String statusRoundRobinScheduler) {
		this.statusRoundRobinScheduler = statusRoundRobinScheduler;
	}

	/**
	 * Verifica se o 'idProcess' correspondente a algum processo alocado na memória.
	 * 
	 * Fluxo condicional (processo na memória) :
	 * 
	 * 	- (false) Se o processo nao estiver na memória, o Dispatcher solicita ao Swapper a transferência do processo do disco para a memória - requestTransferToMenory(idProcess);
	 * 
	 * 	- (true) Se o processo estiver na memória, o Dispatcher libera a CPU - freeUpCPU().
	 *
	 */
	public void dispatchProcess() {

		if (memory.contains(process)) {

			System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date())
					+ ".	Despachante percebe que o processo " + process.getId()
					+ " está na memória");

			freeUpCPU();

			System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date())
					+ ".	Despachante reiniciou o Timer com " + roundRobinScheduler.getTq()
					+ " e liberou a CPU ao processo " + process.getId());

		}

		else {

			System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date())
					+ ".	Despachante percebe que o processo " + process.getId()
					+ " está no disco e solicita que o Swapper traga " + process.getId() + " à memória");

			requestTransferToMemory(process);

		}

	}

	/**
	 * Solicita ao Swapper a transferência de processo do disco para a memória.
	 */
	public void requestTransferToMemory(Process process) {

		swapper.setRequestForTransferOfProcess(process);

		synchronized (monitorSwapper) {

			try {
				monitorSwapper.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

		System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date())
				+ ".	Despachante é avisado pelo Swapper que o processo " + process.getId()
				+ " está na memória");

		dispatchProcess();

	}

	/**
	 * Reinicia o Timer e libera a CPU passando a identificação do próximo processo à CPU.
	 */
	private void freeUpCPU() {

		timer.setTimer(roundRobinScheduler.getTq());
		cPU.setIdProcess(process.getId());

	}

	/**
	 * Notifica o timer que novo processo foi alocado na CPU.
	 */
	private void notifyTimer() {

		synchronized(monitorTimer) {

			monitorTimer.notify();

		}

	}

	@Override
	public void run() {

		while (!statusRoundRobinScheduler.equals("concluded")) {

			synchronized(monitorRoundRobin) {

				try {
					monitorRoundRobin.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}

			if (process != null) {

				dispatchProcess();
				process = null;

				notifyTimer();

			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				throw new IllegalStateException();
			}

		}

		timer.setStatusRoundRobinScheduler("concluded");
		swapper.setStatusRoundRobinScheduler("concluded");
		notifyTimer();

	}

}
