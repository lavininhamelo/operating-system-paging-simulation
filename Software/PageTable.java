package Software;

import java.util.Vector;

/**
 * Pages do Processo
 */
public class PageTable {

  /**
   * Identificação da página.
   */
  private int pageId;

  /**
   * Identificação da página.
   */
  private FramePageTable framePageTable;
  
  /**
   * Bit de Refencia
   */

  private boolean referenceBit;

  /**
   * Bit de Refencia
   */

  private boolean validInvalidBit;

  /**
   * Contador para percorrer os Frames.
   */
  private int count;


  public PageTable(int pageId, boolean referenceBit, boolean validInvalidBit) {
    this.pageId = pageId;
    this.referenceBit = false;
    this.validInvalidBit = false;

  }

  public void setPageId(int pageId) {
    this.pageId = pageId;
  }

  public int getPageId() {
    return this.pageId;
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


}
