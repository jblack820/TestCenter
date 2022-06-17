package output;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.TestCenter;

/**
 *
 * @author takacs.gergely
 */
public class SaveTestCenter {
    
    public void save(TestCenter testCenter) {
        
        File saveLocation = new File(testCenter.getFolderStructure().getTestCenterSaveLocation());
        
        try ( ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(saveLocation));) {
            
            oos.writeObject(testCenter);
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SaveTestCenter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SaveTestCenter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
