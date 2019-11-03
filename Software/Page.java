package Software;

/**
 * Pages do Processo
 */
public class Page {

  /**
   * Identificação Page.
   */ 
  private int idPage;

  /**
   * Identificação Processo.
   */
  private int idProcess;

  public Page() {
  }

  public Page(int idPage, int idProcess) {
    this.idPage = idPage;
    this.idProcess = idProcess;
  }

  public void setId(int idPage) {
    this.idPage = idPage;
  }

  public int getId() {
    return this.idPage;
  }

  public void setIdProcess(int idProcess) {
    this.idProcess = idProcess;
  }

  public int getIdProcess() {
    return this.idProcess;
  }

}