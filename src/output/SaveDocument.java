package output;

//<editor-fold defaultstate="collapsed" desc="IMPORTS">
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.xmlbeans.XmlOptions;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;
import testcenterfx.CreateTestDocumentController;
//</editor-fold>

public class SaveDocument {

    public void saveSingelPageDocument(XWPFDocument document, FileOutputStream fileOutputStream) {
        try {
            document.write(fileOutputStream);
        } catch (IOException ex) {
            Logger.getLogger(SaveDocument.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void saveMultiPageDocument(List<XWPFDocument> documentList, FileOutputStream fileOutputStream) throws IOException {
        XWPFDocument outputXPFDoc = mergePages(documentList);
        outputXPFDoc.write(fileOutputStream);
        outputXPFDoc = null;
    }

    private void appendBody(XWPFDocument outputXPFDoc, XWPFDocument currentXWPFDoc) throws Exception {
        CTBody src1Body = outputXPFDoc.getDocument().getBody();
        CTBody src2Body = currentXWPFDoc.getDocument().getBody();
        appendBody(src1Body, src2Body);
    }

    private void appendBody(CTBody finalBody, CTBody currentBody) throws Exception {
        XmlOptions optionsOuter = new XmlOptions();
        optionsOuter.setSaveOuter();
        String appendString = currentBody.xmlText(optionsOuter);
        String srcString = finalBody.xmlText();
        String prefix = srcString.substring(0, srcString.indexOf(">") + 1);
        String mainPart = srcString.substring(srcString.indexOf(">") + 1, srcString.lastIndexOf("<"));
        String sufix = srcString.substring(srcString.lastIndexOf("<"));
        String addPart = appendString.substring(appendString.indexOf(">") + 1, appendString.lastIndexOf("<"));
        System.out.println("Vincent was here");
        CTBody makeBody = (CTBody) CTBody.Factory.parse(prefix + mainPart + addPart + sufix);
        finalBody.set(makeBody);
    }

    private XWPFDocument mergePages(List<XWPFDocument> documentList) {
        XWPFDocument outputXPFDoc = null;

        for (int i = 0; i < documentList.size(); i++) {
            if (null != CreateTestDocumentController.propertyChangeSupport) {
                CreateTestDocumentController.updateDoneValue(i + 1);
            }
            outputXPFDoc = documentList.get(0);
            if (i != 0) {
                XWPFDocument currentXWPFDoc = documentList.get(i);
                currentXWPFDoc.removeBodyElement(0);
                try {
                    appendBody(outputXPFDoc, currentXWPFDoc);

                } catch (Exception ex) {
                    Logger.getLogger(SaveDocument.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (i != documentList.size() - 1) {
                outputXPFDoc.createParagraph().setPageBreak(true);
            }
        }
        return outputXPFDoc;
    }

    private String getTime() {
        long yourmilliseconds = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        Date resultdate = new Date(yourmilliseconds);
        return sdf.format(resultdate);
    }

}
