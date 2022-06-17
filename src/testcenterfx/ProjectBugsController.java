package testcenterfx;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import app.Main;
import config.AppConfig;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import model.TestCase;
import model.TestCaseTableData;
import model.TestProject;
import util.FXWindowUtils;
import util.JsonUtils;
import util.OpenWordDocument;
//</editor-fold>

/**
 * FXML Controller class
 *
 * @author takacs.gergely
 */
public class ProjectBugsController implements Initializable {

    //<editor-fold defaultstate="collapsed" desc="FXML PROPERTIES">
    @FXML
    private Pane basePane;
    @FXML
    private AnchorPane dragPane;
    @FXML
    private ImageView closeIcon;
    @FXML
    private ImageView refreshIcon;
    @FXML
    private Pane progressPane;
    @FXML
    private ImageView folderIcon;
    @FXML
    private Line minimizeIcon;
    @FXML
    private Pane logoPane;
    @FXML
    private Pane mypopup;
    @FXML
    private TableView<TestCaseTableData> testCasteTable;
    @FXML
    private TableColumn<TestCaseTableData, Boolean> checkBoxColumn;
    @FXML
    private TableColumn<TestCaseTableData, String> testCaseIdColumn;
    @FXML
    private TableColumn<TestCaseTableData, String> testCaseNameColumn;
    @FXML
    private TableColumn<TestCaseTableData, LocalDate> bugTicketDateColumn;
    @FXML
    private Label projectNameLabel;
    @FXML
    private CheckBox mainCheckBox;
    
