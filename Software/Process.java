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
	 * Numero de Paginas.
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
	 * Identificação da tabela de páginas.
	 */
	private int pageTableId;
	/**
	 * Identificação da tabela de páginas.
	 */
	private PageTable pageTable;
	/**
	 * Vetor de Paginas
	 */
	private Vector<Page> pages;

	public Process(int id, int np, int tc, int tb) {
		this.id = id;
		this.np = np;
		this.tc = tc;
		this.tb = tb;
		this.pages = new Vector<Page>();

		for (int i = 0; i < np; i++) {
			pages.add(new Page(i, id));
		}
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

	public Page getPageById(int pageId) {
		for (Page page : pages) {
			if (page.getId() == pageId) {
				return page;
			}
		}

		return null;
	}

	// TODO
	public int getIdFrame(int idPage) {
		return 0;
	}

	public boolean isPageValid(Page page) {

		if (page.getId() == pageTable.getPageId()) {
			System.out.println("Oi");
		}

		return false;
	}
}
