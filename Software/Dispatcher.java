package Software;

import Software.Schedulers.RoundRobinScheduler;
import Hardware.CPU;
import Hardware.Memory;

import Software.Notification.MonitorDispatcherPager;
import Software.Notification.MonitorRoundRobinDispatcher;
import Software.Notification.MonitorDispatcherTimer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Responsável por alocar o processo selecionado pelo scheduler na CPU.
 */
public class Dispatcher extends Thread {

	/**
	 * Comunicação com o RoundRobinScheduler.
	 */
	private RoundRobinScheduler roundRobinScheduler;

	private Random ger = new Random();

	/**
	 * Comunicação com o Pager.
	 */
	private Pager pager;

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
	 * Página do processo escolhida.
	 */
	private Page page;

	/**
	 * Seed para a selecionar a página do processo.
	 */
	private int seed;

	/**
	 * Monitor entre RoundRobinScheduler e Dispatcher.
	 */
	private MonitorRoundRobinDispatcher monitorRoundRobin;

	/**
	 * Monitor entre Dispacther e Timer.
	 */
	private MonitorDispatcherTimer monitorTimer;

	/**
	 * Monitor entre Dispatcher e Pager.
	 */
	private MonitorDispatcherPager monitorPager;

	/**
	 * Monitoramento de status do Criador de Processos.
	 */
	private String statusRoundRobinScheduler;

	public Dispatcher(int seed, RoundRobinScheduler roundRobinScheduler, MonitorRoundRobinDispatcher monitorRoundRobin,
			MonitorDispatcherTimer monitorTimer, MonitorDispatcherPager monitorPager) {
		this.seed = seed;
		this.roundRobinScheduler = roundRobinScheduler;
		this.monitorRoundRobin = monitorRoundRobin;
		this.monitorTimer = monitorTimer;
		this.monitorPager = monitorPager;
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

	public void setPager(Pager pager) {
		this.pager = pager;
	}

	public void setStatusRoundRobinScheduler(String statusRoundRobinScheduler) {
		this.statusRoundRobinScheduler = statusRoundRobinScheduler;
	}

	/**
	 * Verifica se o 'idProcess' correspondente a algum processo alocado na memória.
	 * 
	 * Fluxo condicional (processo na memória) :
	 * 
	 * - (false) Se o processo nao estiver na memória, o Dispatcher solicita ao
	 * Pager a transferência do processo do disco para a memória -
	 * requestTransferToMenory(idProcess);
	 * 
	 * - (true) Se o processo estiver na memória, o Dispatcher libera a CPU -
	 * freeUpCPU().
	 *
	 */

	public void dispatchProcess(Page page) {

		// Verifica se a página do processo está na Pagetabe.

		if (process.isPageValid(page)) {

			System.out.println(
					new SimpleDateFormat("HH:mm:ss").format(new Date()) + ".	Despachante percebe que a página "
							+ page.getId() + " do processo " + process.getId() + " está na memória");

			freeUpCPU();

			System.out.println(
					new SimpleDateFormat("HH:mm:ss").format(new Date()) + ".	Despachante reiniciou o Timer com "
							+ roundRobinScheduler.getTq() + " e liberou a CPU ao processo " + process.getId());

		} else {
			System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date())
					+ ".	Despachante percebe que a página " + page.getId() + " do processo " + process.getId()
					+ " não está na memória e solicita que o Pager traga " + page.getId() + " à memória");

			requestTransferToMemory(process, page);

		}
	}

	/**
	 * Solicita ao Pager a transferência da página do disco para a memória.
	 */
	public void requestTransferToMemory(Process process, Page page) {

		pager.setRequestForTransferOfProcess(process, page);

		synchronized (monitorPager) {
			try {
				monitorPager.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date())
				+ ".	Despachante é avisado pelo Pager que a página " + page.getId() + " do processo "
				+ process.getId() + " esta no quadro " + process.getIdFrame(page.getId()));

		dispatchProcess(page);

	}

	/**
	 * Calcula uma página aleatória do processo.
	 */
	public int chooseProcessPage(Process process) {
		return ger.nextInt(seed);
	}

	/**
	 * Reinicia o Timer e libera a CPU passando a identificação do próximo processo
	 * à CPU.
	 */
	private void freeUpCPU() {
		timer.setTimer(roundRobinScheduler.getTq());
		cPU.setProcess(process);

	}

	/**
	 * Notifica o timer que novo processo foi alocado na CPU.
	 */
	private void notifyTimer() {

		synchronized (monitorTimer) {

			monitorTimer.notify();
		}
	}

	@Override
	public void run() {
		while (!statusRoundRobinScheduler.equals("concluded")) {

			synchronized (monitorRoundRobin) {

				try {
					monitorRoundRobin.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			if (process != null) {
				this.page = process.getPageById(this.chooseProcessPage(process));
				dispatchProcess(this.page);
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
		pager.setStatusRoundRobinScheduler("concluded");
		notifyTimer();
	}
}
