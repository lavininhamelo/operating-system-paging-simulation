package Software;

import Hardware.Memory;
import Hardware.Disk;

import Software.Notification.MonitorDispatcherPager;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Responsável por alocar as páginas de processos na memória.
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
	 * Página alocada pelo Dispatcher para ser enviada para memória.
	 */
	private Page page;
	/**
	 * Página alocada pelo Dispacher para ser enviada para memória.
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

	public void setPage(Page page) {
		this.page = page;
	}

	// Pegar página do processo
	public void setRequestForTransferOfPage(Page requestForTransferOfPage) {
		this.requestForTransferOfPage = requestForTransferOfPage;
	}

	/**
	 * Aloca página na memória.
	 */
	public synchronized void putProcessInToMemory(Page page) {
		memory.addPageProcess(page);
	}

	/**
	 * Solicita que disco transfira processo para a memória.
	 */
	public void transferToMemory() {

		disk.writeInMemory(requestForTransferOfPage.getId());

		System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()) + ".	Pager traz a página do processo "
				+ requestForTransferOfPage.getId() + " do disco e o coloca na memória");
	}

	/**
	 * Notifica ao dispatcher que processo foi transferido do disco para a memória.
	 */
	private void notifyDispatcher() {
		synchronized (monitorDispatcherPager) {
			monitorDispatcherPager.notify();
		}

		System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date())
				+ ".	Pager avisa o Despachante que o a página do processo " + requestForTransferOfPage.getId()
				+ " está na memória");
	}

	@Override
	public void run() {
		while (!statusRoundRobinScheduler.equals("concluded")) {
			if (requestForTransferOfPage != null) {
				transferToMemory();
				notifyDispatcher();
				requestForTransferOfPage = null;

			}

			if (page != null) {
				putProcessInToMemory(page);
				page = null;
			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				throw new IllegalStateException();
			}

		}
	}

}
