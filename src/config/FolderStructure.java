package config;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDate;

public class FolderStructure implements Serializable {

    
    private String testCenterLocation;

    //<editor-fold defaultstate="collapsed" desc="Contructors, Getters, Setters">
    public FolderStructure(String testCenterLocation) {
        this.testCenterLocation = testCenterLocation;
    }

    public FolderStructure() {
    }

    public String getTestCenterLocation() {
        return testCenterLocation;
    }

    public String getActiveProjectsLocation() {
        return testCenterLocation + AppConfig.ACTIVE_PROJECTS_FOLDERNAME + File.separator;
    }

    public String getArchivedProjectsLocation() {
        return testCenterLocation + AppConfig.ARCHIVED_PROJECTS_FOLDERNAME + File.separator;
    }

    public String getDatFileLocation() {
        return testCenterLocation + AppConfig.TEST_CENTER_DAT_FILE_FOLDERNAME + File.separator;
    }

    public String getTestCenterLoadLocation() {
        return testCenterLocation + AppConfig.TEST_CENTER_DAT_FILE_FOLDERNAME + File.separator;
    }

    public String getTestCenterSaveLocation() {
        return createSaveFilelocation();
    }

    public void setTestCenterLocation(String testCenterLocation) {
        this.testCenterLocation = testCenterLocation;
    }
    
    

    
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Utils">
    public static String dashesToUnderScores(String input) {
        return input.replaceAll("-", "_");
    }
    
    public String createSaveFilelocation() {
        String answer = getDatFileLocation()
                + dashesToUnderScores(LocalDate.now().toString())
                + "_TESTCENTER_BACKUP.dat";
        return answer;
    }
//</editor-fold>

}
