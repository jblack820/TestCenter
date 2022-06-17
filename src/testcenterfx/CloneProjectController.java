package testcenterfx;

//<editor-fold defaultstate="collapsed" desc="Imports">
import app.Main;
import static config.AppConfig.CONTENT_FADE_OUT_DURATION;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import model.ProjectType;
import model.TestDocumentCreationDTO;
import model.TestProject;
import util.CloneUtils;
import util.FXWindowUtils;
import util.Utils;
//</editor-fold>

/**
 * FXML Controller class
 *
 * @author takacs.gergely
 */
public class CloneProjectController implements Initializable {
    
    private Stage stage;
    List<TextField> targetProjectFieldList;
    
    //<editor-fold defaultstate="collapsed" desc="FXML Properties">
    @FXML
    private Pane exitPopup;
    @FXML
    private Pane logoPane;    
    @FXML
    private Pane basePane;
    @FXML
    private Pane coverPane;
    @FXML
    private Pane progressPane;
    @FXML
    private AnchorPane dragPane;
    @FXML
    private ImageView closeIcon;
    @FXML
    private Line minimizeIcon;
    @FXML
    private Label appLocationLabel;
    @FXML
    private Label browserLabel;
    @FXML
    private Button createButton;
    @FXML
    private Button backButton;

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SOURCE PROJECT FXML PROPERTIES">    
    @FXML
    private ComboBox<String> projectSelector;
    @FXML
    private TextField projectNameField2;
    @FXML
    private TextField projectTypeField;
    @FXML
    private TextField projectNameShortField2;
    @FXML
    private TextField projectTypeShortField2;
    @FXML
    private TextField dateStartedField2;
    @FXML
    private TextField dateDeadlineField2;
    @FXML
    private TextField versionNumberField2;
    @FXML
    private TextArea appLocationArea2;
    @FXML
    private TextField browserField2;
//</editor-fold>    
    //<editor-fold defaultstate="collapsed" desc="TARGET PROJECT FXML PROPERTIES">
    @FXML
    private TextField projectNameField;
    @FXML
    private ComboBox<String> projectTypeSelector;
    @FXML
    private TextField projectNameShortField;
    @FXML
    private TextField projectTypeShortField;
    @FXML
    private DatePicker dateStartedField;
    @FXML
    private DatePicker dateDeadlineField;
    @FXML
    private TextField versionNumberField;
    @FXML
    private TextArea appLocationArea;
    @FXML
    private TextField browserField;
    @FXML
    private ComboBox<String> testerSelector;
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="INIT">
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        FXWindowUtils.setPageTitle(logoPane, "Projekt", "Klónozása", 8);
        setupWindow();
        setupProjectSelector();
        setupSourceProjectElements();
        setupNewProjectElements();
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="HANDLERS">
    @FXML
    private void handleCreateButtonClicked(ActionEvent actionEvent) throws IOException {

        Task<Void> showPopup = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(() -> {
                    coverPane.setVisible(true);
                    progressPane.setVisible(true);
                });
                return null;
            }
        };
        showPopup.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                Task<Void> startCloneProcess = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        createNewProject();
                        TestProject sourceTestProject = Main.controller.getTestCenter().getTestProjectByName(projectNameField2.getText());
                        TestProject newTestProject = Main.controller.getTestCenter().getTestProjectByName(projectNameField.getText());
                        CloneUtils.cloneAllTestDocumentToAnotherProject(sourceTestProject, newTestProject, testerSelector.getValue());
                        Main.controller.deleteProjectObjectsAndRescanProjectFolder(newTestProject);
                        return null;
                    }
                };
                startCloneProcess.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        coverPane.setVisible(true);
                        progressPane.setVisible(true);
                        try {
                            goToPage(actionEvent, "ListProjectsPage.fxml");
                        } catch (IOException ex) {
                            Logger.getLogger(CloneProjectController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
                new Thread(startCloneProcess).start();
            }
        });
        new Thread(showPopup).start();

    }

    @FXML
    private void handleBackButton(ActionEvent event) throws IOException {
        goToPage(event, "ListProjectsPage.fxml");
    }

    @FXML
    private void handleGoToMainPageButton(ActionEvent event) throws IOException {
        goToPage(event, "WelcomePage.fxml");
    }

    @FXML
    private void minimizeStage(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void handleExitPressed(MouseEvent event) {
        FXWindowUtils.showPopup(exitPopup, basePane, closeIcon, minimizeIcon, logoPane);
    }

    @FXML
    public void handleCancelExit() {
        FXWindowUtils.hidePopup(exitPopup, basePane, closeIcon, minimizeIcon, logoPane);
    }

    @FXML
    public void handleDoExit() {
        System.exit(0);
    }

    @FXML
    private void handleOkayButton(ActionEvent event) {
    }

    @FXML
    private void handleOpenFolderButton(ActionEvent event) {
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="SETUP PAGE ELEMENTS">
    private void setupNewProjectElements() {
        setupRequiredFieldsListeners();
        createTextFieldList();
        startProjectTypeSelectorListener(projectTypeSelector, browserField, projectNameField, browserLabel, projectTypeShortField, appLocationArea, appLocationLabel);
        limitModuleShortNameField();
        makeFieldUpperCase(projectNameShortField);
        setupDatePicker();
        addProjectNameContentListener();
        loadDropDown(testerSelector, Main.controller.getUsers().getTesterNamesList());
    }

    public void setupSourceProjectElements() {
        loadDropDown(projectTypeSelector, getProjectTypes());
        setupProjectSelectorListener();
    }

    public static void addTextLimiter(final TextField tf, final int maxLength) {
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (tf.getText().length() > maxLength) {
                    String s = tf.getText().substring(0, maxLength);
                    tf.setText(s);
                }
            }
        });
    }

    public void startProjectTypeSelectorListener(ComboBox<String> combo, TextField browserField, TextField projectNameField, Label browserLabel, TextField projectTypeShortField, TextArea appLocationArea, Label appLocationLabel) {
        combo.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (projectTypeSelector.getValue() != "") {
                    projectTypeShortField.setOpacity(1.0);
                    projectTypeShortField.setText(ProjectType.getProjectTypeBasedOnName(combo.getValue()).getProjectTypeCodename());

                    if (projectNameField.getText().length() > 0) {
                        addProjectTypeToProjectName(projectNameField, combo.getValue());
                    }

                    if (isMobilApplication(combo.getValue())) {
                        appLocationArea.setText("");
                        appLocationArea.setDisable(true);
                        appLocationLabel.setOpacity(0.4);
                    } else {
                        if (appLocationArea.isDisable()) {
                            appLocationArea.setDisable(false);
                            appLocationLabel.setOpacity(1);
                        }
                    }
                    if (!isRequiredFiledEmpty()) {
                        createButton.setDisable(false);
                    } else {
                        createButton.setDisable(true);
                    }
                } else {
                    projectTypeShortField.setText("");
                    projectTypeShortField.setOpacity(0.6);
                }

            }
        });
    }

    public <T extends Node> void addListenerToNode(T node) {

        switch (node.getClass().getSimpleName()) {
            case "TextField":
                TextField tf = (TextField) node;
                tf.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!isRequiredFiledEmpty()) {
                        createButton.setDisable(false);
                    } else {
                        createButton.setDisable(true);
                    }
                });
                break;
            case "TextArea":
                TextArea ta = (TextArea) node;
                ta.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!isRequiredFiledEmpty()) {
                        createButton.setDisable(false);
                    } else {
                        createButton.setDisable(true);
                    }
                });
                break;
            case "DatePicker":
                DatePicker dp = (DatePicker) node;
                dp.valueProperty().addListener((observable, oldValue, newValue) -> {
                    if (!isRequiredFiledEmpty()) {
                        createButton.setDisable(false);
                    } else {
                        createButton.setDisable(true);
                    }
                });
                break;
            case "ComboBox":
                ComboBox cb = (ComboBox) node;
                cb.valueProperty().addListener((observable, oldValue, newValue) -> {
                    if (!isRequiredFiledEmpty()) {
                        createButton.setDisable(false);
                    } else {
                        createButton.setDisable(true);
                    }
                });
                break;
            default:
                break;
        }

    }

    public void addProjectNameContentListener() {
        projectNameField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                projectTypeSelector.setValue("");
            }
        });
    }

    static boolean isMobilApplication(String value) {
        return value.equalsIgnoreCase(ProjectType.ANDROID_APPLICATION.getProjectTypeName())
                || value.equalsIgnoreCase(ProjectType.IOS_APPLICATION.getProjectTypeName());
    }

    static boolean isWebApplication(String value) {
        return value.equalsIgnoreCase(ProjectType.WEB_APPLICATION.getProjectTypeName());
    }

    static void addProjectTypeToProjectName(TextField projectNameField, String chosenValue) {
        StringBuilder sb = new StringBuilder(projectNameField.getText());
        if (sb.charAt(sb.length() - 1) == ')') {
            removeTextInBrackets(sb);
        }
        sb.append(" (").append(ProjectType.getProjectTypeBasedOnName(chosenValue).getProjectTypeShortName()).append(")");
        projectNameField.setText(sb.toString());
    }

    private static void removeTextInBrackets(StringBuilder sb) {
        int deletionIndex = 0;
        for (int i = 0; i < sb.length(); i++) {
            if (sb.charAt(i) == '(') {
                deletionIndex = i - 1;
                break;
            }

        }
        sb.delete(deletionIndex, sb.length());

    }

    public static void makeFieldUpperCase(final TextField tf) {
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                String capital = tf.getText().toUpperCase();
                tf.setText(capital);
            }
        });
    }

    public void addFieldContentListener(final TextField tf) {
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (!isRequiredFiledEmpty()) {
                    createButton.setDisable(false);
                } else {
                    createButton.setDisable(true);
                }
            }
        });
    }

    public void startDatePickerListener() {
        dateStartedField.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> ov, LocalDate t, LocalDate t1) {
                if (!isRequiredFiledEmpty()) {
                    createButton.setDisable(false);
                } else {
                    createButton.setDisable(true);
                }
            }
        });
    }

    public void startAppLocationAreaListener(TextArea appLocationArea) {
        appLocationArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (!isRequiredFiledEmpty()) {
                    createButton.setDisable(false);
                } else {
                    createButton.setDisable(true);
                }
            }
        });
    }

    public void startBrowserFiledListener(TextField browserField) {
        browserField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (!isRequiredFiledEmpty()) {
                    createButton.setDisable(false);
                } else {
                    createButton.setDisable(true);
                }
            }
        });
    }

    public void startFieldContentListeners() {
        for (TextField tf : targetProjectFieldList) {
            addFieldContentListener(tf);
        }
    }

    public boolean isRequiredFiledEmpty() {
        boolean isTextFieldsEmpty = targetProjectFieldList
                .stream()
                .filter(p -> p.getText()
                .equalsIgnoreCase(""))
                .collect(Collectors.toList())
                .size() > 0;
        boolean isDatePickerEmpty = dateStartedField.getValue() == null;
        boolean isAppLocationEmptyAndRequired = checkifAppLocationEmptyAndRequired();
        boolean isBrowserfieldEmptyAndRequired = checkifBrowserfieldEmpty();
        boolean isProjectTypeSelectorEmpty = null == projectTypeSelector.getValue()
                || projectTypeSelector.getValue().equals("");
        boolean isTesterSelectorEmpty = null == testerSelector.getValue()
                || testerSelector.getValue().equalsIgnoreCase("");

        return isTextFieldsEmpty
                || isDatePickerEmpty
                || isAppLocationEmptyAndRequired
                || isBrowserfieldEmptyAndRequired
                || isProjectTypeSelectorEmpty
                || isTesterSelectorEmpty;
    }

    private boolean checkifBrowserfieldEmpty() {
        return browserField.getText().equalsIgnoreCase("");
    }

    private boolean checkifAppLocationEmptyAndRequired() {
        if (appLocationArea.isDisabled()) {
            return false;
        } else {
            return appLocationArea.getText().equalsIgnoreCase("");
        }
    }

    private void limitModuleShortNameField() {
        addTextLimiter(projectNameShortField, 5);
    }

    private void initPopup(Pane pane) {
        FXWindowUtils.setupPopupWindow(pane);
    }

    private void initDragPane() {
        FXWindowUtils.makeStageDraggable(dragPane);
    }

    private void setupDatePicker() {

        dateStartedField.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                setDisable(empty || date.compareTo(today) < 0);
            }
        });
    }

    private void loadDropDown(ComboBox<String> dropdown, List<String> elements) {
        dropdown.getItems().addAll(elements);
    }

    private void setupWindow() {
        createButton.setDisable(true);
        coverPane.setVisible(false);
        progressPane.setVisible(false);
        initDragPane();
        initPopup(exitPopup);
        
        FXWindowUtils.addUserInfoToDragPane(dragPane);
    }

    private void setupProjectSelector() {
        loadDropDown(projectSelector, getActiveProjectNames());
        setupProjectSelectorListener();
    }

    private void setupRequiredFieldsListeners() {
        List<Node> nodeList = Arrays.asList(
                new Node[]{
                    projectNameField,
                    projectTypeSelector,
                    projectNameShortField,
                    dateStartedField,
                    dateDeadlineField,
                    versionNumberField,
                    appLocationArea,
                    browserField,
                    testerSelector
                });
        nodeList.forEach(node -> {
            addListenerToNode(node);
        });
    }

    private void setupProjectSelectorListener() {
        projectSelector.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                TestProject tp = Main.controller.getTestCenter().getTestProjectByName(newValue);
                projectNameField2.setText(tp.getProjectName());
                projectTypeField.setText(tp.getProjectTypeName());
                projectNameShortField2.setText(tp.getCodeNAme());
                projectTypeShortField2.setText(tp.getProjectType().getProjectTypeShortName());
                dateStartedField2.setText(Utils.convertLocalDateToString(tp.getDateStarted()));
                dateDeadlineField2.setText(Utils.convertLocalDateToString(tp.getProjectDeadline()));
                versionNumberField2.setText(tp.getLatestVersionNum().getStringNumber());
                appLocationArea2.setText(tp.getAppLocation());
                browserField2.setText(tp.getBrowserTestedOn());

            }
        });
    }

    private void createTextFieldList() {
        targetProjectFieldList = new ArrayList<>();
        targetProjectFieldList.addAll(
                Arrays.asList(
                        new TextField[]{
                            projectNameField,
                            projectNameShortField,
                            projectTypeShortField,
                            versionNumberField,
                            browserField}));
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="UTILS">
    private void fillInfoPopupLables(Pane infoPopup, TestDocumentCreationDTO settings) {
//        labelPageNumber.setText(settings.getNumberOfPagesRequired() + " db");
//        labelFileName.setText(Utils.removeHunagrianLettersAndSpaces(settings.getAppSectionUnderTest().toUpperCase()) + ".docm");
//        labelSaveLocation.setText(saveLocation.getText());
    }

    private void goToPage(ActionEvent event, String page) throws IOException {
        Parent nextRoot = FXMLLoader.load(getClass().getResource(page));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Node contentPane = stage.getScene().getRoot().lookup("#basePane").lookup("#contentPane");
        FXWindowUtils.initNodeFadeOutFX(CONTENT_FADE_OUT_DURATION, contentPane, 1.0, 0.01);
        FXWindowUtils.delayAndFadeInNextRoot(stage, nextRoot, event, CONTENT_FADE_OUT_DURATION);
    }

    private List<String> getProjectTypes() {
        return Arrays.asList(ProjectType.values()).stream().map(p -> p.getProjectTypeName()).sorted().collect(Collectors.toList());
    }

    private List<String> getActiveProjectNames() {
        List<String> list = Main.controller.getTestCenter().getActiveTestProjects().stream().map(p -> p.getProjectName()).collect(Collectors.toList());
        return list;
    }
    
      private void createNewProject() {
        Main.controller.createNewProject(
                projectNameField.getText(),
                projectNameShortField.getText(),
                dateStartedField.getValue(),
                dateDeadlineField.getValue(),
                projectTypeSelector.getValue(),
                versionNumberField.getText(),
                appLocationArea.getText(),
                browserField.getText());
    }

//</editor-fold>
}
