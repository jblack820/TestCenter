
package model;

import java.io.Serializable;

public class TestDocumentDTO implements Serializable {
    
    private String projectName;
    private String projectVersionNumber;
    private String testConductedBy;
    private String dateOfTest;
    
    
    private String testCaseId;
    private String areaUnderTest;
    private String projetType;
    private String browserUsed;
    
    private String nameOfTestCase;
    private String appLocation;
    
    
    private String descOfTest;
    private String preconditionsOfTest;

    //<editor-fold defaultstate="collapsed" desc="CONSTRUCTOR">
    
    
     public TestDocumentDTO(String projectName, String projectVersionNumber, String testConductedBy, String dateOfTest, 
             String testCaseId, String areaUnderTest, String projetType, String browserUsed, 
             String nameOfTestCase, String appLocation, 
             String descOfTest, String preconditionsOfTest) {
        this.projectName = projectName;
        this.projectVersionNumber = projectVersionNumber;
        this.testConductedBy = testConductedBy;
        this.dateOfTest = dateOfTest;
        this.testCaseId = testCaseId;
        this.areaUnderTest = areaUnderTest;
        this.projetType = projetType;
        this.browserUsed = browserUsed;
        this.nameOfTestCase = nameOfTestCase;
        this.appLocation = appLocation;
        this.descOfTest = descOfTest;
        this.preconditionsOfTest = preconditionsOfTest;
    }
    
//</editor-fold>
        
    //<editor-fold defaultstate="collapsed" desc="GETTERS AND SETTERS">
     
     public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectVersionNumber() {
        return projectVersionNumber;
    }

    public void setProjectVersionNumber(String projectVersionNumber) {
        this.projectVersionNumber = projectVersionNumber;
    }

    public String getTestConductedBy() {
        return testConductedBy;
    }

    public void setTestConductedBy(String testConductedBy) {
        this.testConductedBy = testConductedBy;
    }

    public String getDateOfTest() {
        return dateOfTest;
    }

    public void setDateOfTest(String dateOfTest) {
        this.dateOfTest = dateOfTest;
    }

    public String getTestCaseId() {
        return testCaseId;
    }

    public void setTestCaseId(String testCaseId) {
        this.testCaseId = testCaseId;
    }

    public String getAreaUnderTest() {
        return areaUnderTest;
    }

    public void setAreaUnderTest(String areaUnderTest) {
        this.areaUnderTest = areaUnderTest;
    }

    public String getProjetType() {
        return projetType;
    }

    public void setProjetType(String projetType) {
        this.projetType = projetType;
    }

    public String getBrowserUsed() {
        return browserUsed;
    }

    public void setBrowserUsed(String browserUsed) {
        this.browserUsed = browserUsed;
    }

    public String getNameOfTestCase() {
        return nameOfTestCase;
    }

    public void setNameOfTestCase(String nameOfTestCase) {
        this.nameOfTestCase = nameOfTestCase;
    }

    public String getAppLocation() {
        return appLocation;
    }

    public void setAppLocation(String appLocation) {
        this.appLocation = appLocation;
    }

    public String getDescOfTest() {
        return descOfTest;
    }

    public void setDescOfTest(String descOfTest) {
        this.descOfTest = descOfTest;
    }

    public String getPreconditionsOfTest() {
        return preconditionsOfTest;
    }

    public void setPreconditionsOfTest(String preconditionsOfTest) {
        this.preconditionsOfTest = preconditionsOfTest;
    }
     
     
   
//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="TOSTRING">
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TestDocumentDTO{projectName=").append(projectName);
        sb.append(", projectVersionNumber=").append(projectVersionNumber);
        sb.append(", testConductedBy=").append(testConductedBy);
        sb.append(", dateOfTest=").append(dateOfTest);
        sb.append(", testCaseId=").append(testCaseId);
        sb.append(", areaUnderTest=").append(areaUnderTest);
        sb.append(", projetType=").append(projetType);
        sb.append(", browserUsed=").append(browserUsed);
        sb.append(", nameOfTestCase=").append(nameOfTestCase);
        sb.append(", appLocation=").append(appLocation);
        sb.append(", descOfTest=").append(descOfTest);
        sb.append(", preconditionsOfTest=").append(preconditionsOfTest);
        sb.append('}');
        return sb.toString();
    }    
//</editor-fold>
    
}
