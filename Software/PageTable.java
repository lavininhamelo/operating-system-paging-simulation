package Software;

/**
 * Pages do Processo
 */
public class PageTable {

  /**
   * Identificação da página.
   */
  private Page page;

  /**
   * Bit de Refencia
   */

  private boolean referenceBit;

  /**
   * Bit de Refencia
   */

  private boolean validInvalidBit;

  private int frameId;

  public PageTable(int pageId, int processId) {

    this.page = new Page(pageId, processId);
    this.referenceBit = false;
    this.validInvalidBit = false;

  }

  public void setValidInvalidBit(boolean validInvalidBit) {
    this.validInvalidBit = validInvalidBit;
  }

  public boolean getValidInvalidBit() {
    return this.validInvalidBit;
  }

  public void setReferenceBit(boolean referenceBit) {
    this.referenceBit = referenceBit;
  }

  public boolean getReferenceBit() {
    return this.referenceBit;
  }

  public void setFrameId(int frameId) {
    this.frameId = frameId;
  }

  public int getFrameId() {
    return this.frameId;
  }

  public void setPage(Page page) {
    this.page = page;
  }

  public Page getPage() {
    return this.page;
  }

}
