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
	private Vector<Page> paginas;

	public Process(int id, int np, int tc, int tb) {
		this.id = id;
		this.np = np;
		this.tc = tc;
		this.tb = tb;
		this.paginas = new Vector<Page>();

		for (int i = 0; i < np; i++) {
			paginas.add(new Page(i, id));
		}
		this.pageTable = new PageTable(id);
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
		Page p = new Page();
		for (Page a : paginas) {
			if (a.getId() == id) {
				p = a;
			}
		}
		return p;
	}

	public int getIdFrame(int idPage) {
		return this.pageTable.getId();
	}

	public boolean isPageValid(Page page) {
		return pageTable.getValidInvalidBit();
	}

	public int getPageTableId() {
		return pageTableId;
	}

	public void setPageTableId(int pageTableId) {
		this.pageTableId = pageTableId;
	}

	public PageTable getPageTable() {
		return pageTable;
	}

	public void setPageTable(PageTable pageTable) {
		this.pageTable = pageTable;
	}

}
