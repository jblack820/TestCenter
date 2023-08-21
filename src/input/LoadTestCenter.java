package input;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.TestCenter;


public class LoadTestCenter {

    public TestCenter load(TestCenter tc) {

        File testCenterFile = getLatestDatFile(tc);

        TestCenter testCenter = null;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(testCenterFile));) {

            testCenter = (TestCenter) ois.readObject();

        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(LoadTestCenter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return testCenter;
    }

    public File getLatestDatFile(TestCenter testCenter) {
        File testCenterFile;
        File folder = new File(testCenter.getFolderStructure().getTestCenterLoadLocation());
        File[] files = folder.listFiles();

        if (files.length < 1) {
            return null;
        }

        if (files.length == 1) {
            return files[0];
        }

        long max = files[0].lastModified();
        testCenterFile = files[0];

        for (int i = 1; i < files.length; i++) {

            if (files[i].lastModified() > max) {
                testCenterFile = files[i];
            }
        }
        return testCenterFile;
    }

}
