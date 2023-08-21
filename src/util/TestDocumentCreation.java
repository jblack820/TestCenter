package util;

import java.util.List;
import model.TableFontWeight;
import config.TestDocumentConfig;
import model.TestDocumentCreationDTO;
import model.TestProject;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;

/**
 * @author Greg
 */
public class TestDocumentCreation {

    static XWPFDocument testDocumentTemplate;
    static List<XWPFTable> documentTables;

    public static void fillTestDocument(XWPFDocument testDocTemplate, TestProject tp, TestDocumentCreationDTO formData, int currentPageNum) {
        testDocumentTemplate = testDocTemplate;
        documentTables = testDocTemplate.getTables();
        writeTable(tp, formData, currentPageNum);
    }

    private static void writeTable(TestProject tp, TestDocumentCreationDTO formData, int currentPageNum) {

        writeTableCell(TestDocumentConfig.PROJECT_NAME_CELL, formData.getProjectName());
        writeTableCell(TestDocumentConfig.VERSION_NUMBER_CELL, formData.getProjectVersionNumber());
        writeTableCell(TestDocumentConfig.NAME_OF_TESTER_CELL, formData.getTestConductedBy());
        writeTableCell(TestDocumentConfig.DATE_OF_TEST_CELL, formData.getDateOfTest());
        writeTableCell(TestDocumentConfig.TEST_CASE_ID_CELL, createTestCaseId(formData, currentPageNum));
        writeTableCell(TestDocumentConfig.AREA_OF_TEST_CELL, formData.getAreaUnderTest());
        writeTableCell(TestDocumentConfig.PROJECT_TYPE_CELL, tp.getProjectTypeName());
        writeTableCell(TestDocumentConfig.BROWSER_OPSYSTEM_CELL, formData.getBrowserUsed());
        writeTableCell(TestDocumentConfig.APP_LOCATION_CELL, formData.getAppLocation());

    }

    public static void writeTableCell(TestDocumentConfig documentCell, String text) {

        XWPFRun run = documentTables
                .get(documentCell.getTableNum().getVALUE())
                .getRow(documentCell.getTableRowNum().getVALUE())
                .getCell(documentCell.getTableCellNum().getVALUE())
                .getParagraphs().get(0)
                .createRun();

        run.setBold(documentCell.getFontWeight() == TableFontWeight.BOLD);
        run.setFontSize(documentCell.getFontSize());
        run.setFontFamily(TestDocumentConfig.FONT_TYPE);
        run.setText(text);

    }

    public static void clearTableCell(TestDocumentConfig documentCell) {
        XWPFRun run = documentTables
                .get(documentCell.getTableNum().getVALUE())
                .getRow(documentCell.getTableRowNum().getVALUE())
                .getCell(documentCell.getTableCellNum().getVALUE())
                .getParagraphs().get(0)
                .createRun();

        run.setText("");

    }

    private static String createTestCaseId(TestDocumentCreationDTO formData, int currentPageNum) {
        StringBuilder sb = new StringBuilder();

        sb
                .append(formData.getTestCaseId())
                .append("-")
                .append(String.format("%03d", currentPageNum).toUpperCase());

        return sb.toString();

    }

}
