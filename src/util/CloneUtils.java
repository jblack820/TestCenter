package util;

import config.AppConfig;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.TableCellNum;
import model.TableFontWeight;
import model.TableNum;
import model.TableRowNum;
import model.TestDocumentHeaderCopyDTO;
import model.TestProject;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;

public class CloneUtils {

    static XWPFDocument currentTargetPage = null;

    public static void cloneAllTestDocumentToAnotherProject(TestProject sourceProject, TestProject targetProject, String testerName) {

        System.out.println("Cloning of " + sourceProject.getProjectName() + " to " + targetProject.getProjectName() + " started...");
        List<XWPFDocument> testDoclist = sourceProject.getAllXWPFTestDocumnets();
        for (XWPFDocument sourceDocument : testDoclist) {
            cloneDocumentToTargetProject(targetProject, sourceDocument, testerName);
        }
        System.out.println("Cloning competed!");
    }

    public static void cloneSingleDocumentToAnotherProject(TestProject sourceProject, File sourceFile, TestProject targetProject, String testerName) {
        System.out.println("source project: " + sourceProject.getProjectName());
        XWPFDocument sourceDocument = sourceProject.getXWPFDocument(sourceFile);
        cloneDocumentToTargetProject(targetProject, sourceDocument, testerName);

    }

