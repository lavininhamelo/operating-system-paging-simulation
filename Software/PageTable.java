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
  private Vector<FramePageTable> framePageTable;

  /**
   * Contador para percorrer os Frames.
   */
  private int count;

  public PageTable(int pageId, int referenceBit) {
    this.pageId = pageId;
    this.framePageTable = new Vector<FramePageTable>();
    this.count = 0;

    for (int i = 0; i < np; i++) {
      framePageTable.add(new FramePageTable(i, id));
    }
  }

}