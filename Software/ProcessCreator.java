package Software;

import Software.Buffers.InputBuffer;
import Software.Buffers.ReadyBuffer;

import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.Collection;
import java.util.Vector;

/**
 * Responsável por construir processos e adicioná-los na fila de entrada.
 */
public class ProcessCreator extends Thread {

	/**
	 * Comunicação com o InputBuffer.
	 */
	private static InputBuffer inputBuffer;


	/**
	 * Comunicação com o ReadyBuffer.
	 */
	private static ReadyBuffer readyBuffer;

	/**
	 * Comunicação com o Process.
	 */
	private Process process;

	/**
	 * Grupo para alocação e manipulação dos processos.
	 */
	private Collection<Process> processosParaCriar;

	/**
	 * Grupo para alocação dos processos que ja foram criados.
	 */
	private Collection<Process> processosCriados;

	public ProcessCreator(Collection<Process> vector) {

		// inputBuffer = new InputBuffer();
		readyBuffer = new ReadyBuffer();
		this.processosParaCriar = vector;
		this.processosCriados = new Vector<>();

	}

	/**
	 * Inicializa novas intancias de objeto e adiciona no ReadyBuffer.
	 */
	public void createProcess(Process process) {
		readyBuffer.add(process);

		System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date())
				+ ".	Criador de processos criou o processo " + process.getId()
				+ " e o colocou na fila de prontos");

		processosCriados.add(process);
	}

	@Override
	public void run() {

		int segundos = 0;

		while (processosParaCriar.size() != processosCriados.size()) {

			int finalSegundos = segundos;
			processosParaCriar.forEach(process -> {

				if (process.getTc() == finalSegundos)
					createProcess(process);

			});

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				throw new IllegalStateException();
			}

			segundos++;

		}

	}

}
