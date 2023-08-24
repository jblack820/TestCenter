package input;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;

import config.AppConfig;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.net.URISyntaxException;
import java.net.URL;


public class ReadTemplate {

    public XWPFDocument getTemplate(String resourceName) {
        XWPFDocument document = null;
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource(resourceName).getFile());
            System.out.println("File exists: " + file.exists());
            InputStream inputStream = new FileInputStream(file);
            System.out.println("Thread: " + Thread.currentThread().getName());
            System.out.println("Fileinputstream ready: " + inputStream!=null);
            document = new XWPFDocument(inputStream);
            System.out.println("beolvasta");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return document;
    }

}

