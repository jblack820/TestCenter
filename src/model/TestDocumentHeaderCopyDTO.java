
package model;

import java.io.Serializable;

public class TestDocumentHeaderCopyDTO implements Serializable {
    
    private String projectName;
    private String projectVersionNumber;
    private String testConductedBy;
    private String dateOfTest;
    
    
    private String testCaseId;
    private String areaUnderTest;
    private String projetType;
    private String browswerOrOyType;
    
    
    private String testCaseTitle;
    private String appLocation;
    
    private String decriptionofTest;
    private String requirementsOfTest;
    

    //<editor-fold defaultstate="collapsed" desc="CONSTRUCTOR">

    public TestDocumentHeaderCopyDTO(String projectName, String projectVersionNumber, String testConductedBy, String dateOfTest, String testCaseId, String areaUnderTest, String projetType, String browswerOrOyType, String testCaseTitle, String appLocation, String decriptionofTest, String requirementsOfTest) {
        this.projectName = projectName;
        this.projectVersionNumber = projectVersionNumber;
        this.testConductedBy = testConductedBy;
        this.dateOfTest = dateOfTest;
        
        this.testCaseId = testCaseId;
        this.areaUnderTest = areaUnderTest;
        this.projetType = projetType;
        this.browswerOrOyType = browswerOrOyType;
        
        this.testCaseTitle = testCaseTitle;
        this.appLocation = appLocation;
        this.decriptionofTest = decriptionofTest;
        this.requirementsOfTest = requirementsOfTest;
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

    public String getBrowswerOrOyType() {
        return browswerOrOyType;
    }

    public void setBrowswerOrOyType(String browswerOrOyType) {
        this.browswerOrOyType = browswerOrOyType;
    }

    public String getTestCaseTitle() {
        return testCaseTitle;
    }

    public void setTestCaseTitle(String testCaseTitle) {
        this.testCaseTitle = testCaseTitle;
    }

    public String getAppLocation() {
        return appLocation;
    }

    public void setAppLocation(String appLocation) {
        this.appLocation = appLocation;
    }

    public String getDecriptionofTest() {
        return decriptionofTest;
    }

    public void setDecriptionofTest(String decriptionofTest) {
        this.decriptionofTest = decriptionofTest;
    }

    public String getRequirementsOfTest() {
        return requirementsOfTest;
    }

    public void setRequirementsOfTest(String requirementsOfTest) {
        this.requirementsOfTest = requirementsOfTest;
    }
    



//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="TOSTRING">

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TestDocumentHeaderCopyDTO{projectName=").append(projectName);
        sb.append(", projectVersionNumber=").append(projectVersionNumber);
        sb.append(", testConductedBy=").append(testConductedBy);
        sb.append(", dateOfTest=").append(dateOfTest);
        sb.append(", testCaseId=").append(testCaseId);
        sb.append(", areaUnderTest=").append(areaUnderTest);
        sb.append(", projetType=").append(projetType);
        sb.append(", browswerOrOyType=").append(browswerOrOyType);
        sb.append(", testCaseTitle=").append(testCaseTitle);
        sb.append(", appLocation=").append(appLocation);
        sb.append(", decriptionofTest=").append(decriptionofTest);
        sb.append(", requirementsOfTest=").append(requirementsOfTest);
        sb.append('}');
        return sb.toString();
    }
  
    //</editor-fold>
    
}
    

