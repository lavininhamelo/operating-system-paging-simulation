package Software;

/**
 * Processo.
 */
public class Process {

	/**
	 * Identificação.
	 */
	private int id;

	/**
	 * Tamanho do processo.
	 */
	private int tp;

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

	public Process(int id, int tp, int tc, int tb) {
		this.id = id;
		this.tp = tp;
		this.tc = tc;
		this.tb = tb;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setTp(int tp) {
		this.tp = tp;
	}

	public int getTp() {
		return tp;
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

}
