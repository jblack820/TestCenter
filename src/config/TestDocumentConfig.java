package config;

import model.TableCellNum;
import model.TableFontWeight;
import model.TableNum;
import model.TableRowNum;

/**
 *
 * @author takacs.gergely
 */
public enum TestDocumentConfig {

        
    //FIRST ROW
    PROJECT_NAME_CELL(TableNum.ONE, TableRowNum.ONE, TableCellNum.ZERO, TableFontWeight.BOLD, 8),
    VERSION_NUMBER_CELL(TableNum.ONE, TableRowNum.ONE, TableCellNum.ONE, TableFontWeight.BOLD, 8),
    NAME_OF_TESTER_CELL(TableNum.ONE, TableRowNum.ONE, TableCellNum.TWO, TableFontWeight.BOLD, 8),
    DATE_OF_TEST_CELL(TableNum.ONE, TableRowNum.ONE, TableCellNum.THREE, TableFontWeight.BOLD, 8),
    
    //SECOND ROW
    TEST_CASE_ID_CELL(TableNum.ONE, TableRowNum.THREE, TableCellNum.ZERO, TableFontWeight.BOLD, 11),
    AREA_OF_TEST_CELL(TableNum.ONE, TableRowNum.THREE, TableCellNum.ONE, TableFontWeight.BOLD, 8),
    PROJECT_TYPE_CELL(TableNum.ONE, TableRowNum.THREE, TableCellNum.TWO, TableFontWeight.BOLD, 8),
    BROWSER_OPSYSTEM_CELL(TableNum.ONE, TableRowNum.THREE, TableCellNum.THREE, TableFontWeight.BOLD, 8),
    
    //THIRD ROW
    APP_LOCATION_CELL(TableNum.ONE, TableRowNum.FIVE, TableCellNum.ONE, TableFontWeight.BOLD, 8); 
       

    
    TableNum tableNum;
    TableRowNum tableRowNum;
    TableCellNum tableCellNum;
    TableFontWeight fontWeight;
    int fontSize;
    
    public static final String FONT_TYPE = "Century Gothic";
    public static final int NUMBER_OF_TABLES_PER_PAGE = 4;
    public static final String CHECKBOX_CHECKED_CHARACTER = "\u2612";
    

//<editor-fold defaultstate="collapsed" desc="CONSTRUCTOR">
    private TestDocumentConfig(TableNum tableNum, TableRowNum tableRowNum, TableCellNum tableCellNum, TableFontWeight fontWeight, int fontSize) {
        this.tableNum = tableNum;
        this.tableRowNum = tableRowNum;
        this.tableCellNum = tableCellNum;
        this.fontWeight = fontWeight;
        this.fontSize = fontSize;
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="GETTERS">
    public TableNum getTableNum() {
        return tableNum;
    }
    
    public TableRowNum getTableRowNum() {
        return tableRowNum;
    }
    
    public TableCellNum getTableCellNum() {
        return tableCellNum;
    }
    
    public TableFontWeight getFontWeight() {
        return fontWeight;
    }
    
    public int getFontSize() {
        return fontSize;
    }
//</editor-fold>

}
