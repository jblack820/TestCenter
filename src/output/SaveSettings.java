package output;

import config.FolderStructure;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.AppUtils;

/**
 *
 * @author takacs.gergely
 */
public class SaveSettings {

    public void save(FolderStructure appSettings) {

        //todo
        
    }

    public void saveSettingsToUserHome(String TestCenterLocationpath) {

        String configFileFolderPath = System.getProperty("user.home") + File.separator + ".testcenter";

        File configFile = new File(configFileFolderPath + File.separator + "testcenterlocation.txt");

        if (!configFile.exists()) {

            AppUtils.createFolder(System.getProperty("user.home") + File.separator, ".testcenter");

            try {
                configFile.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(SaveSettings.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        PrintWriter pw = null;

        try {
            pw = new PrintWriter(new FileWriter(configFile));
        } catch (IOException ex) {
            Logger.getLogger(SaveSettings.class.getName()).log(Level.SEVERE, null, ex);
        }

        pw.write(TestCenterLocationpath);

        pw.flush();
        pw.close();

    }

}
