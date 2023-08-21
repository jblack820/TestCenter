package testcenterfx;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import app.Main;
import config.AppConfig;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.Stage;
import model.TestProject;
import model.TestProjectTableData;
import util.FXWindowUtils;
//</editor-fold>

/**
 * FXML Controller class
 *
 * @author takacs.gergely
 */
public class ReActivateProjectsController implements Initializable {

    //<editor-fold defaultstate="collapsed" desc="FXML PROPERTIES">
    @FXML
    private Pane basePane;
    @FXML
    private TextField searchBar;
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
    private TableView<TestProjectTableData> projectsTable;
    @FXML
    private TableColumn<TestProjectTableData, Boolean> checkBoxColumn;
    @FXML
    private TableColumn<TestProjectTableData, String> projectNameColumn;
    @FXML
    private CheckBox mainCheckBox;
    @FXML
    private Button ArchiveButton;

//</editor-fold>   
    //<editor-fold defaultstate="collapsed" desc="PROPERTIES">
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
        FXWindowUtils.setPageTitle(logoPane, "Projektek", "Reaktiválása");
        setupTableView("");
        initExitPopup();
        initDragPane();
        setupTopChecbox();
        setupSearchBar();
        FXWindowUtils.addUserInfoToDragPane(dragPane);
        FXWindowUtils.addVersionInfoToDragPane(dragPane);
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
    
    private void setupSearchBar() {
        searchBar.setPromptText("Kulcsszó");
        searchBar.setFont(Font.font("Century Gothic", FontPosture.ITALIC, 14));
        //setSearchBar(searchBar);
        
        searchBar.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (searchBar.getText().equalsIgnoreCase("")) {
                    searchBar.setFont(Font.font("Century Gothic", FontPosture.ITALIC, 14));
                } else {
                    searchBar.setFont(Font.font("Century Gothic", FontPosture.REGULAR, 18));
                }
                setupTableView(searchBar.getText());
            }
        });
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
    private void handleBackButton(ActionEvent event) throws IOException {
        Parent nextRoot = FXMLLoader.load(getClass().getResource("WelcomePage.fxml"));
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
    private void handleReActivateButton(ActionEvent event) throws IOException {
        List<String> selectedProjectFolderNames = getSelectedProjectFolderNames();
        System.out.println("\n Selected project to reactivate: " + selectedProjectFolderNames.toString());
        Main.controller.reActivateProjects(selectedProjectFolderNames);
        Main.controller.getTestCenter().removeFolderNamesFromArchivedProjectFolderNames(selectedProjectFolderNames);
        WelcomePageController.isDocumentsScanned = false;
        goToWelcomePage(event);
        
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
    private void setupTableView(String key) {
        
        List<String> archivedProjectNames = Main.controller.getTestCenter().getARCHIVED_PROJECT_NAMES()
                .stream()
                .map(p -> p.getProjectName())
                .collect(Collectors.toList());
        
        List<TestProjectTableData> sourceList = null;
        if (!archivedProjectNames.isEmpty()) {
            sourceList = getTebleData(
                    archivedProjectNames)
                    .stream()
                    .filter(p -> isContainsKeywords(p.getTestProjectName().getValue(), key.split(" ")))
                    .collect(Collectors.toList());
        } else {
            sourceList = new ArrayList<>();
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
        
        projectsTable.getColumns().setAll(checkBoxColumn, projectNameColumn);
    }
    
    private List<TestProjectTableData> getTebleData(List<String> projectNames) {
        if (projectNames.isEmpty()) {
            return null;
        }
        
        List<TestProjectTableData> list = new ArrayList<>();
        
        for (String name : projectNames) {
            TestProjectTableData td = new TestProjectTableData(new SimpleStringProperty(name));
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
        return new File(currentProject.getProjectFolderPath() + AppConfig.DEFECT_LOGS_FOLDERNAME);
    }
    
    private List<String> getSelectedProjectFolderNames() {
        List<String> list = new ArrayList<>();
        for (TestProjectTableData project : observableTestProjectList) {
            if (project.getIsSelectedBooleanProperty().get() == true) {
                String projectName = project.getTestProjectName().get();
                String folderName = Main.controller.getTestCenter().getArchivedProjectFolderNameByProjectname(projectName);
                list.add(folderName);
            }
        }
        return list;
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
    
    private boolean isContainsKeywords(String text, String[] keywords) {
        
        for (String keyword : keywords) {
            if (!text.toLowerCase().contains(keyword.toLowerCase())) {
                return false;
            }
        }
        return true;
    }

//</editor-fold>
}
