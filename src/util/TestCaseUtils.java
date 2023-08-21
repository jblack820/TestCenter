package util;

import config.TestDocumentConfig;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import model.DefectType;
import model.TestCase;
import model.TestResult;
import model.TestStep;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

/**
 *
 * @author takacs.gergely
 */
public class TestCaseUtils {

    static int tableNuber = 0;

    static List<TestCase> getTestCaseObjectsFromOldXWPFFormat(XWPFDocument document) {
        int testCaseCounter = 0;
        List<TestCase> list = new ArrayList<>();
        TestCase testCase = null;
        Iterator<XWPFTable> tabeIterator = document.getTablesIterator();
        int counter = 1;
        while (tabeIterator.hasNext()) {
            XWPFTable table = tabeIterator.next();
            switch (counter) {
                case 1:
                    testCase = new TestCase();
                    testCaseCounter++;
                    //read headline info
                    XWPFTableRow row = table.getRow(1);
                    testCase.setProjectName(row.getCell(0).getText());
                    testCase.setProductVersionNumber(row.getCell(1).getText());
                    testCase.setTestConductedBy(row.getCell(3).getText());
                    XWPFTableRow row2 = table.getRow(3);
                    testCase.setTestCaseId(row2.getCell(0).getText());
                    testCase.setTestAreaName(row2.getCell(1).getText());
                    testCase.setOpSystemBrowswer(row2.getCell(2).getText());
                    StringBuilder sb = new StringBuilder();
                    sb.append(row2.getCell(3).getText());
                    sb.deleteCharAt(sb.length() - 1);
                    String dateString = sb.toString();
                    testCase.setDateOfTest(LocalDate.parse(Utils.convertPointsToDashes(dateString)));
                    XWPFTableRow row3 = table.getRow(5);
                    if (!Utils.isBlank(row3.getCell(0).getText())) {
                        testCase.setNameOfTestCase(row3.getCell(0).getText());
                    } else {
                        testCase.setNameOfTestCase("");
                    }
                    testCase.setAppLocation(row3.getCell(1).getText());
                    XWPFTableRow row4 = table.getRow(7);
                    if (!Utils.isBlank(row4.getCell(0).getText())) {
                        testCase.setTestCaseDescription(row4.getCell(0).getText());
                    } else {
                        testCase.setTestCaseDescription("");
                    }
                    if (!Utils.isBlank(row4.getCell(1).getText())) {
                        testCase.setTestCaseRequirements(row4.getCell(1).getText());
                    } else {
                        testCase.setTestCaseRequirements("");
                    }
                    counter++;
                    break;
                case 2:
                    //reading test steps
                    TestStep step = null;
                    for (int i = 1; i <= 15; i++) {
                        XWPFTableRow currentRow = table.getRow(i);
                        if (Utils.isBlank(currentRow.getCell(1).getText())) {
                            break;
                        } else {
                            step = new TestStep(i + ". " + currentRow.getCell(1).getText(), currentRow.getCell(2).getText().equalsIgnoreCase(String.valueOf('\u2612')));
                        }
                        testCase.getTEST_STEPS().add(step);
                    }
                    counter++;
                    break;
                default:
                    //read test results
                    XWPFTableRow currentRow = table.getRow(0);
                    switch (currentRow.getCell(1).getText()) {
                        case "SIKERES":
                            testCase.setTestResult(TestResult.PASSED);
                            break;
                        case "SIKERTELEN":
                            testCase.setTestResult(TestResult.FAILED);
                            break;
                        default:
                            testCase.setTestResult(TestResult.NOT_COMPLETED_YET);
                            break;
                    }
                    XWPFTableRow currentRow3 = table.getRow(3);
                    switch (currentRow3.getCell(1).getText()) {
                        case "BLOKKOL\u00d3":
                            testCase.setDefectType(DefectType.BLOKKOLO);
                            break;
                        case "S\u00daLYOS":
                            testCase.setDefectType(DefectType.SULYOS);
                            break;
                        case "K\u00d6ZEPES":
                            testCase.setDefectType(DefectType.KOZEPES);
                            break;
                        case "ENYHE":
                            testCase.setDefectType(DefectType.ENYHE);
                            break;
                        default:
                            testCase.setDefectType(DefectType.NEM_ERTEKELT);
                            break;
                    }
                    XWPFTableRow currentRow4 = table.getRow(4);
                    if (Utils.isBlank(currentRow4.getCell(1).getText())) {
                        testCase.setNumberOfImagesAttached(0);
                    } else {
                        try {
                            testCase.setNumberOfImagesAttached(Integer.parseInt(currentRow4.getCell(1).getText()));
                        } catch (NumberFormatException e) {
                            testCase.setNumberOfImagesAttached(0);
                        }
                    }
                    XWPFTableRow currentRow6 = table.getRow(6);
                    testCase.setDescriptionOfDefect(currentRow6.getCell(0).getText());
                    list.add(testCase);
                    counter = 1;
                    break;
            }
        }
        return list;
    }

