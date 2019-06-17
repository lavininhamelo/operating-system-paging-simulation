package Software.Schedulers;

import Software.Buffers.ReadyBuffer;
import Software.Buffers.InputBuffer;

import Software.Swapper;
import Software.Process;

import Software.Notification.MonitorFCFSSwapper;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Responsável por realizar o escalonamento dos processos que serão adicionados
 * na memória.
 */
public class FCFSScheduler extends Thread implements Runnable {

	/**
	 * Comunicação com o ReadyBuffer.
	 */
	private static ReadyBuffer readyBuffer;

	/**
	 * Comunicação com o InputBuffer.
	 */
	private static InputBuffer inputBuffer;

	/**
	 * Comunicação com o Swapper.
	 */
	private static Swapper swapper;

	/**
	 * Monitor entre FCFSScheduler e Swapper.
	 */
	private MonitorFCFSSwapper monitorSwapper;

	/**
	 * Processo escolhido pelo FCFSScheduler para alocaçao na memoria.
	 */
	private Process process;

	/**
	 * Monitoramento de status do Criador de Processos.
	 */
	private String statusProcessCreator;

	public FCFSScheduler(MonitorFCFSSwapper monitorSwapper) {
		this.inputBuffer = new InputBuffer();
		this.readyBuffer = new ReadyBuffer();
		this.monitorSwapper = monitorSwapper;
		this.statusProcessCreator = "";
	}

	public void setSwapper(Swapper swapper) {
		this.swapper = swapper;
	}

	public void setStatusProcessCreator(String statusProcessCreator) {
		this.statusProcessCreator = statusProcessCreator;
	}

	/**
	 * Verifica se há processos na fila de entradas. Caso positivo, o método busca e remove o primeiro processo da fila, que será o processo selecionado para ser alocado na memória.
	 */
	public void chooseProcess() {

		if (inputBuffer.size() > 0) {

			process = inputBuffer.firstElement();
			inputBuffer.remove(process);
			System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date())
					+ ".	Escalonador FCFS de longo prazo escolheu e retirou o processo " + process.getId()
					+ " da fila de entrada");

		}
	}

	/**
	 * Envia o processo escolhido para o swapper solicitando que o processo seja alocado na memória.
	 */
	private synchronized void sendProcessToSwapper(Process process) {

		swapper.setProcess(process);
		System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date())
				+ ".	Escalonador FCFS de longo prazo solicitou que o Swapper traga " + process.getId()
				+ " à memória");

	}

	/**
	 * Coloca processo que foi selecionado pelo escalonador na fila de prontos.
	 */
	private void putProcessIntoReadyBuffer(Process process) {

		readyBuffer.add(process);

		System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date())
				+ ".	Escalonador FCFS de longo prazo colocou " + process.getId()
				+ " na fila de prontos");

	}

	/**
	 * Notifica Swapper que recebeu novo processo.
	 */
	private void notifySwapper() {

		synchronized(monitorSwapper) {

			monitorSwapper.notify();

		}

	}

	/**
	 * Realiza ciclo de tempo do escalonador.
	 *
	 * Para cada ciclo de tempo deverá ser realizado os seguintes processos:
	 *
	 * - Verificação da fila de entrada para alocação de novos processos.
	 *
	 */
	public void run() {

		while (!statusProcessCreator.equals("concluded") || !inputBuffer.isEmpty()) {

			chooseProcess();

			if (process != null) {

				sendProcessToSwapper(process);

				notifySwapper();

				synchronized(monitorSwapper) {

					try {
						monitorSwapper.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}

				putProcessIntoReadyBuffer(process);
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
