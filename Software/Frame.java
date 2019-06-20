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

  public Frame(int id) {
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