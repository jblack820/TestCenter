
package model;

import java.io.Serializable;

public class TestDocumentCreationDTO implements Serializable {
    
    private String projectName;
    private String projectVersionNumber;
    private String testConductedBy;
    private String dateOfTest;
    
    
    private String testCaseId;
    private String areaUnderTest;
    private String projetType;
    private String browserUsed;
    
    
    private String appLocation;
    
    private int numberOfPagesRequired;

    //<editor-fold defaultstate="collapsed" desc="CONSTRUCTOR">
    public TestDocumentCreationDTO(
            String projectName, String projectVersionNumber, String testConductedBy, String dateOfTest, 
            String testCaseId, String areaUnderTest, String projetType, String browserUsed, 
            String appLocation, int numberOfPagesRequired) {
        this.projectName = projectName;
        this.projectVersionNumber = projectVersionNumber;
        this.testConductedBy = testConductedBy;
        this.dateOfTest = dateOfTest;
        this.testCaseId = testCaseId;
        this.areaUnderTest = areaUnderTest;
        this.projetType = projetType;
        this.browserUsed = browserUsed;        
        this.appLocation = appLocation;
        this.numberOfPagesRequired = numberOfPagesRequired;
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
    

    
    public String getAppLocation() {
        return appLocation;
    }
    
    public void setAppLocation(String appLocation) {
        this.appLocation = appLocation;
    }
    
    public int getNumberOfPagesRequired() {
        return numberOfPagesRequired;
    }
    
    public void setNumberOfPagesRequired(int numberOfPagesRequired) {
        this.numberOfPagesRequired = numberOfPagesRequired;
    }
//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="TOSTRING">
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("FormData{projectName=").append(projectName);
        sb.append(", projectVersionNumber=").append(projectVersionNumber);
        sb.append(", testConductedBy=").append(testConductedBy);
        sb.append(", dateOfTest=").append(dateOfTest);
        sb.append(", testCaseId=").append(testCaseId);
        sb.append(", areaUnderTest=").append(areaUnderTest);
        sb.append(", projetType=").append(projetType);
        sb.append(", browserUsed=").append(browserUsed);
        sb.append(", appLocation=").append(appLocation);
        sb.append(", numberOfPagesRequired=").append(numberOfPagesRequired);
        sb.append('}');
        return sb.toString();
    }
//</editor-fold>
   
    
}
