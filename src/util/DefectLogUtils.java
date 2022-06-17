package util;

import java.util.List;
import model.TableCellNum;
import model.TableFontWeight;
import model.TableNum;
import model.TableRowNum;
import model.TestCase;
import model.TestStep;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPermStart;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STEdGrp;

public class DefectLogUtils {

    static XWPFDocument defectLogTemplate;
    static List<XWPFTable> documentTables;
    static TestCase testCase;

    public static void FillDefectLog(XWPFDocument defectLogTemplateDocument, TestCase tc) {
        defectLogTemplate = defectLogTemplateDocument;
        documentTables = defectLogTemplate.getTables();
        testCase = tc;

        writeFirstTable();
        writeSecondTable();
        writeThirdTable();
        addDocumentProtection();
    }

    private static void writeFirstTable() {
        
        writeTableCell(TableNum.ONE,
                TableRowNum.ONE,
                TableCellNum.ZERO,
                TableFontWeight.BOLD,
                10,
                "HJ-"+testCase.getTestCaseId());

        
        writeTableCell(TableNum.ONE,
                TableRowNum.ONE,
                TableCellNum.ONE,
                TableFontWeight.BOLD,
                9,
                testCase.getProductVersionNumber());

        
        writeTableCell(TableNum.ONE,
                TableRowNum.ONE,
                TableCellNum.TWO,
                TableFontWeight.BOLD,
                9,
                testCase.getTestConductedBy());

        
        writeTableCell(TableNum.ONE,
                TableRowNum.ONE,
                TableCellNum.THREE,
                TableFontWeight.BOLD,
                9,
                Utils.convertDateToHungarianTextFormat(testCase.getDateOfTest()));

       
        writeTableCell(TableNum.ONE,
                TableRowNum.THREE,
                TableCellNum.ZERO,
                TableFontWeight.BOLD,
                9,
                testCase.getProjectName());

       
        writeTableCell(TableNum.ONE,
                TableRowNum.THREE,
                TableCellNum.ONE,
                TableFontWeight.BOLD,
                9,
                testCase.getModuleName());

       
        writeTableCell(TableNum.ONE,
                TableRowNum.THREE,
                TableCellNum.TWO,
                TableFontWeight.BOLD,
                9,
                testCase.getProjectType());

       
        writeTableCell(TableNum.ONE,
                TableRowNum.THREE,
                TableCellNum.THREE,
                TableFontWeight.BOLD,
                9,
                testCase.getBrowserAppTestedOn());

        
        writeTableCell(TableNum.ONE,
                TableRowNum.FIVE,
                TableCellNum.ZERO,
                TableFontWeight.BOLD,
                9,
                testCase.getNameOfTest());

        
        writeTableCell(TableNum.ONE,
                TableRowNum.FIVE,
                TableCellNum.ONE,
                TableFontWeight.REGULAR,
                9,
                testCase.getAppLocation());

        
        writeTableCell(TableNum.ONE,
                TableRowNum.SEVEN,
                TableCellNum.ZERO,
                TableFontWeight.REGULAR,
                9,
                testCase.getTestCaseDescription());

        
        writeTableCell(TableNum.ONE,
                TableRowNum.SEVEN,
                TableCellNum.ONE,
                TableFontWeight.REGULAR,
                9,
                testCase.getDescriptionOfDefect());
    }

    private static void writeSecondTable() {

        int currentRowNum = 1;
        for (TestStep testStep : testCase.getTEST_STEPS()) {
            writeTestStep(
                    TableNum.TWO,
                    currentRowNum,
                    TableCellNum.ONE,
                    TableFontWeight.REGULAR,
                    9,
                    testStep.getDescription());
            if (testStep.isIsFailed()) {
                writeTestStep(
                        TableNum.TWO,
                        currentRowNum,
                        TableCellNum.TWO,
                        TableFontWeight.BOLD,
                        10,
                        "x");
            }
            currentRowNum++;
        }
    }

    private static void writeThirdTable() {
        writeTableCell(TableNum.THREE,
                TableRowNum.ZERO,
                TableCellNum.ONE,
                TableFontWeight.BOLD,
                11,
                testCase.getDefectType().getFullName().toUpperCase());
    }

    //<editor-fold defaultstate="collapsed" desc="UTILS">
    public static void writeTableCell(
            TableNum tableNum,
            TableRowNum tableRowNum,
            TableCellNum tableCellNum,
            TableFontWeight fontWeight,
            int fontSize,
            String text) {
        
        boolean isBoldFont = fontWeight == TableFontWeight.BOLD;
        
        XWPFRun run = documentTables
                .get(tableNum.getVALUE())
                .getRow(tableRowNum.getVALUE())
                .getCell(tableCellNum.getVALUE())
                .getParagraphs().get(0)
                .createRun();
        run.setBold(isBoldFont);
        run.setFontSize(fontSize);
        run.setFontFamily("Century Gothic");
        run.setText(text);
    }
    
    public static void writeTestStep(
            TableNum tableNum,
            int rowNum,
            TableCellNum tableCellNum,
            TableFontWeight fontWeight,
            int fontSize,
            String text) {
        
        boolean isBoldFont = fontWeight == TableFontWeight.BOLD;
        
        XWPFRun run = documentTables
                .get(tableNum.getVALUE())
                .getRow(rowNum)
                .getCell(tableCellNum.getVALUE())
                .getParagraphs().get(0)
                .createRun();
        run.setBold(isBoldFont);
        run.setFontSize(fontSize);
        run.setFontFamily("Century Gothic");
        run.setText(text);
    }
    
    private static void addDocumentProtection() {
        
        CTPermStart ctPermStart = defectLogTemplate.getDocument().getBody().addNewPermStart();
        ctPermStart.setEdGrp(STEdGrp.EVERYONE);
        ctPermStart.setId("123456"); //note the Id
        
        XWPFParagraph paragraph = defectLogTemplate.createParagraph();
        XWPFRun run = paragraph.createRun();
        run.setText("*** Ez a dokumentumterület szerkeszthető ***");
        
        defectLogTemplate.getDocument().getBody().addNewPermEnd().setId("123456");
        
        defectLogTemplate.enforceReadonlyProtection();
    }
//</editor-fold>

}
