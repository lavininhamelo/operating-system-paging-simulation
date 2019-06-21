package Software.Buffers;

import java.util.Vector;

import Software.Process;

/**
 * Responsável pela fila de processos prontos.
 */
public class ReadyBuffer extends Vector<Process> {

	/**
	 * Grupo para alocação dos processos prontos para serem alocados na CPU.
	 */
	private static Vector<Process> readyBuffer;
 
	public ReadyBuffer() {
		readyBuffer = new Vector<>();
	}

	@Override
	public boolean add(Process process) {
		return readyBuffer.add(process);
	}

	public Vector<Process> getAll() {
		return readyBuffer;
	}

	@Override
	public synchronized int size() {
		return readyBuffer.size();
	}

	@Override
	public synchronized Process firstElement() {
		return readyBuffer.firstElement();
	}

	@Override
	public boolean remove(Object o) {
		return readyBuffer.remove(o);
	}



}
