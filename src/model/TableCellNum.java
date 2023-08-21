package model;

/**
 *
 * @author takacs.gergely
 */
public enum TableCellNum {
    
    ZERO(0), ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7);
    
    private final int VALUE;

    private TableCellNum(int VALUE) {
        this.VALUE = VALUE;
    }

    public int getVALUE() {
        return VALUE;
    }
    
}
