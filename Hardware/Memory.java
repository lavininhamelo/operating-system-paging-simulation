package Hardware;

import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.Vector;

import Software.Process;

/**
 * Responsável por gerenciar os processos que serão utilizados posteriormente pela CPU.
 */
public class Memory extends Vector<Process>{

	/**
	 * Tamanho da memória.
	 */
	private static int tmp;

	/**
	 * Tamanho ocupado pelos processos.
	 * 
	 */
	private static int tmpAllocated;

	/**
	 * Comunicação com a CPU.
	 */
	private CPU cPU;

	/**
	 * Comunicação com o Disco.
	 */
	private Disk disk;

	/**
	 * Grupo para alocação e manipulação dos processos na Memória.
	 */
	private static Vector<Partition> storage;

	public Memory(int tmp) {
		this.tmp = tmp;
		this.storage = new Vector<>();
		this.storage.add(new Partition(1, tmp));
	}

	public void setDisk(Disk disk) {
		this.disk = disk;
	}

	/**
	 * Retira o processo da memória e transfere para o disco.
	 */
	public void writeInDisk(Process process) {
		disk.add(process);
	}

	/**
	 * Aloca novo processo na memória.
	 */
	public void addProcess(Process process) {

		boolean allocatedProcess = false;

		while(!allocatedProcess) {

			Vector<Partition> emptyPartitions = new Vector<>();

			storage.forEach(partition -> {
				if (partition.getProcess() == null)
					if (partition.getSize() >= process.getTp())
						emptyPartitions.add(partition);
			 });

		 	if (!emptyPartitions.isEmpty()) {

		 		System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date())
						+ ".	Swapper percebe que há espaço ao processo " + process.getId()
						+ " na memória");

				Partition partition = emptyPartitions.firstElement();
				int indexPartition = storage.indexOf(partition);

				Partition emptySpace = new Partition(process.getTp() + 1 , partition.getEnd());
				partition.setEnd(process.getTp());
				partition.setProcess(process);

				storage.remove(indexPartition);
				storage.add(indexPartition, partition);
				storage.add(indexPartition+1, emptySpace);

				allocatedProcess = true;
			 }

		 	else {

		 		System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date())
						+ ".	Swapper percebe que não há espaço ao processo " + process.getId()
						+ " na memória");

				Vector<Partition> allocatedPartitions = new Vector<>();

				storage.forEach(partition -> {
					if (partition.getProcess() != null) {
						allocatedPartitions.add(partition);
					}
				});

				Process processBackup = allocatedPartitions.firstElement().getProcess();

				allocatedPartitions.firstElement().setProcess(null);

				writeInDisk(processBackup);

				System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date())
						+ ".	Swapper retirou o processo " + processBackup.getId()
						+ " para liberar espaço na memória, e o enviou ao disco");

				for (int i = 0; i < storage.size() - 1; i++) {
					if (storage.get(i).getProcess() == null && storage.get(i + 1).getProcess() == null) {
						storage.get(i).setEnd(storage.get(i + 1).getEnd());
						storage.remove(i + 1);
						break;
					}

				}
			}

		}

	}

	/**
	 * Retorna o processo alocado na memória especificado pelo parâmetro de entrada 'idProcess'.
	 */
	public Process getProcess(int idProcess) {

		for (Partition partition : storage) {
			if (partition.getProcess().getId() == idProcess)
				return partition.getProcess();

		}

		return null;

	}

	/**
	 * Verifica se o 'process' correspondente a algum processo alocado na memória.
	 */
	public boolean contains(Process process) {

		for (Partition partition : storage) {
			if (partition.getProcess() != null)
				if (partition.getProcess().getId() == process.getId())
					return true;

		}

		return false;

	}

	// public boolean checkMemorySpace(int tp) {}

	// public void setTmpAllocated(int tmpAllocated) {}

	// public int getTmpAllocated() {}

	// public void setTmp(int tmp) {}

	// public int getTmp() {}

	// public void removeProcess(int idProcess) {}

}
