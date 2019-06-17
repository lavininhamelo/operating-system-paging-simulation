package Hardware;

import Software.Process;

/**
 * Partition
 */
public class Partition {

    private Process process;

    private int start;

    private int end;

    public Partition(int start, int end) {
        this.start = start;
        this.end = end;
    }

    /**
     * @param process the process to set
     */
    public void setProcess(Process process) {
        this.process = process;
    }

    /**
     * @param end the end to set
     */
    public void setEnd(int end) {
        this.end = end;
    }

    /**
     * @param start the start to set
     */
    public void setStart(int start) {
        this.start = start;
    }

    /**
     * @return the process
     */
    public Process getProcess() {
        return process;
    }

    /**
     * @return the end
     */
    public int getEnd() {
        return end;
    }

    /**
     * @return the start
     */
    public int getStart() {
        return start;
    }

    public int getSize() {
        return end - start + 1;
    }


    
}