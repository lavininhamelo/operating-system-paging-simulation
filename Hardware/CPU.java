package Hardware;

import Software.Page;

/**
 * Responsável pela execução das páginas.
 */
public class CPU {

	private static int idPage;

	/**
	 * Página alocada na CPU.
	 */
	private static Page page;

	/**
	 * Comunicação com a Memória.
	 */
	private Memory memory;

	public CPU() {
	}

	public void setMemory(Memory memory) {
		this.memory = memory;
	}

	/**
	 * Recebe e busca o proximo processo a ser alocado na CPU.
	 */
	public void setProcess(Process paramProcess) {
		process = paramProcess;
	}

	/**
	 * Retorna o processo atualmente alocado na CPU.
	 */
	public Process getProcess() {
		return process;
	}

	/**
	 * Atualiza o tempo de processamento do processo alocado na CPU.
	 */
	private void updateProcessBurstTime() {
		process.setTb(process.getTb() - 1);
	}

	/**
	 * Realiza ciclo de processamento.
	 *
	 * Para cada ciclo de processamento deverá ser realizado os seguintes processos:
	 *
	 * - Retirar um segundo do time burst do processo.
	 */
	public void run() {
		updateProcessBurstTime();
	}

}
