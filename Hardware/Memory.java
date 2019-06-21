package Hardware;

import Software.Frame;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.Vector;

import Software.Page;
import Software.Process;

/**
 * Responsável por gerenciar os processos que serão utilizados posteriormente
 * pela CPU.
 */
public class Memory extends Vector<Process> {

	/**
	 * Tamanho da memória.
	 */
	private static int tmp;

	/**
	 * Tamanho da memória.
	 */
	private static int nframes;

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
	// private static Vector<Partition> storage;

	private static Vector<Frame> quadros;

	public Memory(int nframes) {
		this.nframes = nframes;
		// this.storage = new Vector<>();
		// this.storage.add(new Partition(1, tmp));
		this.quadros = new Vector<Frame>();

		for (int i = 0; i < nframes; i++) {
			quadros.add(new Frame(i));
		}
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
	 * Aloca nova página na memória com Second Chance.
	 */
	public void addProcess(Process process, Page page) {
		if (!quadros.firstElement().isReference() && quadros.firstElement().getPage() == null) {
			System.out.println(
					new SimpleDateFormat("HH:mm:ss").format(new Date()) + ".	Pager percebe que há um quadro livre.");
			Frame f = quadros.remove(0);
			f.setReference(true);
			f.setPage(page);
			quadros.add(f);

			process.getPageTable().setReferenceBit(true);

		} else {
			while (quadros.firstElement().isReference()) {
				Frame f = quadros.remove(0);
				f.setReference(false);
				quadros.add(f);
				System.out.println(f.getReference());
			}

			System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date())
					+ ".	Pager percebe que não há quadros livres e substitui a página "
					+ quadros.get(0).getPage().getId() + " na que está no quadro " + quadros.get(0).getId());
			Frame f = quadros.remove(0);
			// Process processBackup = this.getProcess(f.getPage().getIdProcess());
			// System.out.println("----------------------------------------" +
			// processBackup.getId());
			writeInDisk(process);
			f.setReference(true);
			f.setPage(page);

			// for (Frame frame : quadros) {
			// frame.getPage()
			// }

			quadros.add(f);
			process.getPageTable().setReferenceBit(true);

		}
	}

	/**
	 * Retorna o processo alocado na memória especificado pelo parâmetro de entrada
	 * 'idProcess'.
	 */
	// public Process getProcess(int idProcess) {

	// for (Partition partition : storage) {
	// if (partition.getProcess().getId() == idProcess)
	// return partition.getProcess();

	// }

	// return null;

	// }

	/**
	 * Verifica se o 'process' correspondente a algum processo alocado na memória.
	 */
	public boolean contains(Process process) {
		for (Frame frame : quadros) {
			if (frame.getPage() != null)
				if (frame.getPage().getIdProcess() == process.getId())
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
