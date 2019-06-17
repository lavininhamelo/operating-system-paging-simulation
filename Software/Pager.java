package Software;

import Hardware.Memory;
import Hardware.Disk;

import Software.Notification.MonitorDispatcherPager;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Responsável por alocar os processos na memória.
 */
public class Pager extends Thread implements Runnable {

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
	 * Monitor entre Dispatcher e Swapper.
	 */
	private MonitorDispatcherPager monitorDispatcherPager;

	/**
	 * Monitoramento de status do RoundRobinScheduler.
	 */
	private String statusRoundRobinScheduler;

	public Pager(MonitorDispatcherPager monitorDispatcherPager) {
		this.monitorDispatcherPager = monitorDispatcherPager;
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
				+ ".	Pager traz processo " + requestForTransferOfProcess.getId()
				+ " do disco e o coloca na memória");
	}

	/**
	 * Notifica ao dispatcher que processo foi transferido do disco para a memória.
	 */
	private void notifyDispatcher() {

		synchronized (monitorDispatcherPager) {

			monitorDispatcherPager.notify();

		}

		System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date())
				+ ".	Pager avisa o Despachante que o processo " + requestForTransferOfProcess.getId()
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
