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
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setNp(int np) {
		this.np = np;
	}

	public int getNp() {
		return np;
	}

	public void setTc(int tc) {
		this.tc = tc;
	}

	public int getTc() {
		return tc;
	}

	public void setTb(int tb) {
		this.tb = tb;
	}

	public int getTb() {
		return tb;
	}

	public Page getPageById(int id) {
		
		return this
	}

}
