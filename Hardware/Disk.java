package Hardware;

import java.util.Vector;
import java.util.Date;
import java.text.SimpleDateFormat;

import Software.Page;
import Software.PageTable;
import Software.Process;
import Software.Buffers.ReadyBuffer;

/**
 * Responsável por armazenar e gerenciar os processos que não poderam ser
 * alocados na memória.
 */
public class Disk extends Vector<Process> {

	/**
	 * Comunicação com a Memória.
	 */
	private Memory memory;

	private static ReadyBuffer readyBuffer;

	/**
	 * Grupo para alocação e manipulação dos processos no Disco.
	 */
	private static Vector<Process> storage;

	public Disk() {
		storage = new Vector<Process>();
		readyBuffer = new ReadyBuffer();
	}

	public void setMemory(Memory memory) {
		this.memory = memory;
	}

	public void setReadyBuffer(ReadyBuffer readyBuffer) {
		this.readyBuffer = readyBuffer;
	}

	/**
	 * Retira o processo do disco e transfere para a memória. O processo a ser
	 * transferido é especificado pelo parâmetro de entrada ‘idProcess’ deste
	 * método.
	 */
	public void writeInMemory(Process process, Page page) {

		// if (process != null) {
		memory.addProcess(process, page);
		storage.remove(process);
		// }

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

	public void printNotFinished() {
		for (Process process : readyBuffer.getAll()) {
			if (process.getTb() > 0)
				System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()) + ".	O processo "
						+ process.getId() + " não foi terminado.");
		}

		for (Process process : storage) {
			if (process.getTb() > 0)
				System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()) + ".	O processo "
						+ process.getId() + " não foi terminado.");
		}
	} 

	/**
	 * Aloca novo processo no disco.
	 */
	@Override
	public boolean add(Process process) {
		return storage.add(process);
	}

	public void setInvalidBit(int idFrame, Page page) {
		for (Process p : storage) {
			for (PageTable a : p.getPageTable()) {
				if (a.getFrameId() == idFrame&&page.getIdProcess()!=a.getPage().getIdProcess()) {
					a.setValidInvalidBit(false);
				}
			}

		}
		for (Process p : readyBuffer) {
			for (PageTable a : p.getPageTable()) {
				if (a.getFrameId() == idFrame&&page.getIdProcess()!=a.getPage().getIdProcess()) {
					a.setValidInvalidBit(false);
				}
			}

		}
	}
}
