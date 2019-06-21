package Software;

import java.util.Vector;

/**
 * Processo.
 */
public class Process {

	/**
	 * Identificação.
	 */
	private int id;

	/**
	 * Numero de páginas.
	 */
	private int np;

	/**
	 * Tempo de chegada na fila de entrada.
	 */
	private int tc;

	/**
	 * Tempo de CPU burst.
	 */
	private int tb;

	/**
	 * Vetor de páginas
	 */
	private Vector<PageTable> pageTable;

	public Process(int id, int np, int tc, int tb) {
		this.id = id;
		this.np = np;
		this.tc = tc;
		this.tb = tb;
		this.pageTable = new Vector<PageTable>();

		for (int i = 0; i < np; i++) {
			pageTable.add(new PageTable(i, id));
		}

		// for (PageTable b : pageTable) {
		// System.out.println("Page ID: " + b.getPage().getId() + " Bit de Referência: "
		// + b.getReferenceBit()
		// + " Bit iválido: " + b.getValidInvalidBit());
		// }

	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setNp(int np) {
		this.np = np;
	}

	public int getNp() {
		return this.np;
	}

	public void setTc(int tc) {
		this.tc = tc;
	}

	public int getTc() {
		return this.tc;
	}

	public void setTb(int tb) {
		this.tb = tb;
	}

	public int getTb() {
		return this.tb;
	}

	public Page getPageById(int id) {
		int novoId = id % np;
		Page p = new Page();
		for (PageTable a : pageTable) {
			if (a.getPage().getId() == novoId) {
				p = a.getPage();
			}
		}
		return p;
	}

	public int getIdFrame(int idPage) {
		int idFrame = -1;
		for (PageTable a : pageTable) {
			if (a.getPage().getId() == idPage) {
				idFrame = a.getFrameId();
			}
		}
		return idFrame;
	}

	public boolean isPageValid(Page page) {
		boolean isValid = true;
		for (PageTable a : pageTable) {
			if (a.getPage().getId() == page.getId()) {
				isValid = a.getValidInvalidBit();
			}
		}
		return isValid;
	}

	public PageTable getPageTable(Page page) {
		PageTable pt = null;
		for (PageTable a : pageTable) {
			if (a.getPage().getId() == page.getId()) {
				pt = a;
			}
		}
		return pt;
	}

	public Vector<PageTable> getPageTable() {
		return this.pageTable;
	}

}
