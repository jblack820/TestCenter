package model;

/**
 *
 * @author takacs.gergely
 */
public enum TableRowNum {
    
    ZERO(0), ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8);
    
    private final int VALUE;

    private TableRowNum(int VALUE) {
        this.VALUE = VALUE;
    }

    public int getVALUE() {
        return VALUE;
    }
    
}
