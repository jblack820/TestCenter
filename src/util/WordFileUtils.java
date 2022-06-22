package util;

import config.TestDocumentConfig;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class WordFileUtils {

    
    

    public static List<File> getAllMicrosoftLockFiles(String folderPath) {
        List<File> lockFiles = new ArrayList<>();
        File[] files = new File(folderPath).listFiles();

        if (files.length > 0) {
            for (File file : files) {
                if (file.getName().substring(0, 2).equalsIgnoreCase(TestDocumentConfig.WORD_DOCUMENT_LOCK_FILE_PREFIX)) {
                    lockFiles.add(file);
                }
            }
        }
        return lockFiles;
    }

    public static Map<String, String> extractLockFilesNamesAndOwners(List<File> files) {
        Map<String, String> namesAndOwners = new HashMap<>();
        for (File file : files) {
            namesAndOwners.put(file.getName().substring(2), getOwnerOfLockFile(file));
        }
        return namesAndOwners;
    }

    public static boolean isWordFileLocked(String path) {
        String wordFileName = getWordFileNameFromFullPath(path);
        String folderPath = path.substring(0, path.length() - wordFileName.length());
        String lockFileName = TestDocumentConfig.WORD_DOCUMENT_LOCK_FILE_PREFIX + wordFileName.substring(2);
        File folder = new File(folderPath);
        List<String> filesInDirectory = Arrays.asList(folder.list());
        return filesInDirectory.contains(lockFileName);
    }
    
    public static String getOwnerOfOpenedWordFile (String openedWordFilePath){
        String wordFileName = getWordFileNameFromFullPath(openedWordFilePath);
        String folderPath = openedWordFilePath.substring(0, openedWordFilePath.length() - wordFileName.length());
        String lockFileName = TestDocumentConfig.WORD_DOCUMENT_LOCK_FILE_PREFIX + wordFileName.substring(2);
        return getOwnerOfLockFile(new File(folderPath+lockFileName));
        
    }

//<editor-fold defaultstate="collapsed" desc="PRIVATE METHODS">
    private static String getOwnerOfLockFile(File lockFile) {
        String s = "";
        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(lockFile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(WordFileUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        byte[] allBytes = new byte[(int) lockFile.length()];

        try {
            inputStream.read(allBytes);
        } catch (IOException ex) {
            Logger.getLogger(WordFileUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            inputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(WordFileUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        s = new String(allBytes, StandardCharsets.ISO_8859_1);

        return extractNameFromString(s);

    }

    private static String extractNameFromString(String s) {
        StringBuilder sb = new StringBuilder();
        int spaceCounter = 0;
        for (int i = 0; i < s.length(); i++) {
            char current = s.charAt(i);
            if (!isCommonLetter(current)) {
                spaceCounter++;
                if (spaceCounter == 2) {
                    break;
                } else {
                    sb.append(current);
                }
            } else {
                sb.append(current);
                spaceCounter = 0;
            }

        }
        sb.deleteCharAt(0);
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    private static boolean isCommonLetter(char current) {
        boolean isUppercaseLetter = current >= 'A' && current <= 'Z';
        boolean isLowercaseLetter = current >= 'a' && current <= 'z';
        return isLowercaseLetter || isUppercaseLetter;
    }

    

    

    public static String getWordFileNameFromFullPath(String path) {
        String pattern = Pattern.quote(System.getProperty("file.separator"));
        String pathFirstTwoCharRemoved = path.substring(2);
        List<String> pathComponentsList = Arrays.asList(pathFirstTwoCharRemoved.split(pattern));
        return pathComponentsList.get(pathComponentsList.size() - 1);

    }
//</editor-fold>

}
