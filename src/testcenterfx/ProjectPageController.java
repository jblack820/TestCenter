package testcenterfx;

//<editor-fold defaultstate="collapsed" desc="IMPORTS">
import app.Main;
import config.AppConfig;
import static config.AppConfig.CONTENT_FADE_OUT_DURATION;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import model.TestDocument;
import model.TestModuleStat;
import model.TestProject;
import util.FXWindowUtils;
import util.JsonUtils;
import util.OpenWordDocument;
import util.Utils;
//</editor-fold>

/**
 * FXML Controller class
 *
 * @author takacs.gergely
 */
public class ProjectPageController implements Initializable {

    //<editor-fold defaultstate="collapsed" desc="FXML PROPERTIES">
    @FXML
    private Pane basePane;
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
    private TableView<TestModuleStat> statTable;
    @FXML
    private TableColumn<TestModuleStat, Integer> idColumn;
    @FXML
    private TableColumn<TestModuleStat, String> docNameColumn;
    @FXML
    private TableColumn<TestModuleStat, Integer> totalColumn;
    @FXML
    private TableColumn<TestModuleStat, Integer> completedColumn;
    @FXML
    private TableColumn<TestModuleStat, Integer> failedColumn;    
    @FXML
    private PieChart pieChart;    
    @FXML
    private Label projectNameLabel;
    @FXML
    private Label projectStartDateLabel;
    @FXML
    private Label projectDeadlineLabel;
    @FXML
    private Label projectHealthLabel;
    @FXML
    private Label totalTestLabel;
    @FXML
    private Label completedTestLabel;
    @FXML
    private Label completedPercentLabel;
    @FXML
    private Label passedTestsLabel;
    @FXML
    private Label failedTestsLabel;
    @FXML
    private Label notCompletedLabel;
    @FXML
    private Pane noProjecDataLabel;
    @FXML
    private Pane successPane;
    @FXML
    private Pane opacityPane;
    @FXML
    private Pane statPane;
    @FXML
    private Button createDefectLogsButton;
    @FXML
    private Pane scanningDocsPane;    
    @FXML
    private ImageView refreshIcon;
    

//</editor-fold>   
    //<editor-fold defaultstate="collapsed" desc="PROPERTIES">
    private ObservableList<TestModuleStat> testModuleList;
    private static TestProject currentProject;
    ObservableList<PieChart.Data> pieChartData;
    private Stage stage;
    int numOfAllTests;
    int numOfPassedTests;
    int numOfFailedTests;
    int numOfNotCompletedTests;
    int numOfCompletedTests;
    int completedRate;
    int successRate;   
//</editor-fold>   
    //<editor-fold defaultstate="collapsed" desc="INITALIZE">

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        currentProject = Main.controller.getCurrentTestProject();
        setupProjectData();
        FXWindowUtils.setPageTitle(logoPane, "Projekt", "Adatok");
        hidePageElementsIfNoDataPresent();
        displaySuccessPaneIfProjectCompleted();
        setupTableView();
        setupLabels();
        initExitPopup();
        initDragPane();
        initPieChart();
        FXWindowUtils.addUserInfoToDragPane(dragPane);
        FXWindowUtils.addVersionInfoToDragPane(dragPane);
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="HANDLERS">
    
