package testcenterfx;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import app.Main;
import config.AppConfig;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import model.DirectoryChecker;
import model.TestCase;
import model.TestCaseTableData;
import model.TestProject;
import model.TestProjectTableData;
import util.ArchiveProjectUtils;
import util.FXWindowUtils;
import util.Utils;
//</editor-fold>

/**
 * FXML Controller class
 *
 * @author takacs.gergely
 */
public class ArchiveProjectsController implements Initializable {

    //<editor-fold defaultstate="collapsed" desc="FXML PROPERTIES">
    @FXML
    private Pane basePane;
    
    @FXML
    private Pane disablePane;
    @FXML
    private Pane archiveErrorPane;
    @FXML
    private TextArea archiveErrorTextArea;
    @FXML
    private AnchorPane dragPane;
    @FXML
    private ImageView closeIcon;
    @FXML
    private Line minimizeIcon;
    @FXML
    private Pane logoPane;
    @FXML
    private Pane mypopup;
    @FXML
    private Pane contentPane;

    @FXML
    private TableView<TestProjectTableData> projectsTable;
    @FXML
    private TableColumn<TestProjectTableData, Boolean> checkBoxColumn;
    @FXML
    private TableColumn<TestProjectTableData, String> projectNameColumn;
    @FXML
    private TableColumn<TestProjectTableData, String> dateColumn;
    @FXML
    private TableColumn<TestProjectTableData, String> progressColumn;

