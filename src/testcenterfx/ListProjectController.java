package testcenterfx;

//<editor-fold defaultstate="collapsed" desc="IMPORTS">
import app.Main;
import static config.AppConfig.CONTENT_FADE_OUT_DURATION;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import model.TestProject;
import util.FXWindowUtils;
//</editor-fold>

/**
 * FXML Controller class
 *
 * @author takacs.gergely
 */
public class ListProjectController implements Initializable {

    //<editor-fold defaultstate="collapsed" desc="FXML ATTRIBUTES">
    @FXML
    private AnchorPane anchorPaneBase;
    @FXML
    private Pane basePane;
    @FXML
    private AnchorPane dragPane;
    @FXML
    private Pane closePane;
    @FXML
    private ImageView closeIcon;
    @FXML
    private Pane minimizePane;
    @FXML
    private Line minimizeIcon;
    @FXML
    private Pane logoPane;
    private ObservableList<TestProject> projectList;
    @FXML
    private Pane mypopup;
    @FXML
    private TableColumn<TestProject, LocalDate> projectDateColumn;
    @FXML
    private TableColumn<TestProject, String> projectNameColumn;
    @FXML
    private TableColumn<TestProject, ProgressBar> progressColumn;
    @FXML
    private TableColumn<TestProject, String> percColumn;
    @FXML
    private TableView<TestProject> projectTable;
    @FXML
    private Button openButton;
    @FXML
    private Button toWelcomPageButton;

