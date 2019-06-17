package Hardware;

import java.util.Vector;

import Software.Process;

/**
 * Responsável por armazenar e gerenciar os processos que não poderam ser alocados na memória.
 */
public class Disk extends Vector<Process> {

	/**
	 * Comunicação com a Memória.
	 */
	private Memory memory;

	/**
	 * Grupo para alocação e manipulação dos processos no Disco.
	 */
	private static Vector<Process> storage;

	public Disk() {
		this.storage = new Vector<>();
	}

	public void setMemory(Memory memory) {
		this.memory = memory;
	}

	/**
	 * Retira o processo do disco e transfere para a memória. O processo a ser transferido é especificado pelo parâmetro de entrada ‘idProcess’ deste método.
	 */
	public void writeInMemory(int idProcess) {

		Process process = getProcess(idProcess);

		memory.addProcess(process);

		storage.remove(process);

	}

	/**
	 * Retorna o processo alocado na memória especificado pelo parâmetro de entrada 'idProcess'.
	 */
	public Process getProcess(int idProcess) {

		for (Process process : storage) {
			if (process.getId() == idProcess)
				return process;

		}

		return null;
	}

	/**
	 * Aloca novo processo no disco.
	 */
	@Override
	public boolean add(Process Process) {

		return storage.add(Process);
		
	}

}
