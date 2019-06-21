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
   * Bit de Refencia
   */

  private boolean referenceBit;

  /**
   * Bit de Refencia
   */

  private boolean validInvalidBit;

  private int frameId;

  public PageTable(int pageId) {
    this.pageId = pageId;
    this.referenceBit = false;
    this.validInvalidBit = false;

  }

  public void setId(int pageId) {
    this.pageId = pageId;
  }

  public int getId() {
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

  public void setFrameId(int frameId) {
    this.frameId = frameId;
  }

  public int getFrameId() {
    return this.frameId;
  }

}