    @FXML
    private void exitSuccesPane (){
        successPane.setVisible(false);
        opacityPane.setVisible(false);
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

    @FXML
    public void handleDoExit() {
        System.exit(0);
    }

    @FXML
    private void handleOpenButton(ActionEvent event) {
        TestProject tp = app.Main.controller.getCurrentTestProject();
        JsonUtils.saveTestProjectJSON(tp);
    }

    @FXML
    private void handleBackButton(ActionEvent event) throws IOException {
        Parent nextRoot = FXMLLoader.load(getClass().getResource("ListProjectsPage.fxml"));
        FXWindowUtils.initTransitionToNextPage(event, stage, nextRoot);
    }

    private void initExitPopup() {
        FXWindowUtils.setupPopupWindow(mypopup);
    }

    private void initDragPane() {
        FXWindowUtils.makeStageDraggable(dragPane);
    }

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
                        Main.controller.deleteProjectObjectsAndRescanProjectFolder(currentProject);
                        return null;
                    }
                };
                rescan.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        ActionEvent aevent = new ActionEvent(mouseEvent.getSource(), mouseEvent.getTarget());
                        Parent nextRoot = null;
                        try {
                            nextRoot = FXMLLoader.load(getClass().getResource("ProjectPage.fxml"));
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
    private void handleFolderIconClicked(MouseEvent event) {
        try {
            Desktop.getDesktop().open(new File(currentProject.getProjectFolderPath()+AppConfig.TEST_DOCUMENTS_FOLDERNAME));
        } catch (IOException ex) {
            Logger.getLogger(CreateTestDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleCreateBugTickets(ActionEvent event) throws IOException {
        Parent nextRoot = FXMLLoader.load(getClass().getResource("ProjectBugsPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXWindowUtils.initNodeFadeOutFX(CONTENT_FADE_OUT_DURATION, contentPane, 1.0, 0.01);
        FXWindowUtils.delayAndFadeInNextRoot(stage, nextRoot, event, CONTENT_FADE_OUT_DURATION);
    }

    

    @FXML
    private void handleGenerateTestDocument(ActionEvent event) throws IOException {
        Parent nextRoot = FXMLLoader.load(getClass().getResource("CreateTestDocumentFXMLPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXWindowUtils.initNodeFadeOutFX(CONTENT_FADE_OUT_DURATION, contentPane, 1.0, 0.01);
        FXWindowUtils.delayAndFadeInNextRoot(stage, nextRoot, event, CONTENT_FADE_OUT_DURATION);
    }

    public static void handleOpenTestDocumentRequest(String modulName, TableCell tc) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                tc.getScene().setCursor(Cursor.WAIT);
            }
        });
        String path = getDocumnetPath(modulName);
        OpenWordDocument.open(path);
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

    private static String getDocumnetPath(String modulName) {
        
        return currentProject
                .getAllTestDocuments()
                .stream()
                .filter(p -> p.getModulName().equalsIgnoreCase(modulName))
                .findFirst()
                .get()
                .getWordFile()
                .getAbsolutePath();
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SETUP PAGE">
    private void setupProjectData() {
        numOfAllTests = currentProject.getAllTestcases().size();
        numOfPassedTests = currentProject.getPassedTestcases().size();
        numOfFailedTests = currentProject.getFailedTestcases().size();
        numOfNotCompletedTests = currentProject.getNotCompletedTestcases().size();
        numOfCompletedTests = numOfAllTests - numOfNotCompletedTests;
        successRate = ronudToClosestInt((double) numOfPassedTests / ((double) numOfCompletedTests) * 100);
        completedRate = ronudToClosestInt((double) numOfCompletedTests / ((double) numOfAllTests) * 100);
    }

    private int ronudToClosestInt(double source) {
        int intValue = (int) Math.round(source);
        double d = (double) source - intValue;
        if (d >= 0.5) {
            return intValue + 1;
        }
        return intValue;
    }

    private void hidePageElementsIfNoDataPresent() {
        if (numOfAllTests == 0) {
            noProjecDataLabel.setVisible(true);
            createDefectLogsButton.setDisable(true);
            statPane.setOpacity(0.0);
        }
    }

    private void displaySuccessPaneIfProjectCompleted() {
        if (numOfAllTests > 0
                && numOfCompletedTests == numOfAllTests
                && numOfPassedTests == numOfAllTests) {
            opacityPane.setVisible(true);
            successPane.setVisible(true);
            successPane.setOpacity(0.8);

        }
    }



//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SETUP TABLE">
    private void setupTableView() {

        List<TestDocument> moduleList = currentProject.getTestDocuments();
        List<TestModuleStat> sourceList = getTestModuleStatList(moduleList);
        testModuleList = FXCollections.observableArrayList(sourceList);
        statTable.setItems(testModuleList);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("testModuleId"));
        docNameColumn.setCellValueFactory(new PropertyValueFactory<>("testmoduleName"));
        docNameColumn.setCellFactory(CustomTestDocumentNameTableCell.forTableColumn());
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("numOfTotalTestCases"));        
        completedColumn.setCellValueFactory(new PropertyValueFactory<>("numOfCompletedTestCases"));
        failedColumn.setCellValueFactory(new PropertyValueFactory<>("numOfFailedTestCases"));
        docNameColumn.setStyle("-fx-alignment: CENTER-LEFT");
        totalColumn.setStyle("-fx-alignment: CENTER");
        completedColumn.setStyle("-fx-alignment: CENTER");
        failedColumn.setStyle("-fx-alignment: CENTER");
        statTable.getColumns().setAll(idColumn, docNameColumn, totalColumn, failedColumn, completedColumn);
        
    }

    private List<TestModuleStat> getTestModuleStatList(List<TestDocument> moduleList) {
        List<TestModuleStat> sourceList = new ArrayList<>();
        int idCounter = 1;
        for (TestDocument testModule : moduleList) {
            TestModuleStat tms = new TestModuleStat(
                    idCounter,
                    testModule.getModuleNameProperty(),
                    testModule.getTEST_CASES().size(),
                    testModule.getFailedTestCases().size(),
                    testModule.getCompletedTestCases().size());
            idCounter++;
            sourceList.add(tms);
        }

        return sourceList;
    }

    private void setupLabels() {
        projectNameLabel.setText(currentProject.getProjectName());
        projectStartDateLabel.setText(Utils.convertLocalDateToString(currentProject.getDateStarted()));
        setProjectDeadLine();
        totalTestLabel.setText(String.valueOf(numOfAllTests));
        completedTestLabel.setText(String.valueOf(numOfCompletedTests));
        completedPercentLabel.setText(String.valueOf(completedRate) + "%");
        passedTestsLabel.setText(String.valueOf(numOfPassedTests));
        failedTestsLabel.setText(String.valueOf(numOfFailedTests));
        notCompletedLabel.setText(String.valueOf(numOfNotCompletedTests));
    }

    private void setProjectDeadLine() {
        
        if (null!=currentProject.getProjectDeadline()){
            projectDeadlineLabel.setText(Utils.convertLocalDateToString(currentProject.getProjectDeadline()));
        } else {
            projectDeadlineLabel.setText("-");
        }        
        
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="PIECHART">

    private void initPieChart() {
        pieChartData = createData();
        pieChart.setLegendVisible(false);
        pieChart.setLabelsVisible(false);
        pieChart.setData(pieChartData);
        pieChart.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.BLACK, 30, 0.2, 1, 5));
        projectHealthLabel.setText(successRate + "%");
    }

    private ObservableList<PieChart.Data> createData() {
        List<Integer> data = calaculatePieChartData();
        return FXCollections.observableArrayList(
                new PieChart.Data("Passed", data.get(0)),
                new PieChart.Data("Failed", data.get(1)),
                new PieChart.Data("Empty", data.get(2))
        );
    }

    private List<Integer> calaculatePieChartData() {
        List<Integer> data = new ArrayList<>();
        int percentagePassed, percentageFailed, percentageNotCompleted;
        percentagePassed = ronudToClosestInt(((double) numOfPassedTests / (double) numOfAllTests) * 100);
        percentageFailed = ronudToClosestInt(((double) numOfFailedTests / (double) numOfAllTests) * 100);
        percentageNotCompleted = 100 - (percentagePassed + percentageFailed);
        data.add(percentagePassed);
        data.add(percentageFailed);
        data.add(percentageNotCompleted);
        return data;
    }
//</editor-fold>

}
