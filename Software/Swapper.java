package Software;

import Software.Schedulers.FCFSScheduler;
import Hardware.Memory;
import Hardware.Disk;

import Software.Notification.MonitorDispatcherSwapper;
import Software.Notification.MonitorFCFSSwapper;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Responsável por alocar os processos na memória.
 */
public class Swapper extends Thread implements Runnable {

	/**
	 * Comunicação com o FCFSScheduler.
	 */
	private static FCFSScheduler fCFSScheduler;

	/**
	 * Comunicação com o Dispatcher.
	 */
	private Dispatcher dispatcher;

	/**
	 * Comunicação com a Memória.
	 */
	private Memory memory;

	/**
	 * Comunicação com o Disco.
	 */
	private Disk disk;

	/**
	 * Processo alocado pelo FCFSScheduler para ser enviado para memoria.
	 */
	private Process process;

	/**
	 * Processo alocado pelo Dispatcher para ser transferido do disco para memoria.
	 */
	private Process requestForTransferOfProcess;

	/**
	 * Monitor entre FCFSScheduler e Swapper.
	 */
	private MonitorFCFSSwapper monitorFCFS;

	/**
	 * Monitor entre Dispatcher e Swapper.
	 */
	private MonitorDispatcherSwapper monitorDispatcherSwapper;

	/**
	 * Monitoramento de status do RoundRobinScheduler.
	 */
	private String statusRoundRobinScheduler;

	public Swapper(FCFSScheduler fCFSScheduler, MonitorFCFSSwapper monitorFCFS, MonitorDispatcherSwapper monitorDispatcherSwapper) {
		this.fCFSScheduler = fCFSScheduler;
		this.monitorFCFS = monitorFCFS;
		this.monitorDispatcherSwapper = monitorDispatcherSwapper;
		this.statusRoundRobinScheduler = "";
	}

	public void setDisk(Disk disk) {
		this.disk = disk;
	}

	public void setMemory(Memory memory) {
		this.memory = memory;
	}

	public void setStatusRoundRobinScheduler(String statusRoundRobinScheduler) {
		this.statusRoundRobinScheduler = statusRoundRobinScheduler;
	}

	public void setProcess(Process process) {
		this.process = process;
	}

	public void setRequestForTransferOfProcess(Process requestForTransferOfProcess) {
		this.requestForTransferOfProcess = requestForTransferOfProcess;
	}

	/**
	 * Aloca processo na memória.
	 */
	public synchronized void putProcessInToMemory(Process process) {

		memory.addProcess(process);

	}

	/**
	 * Solicita que disco transfira processo para a memória.
	 */
	public void transferToMemory() {

		disk.writeInMemory(requestForTransferOfProcess.getId());

		System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date())
				+ ".	Swapper traz processo " + requestForTransferOfProcess.getId()
				+ " do disco e o coloca na memória");
	}

	/**
	 * Solicita transferência de processos da memória para o disco até que haja espaço suficiente para alocação.
	 *
	private void freeUpMemorySpace(int tp) {

	}*/

	/**
	 * Notifica ao scheduler que processo foi alocado na memória.
	 */
	private void notifyScheduler() {

		synchronized(monitorFCFS) {

			monitorFCFS.notify();

		}

		System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date())
				+ ".	Swapper avisa o Escalonador FCFS de longo prazo que o processo " + process.getId()
				+ " está na memória");

	}


	/**
	 * Notifica ao dispatcher que processo foi transferido do disco para a memória.
	 */
	private void notifyDispatcher() {

		synchronized (monitorDispatcherSwapper) {

			monitorDispatcherSwapper.notify();

		}

		System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date())
				+ ".	Swapper avisa o Despachante que o processo " + requestForTransferOfProcess.getId()
				+ " está na memória");

	}

	@Override
	public void run() {

		while (!statusRoundRobinScheduler.equals("concluded")) {

			if (requestForTransferOfProcess != null) {

				transferToMemory();
				notifyDispatcher();
				requestForTransferOfProcess = null;

			}

			if (process != null) {

				putProcessInToMemory(process);
				notifyScheduler();
				process = null;

			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				throw new IllegalStateException();
			}

		}
	}

}
