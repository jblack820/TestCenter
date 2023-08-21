package util;

import app.Main;
import config.AppConfig;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.TestProject;

public class ArchiveProjectUtils {

    public static void archiveProject(TestProject tp) {

        String projectPath = tp.getProjectFolderPath();

        String archivedPath = projectPath.replace(AppConfig.ACTIVE_PROJECTS_FOLDERNAME, AppConfig.ARCHIVED_PROJECTS_FOLDERNAME);

        tp.setProjectFolderPath(archivedPath);

        try {
            Files.move(new File(projectPath).toPath(), new File(archivedPath).toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            Logger.getLogger(ArchiveProjectUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void reactivateArchivedProject(String projectFolderName) {

        String archivedPath = Main.controller.getTestCenter()
                .getFolderStructure().getArchivedProjectsLocation()
                + File.separator
                + projectFolderName;
        
        String reActivatePath = Main.controller.getTestCenter()
                .getFolderStructure().getActiveProjectsLocation()
                + File.separator
                + projectFolderName;
        
        try {
            Files.move(new File(archivedPath).toPath(), new File(reActivatePath).toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            Logger.getLogger(ArchiveProjectUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }

    
    
}
