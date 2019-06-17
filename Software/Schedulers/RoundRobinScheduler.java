package Software.Schedulers;

import Software.Buffers.ReadyBuffer;
import Software.Dispatcher;
import Software.Process;
import Software.Timer;

import Software.Notification.MonitorRoundRobinDispatcher;
import Software.Notification.MonitorRoundRobinTimer;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Responsávelo por realizar o escalonamento dos processos que serão alocados na CPU.
 */
public class RoundRobinScheduler extends Thread implements Runnable{

	/**
	 * Time quantum.
	 */
	private static int tq;

	/**
	 * Comunicação com o ReadyBuffer.
	 */
	private static ReadyBuffer readyBuffer;

	/**
	 * Comunicação com o Dispatcher.
	 */
	private Dispatcher dispatcher;

	/**
	 * Comunicação com o Timer.
	 */
	private Timer timer;

	/**
	 * Processo escolhido pelo RoundRobin para alocaçao na CPU
	 */
	private Process process;

	/**
	 * Monitor entre RoundRobin e Dispatcher.
	 */
	private MonitorRoundRobinDispatcher monitorDispatcher;

	/**
	 * Monitor entre RoundRobinScheduler e Timer.
	 */
	private MonitorRoundRobinTimer monitorTimer;

	/**
	 * Monitoramento de status do FCFSCheduler.
	 */
	private String statusProcess;

	public RoundRobinScheduler(int tq, MonitorRoundRobinDispatcher monitorDispatcher, MonitorRoundRobinTimer monitorTimer) {
		this.tq = tq;
		readyBuffer = new ReadyBuffer();
		this.monitorDispatcher = monitorDispatcher;
		this.monitorTimer = monitorTimer;
		statusProcess = "";
	}
	
	public void setDispatcher(Dispatcher dispatcher){
		this.dispatcher = dispatcher;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	public int getTq() {
		return tq;
	}

	public void setProcess(Process process) {
		this.process = process;
	}

	public Process getProcess() {
		return process;
	}

	public void setStatusProcess(String statusProcess) {
		this.statusProcess = statusProcess;
	}

	/**
	 * Verifica se há processos na fila de prontos. Caso positivo, o método busca e remove o processo que possui menor CPU burst time da fila, que será o processo selecionado para ser alocado na CPU.
	 */
	public void chooseProcess() {

		if (readyBuffer.size() > 0) {

			process = readyBuffer.firstElement();

			readyBuffer.remove(process);
			System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date())
					+ ".	Escalonador RoundRobin escolheu e retirou o processo " + process.getId()
					+ " da fila de prontos");

		}

	}

	/**
	 * Envia o processo escolhido para o dispatcher solicitando que o processo seja alocado na CPU.
	 */
	private void sendProcessToDispatcher() {

		dispatcher.setProcess(process);

	}

	/**
	 *  Verifica se o processo ainda possui Time CPU Burst e caso positivo retorna o processo para a fila de prontos.
	 */
	private void returnProcessToReadyBuffer() {

		if (process.getTb() > 0)

			readyBuffer.add(process);

		else

			System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date())
					+ ".	Processo " + process.getId()
					+ " terminou sua execução");

		process = null;

	}

	/**
	 * Notifica ao dispatcher que recebeu novo processo.
	 */
	private void notifyDispatcher() {

		synchronized (monitorDispatcher) {

			monitorDispatcher.notify();

		}

	}

	/**
	 * Realiza ciclo de tempo do escalonador.
	 *
	 * Para cada ciclo de tempo deverá ser realizado os seguintes processos:
	 *
	 * 	- Verificação da fila de prontos para alocação de novos processos na CPU.
	 *
	 */
	public void run() {

		while (!statusProcess.equals("concluded") || readyBuffer.size() > 0 || !(process == null)) {

			chooseProcess();

			if (process != null) {

				sendProcessToDispatcher();

				System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date())
						+ ".	Escalonador Round-Robin de CPU escolheu o processo " + process.getId()
						+ ", retirou-o da fila de prontos e o encaminhou ao Despachante");

				notifyDispatcher();

				synchronized (monitorTimer) {

					try {
						monitorTimer.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}

				returnProcessToReadyBuffer();

			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				throw new IllegalStateException();
			}

		}

		dispatcher.setStatusRoundRobinScheduler("concluded");
		notifyDispatcher();

	}
}
