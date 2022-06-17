package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DefectLog implements Serializable {

    private String testProjectName;
    private ProductVersionNumber versionNumber;
    private LocalDate dateCreated;
    private LocalDate dateResolved;

    private String nameOfTest;
    private String module;
    private String reportedBy;
    private String fixedBy;

    private String id;
    private String environmentOSBrowser;

    private String descriptionOfDefect;
    private String expectedFunctionality;

    private DefectType defectType;
    private DefectPriority defectPriority;
    private List<TestStep> originalTestSteps;    
    private List<Integer> failedTestStepIndexes;
    private final List<DefectStatusRecord> defectStatusHistory;
    
    private final List<String> SCREENSHOT_IMAGES_PATHS;    

    //<editor-fold defaultstate="collapsed" desc="constructor,getter,setter,etc">
    public DefectLog(String testProjectName,
            ProductVersionNumber versionNumber,
            LocalDate dateResolved,
            String nameOfTest,
            String module,
            String reportedBy,
            String fixedBy,
            String id,
            String environmentOSBrowser,
            String descriptionOfDefect,
            String expectedFunctionality,
            DefectType defectType,
            DefectPriority defectPriority,
            List<TestStep> originalTestSteps,
            DefectStatus defectStatus
    ) {

        this.testProjectName = testProjectName;
        this.versionNumber = versionNumber;
        this.dateCreated = LocalDate.now();
        this.dateResolved = dateResolved;
        this.nameOfTest = nameOfTest;
        this.module = module;
        this.reportedBy = reportedBy;
        this.fixedBy = fixedBy;
        this.id = id;
        this.environmentOSBrowser = environmentOSBrowser;
        this.descriptionOfDefect = descriptionOfDefect;
        this.expectedFunctionality = expectedFunctionality;
        this.defectType = defectType;
        this.defectPriority = defectPriority;
        this.originalTestSteps = originalTestSteps;
        this.failedTestStepIndexes = new ArrayList<>();
        this.SCREENSHOT_IMAGES_PATHS = new ArrayList<>();
        this.defectStatusHistory = new ArrayList<>();
    }

    public List<DefectStatusRecord> getDefectStatusHistory() {
        return defectStatusHistory;
    }
    
    

    public String getTestProjectName() {
        return testProjectName;
    }

    public ProductVersionNumber getVersionNumber() {
        return versionNumber;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public LocalDate getDateResolved() {
        return dateResolved;
    }

    public String getNameOfTest() {
        return nameOfTest;
    }

    public String getModule() {
        return module;
    }

    public String getReportedBy() {
        return reportedBy;
    }

    public String getFixedBy() {
        return fixedBy;
    }

    public String getId() {
        return id;
    }

    public String getEnvironmentOSBrowser() {
        return environmentOSBrowser;
    }

    public String getDescriptionOfDefect() {
        return descriptionOfDefect;
    }

    public String getExpectedFunctionality() {
        return expectedFunctionality;
    }

    public DefectType getDefectType() {
        return defectType;
    }

    public DefectPriority getDefectPriority() {
        return defectPriority;
    }

    public List<TestStep> getOriginalTestSteps() {
        return originalTestSteps;
    }

    public List<String> getSCREENSHOT_IMAGES_PATHS() {
        return SCREENSHOT_IMAGES_PATHS;
    }

    public void setTestProjectName(String testProjectName) {
        this.testProjectName = testProjectName;
    }

    public void setVersionNumber(ProductVersionNumber versionNumber) {
        this.versionNumber = versionNumber;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setDateResolved(LocalDate dateResolved) {
        this.dateResolved = dateResolved;
    }

    public void setNameOfTest(String nameOfTest) {
        this.nameOfTest = nameOfTest;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public void setReportedBy(String reportedBy) {
        this.reportedBy = reportedBy;
    }

    public void setFixedBy(String fixedBy) {
        this.fixedBy = fixedBy;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEnvironmentOSBrowser(String environmentOSBrowser) {
        this.environmentOSBrowser = environmentOSBrowser;
    }

    public void setDescriptionOfDefect(String descriptionOfDefect) {
        this.descriptionOfDefect = descriptionOfDefect;
    }

    public void setExpectedFunctionality(String expectedFunctionality) {
        this.expectedFunctionality = expectedFunctionality;
    }

    public void setDefectType(DefectType defectType) {
        this.defectType = defectType;
    }

    public void setDefectPriority(DefectPriority defectPriority) {
        this.defectPriority = defectPriority;
    }

    public void setOriginalTestSteps(List<TestStep> originalTestSteps) {
        this.originalTestSteps = originalTestSteps;
    }

    public DefectStatusRecord getCurrentDefectStatus(){
        if (defectStatusHistory.isEmpty()){return null;}
        return defectStatusHistory.get(defectStatusHistory.size()-1);
    }
    
    public void updateDefectStatusRecorHistory (DefectStatusRecord record){
        defectStatusHistory.add(record);
    }

    //</editor-fold>
}
