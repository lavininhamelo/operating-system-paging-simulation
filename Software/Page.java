package Software;

/**
 * Pages do Processo
 */
public class Page {

  /**
   * Identificação Page.
   */
  private int id;

  /**
   * Identificação Processo.
   */
  private int idProcess;

  public Page() {
  }

  public Page(int id, int idProcess) {
    this.id = id;
    this.idProcess = idProcess;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getId() {
    return this.id;
  }

  public void setIdProcess(int idProcess) {
    this.idProcess = idProcess;
  }

  public int getIdProcess() {
    return this.idProcess;
  }

}