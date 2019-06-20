package Hardware;

import java.util.Vector;

import Software.Process;
import Software.Page;
import Software.Frame;

/**
 * Responsável por armazenar e gerenciar os processos que não poderam ser
 * alocados na memória.
 */
public class Disk extends Vector<Page> {

	/**
	 * Comunicação com a Memória.
	 */
	private Memory memory;

	/**
	 * Grupo para alocação e manipulação das páginas de processos no Disco.
	 */
	private static Vector<Page> storage;

	public Disk() {
		this.storage = new Vector<>();
	}

	public void setMemory(Memory memory) {
		this.memory = memory;
	}

	/**
	 * Retira o processo do disco e transfere para a memória. O processo a ser
	 * transferido é especificado pelo parâmetro de entrada ‘idProcess’ deste
	 * método.
	 */
	public void writeInMemory(int idProcess) {

		Process process = getProcess(idProcess);

		memory.addPageProcess(process);

		storage.remove(process);

	}

	/**
	 * Retorna o processo alocado na memória especificado pelo parâmetro de entrada
	 * 'idProcess'.
	 */
	public Process getProcess(int idProcess) {

		for (Process process : storage) {
			if (process.getId() == idProcess)
				return process;

		}

		return null;
	}

	/**
	 * Aloca uma nova página no disco.
	 */
	@Override
	public boolean add(Page page) {

		return storage.add(page);

	}

}
