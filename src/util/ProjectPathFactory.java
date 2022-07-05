
package util;

import app.Main;
import java.io.File;

public class ProjectPathFactory {
    
    public static String getActiveProjectFolderPath (String folderName){
    
        return new StringBuilder()
                .append(Main.controller.getTestCenter().getFolderStructure().getActiveProjectsLocation())
                .append(folderName)
                .append(File.separator)
                .toString();
    }
    
    
    
}
