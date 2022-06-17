package util;

//import config.AppSettings;
import app.Main;
import config.AppConfig;
import java.io.File;

public class AppUtils {

    public static void createTestCenterFolderStructure(String path) {
        
        String tcFolderName = AppConfig.TEST_CENTER_FOLDERNAME;
        String tcFolderPath = Main.controller.getTestCenter().getFolderStructure().getTestCenterLocation();
        createFolder(path, tcFolderName);
        createFolder(tcFolderPath, AppConfig.ACTIVE_PROJECTS_FOLDERNAME);
        createFolder(tcFolderPath, AppConfig.ARCHIVED_PROJECTS_FOLDERNAME);
        
    }

    public static void createFolder(String path, String name) {        
        File directory = new File(path + name + "//");
        
        if (!directory.exists()) {
            directory.mkdirs();
            System.out.println("Folder created: " + name);
        } else {
            System.out.println("Folder creating failed: " + name);
        }

    }


}