    @FXML
    private Button createDefectLogsButton;

//</editor-fold>   
    //<editor-fold defaultstate="collapsed" desc="PROPERTIES">
    private ObservableList<TestCaseTableData> observableTestCaseList;
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
        setCurrentProject();
        setupTableView();
        initExitPopup();
        initDragPane();
        setupCreateButton();
        setupLabels();
        FXWindowUtils.setPageTitle(logoPane, "Hibás", "Tesztesetek");
        FXWindowUtils.addUserInfoToDragPane(dragPane);
        FXWindowUtils.addVersionInfoToDragPane(dragPane);
    }
    
    private void setupLabels() {
        projectNameLabel.setText(currentProject.getProjectName());
    }
    
    private void setCurrentProject() {
        currentProject = Main.controller.getCurrentTestProject();
    }
    
    private void setupCreateButton() {
        mainCheckBox.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (!mainCheckBox.isSelected()) {                    
                    createDefectLogsButton.setDisable(true);
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
    
    private void handleOpenButton(ActionEvent event) {
        TestProject tp = app.Main.controller.getCurrentTestProject();
        JsonUtils.saveTestProjectJSON(tp);
    }
    
    private void handleSaveBugsStatusButton(ActionEvent event) {
        TestProject tp = app.Main.controller.getCurrentTestProject();
        JsonUtils.saveProjectBugListJSON(tp);
    }
    
    @FXML
    private void handleBackButton(ActionEvent event) throws IOException {
        Parent nextRoot = FXMLLoader.load(getClass().getResource("ProjectPage.fxml"));
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
    private void handleCreateBugTickets(ActionEvent actionEvent) throws IOException {
        List<TestCase> selectedFailedTestCaseList = getSelectedTestCases();
        
        Task<Void> showProgressWindow = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(() -> {
                    progressPane.setVisible(true);
                });
                return null;
            }
        };
        showProgressWindow.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                
                
                Task<Void> createBugTickets = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    Main.controller.createAndSaveDefectLogs(selectedFailedTestCaseList);
                    return null;
                }
                
            };
                
            createBugTickets.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        updateProjectData();
                        try {
                            reloadPage(actionEvent);
                        } catch (IOException ex) {
                            Logger.getLogger(ProjectBugsController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
            });                
               
           
            new Thread(createBugTickets).start();
            }
        });
        new Thread(showProgressWindow).start();
        
    }
    
    @FXML
    private void handleMainCheckBox() {
        if (mainCheckBox.isSelected()) {
            checkAllBoxes();
        } else {
            clearAllBoxes();
        }
    }
    
    @FXML
    private void handleFolderIconEntered() {
        folderIcon.setOpacity(1.0);
    }
    
    @FXML
    private void handleFolderIconExited() {
        folderIcon.setOpacity(0.6);
    }
    
    @FXML
    private void handleRefreshIconEntered() {
        refreshIcon.setOpacity(1.0);
    }
    
    @FXML
    private void handleRefreshIconExited() {
        refreshIcon.setOpacity(0.5);
    }
    
    public static void handleOpenTestCaseRequest(String documentId, TableCell tc) {
        
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                tc.getScene().setCursor(Cursor.WAIT);
            }
        });
        
        String path = getDocumnetPath(documentId);
        int pageNumber = getPageNumber(documentId);
        OpenWordDocument.open(path, pageNumber);
        
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ProjectBugsController.class.getName()).log(Level.SEVERE, null, ex);
                }
                tc.getScene().setCursor(Cursor.DEFAULT);
            }
        });
        
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SETUP TABLE">
    private void setupTableView() {
        List<TestCase> failedTestCaseList = currentProject.getFailedTestcases();
        List<TestCaseTableData> sourceList = getTestCaseTableDataList(failedTestCaseList);
        observableTestCaseList = FXCollections.observableArrayList(sourceList);
        testCasteTable.setItems(observableTestCaseList);
        checkBoxColumn.setCellValueFactory((c) -> {
            c.getValue().getisSelectedBooleanProperty().addListener((ch, o, n) -> {

                //observableTestCaseList.stream().forEach(p -> System.out.println(p.getisSelectedBooleanProperty().get()));
                if (observableTestCaseList
                        .stream()
                        .filter(p -> p.getisSelectedBooleanProperty().get() == true)
                        .collect(Collectors.toList())
                        .size() > 0) {
                    
                    createDefectLogsButton.setDisable(false);
                } else {
                    createDefectLogsButton.setDisable(true);
                }
            });
            return c.getValue().getisSelectedBooleanProperty();
            
        });
        checkBoxColumn.setCellFactory(CheckBoxTableCell.forTableColumn(checkBoxColumn));
        testCaseIdColumn.setCellValueFactory(new PropertyValueFactory<>("testCaseId"));
        testCaseIdColumn.setCellFactory(CustomTextFieldTableCell.forTableColumn());
        testCaseNameColumn.setCellValueFactory(new PropertyValueFactory<>("testCaseName"));
        bugTicketDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        testCaseIdColumn.setStyle("-fx-alignment: CENTER-LEFT");
        testCaseNameColumn.setStyle("-fx-alignment: CENTER-LEFT");
        bugTicketDateColumn.setStyle("-fx-alignment: CENTER-LEFT");
        testCasteTable.getColumns().setAll(checkBoxColumn, testCaseIdColumn, testCaseNameColumn, bugTicketDateColumn);
    }
    
    private List<TestCaseTableData> getTestCaseTableDataList(List<TestCase> moduleList) {
        List<TestCaseTableData> list = new ArrayList<>();
        for (TestCase testCase : moduleList) {
            TestCaseTableData tableData = new TestCaseTableData(
                    testCase.getTestCaseId(),
                    testCase.getNameOfTest(),
                    null);
            list.add(tableData);
        }
        return list;
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="UTILS">
    private void updateProjectData() {
        Main.controller.deleteProjectObjectsAndRescanProjectFolder(currentProject);
    }
    
    private File getDefectLogsFolder() {
        return new File(currentProject.getProjectFolderPath() + AppConfig.DEFECT_LOGS_FOLDERNAME);
    }
    
    private List<TestCase> getSelectedTestCases() {
        List<TestCase> selectedFailedTestCaseList = new ArrayList<>();
        for (TestCaseTableData testCaseTableData : observableTestCaseList) {
            if (testCaseTableData.getisSelectedBooleanProperty().get() == true) {
                selectedFailedTestCaseList.add(currentProject.getTestCaseById(testCaseTableData.getTestCaseId()));
            }
        }
        return selectedFailedTestCaseList;
    }
    
    private void checkAllBoxes() {
        observableTestCaseList.stream().forEach(p -> p.setgetisSelectedBooleanProperty(Boolean.TRUE));
    }
    
    private void clearAllBoxes() {
        observableTestCaseList.stream().forEach(p -> p.setgetisSelectedBooleanProperty(Boolean.FALSE));
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

}
