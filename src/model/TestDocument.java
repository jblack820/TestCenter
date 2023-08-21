package model;

import input.ReadTemplate;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

/**
 *
 * @author Greg
 */
public class TestDocument implements Serializable, Comparable<TestDocument> {

    private SimpleStringProperty modulName;
    private String documentCodeName;
    private File wordFile;
    private final List<TestCase> TEST_CASES;

    public TestDocument(String modulName, File wordFile, String documentCodeName) {
        this.modulName = new SimpleStringProperty(modulName);
        this.wordFile = wordFile;
        this.documentCodeName = documentCodeName;
        this.TEST_CASES = new ArrayList<>();
    }

    public List<TestCase> getFailedTestCases() {
        return getTEST_CASES()
                .stream()
                .filter(p -> p.getTestResult() == TestResult.FAILED)
                .collect(Collectors.toList());
    }

    public List<TestCase> getTestCasesOnlyWarnings() {
        return getTEST_CASES()
                .stream()
                .filter(p -> p.getTestResult() != TestResult.FAILED)
                .filter(p -> (p.getDescriptionOfDefect().length()) > 1)
                .collect(Collectors.toList());
    }

    public List<TestCase> getTestCasesFailedPlusWithWarnings() {
        return getTEST_CASES()
                .stream()
                .filter(p -> (p.getDescriptionOfDefect().length()) > 1)
                .collect(Collectors.toList());
    }

    public List<TestCase> getCompletedTestCases() {
        return getTEST_CASES()
                .stream()
                .filter(p -> p.getTestResult() != TestResult.NOT_COMPLETED_YET)
                .collect(Collectors.toList());
    }

    public String getModulName() {
        return modulName.get();
    }

    public SimpleStringProperty getModuleNameProperty() {
        return modulName;
    }

    public File getWordFile() {

        return wordFile;
    }
    
    public XWPFDocument getXWPFdocument (){
        XWPFDocument document = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(wordFile.getAbsolutePath());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TestDocument.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            document = new XWPFDocument(fis);
        } catch (IOException ex) {
            Logger.getLogger(ReadTemplate.class.getName()).log(Level.SEVERE, null, ex);
        }
        return document;
    
    }

    public String getDocumentCodeName() {
        return documentCodeName;
    }

    public void setDocumentCodeName(String documentCodeName) {
        this.documentCodeName = documentCodeName;
    }
    
    
    

    public List<TestCase> getTEST_CASES() {
        return TEST_CASES;
    }

    public void setModulName(String modulName) {
        this.modulName.set(modulName);
    }

    public void setWordFile(File wordFile) {
        this.wordFile = wordFile;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.modulName);
        hash = 23 * hash + Objects.hashCode(this.documentCodeName);
        hash = 23 * hash + Objects.hashCode(this.wordFile);
        hash = 23 * hash + Objects.hashCode(this.TEST_CASES);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TestDocument other = (TestDocument) obj;
        if (!Objects.equals(this.documentCodeName, other.documentCodeName)) {
            return false;
        }
        if (!Objects.equals(this.modulName, other.modulName)) {
            return false;
        }
        if (!Objects.equals(this.wordFile, other.wordFile)) {
            return false;
        }
        if (!Objects.equals(this.TEST_CASES, other.TEST_CASES)) {
            return false;
        }
        return true;
    }
    
    
    

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TestDocument{modulName=").append(modulName);
        sb.append(", documentCodeName=").append(documentCodeName);
        sb.append(", wordFile=").append(wordFile);
        sb.append(", TEST_CASES=").append(TEST_CASES);
        sb.append('}');
        return sb.toString();
    }

   

    @Override
    public int compareTo(TestDocument o) {
        return this.getModulName().compareTo(o.getModulName());
    }

}
