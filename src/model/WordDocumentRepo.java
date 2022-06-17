
package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.xwpf.usermodel.XWPFDocument;


public class WordDocumentRepo implements Serializable {
    
    private final List<XWPFDocument> DOCUMENT_LIST = new ArrayList<>();

    public List<XWPFDocument> getDOCUMENTS() {
        return DOCUMENT_LIST;
    }

    public void addDocument(XWPFDocument document) {
        DOCUMENT_LIST.add(document);        
    }
    
    public void clear (){
        DOCUMENT_LIST.clear();
    }
    
    
    
    
}
