package util;

//<editor-fold defaultstate="collapsed" desc="IMPORTS">
import app.Main;
import config.AppConfig;
import controller.TestCenterController;
import input.ReadTemplate;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.TestCase;
import model.TestDocumentCreationDTO;
import model.TestDocumentHeaderCopyDTO;
import model.TestProject;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
//</editor-fold>

/**
 *
 * @author takacs.gergely
 */
public class Utils {

    //<editor-fold defaultstate="collapsed" desc="OUTPUT STREAMS">
    public static FileOutputStream getTestDocumentOutputStream(TestDocumentCreationDTO templateData, TestProject project) {
        FileOutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(
                    project.getActiveProjectFolderPath()
                    + getFileSystemFriendyName(templateData.getProjectName())
                    + "//"
                    + constructWordDocumentFileName(templateData));

        } catch (FileNotFoundException ex) {
            Logger.getLogger(TestCenterController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return outputStream;
    }

    public static FileOutputStream getTestDocumentOutputStream(TestDocumentHeaderCopyDTO formData, TestProject targetproject) {
        FileOutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(
                    targetproject.getActiveProjectFolderPath()
                    + config.AppConfig.TEST_DOCUMENTS_FOLDERNAME
                    + "//"
                    + constructWordDocumentFileName(formData, targetproject));

        } catch (FileNotFoundException ex) {
            Logger.getLogger(TestCenterController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return outputStream;
    }

    public static FileOutputStream getTestDocumentOutputStream(TestProject targetproject, String wordFileName) {
        FileOutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(
                    targetproject.getActiveProjectFolderPath()
                    + config.AppConfig.TEST_DOCUMENTS_FOLDERNAME
                    + "//"
                    + wordFileName);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(TestCenterController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return outputStream;
    }

    public static FileOutputStream getOutputStream(TestDocumentCreationDTO formData, String path) {
        FileOutputStream outputStream = null;

        String consrtructedPath
                = path
                + "//"
                + constructWordDocumentFileName(formData);

        try {
            outputStream = new FileOutputStream(consrtructedPath);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TestCenterController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return outputStream;
    }

    public static FileOutputStream getDefectLogOutputStream(String fileName, File targetDir) {
        FileOutputStream outputStream = null;

        File file;
        String path = targetDir.getPath() + "//" + fileName + ".docx";

        file = new File(path);

        try {
            outputStream = new FileOutputStream(file);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(TestCenterController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return outputStream;
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="STRING UTILS">

    public static String constructWordDocumentFileName(TestDocumentCreationDTO formData) {
        StringBuilder filename = new StringBuilder();
        String datePrefix = getDateString();
        filename.append(datePrefix).append("_");

        filename.append(getExtendedProjectName(formData)).append("_");
        filename.append(getFileSystemFriendyName(formData.getAreaUnderTest()));
        filename.append(".docm");
        return filename.toString();
    }

    public static String constructWordDocumentFileName(TestDocumentHeaderCopyDTO formData, TestProject targetproject) {
        StringBuilder filename = new StringBuilder();
        String datePrefix = getDateString();
        filename.append(datePrefix).append("_");

        filename.append(getExtendedProjectName(formData, targetproject)).append("_");
        filename.append(getFileSystemFriendyName(formData.getAreaUnderTest()));
        filename.append(".docm");
        return filename.toString();
    }

    public static String getAbbrevation(TestDocumentCreationDTO formData) {
        String line = formData.getTestCaseId();
        StringBuilder sb = new StringBuilder();
        boolean isToBeSaved = false;

        for (int i = 0; i < line.length(); i++) {

            if (isToBeSaved) {
                sb.append(line.charAt(i));
            }

            if (line.charAt(i) == '-') {
                isToBeSaved = !isToBeSaved;
            }

        }
        return sb.toString();
    }

    public static String changeDateFormatForFilename(TestDocumentCreationDTO projectSettings) {
        String date = projectSettings.getDateOfTest();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < date.length(); i++) {
            if (date.charAt(i) == '.') {
                sb.append('_');
            } else {
                sb.append(date.charAt(i));
            }
        }
        return sb.toString();
    }

    public static String getFileSystemFriendyName(String text) {

        if (text.length() <= 2) {
            return text;
        }

        String source = text.toUpperCase();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < source.length(); i++) {

            switch (source.charAt(i)) {

                case 'Á':
                    sb.append('A');
                    break;
                case 'É':
                    sb.append('E');
                    break;
                case 'Ü':
                    sb.append('U');
                    break;
                case 'Ű':
                    sb.append('U');
                    break;
                case 'Ú':
                    sb.append('U');
                    break;
                case 'Ö':
                    sb.append('O');
                    break;
                case 'Ő':
                    sb.append('O');
                    break;
                case 'Ó':
                    sb.append('O');
                    break;
                case 'Í':
                    sb.append('I');
                    break;
                case ' ':
                    sb.append('_');
                    break;
                case '.':
                    sb.append('_');
                    break;
                default:
                    sb.append(source.charAt(i));
            }

        }
        return removeDoubleUnderscores(sb);
    }

    public static String removeDoubleUnderscores(StringBuilder sb) {

        Character current = '*';
        int textLength = sb.length();
        for (int i = 0; i < textLength; i++) {

            if (sb.charAt(i) == '_') {

                if (current == '_') {
                    sb.deleteCharAt(i);
                    textLength--;
                }
            }
            current = sb.charAt(i);
        }
        return sb.toString();
    }

    public static String convertPointsToDashes(String line) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == '.') {
                sb.append('-');
            } else {
                sb.append(line.charAt(i));
            }

        }
        return sb.toString();
    }

    public static String convertDateToHungarianTextFormat(LocalDate date) {
        StringBuilder sb = new StringBuilder();
        sb.append(date.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))).append(".");
        return sb.toString();

    }
    
