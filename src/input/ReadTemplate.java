package input;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class ReadTemplate {

    public XWPFDocument getTemplate(String fileLocation) {
        XWPFDocument document = null;
        
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileLocation);        
        
        try {
            document = new XWPFDocument(inputStream);
        } catch (IOException ex) {
            Logger.getLogger(ReadTemplate.class.getName()).log(Level.SEVERE, null, ex);
        }
        return document;
    }

}

