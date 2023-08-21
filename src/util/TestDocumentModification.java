package util;

import java.util.List;
import model.TableFontWeight;
import model.TestDocument;
import config.TestDocumentConfig;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import static util.TestDocumentCreation.documentTables;

/**
 *
 * @author takacs.gergely
 */
public class TestDocumentModification {

    public static void modifySelectedDocuments(List<TestDocument> list, TestDocumentConfig testDocumentCell, String newValue) {
        for (TestDocument testDocument : list) {
            changePropertyOfDocument(testDocument, testDocumentCell, newValue);
        }
    }

    private static void changePropertyOfDocument(TestDocument doc, TestDocumentConfig testDocumentCell, String newValue) {
        XWPFDocument xwpfdoc = doc.getXWPFdocument();

        changeCellDataOnEveryPage(xwpfdoc, testDocumentCell, newValue);

    }

    private static void changeCellDataOnEveryPage(XWPFDocument xwpfdoc, TestDocumentConfig testDocumentCell, String newValue) {
        int pagenumber = 0;
        int totalNumOfPages = getNumOfPages(xwpfdoc);

        while (pagenumber <= totalNumOfPages) {
            clearTableCell(pagenumber, testDocumentCell);
            writeTableCell(pagenumber, testDocumentCell, newValue);
        }
    }

    private static int getNumOfPages(XWPFDocument xwpfdoc) {
        int totalNumOfTablesInDocument = xwpfdoc.getTables().size();
        return totalNumOfTablesInDocument / TestDocumentConfig.NUMBER_OF_TABLES_PER_PAGE;
    }

    public static void writeTableCell(int pageNumber, TestDocumentConfig documentCell, String text) {

        XWPFRun run = documentTables
                .get(documentCell.getTableNum().getVALUE() + pageNumber)
                .getRow(documentCell.getTableRowNum().getVALUE())
                .getCell(documentCell.getTableCellNum().getVALUE())
                .getParagraphs().get(0)
                .createRun();

        run.setBold(documentCell.getFontWeight() == TableFontWeight.BOLD);
        run.setFontSize(documentCell.getFontSize());
        run.setFontFamily(TestDocumentConfig.FONT_TYPE);
        run.setText(text);

    }

    public static void clearTableCell(int pageNumber, TestDocumentConfig documentCell) {
        XWPFRun run = documentTables
                .get(documentCell.getTableNum().getVALUE() + pageNumber)
                .getRow(documentCell.getTableRowNum().getVALUE())
                .getCell(documentCell.getTableCellNum().getVALUE())
                .getParagraphs().get(0)
                .createRun();

        run.setText("");

    }

}
