package model;

import config.FolderStructure;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * @author Greg
 */
public class TestCenter implements Serializable {

    private FolderStructure folderStructure;
    private final List<TestProject> ACTIVE_TEST_PROJECTS;
    private final List<TestProject> ARCHIVED_TEST_PROJECTS;
    private final List<User> USERS;
    private final List<TestDevice> TEST_DEVICES;
    private final List<ArchivedProjectName> ARCHIVED_PROJECT_NAMES;

    public TestCenter() {
        folderStructure = new FolderStructure();
        ACTIVE_TEST_PROJECTS = new ArrayList<>();
        ARCHIVED_TEST_PROJECTS = new ArrayList<>();
        USERS = new ArrayList<>();
        TEST_DEVICES = new ArrayList<>();
        ARCHIVED_PROJECT_NAMES = new ArrayList<>();
    }

    public List<ArchivedProjectName> getARCHIVED_PROJECT_NAMES() {
        return ARCHIVED_PROJECT_NAMES;
    }

    public ArchivedProjectName getArchivedProjectNameByProjectName(String projectName) {

        ArchivedProjectName answer = null;

        for (ArchivedProjectName testProject : ARCHIVED_PROJECT_NAMES) {
            if (testProject.getProjectName().equalsIgnoreCase(projectName)) {
                answer = testProject;
                break;
            }
        }
        return answer;
    }

    public String getArchivedProjectFolderNameByProjectname(String name) {

        Optional<String> nameOptional = ARCHIVED_PROJECT_NAMES
                .stream()
                .filter(p -> p.getProjectName().equalsIgnoreCase(name))
                .findFirst()
                .map(p -> p.getFolderName());

        return nameOptional.isPresent() ? nameOptional.get() : null;
    }

    public void removeFolderNamesFromArchivedProjectFolderNames(List<String> folderNames) {
        for (String folderName : folderNames) {
            removeItemFromArchivedProjectFolderNames(folderName);
        }
    }

    private void removeItemFromArchivedProjectFolderNames(String folderName) {

        Iterator it = ARCHIVED_PROJECT_NAMES.iterator();
        while (it.hasNext()) {
            ArchivedProjectName name = (ArchivedProjectName) it.next();
            if (name.getFolderName().equalsIgnoreCase(folderName)) {
                it.remove();
                break;
            }
        }

    }

    public FolderStructure getFolderStructure() {
        return folderStructure;
    }

    public void setFolderStructure(FolderStructure folderStructure) {
        this.folderStructure = folderStructure;
    }

    public List<User> getUSERS() {
        return USERS;
    }

    public void addUser(User u) {
        this.USERS.add(u);
    }

    public List<TestDevice> getTEST_DEVICES() {
        return TEST_DEVICES;
    }

    public void addTestDevice(TestDevice device) {
        this.TEST_DEVICES.add(device);
    }

    public void addTestDevices(List<TestDevice> devices) {
        this.TEST_DEVICES.addAll(devices);
    }

    public List<TestProject> getActiveTestProjects() {
        return ACTIVE_TEST_PROJECTS;
    }

    public List<TestProject> get_ARCHIVED_TEST_PROJECTS() {
        return ARCHIVED_TEST_PROJECTS;
    }

    public void addTestProjectToActiveProjects(TestProject testProject) {
        this.getActiveTestProjects().add(testProject);

    }

    public void addTestProjectToArchivedProjects(TestProject testProject) {
        this.get_ARCHIVED_TEST_PROJECTS().add(testProject);

    }

    public Map<String, Integer> getMultiprojectResults(String keyword) {
        Map<String, Integer> map = new HashMap<>();

        map.put("All test cases", getTestProjectsContainingKeyword(keyword)
                .stream()
                .mapToInt(TestProject::getNumberOfAllTestCases)
                .sum());

        map.put("Failed test cases", getTestProjectsContainingKeyword(keyword)
                .stream()
                .mapToInt(TestProject::getNumberOfFailedTestCases)
                .sum());

        map.put("Successful test cases", getTestProjectsContainingKeyword(keyword)
                .stream()
                .mapToInt(TestProject::getNumberOfPassedTestCases)
                .sum());

        map.put("Not cmpleted test cases", getTestProjectsContainingKeyword(keyword)
                .stream()
                .mapToInt(TestProject::getNumberOfNotCompletedTestcases)
                .sum());
        return map;
    }

    public List<TestProject> getTestProjectsContainingKeyword(String keyword) {
        return ACTIVE_TEST_PROJECTS
                .stream()
                .filter(p -> p.getProjectName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());

    }

    public void removeSelectedProjectsFromActiveProjects(List<TestProject> list) {
        for (TestProject testProject : list) {
            if (ACTIVE_TEST_PROJECTS.contains(testProject)) {
                Iterator it = ACTIVE_TEST_PROJECTS.iterator();
                while (it.hasNext()) {
                    TestProject current = (TestProject) it.next();
                    if (current.equals(testProject)) {
                        it.remove();
                        break;
                    }
                }
            }
        }
    }

    public List<TestCase> getAllFailedTestCasesOfOneProject(String keyword) {
        return getTestProjectsContainingKeyword(keyword)
                .stream()
                .flatMap(p -> p.getFailedTestcases().stream())
                .collect(Collectors.toList());

    }

    public TestProject getTestProjectByName(String projectName) {
        TestProject result = null;
        for (TestProject testProject : ACTIVE_TEST_PROJECTS) {
            if (testProject.getProjectName().equalsIgnoreCase(projectName)) {
                result = testProject;
                break;
            }
            for (TestProject testProject1 : ARCHIVED_TEST_PROJECTS) {
                if (testProject.getProjectName().equalsIgnoreCase(projectName)) {
                    result = testProject1;
                    break;
                }
            }
        }
        return result;
    }

    public TestProject getActiveTestProjectByName(String projectName) {

        TestProject result = null;
        for (TestProject testProject : ACTIVE_TEST_PROJECTS) {
            if (testProject.getProjectName().equalsIgnoreCase(projectName)) {
                result = testProject;
                break;
            }
        }
        return result;
    }

    public TestProject getArchivedTestProjectByName(String projectName) {

        TestProject result = null;
        for (TestProject testProject : ARCHIVED_TEST_PROJECTS) {
            if (testProject.getProjectName().equalsIgnoreCase(projectName)) {
                result = testProject;
                break;
            }
        }
        return result;
    }

    public TestCase getTestCaseById(String Id) {
        TestCase testCase = null;
        for (TestProject testProject : ACTIVE_TEST_PROJECTS) {
            for (TestCase tcase : testProject.getAllTestcases()) {
                if (tcase.getTestCaseId().equalsIgnoreCase(Id)) {
                    testCase = tcase;
                }
            }
        }
        return testCase;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nTestCenter{TEST_PROJECTS=").append(ACTIVE_TEST_PROJECTS);
        sb.append(", ARCHIVED_TEST_PROJECTS=").append(ARCHIVED_TEST_PROJECTS);
        sb.append('}');
        return sb.toString();
    }

}
