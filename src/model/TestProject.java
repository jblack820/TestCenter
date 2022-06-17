package model;

import config.AppConfig;
import java.io.File;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class TestProject implements Serializable {

    private SimpleStringProperty projectName;
    private SimpleStringProperty codeNAme;
    private final String folderName;
    private ProjectType projectType;
    private String ProjectFolderPath;
    private ObjectProperty<LocalDate> dateStarted;
    private LocalDate projectDeadline;
    private String appLocation;
    private String browserOrOperationSystemTestedOn;
    private List<TestDocument> testDocuments;
    private final List<ProductVersionNumber> PRODUCT_VERSION_NUMBERS;
    private final List<TestProjectSnapshot> TEST_PROJECT_SNAPSHOTS;

    //<editor-fold defaultstate="collapsed" desc="constructor,getters/setters..etc">
    public TestProject(String projectName, LocalDate DATE_STARTED, LocalDate projectDeadline) {
        this.projectName = new SimpleStringProperty(projectName);
        this.projectDeadline = projectDeadline;
        this.testDocuments = new ArrayList<>();
        this.dateStarted = new SimpleObjectProperty<>(DATE_STARTED);
        this.folderName = createFolderName(this.projectName);
        this.ProjectFolderPath = AppConfig.ACTIVE_PROJECTS_FOLDERNAME + this.folderName;
        this.PRODUCT_VERSION_NUMBERS = new ArrayList<>();
        this.TEST_PROJECT_SNAPSHOTS = new ArrayList<>();
        this.codeNAme = new SimpleStringProperty();
    }

    public TestProject(String projectName, String shortName, LocalDate DATE_STARTED, LocalDate projectDeadline) {
        this.projectName = new SimpleStringProperty(projectName);
        this.projectDeadline = projectDeadline;
        this.testDocuments = new ArrayList<>();
        this.dateStarted = new SimpleObjectProperty<>(DATE_STARTED);
        this.folderName = createFolderName(this.projectName);
        this.ProjectFolderPath = AppConfig.ACTIVE_PROJECTS_FOLDERNAME + this.folderName;
        this.PRODUCT_VERSION_NUMBERS = new ArrayList<>();
        this.TEST_PROJECT_SNAPSHOTS = new ArrayList<>();
        this.codeNAme = new SimpleStringProperty(shortName);
    }

    public TestProject(String projectName, String shortName, LocalDate DATE_STARTED, LocalDate projectDeadline, String appLocation, String browser) {
        this.projectName = new SimpleStringProperty(projectName);
        this.projectDeadline = projectDeadline;
        this.testDocuments = new ArrayList<>();
        this.dateStarted = new SimpleObjectProperty<>(DATE_STARTED);
        this.folderName = createFolderName(this.projectName);
        this.ProjectFolderPath = AppConfig.ACTIVE_PROJECTS_FOLDERNAME + this.folderName;
        this.PRODUCT_VERSION_NUMBERS = new ArrayList<>();
        this.TEST_PROJECT_SNAPSHOTS = new ArrayList<>();
        this.codeNAme = new SimpleStringProperty(shortName);
        this.appLocation = appLocation;
        this.browserOrOperationSystemTestedOn = browser;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setProjectNameProperty(SimpleStringProperty projectName) {
        this.projectName = projectName;
    }

    public ProjectType getProjectType() {
        return projectType;
    }

    public void setProjectType(ProjectType projectType) {
        this.projectType = projectType;
    }

    public String getProjectTypeName() {
        return this.projectType.getProjectTypeName();
    }

    public void setProjectName(String projectName) {
        this.projectName.set(folderName);
    }

    public void setCodeNAme(SimpleStringProperty codeNAme) {
        this.codeNAme = codeNAme;
    }

    public void setCodeNAme(String codeNAme) {
        this.codeNAme.set(codeNAme);
    }

    public void setDATE_STARTED(ObjectProperty<LocalDate> dateStarted) {
        this.dateStarted = dateStarted;
    }

    public ObjectProperty<LocalDate> getDateStartedProperty() {
        return dateStarted;
    }

    public LocalDate getDateStarted() {
        return dateStarted.get();
    }

    public List<ProductVersionNumber> getProductVersionNumbers() {
        return PRODUCT_VERSION_NUMBERS;
    }

    public List<TestProjectSnapshot> getTestProjectSnapshots() {
        return TEST_PROJECT_SNAPSHOTS;
    }

    public void setProjectNameProperty(String projectName) {
        this.projectName.set(projectName);
    }

    public void setProjectFolderPath(String ProjectFolderPath) {
        this.ProjectFolderPath = ProjectFolderPath;
    }

    public void setProjectDeadline(LocalDate projectDeadline) {
        this.projectDeadline = projectDeadline;
    }

    public String getProjectName() {
        return projectName.get();
    }

    public SimpleStringProperty getProjectNameProperty() {
        return projectName;
    }

    public String getCodeNAme() {
        return codeNAme.get();
    }

    public SimpleStringProperty getCodeNAmeProperty() {
        return codeNAme;
    }

    public String getProjectFolderPath() {
        return ProjectFolderPath;
    }

    public LocalDate getProjectDeadline() {
        return projectDeadline;
    }

    public List<TestDocument> getTestDocuments() {
        return testDocuments;
    }

    public ProductVersionNumber getLatestVersionNum() {
        if (PRODUCT_VERSION_NUMBERS.isEmpty()) {
            return null;
        }
        return this.PRODUCT_VERSION_NUMBERS.get(PRODUCT_VERSION_NUMBERS.size()-1);
    }
    
      public String getLatestVersionNumber() {
        if (!PRODUCT_VERSION_NUMBERS.isEmpty()) {
            return PRODUCT_VERSION_NUMBERS.get(PRODUCT_VERSION_NUMBERS.size() - 1).getStringNumber();
        }
        return null;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.projectName);
        hash = 31 * hash + Objects.hashCode(this.codeNAme);
        hash = 31 * hash + Objects.hashCode(this.ProjectFolderPath);
        hash = 31 * hash + Objects.hashCode(this.dateStarted);
        hash = 31 * hash + Objects.hashCode(this.projectDeadline);
        hash = 31 * hash + Objects.hashCode(this.testDocuments);
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
        final TestProject other = (TestProject) obj;
        if (!Objects.equals(this.projectName, other.projectName)) {
            return false;
        }
        if (!Objects.equals(this.codeNAme, other.codeNAme)) {
            return false;
        }
        if (!Objects.equals(this.ProjectFolderPath, other.ProjectFolderPath)) {
            return false;
        }
        if (!Objects.equals(this.dateStarted, other.dateStarted)) {
            return false;
        }
        if (!Objects.equals(this.projectDeadline, other.projectDeadline)) {
            return false;
        }
        if (!Objects.equals(this.testDocuments, other.testDocuments)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TestProject{projectName=").append(projectName);
        sb.append(", codeNAme=").append(codeNAme);
        sb.append(", folderName=").append(folderName);
        sb.append(", projectType=").append(projectType);
        sb.append(", ProjectFolderPath=").append(ProjectFolderPath);
        sb.append(", DATE_STARTED=").append(dateStarted);
        sb.append(", projectDeadline=").append(projectDeadline);
        sb.append(", appLocation=").append(appLocation);
        sb.append(", browserTestedOn=").append(browserOrOperationSystemTestedOn);
        sb.append(", modules=").append(testDocuments);
        sb.append(", PRODUCT_VERSION_NUMBERS=").append(PRODUCT_VERSION_NUMBERS);
        sb.append(", TEST_PROJECT_SNAPSHOTS=").append(TEST_PROJECT_SNAPSHOTS);
        sb.append('}');
        return sb.toString();
    }

//</editor-fold>
    public void createSnapshot() {

        if (PRODUCT_VERSION_NUMBERS.size() > 0) {
            TEST_PROJECT_SNAPSHOTS.add(new TestProjectSnapshot(
                    testDocuments,
                    getLastElement(PRODUCT_VERSION_NUMBERS)));
        }
    }

    public List<TestDocument> getAllTestDocuments() {
        return testDocuments;
    }

    public List<XWPFDocument> getAllXWPFTestDocumnets() {
        return testDocuments
                .stream()
                .map(p -> p.getXWPFdocument())
                .collect(Collectors.toList());
    }

    public List<File> getWordFileList() {
        return this.getAllTestDocuments().stream().map(p -> p.getWordFile()).collect(Collectors.toList());
    }

    public XWPFDocument getXWPFDocument(File file) {

        System.out.println("searching for file: " + file.getAbsolutePath());

        return testDocuments
                .stream()
                .filter(p -> p.getWordFile().getAbsolutePath().equalsIgnoreCase(file.getAbsolutePath()))
                .findFirst()
                .get()
                .getXWPFdocument();
    }

    public String getAppLocation() {
        return appLocation;
    }

    public List<TestCase> getAllTestCasesDone() {
        return getAllTestcases()
                .stream()
                .filter(p -> !p.getTestResult().equals(TestResult.NOT_COMPLETED_YET))
                .collect(Collectors.toList());
    }

    public void setAppLocation(String appLocation) {
        this.appLocation = appLocation;
    }

    public String getBrowserTestedOn() {
        return browserOrOperationSystemTestedOn;
    }
    
    public double getProgresRatePercent (){
        return getNumberOfCompletedTestCases() / getNumberOfAllTestCases();
    }

    public void setBrowserTestedOn(String browserTestedOn) {
        this.browserOrOperationSystemTestedOn = browserTestedOn;
    }  

    public void addNewVersionNumber(String versionNumber) {
        ProductVersionNumber newVersionNumber = new ProductVersionNumber(versionNumber);
        if (!isVersionNumberAlreadyRegistered(versionNumber)) {
            PRODUCT_VERSION_NUMBERS.add(newVersionNumber);
        }
    }

    public boolean isVersionNumberAlreadyRegistered(String versionNumber) {

        if (PRODUCT_VERSION_NUMBERS.isEmpty()) {
            return false;
        }

        return PRODUCT_VERSION_NUMBERS
                .stream()
                .filter(p -> p.getStringNumber().equalsIgnoreCase(versionNumber))
                .collect(Collectors.toList())
                .size() > 0;
    }

    public void refreshProjectVersionNumbers() {
        for (TestCase tc : getAllTestcases()) {
            String versionNumber = tc.getProductVersionNumber();
            if (!isVersionNumberAlreadyRegistered(versionNumber)) {
                PRODUCT_VERSION_NUMBERS.add(new ProductVersionNumber(versionNumber, tc.getDateOfTest()));
            }
        }
    }

    public String getDefectLogFolderLocation() {
        return this.ProjectFolderPath + AppConfig.DEFECT_LOGS_FOLDERNAME;
    }

    public <T> T getLastElement(List<T> t) {
        return t.get(t.size() - 1);
    }

    public void clearTestDocumentObjects() {
        this.testDocuments = new ArrayList<>();
    }

    public List<TestCase> getAllTestcases() {
        List<TestCase> list = new ArrayList<>();
        for (TestDocument testModule : getTestDocuments()) {
            list.addAll(testModule.getTEST_CASES());
        }
        return list;
    }

    public List<TestCase> getFailedTestcases() {
        return getAllTestcases()
                .stream()
                .filter(p -> p.getTestResult() == TestResult.FAILED)
                .collect(Collectors.toList());
    }

    public List<TestCase> getPassedTestcases() {
        return getAllTestcases()
                .stream()
                .filter(p -> p.getTestResult() == TestResult.PASSED)
                .collect(Collectors.toList());
    }

    public List<TestCase> getNotCompletedTestcases() {
        return getAllTestcases()
                .stream()
                .filter(p -> p.getTestResult() == TestResult.NOT_COMPLETED_YET)
                .collect(Collectors.toList());
    }
    
    public List<TestCase> getCompletedTestcases() {
        return getAllTestcases()
                .stream()
                .filter(p -> p.getTestResult() != TestResult.NOT_COMPLETED_YET)
                .collect(Collectors.toList());
    }

    public int getNumberOfAllTestCases() {
        return getAllTestcases().size();
    }

    public int getNumberOfFailedTestCases() {
        return getFailedTestcases().size();
    }

    public int getNumberOfPassedTestCases() {
        return getPassedTestcases().size();
    }

    public int getNumberOfNotCompletedTestcases() {
        return getNotCompletedTestcases().size();
    }
    
    public int getNumberOfCompletedTestCases (){
        return getCompletedTestcases().size();
    }

    public Map<TestDocument, List<TestCase>> getAllTestCasesFailedOrWithRemarkMappedBymodule() {
        Map<TestDocument, List<TestCase>> map = new TreeMap<>();
        for (TestDocument module : this.getTestDocuments()) {
            for (TestCase testCase : module.getTEST_CASES()) {
                if (testCase.getTestResult() == TestResult.FAILED
                        || testCase.getDescriptionOfDefect().length() > 1) {
                    if (!map.keySet().contains(module)) {
                        map.put(module, new ArrayList<>());
                        map.get(module).add(testCase);
                    } else {
                        map.get(module).add(testCase);
                    }
                }
            }
        }
        return map;
    }

    public String getModulesToString() {
        StringBuilder sb = new StringBuilder();
        for (TestDocument module : testDocuments) {
            sb.append(module).append("\n");
        }
        return sb.toString();
    }

    private String createFolderName(SimpleStringProperty projectName) {
        String source = projectName.get();

        return replaceInvalidCahracters(source).toUpperCase();
    }

    private String replaceInvalidCahracters(String source) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < source.length(); i++) {
            char currentChar = source.charAt(i);
            switch (currentChar) {
                case ' ':
                    sb.append('_');
                    break;
                case '.':
                    sb.append('_');
                    break;
                case '-':
                    sb.append('_');
                    break;
                default:
                    sb.append(currentChar);
            }
        }
        return sb.toString();
    }

    public TestCase getTestCaseById(String id) {
        TestCase tc = null;
        for (TestDocument testDocument : testDocuments) {
            for (TestCase testCase : testDocument.getTEST_CASES()) {
                if (testCase.getTestCaseId().equalsIgnoreCase(id)) {
                    tc = testCase;
                }
            }
        }
        return tc;
    }

    public TestDocument getTestDocumentByName(String name) {
        return testDocuments
                .stream()
                .filter(p -> p.getModulName().equalsIgnoreCase(name))
                .findFirst()
                .get();
    }
}
