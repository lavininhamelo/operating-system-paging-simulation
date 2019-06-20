package Software.Buffers;

import java.util.Vector;

import Software.Process;

/**
 * Responsável pelo armazenamento da fila de entrada dos processos.
 */
public class InputBuffer extends Vector<Process> {

	/**
	 * Grupo para alocação dos processos prontos para serem alocados na memória.
	 */
	private static Vector<Process> inputBuffer;

	public InputBuffer() {
		inputBuffer = new Vector<>();
	}

	public void addPageProcess(Process process) {
		inputBuffer.add(process);
	}

	public Vector<Process> getAll() {
		return inputBuffer;
	}

	@Override
	public synchronized int size() {
		return inputBuffer.size();
	}

	@Override
	public synchronized Process firstElement() {
		return inputBuffer.firstElement();
	}

	@Override
	public boolean remove(Object o) {
		return inputBuffer.remove(o);
	}

}
