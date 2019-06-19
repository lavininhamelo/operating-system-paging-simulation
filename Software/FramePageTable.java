package Software;

/**
 * Informações dos Frames da PageTable.
 */
public class FramePageTable {

    /**
     * Identificação.
     */
    private int idFramePageTable;

    /**
     * Bit válido-inválido.
     */
    private boolean validInvalidBit;

    /**
     * Bit válido-inválido.
     */
    private boolean referenceBit;

    /**
     * Página do processo.
     */
    private Page pageProcess;

    public FramePageTable(int idFramePageTable) {
        this.idFramePageTable = idFramePageTable;
        this.validInvalidBit = false;
        this.referenceBit = false;
        this.pageProcess = new Page();
    }

    // public void setId(int idFrame) {
    // this.idFrame = idFrame;
    // }

    // public int getId() {
    // return idFrame;
    // }

}