    private Stage stage;
    @FXML
    private Pane contentPane;
    @FXML
    private Button addNewProjectButton;
    @FXML
    private Pane scanningDocsPane;
    @FXML
    private ImageView refreshIcon;
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="INIT">

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTableView();
        setupElements();
        initSelectionListener();
        FXWindowUtils.addUserInfoToDragPane(dragPane);
        FXWindowUtils.addVersionInfoToDragPane(dragPane);
    }

    private void setupElements() {
        FXWindowUtils.setPageTitle(logoPane, "Aktív", "Teszt projektek");
        initExitPopup();
        initDragPane();
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="HANDLERS">
    @FXML
    private void handleRefreshIconEntered() {
        refreshIcon.setOpacity(1.0);
    }

    @FXML
    private void handleRefreshIconExited() {
        refreshIcon.setOpacity(0.5);
    }

    @FXML
    private void handleRefreshButton(MouseEvent mouseEvent) throws IOException {

        Task<Void> showPopup = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                FXWindowUtils.showPopup(scanningDocsPane, basePane, closeIcon, minimizeIcon, logoPane);

                return null;
            }
        };
        showPopup.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {

                Task<Void> rescan = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {

                        List<TestProject> allProjects = Main.controller.getTestCenter().getActiveTestProjects();

                        for (TestProject project : allProjects) {
                            Main.controller.deleteProjectObjectsAndRescanProjectFolder(project);
                        }

                        return null;
                    }
                };
                rescan.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        ActionEvent aevent = new ActionEvent(mouseEvent.getSource(), mouseEvent.getTarget());
                        Parent nextRoot = null;
                        try {
                            nextRoot = FXMLLoader.load(getClass().getResource("ListProjectsPage.fxml"));
                        } catch (IOException ex) {
                            Logger.getLogger(ProjectPageController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        FXWindowUtils.initTransitionToNextPage(aevent, stage, nextRoot);
                    }
                });
                new Thread(rescan).start();
            }
        });

        new Thread(showPopup).start();

    }

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

    public void handleDeleteProject() {
        //handleWarningWindow();
    }

    @FXML
    public void handleDoExit() {
        System.exit(0);
    }

    @FXML
    private void handleOpenButton(ActionEvent event) throws IOException {
        Parent nextRoot = FXMLLoader.load(getClass().getResource("ProjectPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXWindowUtils.initNodeFadeOutFX(CONTENT_FADE_OUT_DURATION, contentPane, 1.0, 0.01);
        FXWindowUtils.delayAndFadeInNextRoot(stage, nextRoot, event, CONTENT_FADE_OUT_DURATION);
    }

    @FXML
    private void switchToWelcomeScene(ActionEvent event) throws IOException {
        Parent nextRoot = FXMLLoader.load(getClass().getResource("WelcomePage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXWindowUtils.initNodeFadeOutFX(CONTENT_FADE_OUT_DURATION, contentPane, 1.0, 0.01);
        FXWindowUtils.delayAndFadeInNextRoot(stage, nextRoot, event, CONTENT_FADE_OUT_DURATION);
    }

    @FXML
    private void handleGoToArchiveProjectPage(ActionEvent event) throws IOException {
        Parent nextRoot = FXMLLoader.load(getClass().getResource("ArchiveProjects.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXWindowUtils.initNodeFadeOutFX(CONTENT_FADE_OUT_DURATION, contentPane, 1.0, 0.01);
        FXWindowUtils.delayAndFadeInNextRoot(stage, nextRoot, event, CONTENT_FADE_OUT_DURATION);
    }

    @FXML
    private void handleGoToReactivatePageButton(ActionEvent event) throws IOException {
        Parent nextRoot = FXMLLoader.load(getClass().getResource("ReActivateProjectsPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXWindowUtils.initNodeFadeOutFX(CONTENT_FADE_OUT_DURATION, contentPane, 1.0, 0.01);
        FXWindowUtils.delayAndFadeInNextRoot(stage, nextRoot, event, CONTENT_FADE_OUT_DURATION);
    }

    @FXML
    private void handleAddProjectButton(ActionEvent event) throws IOException {
        Parent nextRoot = FXMLLoader.load(getClass().getResource("CreateProjectPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXWindowUtils.initNodeFadeOutFX(CONTENT_FADE_OUT_DURATION, contentPane, 1.0, 0.01);
        FXWindowUtils.delayAndFadeInNextRoot(stage, nextRoot, event, CONTENT_FADE_OUT_DURATION);

    }

    private void handleWarningWindow() {

    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SETUP PAGE ELEMENTS">

    private void initExitPopup() {
        FXWindowUtils.setupPopupWindow(mypopup);
    }

    private void initDragPane() {
        FXWindowUtils.makeStageDraggable(dragPane);
    }

    private void setupTableView() {
        List<TestProject> sourceList = app.Main.controller.getTestCenter().getActiveTestProjects();
        projectList = FXCollections.observableArrayList(sourceList);
        projectTable.setItems(projectList);
        projectDateColumn.setCellValueFactory(cellData -> cellData.getValue().getDateStartedProperty());
        setDateColumnToHungarianDateFormat();
        projectNameColumn.setCellValueFactory(new PropertyValueFactory<>("projectName"));

        progressColumn.setCellValueFactory((row) -> {
            TestProject tp = row.getValue();
            double progressValue = getProjectProgressValue(tp);
            ProgressBar pb = new ProgressBar(progressValue);
            pb.setStyle(getProgressBarColor(progressValue) + "-fx-pref-height: 10;");
            return new SimpleObjectProperty<>(pb);
        });

        percColumn.setCellValueFactory((row) -> {
            TestProject tp = row.getValue();
            double progressValue = getProjectProgressValue(tp);
            String text = getProgressTextFromProgressValue(progressValue);
            return new SimpleStringProperty(text);
        });

        projectTable.getColumns().setAll(projectDateColumn, projectNameColumn, progressColumn, percColumn);

    }

    private void setDateColumnToHungarianDateFormat() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        projectDateColumn.setCellFactory(column -> {
            return new TableCell<TestProject, LocalDate>() {
                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(formatter.format(item) + ".");
                    }
                }
            };
        });
    }

    private void initSelectionListener() {
        ObservableList<TestProject> selectedItems = projectTable.getSelectionModel().getSelectedItems();
        selectedItems.addListener(new ListChangeListener<TestProject>() {
            @Override
            public void onChanged(Change<? extends TestProject> change) {
                if (isOpenButtonDisabled()) {
                    openButton.setDisable(false);
                }
                app.Main.controller.setCurrentTestProject(change.getList().get(0));
            }
        });
    }

    private boolean isOpenButtonDisabled() {
        return openButton.disableProperty().get();
    }

    private double getProjectProgressValue(TestProject tp) {

        double value = 0.0;

        double total = new Double(tp.getAllTestcases().size());
        double done = new Double(tp.getAllTestCasesDone().size());

        if (total != 0 && done != 0) {
            value = done / total;
        }

        return value;

    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="UTILS">
    
    
    private String getProgressTextFromProgressValue (double progressValue){
        return util.Utils.getPercent(progressValue);
    }
    

    private String getProgressBarColor(double progressValue) {
        String done = "-fx-accent: #096A38;";
        String inProgress = "-fx-accent: #263E4D;";

        if (progressValue == 1) {
            return done;
        } else {
            return inProgress;
        }
    }

    

//</editor-fold>
}