    private static void cloneDocumentToTargetProject(TestProject targetProject, XWPFDocument sourceDocument, String testerName) {
        List<XWPFDocument> documentPages = new ArrayList<>();
        TestDocumentHeaderCopyDTO copyDTO = null;

        int tableNum = 0;
        for (XWPFTable currentTable : sourceDocument.getTables()) {

            switch (tableNum) {
                case 0:
                    currentTargetPage = getNewTemplatePage();
                    tableNum++;
                    break;
                case 1:
                    copyDTO = getCopyDto(targetProject, currentTable, testerName);
                    cloneTableONE(copyDTO);
                    tableNum++;
                    break;
                case 2:
                    copyTestSteps(currentTable);
                    tableNum++;
                    break;
                case 3:
                    tableNum = 0;
                    documentPages.add(currentTargetPage);
                    break;
            }

        }

        try {
            app.Main.controller.getDocumentSave().saveMultiPageDocument(documentPages, Utils.getTestDocumentOutputStream(copyDTO, targetProject));
        } catch (IOException ex) {
            Logger.getLogger(CloneUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static TestDocumentHeaderCopyDTO getCopyDto(TestProject targetProject, XWPFTable currentTable, String testerName) {

        return new TestDocumentHeaderCopyDTO(
                targetProject.getProjectName(),
                targetProject.getLatestVersionNumber(),
                testerName.equalsIgnoreCase("") ? currentTable.getRow(1).getCell(2).getText() : testerName,
                Utils.convertDateToHungarianTextFormat(LocalDate.now()),
                convertTestCaseIdToTargetProject(currentTable.getRow(3).getCell(0).getText(), targetProject),
                currentTable.getRow(3).getCell(1).getText(),
                targetProject.getProjectType().getProjectTypeName(),
                targetProject.getBrowserTestedOn(),
                currentTable.getRow(5).getCell(0).getText(),
                targetProject.getAppLocation(),
                currentTable.getRow(7).getCell(0).getText(),
                currentTable.getRow(7).getCell(1).getText());

    }

    private static String convertTestCaseIdToTargetProject(String originalTestCaseId, TestProject targetProject) {

        //TIX-LIN-AUTH-001
        //TIXEL-LIN-AUTH-001
        StringBuilder target = new StringBuilder();

        String[] original = originalTestCaseId.split("-");
        for (int i = 0; i < original.length; i++) {
            if (i == 0) {
                target.append(targetProject.getCodeNAme()).append("-");
            } else if (i==1) {
                target.append(targetProject.getProjectType().getProjectTypeCodename()).append("-");
            } else {
                if (i != original.length - 1) {
                    target.append(original[i]).append("-");
                } else {
                    target.append(original[i]);
                }
            }
        }

        return target.toString();

    }

    private static XWPFDocument getNewTemplatePage() {
        return app.Main.controller.getTemplateService().getTemplate(AppConfig.TEST_DOC_TEMPLATE_NAME);
    }

    private static void writeTableCell(TableNum tableNum, TableRowNum tableRowNum,
            TableCellNum tableCellNum, TableFontWeight fontWeight, int fontSize, String text) {

        boolean isBoldFont = fontWeight == TableFontWeight.BOLD;

        XWPFRun run = currentTargetPage.getTables()
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

    private static void writeTableCell(TableNum tableNum, int tableRowNum,
            TableCellNum tableCellNum, TableFontWeight fontWeight, int fontSize, String text) {

        boolean isBoldFont = fontWeight == TableFontWeight.BOLD;

        XWPFRun run = currentTargetPage.getTables()
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

    private static void cloneTableONE(TestDocumentHeaderCopyDTO formData) {

        
        writeTableCell(TableNum.ONE,
                TableRowNum.ONE,
                TableCellNum.ZERO,
                TableFontWeight.BOLD,
                9,
                formData.getProjectName());

  
        writeTableCell(TableNum.ONE,
                TableRowNum.ONE,
                TableCellNum.ONE,
                TableFontWeight.BOLD,
                8,
                formData.getProjectVersionNumber());

       
        writeTableCell(TableNum.ONE,
                TableRowNum.ONE,
                TableCellNum.TWO,
                TableFontWeight.BOLD,
                8,
                formData.getTestConductedBy());

        
        writeTableCell(TableNum.ONE,
                TableRowNum.ONE,
                TableCellNum.THREE,
                TableFontWeight.BOLD,
                8,
                formData.getDateOfTest());

         
        writeTableCell(TableNum.ONE,
                TableRowNum.THREE,
                TableCellNum.ZERO,
                TableFontWeight.BOLD,
                10,
                formData.getTestCaseId());

            
        writeTableCell(TableNum.ONE,
                TableRowNum.THREE,
                TableCellNum.ONE,
                TableFontWeight.BOLD,
                8,
                formData.getAreaUnderTest());

        
        writeTableCell(TableNum.ONE,
                TableRowNum.THREE,
                TableCellNum.TWO,
                TableFontWeight.BOLD,
                8,
                formData.getProjetType());

        
        writeTableCell(TableNum.ONE,
                TableRowNum.THREE,
                TableCellNum.THREE,
                TableFontWeight.BOLD,
                8,
                formData.getBrowswerOrOyType());

        
        writeTableCell(TableNum.ONE,
                TableRowNum.FIVE,
                TableCellNum.ZERO,
                TableFontWeight.BOLD,
                8,
                formData.getTestCaseTitle());

       
        writeTableCell(TableNum.ONE,
                TableRowNum.FIVE,
                TableCellNum.ONE,
                TableFontWeight.BOLD,
                8,
                formData.getAppLocation());

        writeTableCell(TableNum.ONE,
                TableRowNum.SEVEN,
                TableCellNum.ZERO,
                TableFontWeight.REGULAR,
                9,
                formData.getDecriptionofTest());

        
        writeTableCell(TableNum.ONE,
                TableRowNum.SEVEN,
                TableCellNum.ONE,
                TableFontWeight.REGULAR,
                9,
                formData.getRequirementsOfTest());

    }

    private static void copyTestSteps(XWPFTable oldTable) {

        for (int i = 1; i < oldTable.getRows().size(); i++) {
            String testStep = oldTable.getRow(i).getCell(1).getText();
            if (testStep.equalsIgnoreCase("")) {
                break;
            }
            writeTableCell(TableNum.TWO, i, TableCellNum.ONE, TableFontWeight.REGULAR, 9, testStep);
        }

    }

}
