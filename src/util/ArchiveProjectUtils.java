package util;

import app.Main;
import config.AppConfig;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import model.TestProject;
import testcenterfx.WelcomePageController;

public class ArchiveProjectUtils {

    public static void moveActiveProjectToArchivedFolder(TestProject tp) {
        String projectPath = tp.getActiveProjectFolderPath();
        String archivedPath = projectPath.replace(AppConfig.ACTIVE_PROJECTS_FOLDERNAME, AppConfig.ARCHIVED_PROJECTS_FOLDERNAME);
        boolean isProcessSuccesful = true;

        try {
            Files.move(new File(projectPath).toPath(), new File(archivedPath).toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            isProcessSuccesful = false;
            WelcomePageController.FAILED_TO_ARCHIVE_PROJECTS_LIST.add(tp);
        }
        if (isProcessSuccesful) {
            Main.controller.getTestCenter().getActiveTestProjects().remove(tp);
        }

    }

    public static void moveArchivedProjectToActiveFolder(String projectFolderName) {

        String archivedPath = Main.controller.getTestCenter()
                .getFolderStructure().getArchivedProjectsLocation()
                + File.separator
                + projectFolderName;

        String reActivatePath = Main.controller.getTestCenter()
                .getFolderStructure().getActiveProjectsLocation()
                + File.separator
                + projectFolderName;

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Files.move(new File(archivedPath).toPath(), new File(reActivatePath).toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ex) {
                    WelcomePageController.isReactivateProcessFailed = true;
                    Logger.getLogger(ArchiveProjectUtils.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

    }

}
