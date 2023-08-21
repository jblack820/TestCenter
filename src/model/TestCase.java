package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class TestCase implements Serializable {

    //section one
    private String projectName;
    private String productVersionNumber;    
    private String testConductedBy;
    private LocalDate dateOfTest;
    
    private String testCaseId;
    private String areaUnderTest;
    private String projectType;
    private String browserUsed;
    
    private String nameOfTestCase;
    private String appLocation;
    private String testCaseDescription;
    private String testRequirements;
    
    //section two
    private final List<TestStep> TEST_STEPS;
    
    //section three
    private TestResult testResult;
    private DefectType defectType;
    private int numberOfImagesAttached;
    private String descriptionOfDefect;
    
    private StringProperty nameOfTestCaseProperty;
    private StringProperty testCaseIdProperty;
    
    private DefectLog defectLog;
    

    //<editor-fold defaultstate="collapsed" desc="constructors">

    public TestCase() {
        this.TEST_STEPS = new ArrayList<>();
    }

    public TestCase(
            String projectName,
            String projectVersionNumber,
            String projectType,
            String testConductedBy,
            String testCaseId,
            String opSystemBrowswer,
            String moduleName,
            LocalDate dateOfTest,
            String nameOfTest,
            String appLocation,
            String testCaseDescription,
            String testRequirements,            
            TestResult testResult,
            DefectType defectType,
            int numberOfImagesAttached,
            String descriptionOfDefect) {

        this.projectName = projectName;
        this.productVersionNumber = projectVersionNumber;
        this.projectType = projectType;
        this.testConductedBy = testConductedBy;
        this.testCaseId = testCaseId;
        this.browserUsed = opSystemBrowswer;
        this.areaUnderTest = moduleName;
        this.dateOfTest = dateOfTest;
        this.nameOfTestCase = nameOfTest;
        this.appLocation = appLocation;
        this.testCaseDescription = testCaseDescription;
        this.testRequirements = testRequirements;
        this.TEST_STEPS = new ArrayList<>();
        this.testResult = testResult;
        this.defectType = defectType;
        this.numberOfImagesAttached = numberOfImagesAttached;
        this.descriptionOfDefect = descriptionOfDefect;
        this.testCaseIdProperty = new SimpleStringProperty(this.testCaseId);
        this.nameOfTestCaseProperty = new SimpleStringProperty(this.nameOfTestCase);
        
    }
    
    
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getters/setters..etc">

    public String getProjectName() {
        return projectName;
    }

    public String getProductVersionNumber() {
        return productVersionNumber;
    }

    public String getTestConductedBy() {
        return testConductedBy;
    }

    public String getTestCaseId() {
        return testCaseId;
    }

    public String getModuleName() {
        return areaUnderTest;
    }

    public String getOpSystemBrowswer() {
        return browserUsed;
    }

    public LocalDate getDateOfTest() {
        return dateOfTest;
    }

    public String getNameOfTest() {
        return nameOfTestCase;
    }

    public String getAppLocation() {
        return appLocation;
    }

    public String getTestCaseDescription() {
        return testCaseDescription;
    }

    public String getTestRequirements() {
        return testRequirements;
    }

    public List<TestStep> getTEST_STEPS() {
        return TEST_STEPS;
    }

    public TestResult getTestResult() {
        return testResult;
    }

    public DefectType getDefectType() {
        return defectType;
    }

    public int getNumberOfImagesAttached() {
        return numberOfImagesAttached;
    }

    public String getDescriptionOfDefect() {
        return descriptionOfDefect;
    }

    public StringProperty getNameOfTestCaseProperty() {
        return nameOfTestCaseProperty;
    }

    public StringProperty getTestCaseIdProperty() {
        return testCaseIdProperty;
    }

    public DefectLog getDefectLog() {
        return defectLog;
    }

    public void setDefectLog(DefectLog defectLog) {
        this.defectLog = defectLog;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setProductVersionNumber(String productVersionNumber) {
        this.productVersionNumber = productVersionNumber;
    }

    public String getBrowserAppTestedOn() {
        return browserUsed;
    }

    public void setBrowserAppTestedOn(String browserAppTestedOn) {
        this.browserUsed = browserAppTestedOn;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public void setTestConductedBy(String testConductedBy) {
        this.testConductedBy = testConductedBy;
    }

    public void setTestCaseId(String testCaseId) {
        this.testCaseId = testCaseId;
    }

    public void setTestAreaName(String moduleName) {
        this.areaUnderTest = moduleName;
    }

    public void setOpSystemBrowswer(String opSystemBrowswer) {
        this.browserUsed = opSystemBrowswer;
    }

    public void setDateOfTest(LocalDate dateOfTest) {
        this.dateOfTest = dateOfTest;
    }

    public void setNameOfTestCase(String nameOfTest) {
        this.nameOfTestCase = nameOfTest;
    }

    public void setAppLocation(String appLocation) {
        this.appLocation = appLocation;
    }

    public void setTestCaseDescription(String testCaseDescription) {
        this.testCaseDescription = testCaseDescription;
    }

    public void setTestCaseRequirements(String testRequirements) {
        this.testRequirements = testRequirements;
    }

    public void setTestResult(TestResult testResult) {
        this.testResult = testResult;
    }

    public void setDefectType(DefectType defectType) {
        this.defectType = defectType;
    }

    public void setNumberOfImagesAttached(int numberOfImagesAttached) {
        this.numberOfImagesAttached = numberOfImagesAttached;
    }

    public void setDescriptionOfDefect(String descriptionOfDefect) {
        this.descriptionOfDefect = descriptionOfDefect;
    }

    
    public void addDefectLog (DefectLog defectLog){
        if (null==this.getDefectLog()){
            this.defectLog = defectLog;
        }      
    
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n\nTestCase{projectName=").append(projectName);
        sb.append(", \nprojectVersionNumber=").append(productVersionNumber);
        sb.append(", \ntestConductedBy=").append(testConductedBy);
        sb.append(", \ntestCaseId=").append(testCaseId);
        sb.append(", \nmoduleName=").append(areaUnderTest);
        sb.append(", \nappType=").append(projectType);
        sb.append(", \nopSystemBrowswer=").append(browserUsed);
        sb.append(", \ndateOfTest=").append(dateOfTest);
        sb.append(", \nnameOfTest=").append(nameOfTestCase);
        sb.append(", \nappLocation=").append(appLocation);
        sb.append(",\ntestCaseDescription=").append(testCaseDescription);
        sb.append(", \ntestRequirements=").append(testRequirements);
        sb.append(", \nTEST_STEPS=").append(TEST_STEPS);
        sb.append(", \ntestResult=").append(testResult);
        sb.append(", \ndefectType=").append(defectType);
        sb.append(", \nnumberOfImagesAttached=").append(numberOfImagesAttached);
        sb.append(", \ndescriptionOfDefect=").append(descriptionOfDefect);
        sb.append('}');
        return sb.toString();
    }
//</editor-fold>

    public List<TestStep> getAllFailedTestSteps() {

        List<TestStep> failedTestSteps = new ArrayList<>();
        for (TestStep testStep : TEST_STEPS) {
            if (testStep.isIsFailed()) {
                failedTestSteps.add(testStep);
            }
        }
        return failedTestSteps;
    }
    
    


}