    public static String getTimeStampWithUnderScores (){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(AppConfig.TIME_STAMP_FORMAT_PATTERN);
        StringBuilder sb = new StringBuilder();
        sb.append(LocalDateTime.now().format(formatter));
        sb.append("_");
        return sb.toString();
    }
    
    public static String addTimeStampToStringAtTheBeginning (String source){
        return new StringBuilder().append(getTimeStampWithUnderScores()).append(source).toString();
    }
    
    
    
//</editor-fold>   
    //<editor-fold defaultstate="collapsed" desc="DOCUMENT UTILS">

    public static XWPFDocument getWordDocument(File file) {
        XWPFDocument document = null;
        try {
            if (!file.isHidden()) {
                document = new XWPFDocument(new FileInputStream(file));
            }
        } catch (Exception ex) {
            Logger.getLogger(ReadTemplate.class.getName()).log(Level.SEVERE, null, ex);
        }
        return document;
    }

    public static Optional<String> getFileExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }

    public static List<TestCase> extractTestCaseObjectsFromXWPFDocument(XWPFDocument document) {
        if (isOldFormat(document)) {
            return TestCaseUtils.getTestCaseObjectsFromOldXWPFFormat(document);
        } else {
            return TestCaseUtils.getTestCaseObjectsFromNewXWPFFormat(document);
        }
    }

    public static String convertLocalDateToString(LocalDate localDate) {
        return new StringBuilder(localDate.toString().replaceAll("-", ".")).append(".").toString();
    }

    static boolean isBlank(String line) {
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) != ' ') {
                return false;
            }
        }
        return true;
    }


    public static void blankUnusedTestCases(List<File> files) throws FileNotFoundException {

        for (File file : files) {
            XWPFDocument document = getWordDocument(file);
            int counter = 0;
            for (XWPFTable table : document.getTables()) {
                if (counter == 2) {
                    for (XWPFTableRow row : table.getRows()) {
                        if (row.getCell(1).getText().equalsIgnoreCase("")) {

                            row.getCell(0).removeParagraph(0);
                        }
                    }
                    counter = -1;
                } else {
                    counter++;
                }
            }
            Main.controller.getDocumentSave().saveSingelPageDocument(document, new FileOutputStream(file));
            break;
        }
    }

    private static String getDateString() {
        return new StringBuilder()
                .append(LocalDate.now().getYear())
                .append(getTwoDigitFormat(LocalDate.now().getMonthValue()))
                .append(getTwoDigitFormat(LocalDate.now().getDayOfMonth()))
                .append('_')
                .append(getTwoDigitFormat(LocalDateTime.now().getHour()))
                .append(getTwoDigitFormat(LocalDateTime.now().getMinute()))
                .toString();
    }

    private static String getTwoDigitFormat(int num) {
        return num < 10 ? "0" + num : String.valueOf(num);
    }

    private static String getExtendedProjectName(TestDocumentHeaderCopyDTO formData, TestProject testProject) {
        return new StringBuilder()
                .append(testProject.getCodeNAme())
                .append("_")
                .append(testProject.getProjectType().getProjectTypeCodename()).toString();
    }

    private static String getExtendedProjectName(TestDocumentCreationDTO formData) {
        String text = formData.getTestCaseId();
        char dash = '-';
        int dashCounter = 0;
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == dash) {
                dashCounter++;
            }
            if (dashCounter == 2) {
                break;
            }
            if (text.charAt(i) == dash) {
                sb.append('_');
            } else {
                sb.append(text.charAt(i));
            }
        }
        return sb.toString();

    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="CREATE TEST CASE OBJECTS">

    private static boolean isOldFormat(XWPFDocument document) {
        return !document.getTables().isEmpty() && document.getTables().get(0).getRow(0).getCell(0).getText().contains("TESZT");
    }
//</editor-fold>

    public static boolean isBugTicketPresentToCurrentBug(TestCase failedTestCase) {
        String defectLogFolder = Main.controller.getCurrentTestProject().getActiveProjectFolderPath() + AppConfig.DEFECT_LOGS_FOLDERNAME;
        File[] files = new File(defectLogFolder).listFiles();
        if (null == files) {
            return false;
        }
        return Arrays.asList(files).stream().filter(p -> p.getName().contains(failedTestCase.getTestCaseId())).count() > 0;

    }

    public static String getPercent(double progressValue) {
        if (progressValue == 1) {
            return "100%";
        } else if (progressValue == 0) {
            return "0%";
        } else if (progressValue < 0.1) {
            return new StringBuilder(getPercentText(progressValue)).insert(1, ',').toString();
        } else {
            return getPercentText(progressValue);
        }
    }

    private static String getPercentText(double progressValue) {
        return new StringBuilder()
                .append(String.format("%.2f", progressValue).substring(2, 4))
                .append("%")
                .toString();
    }

}