    @FXML
    private CheckBox mainCheckBox;
    @FXML
    private Button ArchiveButton;

//</editor-fold>   
    //<editor-fold defaultstate="collapsed" desc="PROPERTIES">
    private static final DirectoryChecker directoryChecker = new DirectoryChecker();
    private ObservableList<TestProjectTableData> observableTestProjectList;
    private static TestProject currentProject;
    private static Stage stage;
    int numOfAllTests;
    int numOfPassedTests;
    int numOfFailedTests;
    int numOfNotCompletedTests;
    int numOfCompletedTests;
    int completedRate;
    int successRate;

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="INIT">
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        FXWindowUtils.setPageTitle(logoPane, "Projektek", "Archiválása");
        setupTableView();
        initExitPopup();
        initDragPane();
        setupTopChecbox();
        FXWindowUtils.addUserInfoToDragPane(dragPane);
        FXWindowUtils.addVersionInfoToDragPane(dragPane);
        archiveErrorPane.setVisible(false);
        disablePane.setVisible(false);
    }

    private void setupTopChecbox() {
        mainCheckBox.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (!mainCheckBox.isSelected()) {
                    ArchiveButton.setDisable(true);
                }
            }
        });
    }

    private void initExitPopup() {
        FXWindowUtils.setupPopupWindow(mypopup);
    }

    private void initDragPane() {
        FXWindowUtils.makeStageDraggable(dragPane);
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="HANDLERS">

    @FXML
    private void minimizeStage(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void handleExitPressed(MouseEvent event) {
        FXWindowUtils.showPopup(mypopup, basePane, closeIcon, minimizeIcon, logoPane);
    }

    @FXML
    public void handleCancelExit() {
        FXWindowUtils.hidePopup(mypopup, basePane, closeIcon, minimizeIcon, logoPane);
    }

    @FXML
    public void handleDoExit() {
        System.exit(0);
    }
    
    @FXML
    public void handleRemoveAlert() {
        archiveErrorPane.setVisible(false);
        disablePane.setVisible(false);
    }

    @FXML
    private void handleBackButton(ActionEvent event) throws IOException {
        Parent nextRoot = FXMLLoader.load(getClass().getResource("ListProjectsPage.fxml"));
        FXWindowUtils.initTransitionToNextPage(event, stage, nextRoot);
    }

    @FXML
    private void handleFolderIconClicked(MouseEvent event) {
        try {
            Desktop.getDesktop().open(getDefectLogsFolder());
        } catch (IOException ex) {
            Logger.getLogger(CreateTestDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleRefreshButton(MouseEvent mouseEvent) throws IOException {
        ActionEvent event = new ActionEvent(mouseEvent.getSource(), mouseEvent.getTarget());
        Parent nextRoot = FXMLLoader.load(getClass().getResource("ProjectBugsPage.fxml"));
        FXWindowUtils.initTransitionToNextPage(event, stage, nextRoot);
    }

    @FXML
    private void handleArchiveButton(ActionEvent event) throws IOException {
        List<TestProject> selectedTestProjects = getSelectedProjects();

        List<String> projectsWithOpenedFiles = checkProjectsForOpenedFiles(selectedTestProjects);

        if (!projectsWithOpenedFiles.isEmpty()) {
            showAlert(projectsWithOpenedFiles);
        } else {
            Main.controller.archiveProjects(selectedTestProjects);
            Main.controller.reScanArchivedProjects();
            goToWelcomePage(event);

        }

    }

    @FXML
    private void goToWelcomePage(ActionEvent event) throws IOException {
        Parent nextRoot = FXMLLoader.load(getClass().getResource("WelcomePage.fxml"));
        FXWindowUtils.initTransitionToNextPage(event, stage, nextRoot);
    }

    @FXML
    private void handleMainCheckBox() {
        if (mainCheckBox.isSelected()) {
            checkAllBoxes();
        } else {
            clearAllBoxes();
        }
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SETUP TABLE">
    private void setupTableView() {

        List<TestProject> activeProjects = Main.controller.getTestCenter().getActiveTestProjects();

        List<TestProjectTableData> sourceList = null;
        if (activeProjects == null || activeProjects.isEmpty()) {
            sourceList = new ArrayList();
        } else {
            sourceList = getTebleData(activeProjects);
        }

        observableTestProjectList = FXCollections.observableArrayList(sourceList);
        projectsTable.setItems(observableTestProjectList);

        checkBoxColumn.setCellValueFactory((c) -> {
            c.getValue().getIsSelectedBooleanProperty().addListener((ch, o, n) -> {
                if (observableTestProjectList
                        .stream()
                        .filter(p -> p.getIsSelectedBooleanProperty().get() == true)
                        .collect(Collectors.toList())
                        .size() > 0) {
                    ArchiveButton.setDisable(false);
                } else {
                    ArchiveButton.setDisable(true);
                }
            });
            return c.getValue().getIsSelectedBooleanProperty();

        });
        checkBoxColumn.setCellFactory(CheckBoxTableCell.forTableColumn(checkBoxColumn));

        projectNameColumn.setCellValueFactory(p -> p.getValue().getTestProjectName());
        projectNameColumn.setStyle("-fx-alignment: CENTER-LEFT");

        dateColumn.setCellValueFactory(p -> p.getValue().getDateStarted());
        progressColumn.setCellValueFactory(p -> p.getValue().getProgressRate());
        progressColumn.setStyle("-fx-alignment: CENTER-right");

        projectsTable.getColumns().setAll(checkBoxColumn, projectNameColumn, dateColumn, progressColumn);
    }

    private List<TestProjectTableData> getTebleData(List<TestProject> activeProjects) {

        if (activeProjects.isEmpty()) {
            return null;
        }

        List<TestProjectTableData> list = new ArrayList<>();

        for (TestProject activeProject : activeProjects) {
            TestProjectTableData td = new TestProjectTableData(
                    activeProject.getProjectNameProperty(),
                    new SimpleStringProperty(Utils.convertDateToHungarianTextFormat(activeProject.getDateStarted())),
                    new SimpleStringProperty(Utils.getPercent(activeProject.getProgresRatePercent()))
            );
            list.add(td);
        }
        return list;
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="UTILS">
    private void updateProjectData() {
        Main.controller.deleteProjectObjectsAndRescanProjectFolder(currentProject);
    }

    private File getDefectLogsFolder() {
        return new File(currentProject.getActiveProjectFolderPath() + AppConfig.DEFECT_LOGS_FOLDERNAME);
    }

    private List<TestProject> getSelectedProjects() {
        List<TestProject> selectedprojectsList = new ArrayList<>();
        for (TestProjectTableData project : observableTestProjectList) {
            if (project.getIsSelectedBooleanProperty().get() == true) {
                selectedprojectsList.add(Main.controller.getTestCenter().getActiveTestProjectByName(project.getTestProjectName().getValue()));
            }
        }
        return selectedprojectsList;
    }

    private void checkAllBoxes() {
        observableTestProjectList.stream().forEach(p -> p.setIsSelectedBooleanProperty(Boolean.TRUE));
    }

    private void clearAllBoxes() {
        observableTestProjectList.stream().forEach(p -> p.setIsSelectedBooleanProperty(Boolean.FALSE));
    }

    private static String getDocumnetPath(String documentId) {
        String[] array = documentId.split("-");
        String target = array[array.length - 2];
        return currentProject
                .getAllTestDocuments()
                .stream()
                .filter(p -> p.getDocumentCodeName().contains(target))
                .findFirst()
                .get()
                .getWordFile()
                .getAbsolutePath();
    }

    private static int getPageNumber(String documentId) {
        String[] idArray = documentId.split("-");
        String number = idArray[idArray.length - 1];
        int pageNum = Integer.parseInt(number);
        return pageNum;
    }

    private void reloadPage(ActionEvent event) throws IOException {
        Parent nextRoot = FXMLLoader.load(getClass().getResource("ProjectBugsPage.fxml"));
        FXWindowUtils.initTransitionToNextPage(event, stage, nextRoot);
    }
//</editor-fold>

    private List<String> checkProjectsForOpenedFiles(List<TestProject> selectedTestProjects) {
        DirectoryChecker.OPENED_FILES.clear();

        for (TestProject tp : selectedTestProjects) {

            Path path = new File(tp.getActiveProjectFolderPath()).toPath();

            try {
                Files.walkFileTree(path, directoryChecker);
            } catch (IOException ex) {
                Logger.getLogger(ArchiveProjectsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return DirectoryChecker.OPENED_FILES;
    }

    private void showAlert(List<String> projectsWithOpenedFiles) {
        StringBuilder sb = new StringBuilder();
        for (String projectsWithOpenedFile : projectsWithOpenedFiles) {
            sb.append(projectsWithOpenedFile).append(System.lineSeparator());
        }
        archiveErrorTextArea.setText(sb.toString());
        archiveErrorPane.setVisible(true);
        disablePane.setVisible(true);
    }

}
