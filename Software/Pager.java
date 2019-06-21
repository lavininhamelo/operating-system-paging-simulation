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
	 * Processo alocado pelo Dispatcher para ser transferido do disco para memoria.
	 */
	private Process requestForTransferOfProcess;
	/**
	 * página alocada pelo Dispacher para ser enviado para memoria.
	 */
	private Page requestForTransferOfPage;

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

	public void setRequestForTransferOfProcess(Process requestForTransferOfProcess, Page requestForTransferOfPage) {
		this.requestForTransferOfProcess = requestForTransferOfProcess;
		this.requestForTransferOfPage = requestForTransferOfPage;
	}

	/**
	 * Aloca processo na memória.
	 */
	public synchronized void putProcessInToMemory(Process process, Page page) {
		memory.addProcess(process, page);
	}

	/**
	 * Solicita que disco transfira processo para a memória.
	 */
	public void transferToMemory() {

		disk.writeInMemory(requestForTransferOfProcess, requestForTransferOfPage);

		System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()) + ".	Pager traz a página "
				+ requestForTransferOfPage.getId() + " do processo " + requestForTransferOfProcess.getId()
				+ " do disco e o coloca na memória");
	}

	/**
	 * Notifica ao dispatcher que processo foi transferido do disco para a memória.
	 */
	private void notifyDispatcher() {

		synchronized (monitorDispatcherPager) {

			monitorDispatcherPager.notify();

		}

		System.out.println(
				new SimpleDateFormat("HH:mm:ss").format(new Date()) + ".	Pager avisa o Despachante que a página "
						+ requestForTransferOfPage.getId() + " do processo " + requestForTransferOfProcess.getId()
						+ " está na memória\n" + memory.printFreeFrames() + disk.printNotFinished());

	}

	@Override
	public void run() {

		while (!statusRoundRobinScheduler.equals("concluded")) {

			if (requestForTransferOfProcess != null) {
				transferToMemory();
				notifyDispatcher();
				requestForTransferOfProcess = null;
			}

			// if (process != null) {

			// putProcessInToMemory(requestForTransferOfProcess, requestForTransferOfPage);
			// requestForTransferOfProcess = null;

			// }

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				throw new IllegalStateException();
			}

		}
	}

}
