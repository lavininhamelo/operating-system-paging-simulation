package Hardware;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Vector;

import Software.Process;
import Software.Page;
import Software.Frame;

/**
 * Responsável por gerenciar os processos que serão utilizados posteriormente
 * pela CPU.
 */
public class Memory extends Vector<Frame> {

	/**
	 * Número de frames.
	 */
	private static int nframes;

	/**
	 * Grupo para alocação e manipulação dos frames na Memória.
	 */
	private static Vector<Frame> storage;

	/**
	 * Lista de frames livres.
	 * 
	 */
	private static Vector<Frame> freeFrames;
	/**
	 * Comunicação com o Disco.
	 */
	private Disk disk;

	public Memory(int nframes) {
		this.nframes = nframes;
		this.storage = new Vector<>();

		for (int i = 0; i < nframes; i++) {
			storage.add(new Frame(i));
		}

		this.freeFrames = this.storage;
	}

	public void setDisk(Disk disk) {
		this.disk = disk;
	}

	/**
	 * Retira o frame da memória e transfere para o disco.
	 */
	public void writeInDisk(Page page) {
		disk.add(page);
	}

	/**
	 * Aloca nova página de processo na memória.
	 */
	public void addPageProcess(Page page) {

		// boolean allocatedProcess = false;

		// while (!allocatedProcess) {

		// Vector<Partition> emptyPartitions = new Vector<>();

		// storage.forEach(partition -> {
		// if (partition.getProcess() == null)
		// if (partition.getSize() >= process.getTp())
		// emptyPartitions.add(partition);
		// });

		// if (!emptyPartitions.isEmpty()) {

		// System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date())
		// + ". Swapper percebe que há espaço ao processo " + process.getId() + " na
		// memória");

		// Partition partition = emptyPartitions.firstElement();
		// int indexPartition = storage.indexOf(partition);

		// Partition emptySpace = new Partition(process.getTp() + 1,
		// partition.getEnd());
		// partition.setEnd(process.getTp());
		// partition.setProcess(process);

		// storage.remove(indexPartition);
		// storage.add(indexPartition, partition);
		// storage.add(indexPartition + 1, emptySpace);

		// allocatedProcess = true;
		// }

		// else {

		// System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date())
		// + ". Swapper percebe que não há espaço ao processo " + process.getId() + " na
		// memória");

		// Vector<Partition> allocatedPartitions = new Vector<>();

		// storage.forEach(partition -> {
		// if (partition.getProcess() != null) {
		// allocatedPartitions.add(partition);
		// }
		// });

		// Process processBackup = allocatedPartitions.firstElement().getProcess();

		// allocatedPartitions.firstElement().setProcess(null);

		// writeInDisk(processBackup);

		// System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()) + ".
		// Swapper retirou o processo "
		// + processBackup.getId() + " para liberar espaço na memória, e o enviou ao
		// disco");

		// for (int i = 0; i < storage.size() - 1; i++) {
		// if (storage.get(i).getProcess() == null && storage.get(i + 1).getProcess() ==
		// null) {
		// storage.get(i).setEnd(storage.get(i + 1).getEnd());
		// storage.remove(i + 1);
		// break;
		// }

		// }
		// }

		// }

	}

	/**
	 * Retorna a página alocado na memória especificada pelo parâmetro de entrada
	 * 'idPage'.
	 */
	public Page getProcessPage(int idPage) {
		for (Frame frame : storage) {
			if (frame.getPage().getId() == idPage)
				return frame.getPage();
		}

		return null;
	}

	/**
	 * Verifica se a página está alocada na memória.
	 */
	public boolean contains(Page page) {
		for (Frame frame : storage) {
			if (frame.getPage() != null)
				if (frame.getPage().getId() == page.getId())
					return true;
		}

		return false;
	}
}
