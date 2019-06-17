package Software;

import Software.Process;

/**
 * Pages do Processo
 */
public class PageTable {

  /**
   * Identificação da página.
   */
  private int pageId;

  /**
   * Identificação.
   */
  private boolean validInvalidBit;

  /**
   * Identificação.
   */
  private boolean referenceBit;

  public PageTable(int pageId, int referenceBit) {
    this.pageId = pageId;
    this.validInvalidBit = false;
    this.referenceBit = false;
  }

}