    static List<TestCase> getTestCaseObjectsFromNewXWPFFormat(XWPFDocument document) {
        List<TestCase> list = new ArrayList<>();
        TestCase testCase = null;
        Iterator<XWPFTable> tabeIterator = document.getTablesIterator();
        tableNuber = 0;

        while (tabeIterator.hasNext()) {
            XWPFTable table = tabeIterator.next();
            switch (tableNuber) {
                case 0:
                    //no reqired data in the first table
                    goToNextTable();
                    break;
                case 1:
                    //creating Test Case main data
                    testCase = new TestCase();
                    getDataFromFirstTable(table, testCase);
                    goToNextTable();
                    break;
                case 2:
                    //reading test steps
                    if (null != testCase) {
                        testCase.getTEST_STEPS().addAll(getTestSteps(table));
                    }
                    goToNextTable();
                    break;
                default:
                    //reading test case results
                    if (null != testCase) {
                        testCase.setTestResult(getTestResultFromTable(table));
                        testCase.setDefectType(getDefectTypeFromTable(table, testCase));
                        testCase.setNumberOfImagesAttached(getNumberOfImagesAttached(table));
                        testCase.setDescriptionOfDefect(getDescriptionOfDefect(table));
                    }
                    list.add(testCase);
                    resetToFirstTable();
                    break;
            }
        }
        return list;
    }

    private static LocalDate getLocalDateFromCell(String text) {
        StringBuilder sb = new StringBuilder(text);
        sb.deleteCharAt(sb.length() - 1);
        String dateString = sb.toString();
        return LocalDate.parse(Utils.convertPointsToDashes(dateString));
    }

    private static void getDataFromFirstTable(XWPFTable table, TestCase testCase) {
        //reading data from Row 1
        XWPFTableRow row = table.getRow(1);
        testCase.setProjectName(row.getCell(0).getText());
        testCase.setProductVersionNumber(row.getCell(1).getText());
        testCase.setTestConductedBy(row.getCell(2).getText());
        testCase.setDateOfTest(getLocalDateFromCell(row.getCell(3).getText()));

        //reading data from Row 2
        XWPFTableRow row2 = table.getRow(3);
        testCase.setTestCaseId(row2.getCell(0).getText());
        testCase.setTestAreaName(row2.getCell(1).getText());
        testCase.setProjectType(row2.getCell(2).getText());
        testCase.setBrowserAppTestedOn(row2.getCell(3).getText());

        //reading data from Row 3
        XWPFTableRow row3 = table.getRow(5);
        testCase.setNameOfTestCase(getNameOfTestCaseFromTable(row3));
        testCase.setAppLocation(row3.getCell(1).getText());

        //reading data from Row 4
        XWPFTableRow row4 = table.getRow(7);
        testCase.setTestCaseDescription(getTestCaseDescriptionFromTable(row4));
        testCase.setTestCaseRequirements(getTestCaseRequirementsFromTable(row4));

    }

    private static Collection<? extends TestStep> getTestSteps(XWPFTable table) {
        List<TestStep> list = new ArrayList<>();

        TestStep step;
        for (int i = 1; i <= 15; i++) {
            XWPFTableRow currentRow = table.getRow(i);
            String testStepText = currentRow.getCell(1).getText();
            String testStepText2 = currentRow.getCell(2).getText();
            boolean isFailed = testStepText2.contains(TestDocumentConfig.CHECKBOX_CHECKED_CHARACTER);

            if (Utils.isBlank(testStepText)) {
                break;
            } else {
                step = new TestStep(testStepText, isFailed);
            }
            list.add(step);
        }
        return list;
    }

    private static TestResult getTestResultFromTable(XWPFTable table) {
        XWPFTableRow currentRow = table.getRow(0);
        switch (currentRow.getCell(1).getText()) {
            case "SIKERES":
                return TestResult.PASSED;
            case "SIKERTELEN":
                return TestResult.FAILED;
            default:
                return TestResult.NOT_COMPLETED_YET;

        }
    }

    private static DefectType getDefectTypeFromTable(XWPFTable table, TestCase testCase) {
        XWPFTableRow currentRow3 = table.getRow(3);
        if (testCase.getTestResult() == TestResult.FAILED) {
            switch (currentRow3.getCell(1).getText()) {
                case "BLOKKOLÓ":
                    return DefectType.BLOKKOLO;
                case "SÚLYOS":
                    return DefectType.SULYOS;
                case "KÖZEPES":
                    return DefectType.KOZEPES;
                default:
                    return DefectType.ENYHE;
            }
        } else {
            return DefectType.NEM_ERTEKELT;
        }
    }

    private static int getNumberOfImagesAttached(XWPFTable table) {
        XWPFTableRow currentRow4 = table.getRow(4);
        if (Utils.isBlank(currentRow4.getCell(1).getText())) {
            return 0;
        } else {
            try {
                return Integer.parseInt(currentRow4.getCell(1).getText());
            } catch (NumberFormatException e) {
                return 0;
            }
        }
    }

    private static String getDescriptionOfDefect(XWPFTable table) {
        XWPFTableRow currentRow6 = table.getRow(6);
        return currentRow6.getCell(0).getText();
    }

    private static String getNameOfTestCaseFromTable(XWPFTableRow row3) {
        if (!Utils.isBlank(row3.getCell(0).getText())) {
            return row3.getCell(0).getText();
        } else {
            return "";
        }
    }

    private static String getTestCaseDescriptionFromTable(XWPFTableRow row4) {
        if (!Utils.isBlank(row4.getCell(0).getText())) {
            return row4.getCell(0).getText();
        } else {
            return "";
        }
    }

    private static String getTestCaseRequirementsFromTable(XWPFTableRow row4) {
        if (!Utils.isBlank(row4.getCell(1).getText())) {
            return row4.getCell(1).getText();
        } else {
            return "";
        }

    }

    private static void goToNextTable() {
        tableNuber++;
    }

    ;
    
    private static void resetToFirstTable() {
        tableNuber = 0;
    }
;

}
