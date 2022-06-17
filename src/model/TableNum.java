package model;

/**
 *
 * @author takacs.gergely
 */
public enum TableNum {
    
    ONE(1), TWO(2), THREE(3);
    
    private final int VALUE;

    private TableNum(int VALUE) {
        this.VALUE = VALUE;
    }

    public int getVALUE() {
        return VALUE;
    }
    
}
