package input;

import config.FolderStructure;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author takacs.gergely
 */
public class LoadSettings {

    public FolderStructure load() throws FileNotFoundException {

        FolderStructure settings = null;

        String configFilePath = System.getProperty("user.home") + File.separator + ".testcenter" + File.separator + "testcenterlocation.txt";
        File configFile = new File(configFilePath);

        if (configFile.exists()) {
            BufferedReader br = null;
            br = new BufferedReader(new FileReader(configFile));
            String testCenterLocation = null;
            try {
                testCenterLocation = br.readLine();
            } catch (IOException ex) {
                Logger.getLogger(LoadSettings.class.getName()).log(Level.SEVERE, null, ex);
            }
            settings = new FolderStructure(testCenterLocation);
        }

        return settings;
    }

    public String getTextCenterLocationFromTXT() {

        String answer = null;

        String configFilePath = System.getProperty("user.home") + File.separator + ".TestCenter" + File.separator + "testcenterlocation.txt";
        File configFile = new File(configFilePath);

        if (configFile.exists()) {
            BufferedReader br = null;

            try {
                br = new BufferedReader(new FileReader(configFile));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(LoadSettings.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                answer = br.readLine();
            } catch (IOException ex) {
                Logger.getLogger(LoadSettings.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return answer;

    }

    private String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }

}
