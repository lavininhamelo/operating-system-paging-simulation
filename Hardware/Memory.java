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

	public static final String reset = "\u001B[0m";
	public static final String red = "\u001B[31m";
	public static final String blue = "\u001B[34m";
	public static final String green = "\u001B[32m";
	public static final String yellow = "\u001b[33m";

	/**
	 * Tamanho da memória.
	 */
	// private static int tmp;

	/**
	 * Tamanho da memória.
	 */
	// private static int nframes;

	/**
	 * Tamanho ocupado pelos processos.
	 * 
	 */
	// private static int tmpAllocated;

	/**
	 * Contador de page faults.
	 */
	private int countPageFaults;

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
		// this.nframes = nframes;
		// this.storage = new Vector<>();
		// this.storage.add(new Partition(1, tmp));
		this.countPageFaults = 0;
		quadros = new Vector<Frame>();

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
			this.countPageFaults++;
			Frame f = quadros.remove(0);
			f.setReference(true);
			f.setPage(page);
			quadros.add(f);

			process.getPageTable(page).setReferenceBit(true);
			process.getPageTable(page).setValidInvalidBit(true);
			process.getPageTable(page).setFrameId(f.getId());
			disk.setInvalidBit(f.getId(), page);
			System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()) + ".	Pager lê do disco a página "
					+ page.getId() + " solicitada e o coloca no quadro " + quadros.get(0).getId());

		} else {

			while (quadros.firstElement().isReference()) {
				Frame f = quadros.remove(0);
				f.setReference(false);
				this.countPageFaults++;
				quadros.add(f);
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
			process.getPageTable(page).setReferenceBit(true);
			process.getPageTable(page).setValidInvalidBit(true);
			process.getPageTable(page).setFrameId(f.getId());
			disk.setInvalidBit(f.getId(), page);
			System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()) + ".	Pager lê do disco a página "
					+ page.getId() + " solicitada e o coloca no quadro " + quadros.get(0).getId() + "\n"
					+ this.printFreeFrames() + disk.printNotFinished());

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
	// public boolean contains(Process process) {
	// for (Frame frame : quadros) {
	// if (frame.getPage() != null)
	// if (frame.getPage().getIdProcess() == process.getId())
	// return true;
	// }

	// return false;
	// }

	public String printFreeFrames() {
		String str = "";
		for (Frame frame : quadros)
			if (frame.getPage() == null)
				str += new SimpleDateFormat("HH:mm:ss").format(new Date()) + green + ".	O quadro " + frame.getId()
						+ " está livre.\n" + reset;
			else
				str += new SimpleDateFormat("HH:mm:ss").format(new Date()) + yellow + ".	O quadro " + frame.getId()
						+ " está ocupado.\n" + reset;

		return str;
	}

	public void printPageFaults() {
		System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()) + red
				+ ".	O número total de page faults foi " + this.countPageFaults + "." + reset);
	}

	// public boolean checkMemorySpace(int tp) {}

	// public void setTmpAllocated(int tmpAllocated) {}

	// public int getTmpAllocated() {}

	// public void setTmp(int tmp) {}

	// public int getTmp() {}

	// public void removeProcess(int idProcess) {}

}
