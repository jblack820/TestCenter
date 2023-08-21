package input;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.net.URISyntaxException;
import java.net.URL;


public class ReadTemplate {

    public XWPFDocument getTemplate(String resourceName) {
        XWPFDocument document = null;



        try {

            FileInputStream fileInputStream = new FileInputStream("C:\\templatedoc.docm");
            document = new XWPFDocument(fileInputStream);
            System.out.println("beolvasta");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return document;
    }

}

