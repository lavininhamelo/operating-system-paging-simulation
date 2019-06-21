package Software;

/**
 * Frames da memória
 */
public class Frame {

  /**
   * Identificação.
   */

  private int id;

  /**
   * Página do frame.
   */

  private Page page;

  /**
   * Bit de Refencia
   */

  private boolean reference;

  public boolean isReference() {
    return reference;
  }

  public Frame(int id) {
    this.id = id;
    this.reference = false;
    this.page = null;
  }

  public void setReference(boolean reference) {
    this.reference = reference;
  }

  public boolean getReference() {
    return this.reference;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getId() {
    return this.id;
  }

  public void setPage(Page page) {
    this.page = page;
  }

  public Page getPage() {
    return this.page;
  }

}