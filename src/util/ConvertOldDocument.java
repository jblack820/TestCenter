package util;

import app.Main;
import config.AppConfig;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.ProjectType;
import model.TableCellNum;
import model.TableFontWeight;
import model.TableNum;
import model.TableRowNum;
import model.TestDocumentDTO;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;

/**
 *
 * @author takacs.gergely
 */
public class ConvertOldDocument {

    static XWPFDocument targetDocument;
    static XWPFDocument oldDocument;

    public static void convertOldDocumentToNew(XWPFDocument doc) {

        oldDocument = doc;
        List<XWPFDocument> documentList = new ArrayList<>();
        TestDocumentDTO formDTO = null;

        int tableNum = 1;

        for (XWPFTable oldDocumentCurrentTable : oldDocument.getTables()) {

            switch (tableNum) {
                case 1:
                    targetDocument = Main.controller.getTemplateService().getTemplate(AppConfig.TEST_DOC_TEMPLATE_NAME);
                    formDTO = getDTOFromOldDocument(oldDocumentCurrentTable, ProjectType.WINDOWS_DESKTOP_APPLICATION, "");
                    copyTableONE(formDTO);
                    tableNum++;
                    break;
                case 2:
                    copyTestSteps(oldDocumentCurrentTable);
                    tableNum++;
                    break;
                case 3:
                    copyResultTable(oldDocumentCurrentTable);
                    documentList.add(targetDocument);
                    tableNum = 1;
                    break;
            }

        }

        try {

            String saveLocation = "C:\\" + Utils.getFileSystemFriendyName(formDTO.getAreaUnderTest()) + ".docm";

            Main.controller.getDocumentSave().saveMultiPageDocument(
                    documentList,
                    new FileOutputStream(new File(saveLocation)));

        } catch (IOException ex) {
            Logger.getLogger(ConvertOldDocument.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void copyTableONE(TestDocumentDTO formData) {

        
        writeTableCell(TableNum.ONE,
                TableRowNum.ONE,
                TableCellNum.ZERO,
                TableFontWeight.BOLD,
                12,
                formData.getProjectName());

        
        writeTableCell(TableNum.ONE,
                TableRowNum.ONE,
                TableCellNum.ONE,
                TableFontWeight.BOLD,
                9,
                formData.getProjectVersionNumber());

        
        writeTableCell(TableNum.ONE,
                TableRowNum.ONE,
                TableCellNum.TWO,
                TableFontWeight.BOLD,
                9,
                formData.getTestConductedBy());

        
        writeTableCell(TableNum.ONE,
                TableRowNum.ONE,
                TableCellNum.THREE,
                TableFontWeight.BOLD,
                9,
                formData.getDateOfTest());

        
        writeTableCell(TableNum.ONE,
                TableRowNum.THREE,
                TableCellNum.ZERO,
                TableFontWeight.BOLD,
                12,
                formData.getTestCaseId());

           
        writeTableCell(TableNum.ONE,
                TableRowNum.THREE,
                TableCellNum.ONE,
                TableFontWeight.BOLD,
                9,
                formData.getAreaUnderTest());

        
        writeTableCell(TableNum.ONE,
                TableRowNum.THREE,
                TableCellNum.TWO,
                TableFontWeight.BOLD,
                9,
                formData.getProjetType());

        
        writeTableCell(TableNum.ONE,
                TableRowNum.THREE,
                TableCellNum.THREE,
                TableFontWeight.REGULAR,
                9,
                "-");

        
        writeTableCell(TableNum.ONE,
                TableRowNum.FIVE,
                TableCellNum.ZERO,
                TableFontWeight.BOLD,
                9,
                formData.getNameOfTestCase());

        
        writeTableCell(TableNum.ONE,
                TableRowNum.FIVE,
                TableCellNum.ONE,
                TableFontWeight.REGULAR,
                9,
                formData.getAppLocation());

        writeTableCell(TableNum.ONE,
                TableRowNum.SEVEN,
                TableCellNum.ZERO,
                TableFontWeight.REGULAR,
                9,
                formData.getDescOfTest());

        
        writeTableCell(TableNum.ONE,
                TableRowNum.SEVEN,
                TableCellNum.ONE,
                TableFontWeight.REGULAR,
                9,
                formData.getPreconditionsOfTest());

    }

    public static void writeTableCell(TableNum tableNum, TableRowNum tableRowNum,
            TableCellNum tableCellNum, TableFontWeight fontWeight, int fontSize, String text) {

        boolean isBoldFont = fontWeight == TableFontWeight.BOLD;

        XWPFRun run = targetDocument.getTables()
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

    public static void writeTableCell(TableNum tableNum, int tableRowNum,
            TableCellNum tableCellNum, TableFontWeight fontWeight, int fontSize, String text) {

        boolean isBoldFont = fontWeight == TableFontWeight.BOLD;

        XWPFRun run = targetDocument.getTables()
                .get(tableNum.getVALUE())
                .getRow(tableRowNum)
                .getCell(tableCellNum.getVALUE())
                .getParagraphs().get(0)
                .createRun();
        run.setBold(isBoldFont);
        run.setFontSize(fontSize);
        run.setFontFamily("Century Gothic");
        run.setText(text);
    }

    private static TestDocumentDTO getDTOFromOldDocument(XWPFTable table, ProjectType projectType, String browser) {

        TestDocumentDTO formData = new TestDocumentDTO(
                table.getRow(1).getCell(0).getText(),
                table.getRow(1).getCell(1).getText(),
                table.getRow(1).getCell(2).getText(),
                table.getRow(3).getCell(3).getText(),
                table.getRow(3).getCell(0).getText(),
                table.getRow(3).getCell(1).getText(),
                projectType.getProjectTypeName(),
                browser,
                table.getRow(5).getCell(0).getText(),
                table.getRow(5).getCell(1).getText(),
                table.getRow(7).getCell(0).getText(),
                table.getRow(7).getCell(1).getText()
        );

        return formData;
    }

    private static void copyTestSteps(XWPFTable oldTable) {

        for (int i = 1; i < oldTable.getRows().size(); i++) {
            String testStep = oldTable.getRow(i).getCell(1).getText();
            if (testStep.equalsIgnoreCase("")) {
                break;
            }
            String marker = oldTable.getRow(i).getCell(2).getText();
            writeTableCell(TableNum.TWO, i, TableCellNum.ONE, TableFontWeight.REGULAR, 9, testStep);
            targetDocument.getTables().get(2).getRow(i).getCell(2).removeParagraph(0);
            targetDocument.getTables().get(2).getRow(i).getCell(2).addParagraph().setAlignment(ParagraphAlignment.CENTER);

            writeTableCell(TableNum.TWO, i, TableCellNum.TWO, TableFontWeight.REGULAR, 12, marker);

        }

    }

    private static void copyResultTable(XWPFTable oldTable) {

        //result   
        String result = oldTable.getRow(0).getCell(1).getText();
        targetDocument.getTables().get(3).getRow(0).getCell(1).removeParagraph(0);
        targetDocument.getTables().get(3).getRow(0).getCell(1).addParagraph().setAlignment(ParagraphAlignment.CENTER);
        writeTableCell(
                TableNum.THREE,
                TableRowNum.ZERO,
                TableCellNum.ONE,
                TableFontWeight.BOLD,
                11,
                result);

        if (result.equalsIgnoreCase("SIKERTELEN")) {

            //writing level of defect
            targetDocument.getTables().get(3).getRow(3).getCell(1).removeParagraph(0);
            targetDocument.getTables().get(3).getRow(3).getCell(1).addParagraph().setAlignment(ParagraphAlignment.CENTER);
            writeTableCell(
                    TableNum.THREE,
                    TableRowNum.THREE,
                    TableCellNum.ONE,
                    TableFontWeight.BOLD,
                    11,
                    oldTable.getRow(3).getCell(1).getText());

            //writing defect desc
            writeTableCell(
                    TableNum.THREE,
                    TableRowNum.SIX,
                    TableCellNum.ZERO,
                    TableFontWeight.REGULAR,
                    9,
                    oldTable.getRow(6).getCell(0).getText());

            //coloring cells
            targetDocument.getTables().get(3).getRow(0).getCell(1).setColor("FF0000");

            //level of defect
            String level = oldTable.getRow(3).getCell(1).getText();

            if (level.equalsIgnoreCase("BLOKKOLÓ")) {
                targetDocument.getTables().get(3).getRow(3).getCell(1).setColor("FF0000");
            } else if (level.equalsIgnoreCase("SÚLYOS")) {
                targetDocument.getTables().get(3).getRow(3).getCell(1).setColor("FF6600");
            } else if (level.equalsIgnoreCase("KÖZEPES")) {
                targetDocument.getTables().get(3).getRow(3).getCell(1).setColor("FF9900");
            } else {
                targetDocument.getTables().get(3).getRow(3).getCell(1).setColor("FFFF99");
            }

            

        } else {
            //coloring cells green
            targetDocument.getTables().get(3).getRow(0).getCell(1).setColor("CCFFCC");
        }

    }

}
