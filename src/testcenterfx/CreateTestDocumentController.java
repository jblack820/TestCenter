package testcenterfx;

//<editor-fold defaultstate="collapsed" desc="IMPORTS">
import app.Main;
import config.AppConfig;
import controller.TestCenterController;
import java.awt.Desktop;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import model.ProjectType;
import model.TestDocument;
import model.TestDocumentCreationDTO;
import model.TestProject;
import model.UserRole;
import util.FXWindowUtils;
import util.Utils;
//</editor-fold>

/**
 * FXML Controller class
 *
 * @author takacs.gergely
 */
public class CreateTestDocumentController implements Initializable {

    //<editor-fold defaultstate="collapsed" desc="PROPERTIES">
    private Stage stage;
    List<TextField> fieldsList;
    TestProject currentProject;
    public static Integer documentDoneCounter;
    public static PropertyChangeSupport propertyChangeSupport;

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="FXML Properties">
    @FXML
    private Pane exitPopup;
    @FXML
    private Pane infoPopup;
    @FXML
    private Pane basePane;
    @FXML
    private Label infoLabel;
    @FXML
    private Label fileSave;
    @FXML
    private Pane logoPane;
    @FXML
    private Label infoLineLabel;
    @FXML
    private Label progressPointsLabel;
    @FXML
    private AnchorPane dragPane;
    @FXML
    private ImageView closeIcon;
    @FXML
    private ImageView okSign;
    @FXML
    private ImageView okSign2;
    @FXML
    private ImageView okSign3;
    @FXML
    private Line minimizeIcon;
    @FXML
    private TextField projectNameField;
    @FXML
    private TextField moduleNameField;
    @FXML
    private TextField moduleNameShortField;
    @FXML
    private ComboBox comboBox;
    @FXML
    private ComboBox<String> userBox;
    @FXML
    private TextField versionfield;
    @FXML
    private TextField browserField;
    @FXML
    private DatePicker dateField;
    @FXML
    private Button createButton;
    @FXML
    private Button fileChooserButton;
    @FXML
    private Pane contentPane;
    @FXML
    private Button okayButton;
    @FXML
    private Button okayButton1;
    @FXML
    private Label labelPageNumber;
    @FXML
    private Label labelFileName;
    @FXML
    private Label labelSaveLocation;
    @FXML
    private Label appLocationLabel;
    @FXML
    private Label browserLabel;
    @FXML
    private Label totalLabel;
    @FXML
    private Label doneLabel;
    @FXML
    private Pane progressPopup;
    @FXML
    private TextField projectTypeField;
    @FXML
    private TextArea appLocationTextArea;
    @FXML
    private ProgressIndicator indicator;
    
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="INIT PAGE">

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        currentProject = Main.controller.getCurrentTestProject();
        setupElements();
        initDragPane();
        initPopup(exitPopup);
        initPopup(infoPopup);
        FXWindowUtils.addUserInfoToDragPane(dragPane);
        FXWindowUtils.addVersionInfoToDragPane(dragPane);
    }

    //</editor-fold>    
    //<editor-fold defaultstate="collapsed" desc="CREATE TEST DOCUMENT">
    @FXML
    private void createButtonClicked(ActionEvent event) {
        //show progress lane
        FXWindowUtils.showPopup(progressPopup, basePane, closeIcon, minimizeIcon, logoPane);
        //get data
        TestDocumentCreationDTO formData = getFormData();
        //create filename
        String wordFileName = Utils.constructWordDocumentFileName(formData);
        //create path
        String savePath = createTestDocumentsSavePath();
        System.out.println("Idáig eljut");
        System.out.println("Thread main: " + Thread.currentThread().getName());
        Task<Void> createDocumentTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                //progress window: set num of total pages
                setupCreationProgressWindowLabel(formData);
                //save all documents
                Main.controller.saveTestDocument(formData, savePath, wordFileName);                
                initGC();
                return null;
            }
        };
        createDocumentTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                setupProgressPointsLabel();
                createTestdocumentObjectAndUpdtaeProjectData(currentProject, wordFileName);
            }
        });
        new Thread(createDocumentTask).start();

    }

    private void createTestdocumentObjectAndUpdtaeProjectData(TestProject currentProject, String wordFileName) {

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                TestDocument newTestdocument = Main.controller.createTestDocumentObjectForProject(currentProject, wordFileName);
                currentProject.getTestDocuments().add(newTestdocument);
                Main.controller.createTestCaseObjectsBasedOnTestDocument(newTestdocument);
                return null;
            }
        };
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                updateProgressPointsLabel();
                updateProgressLabel();                
            }
        });
        new Thread(task).start();

    }

    private void showInfoPane() {

        try {
            Thread.currentThread().wait(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(CreateTestDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }

        Platform.runLater(() -> {
            FXWindowUtils.hidePopup(progressPopup, basePane, closeIcon, minimizeIcon, logoPane);
            fillInfoPopupLables(getFormData());
            FXWindowUtils.showPopup(infoPopup, basePane, closeIcon, minimizeIcon, logoPane);
        });

    }

    private void initGC() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                System.gc();
                return null;
            }
        };
        new Thread(task).start();
    }

    public void setupProgressPointsLabel() {
        okSign.setVisible(true);
        infoLineLabel.setVisible(true);
        progressPointsLabel.setVisible(true);
        progressPointsLabel.setText(". . ");
    }

    public void updateProgressPointsLabel() {
        Platform.runLater(() -> {
            String oldValue = progressPointsLabel.getText();
            String newValue = oldValue + ". .";
            progressPointsLabel.setText(newValue);
        });

    }

    public void updateProgressPointsLabel(String s) {

        Platform.runLater(() -> {
            System.out.println(s);
            String oldValue = progressPointsLabel.getText();
            String newValue = oldValue + ". .";
            progressPointsLabel.setText(". . . . .");
        });

    }

    public TestDocumentCreationDTO getFormData() {
        TestDocumentCreationDTO formData = new TestDocumentCreationDTO(
                projectNameField.getText(),
                versionfield.getText(),
                userBox.getValue(),
                Utils.convertDateToHungarianTextFormat(dateField.getValue()),
                createTestCaseID(moduleNameShortField.getText()),
                moduleNameField.getText(),
                projectTypeField.getText(),
                browserField.getText(),
                appLocationTextArea.getText(),
                Integer.parseInt(comboBox.getValue().toString()));

        return formData;
    }

    private String createTestCaseID(String moduleShortname) {
        return new StringBuilder()
                .append(currentProject.getCodeNAme())
                .append("-")
                .append(currentProject.getProjectType().getProjectTypeCodename())
                .append("-")
                .append(moduleShortname).toString();
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="HANDLERS">
    @FXML
    public void switchToWelcomeScene(ActionEvent event) throws IOException {
        Parent nextRoot = FXMLLoader.load(getClass().getResource("WelcomePage.fxml"));
        FXWindowUtils.initTransitionToNextPage(event, stage, nextRoot);
    }

    public void switchToProjectPage(ActionEvent event) throws IOException {
        Parent nextRoot = FXMLLoader.load(getClass().getResource("ProjectPage.fxml"));
        FXWindowUtils.initTransitionToNextPage(event, stage, nextRoot);
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
    public void handleOkayButton(ActionEvent event) {
        FXWindowUtils.hidePopup(infoPopup, basePane, closeIcon, minimizeIcon, logoPane);
        try {
            switchToProjectPage(event);
        } catch (IOException ex) {
            Logger.getLogger(CreateTestDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleOpenFolderButton(ActionEvent event) {
        try {
            Desktop.getDesktop().open(new File(currentProject.getProjectFolderPath() + AppConfig.TEST_DOCUMENTS_FOLDERNAME));
        } catch (IOException ex) {
            Logger.getLogger(CreateTestDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //</editor-fold>    
    //<editor-fold defaultstate="collapsed" desc="SETUP PAGE ELEMENTS">
    public void setupElements() {
        FXWindowUtils.setPageTitle(logoPane, "Teszt jegyzőkönyv", "Létrehozása", 8);
        fieldsList = setupFieldList();
        fillForm();
        setupDropDownElement();
        setupUserBox();
        setupBrowswerField();
        startFieldContentListeners();
        startDatePickerListener();
        startComboBoxListener();
        startUserBoxListener();
        startTextAreaListener();
        startBrowserFieldListener();
        limitModuleShortNameField();
        makeFieldUpperCase(moduleNameShortField);
        setupDatePicker();
        disableAppLocationFieldIfMobilApp();
        listenDocumentDoneCounter();
        setupInfoPane();

    }

    private void setupInfoPane() {
        progressPopup.setVisible(false);
        okSign.setVisible(false);
        okSign2.setVisible(false);
        infoLineLabel.setVisible(true);
        progressPointsLabel.setText("");
    }

    private void fillForm() {
        projectTypeField.setText(currentProject.getProjectTypeName());
        projectNameField.setText(currentProject.getProjectName());
        versionfield.setText(currentProject.getLatestVersionNumber());
        browserField.setText(currentProject.getBrowserTestedOn());
        setFirstActiveFieldFocus();
        appLocationTextArea.setText(currentProject.getAppLocation());
    }

    public List<TextField> setupFieldList() {
        List<TextField> list = new ArrayList<>();
        list.add(projectNameField);
        list.add(moduleNameField);
        list.add(moduleNameShortField);
        list.add(versionfield);
        list.add(browserField);
        return list;
    }

    private List<String> loadCBox1Elements() {
        List<String> answer = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            answer.add(String.valueOf(i));
        }

        return answer;
    }

    private void setupBrowswerField() {
        browserField.setText(currentProject.getBrowserTestedOn());
    }

    private void setupDropDownElement() {
        comboBox.getItems().addAll(loadCBox1Elements());
    }

    private void limitModuleShortNameField() {
        addTextLimiter(moduleNameShortField, 5);
    }

    private void initPopup(Pane pane) {
        FXWindowUtils.setupPopupWindow(pane);
    }

    private void initDragPane() {
        FXWindowUtils.makeStageDraggable(dragPane);
    }

    private void setupDatePicker() {

        dateField.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                setDisable(empty || date.compareTo(today) < 0);
            }
        });
    }

    private void disableAppLocationFieldIfMobilApp() {
        if (isProjectMobilApplication()) {
            appLocationTextArea.setDisable(true);
        }
    }

    private boolean isProjectMobilApplication() {
        return currentProject.getProjectType() == ProjectType.ANDROID_APPLICATION
                || currentProject.getProjectType() == ProjectType.IOS_APPLICATION;
    }

    private void setFirstActiveFieldFocus() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                moduleNameField.requestFocus();
            }
        });
    }

    private void setupUserBox() {

        List<String> elements = Main.controller
                .getUsers()
                .getUserList()
                .stream()
                .filter(p -> p.getRole() == UserRole.TESTER)
                .map(p -> p.getFullname())
                .collect(Collectors
                        .toList());

        userBox.
                setValue(
                        elements
                                .stream()
                                .filter(p -> p.equalsIgnoreCase(TestCenterController.userLoggedIn.getFullname()))
                                .findFirst()
                                .get());

        userBox.getItems().addAll(elements);

    }

//</editor-fold>    
    //<editor-fold defaultstate="collapsed" desc="LISTENERS">
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

    public static void updateDoneValue(Integer newValue) {
        Integer oldValue = documentDoneCounter;
        documentDoneCounter = newValue;
        propertyChangeSupport.firePropertyChange("documentDoneCounter", oldValue, newValue);
    }

    public void listenDocumentDoneCounter() {
        propertyChangeSupport = new PropertyChangeSupport(CreateTestDocumentController.class);
        propertyChangeSupport.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                updateProgressLabel((Integer) evt.getNewValue());
            }
        });
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
                if (!isThereEmptyField()) {
                    createButton.setDisable(false);
                } else {
                    createButton.setDisable(true);
                }
            }
        });
    }

    public void startDatePickerListener() {
        dateField.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> ov, LocalDate t, LocalDate t1) {
                if (!isThereEmptyField()) {
                    createButton.setDisable(false);
                } else {
                    createButton.setDisable(true);
                }
            }
        });
    }

    public void startComboBoxListener() {
        comboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                if (!isThereEmptyField()) {
                    createButton.setDisable(false);
                } else {
                    createButton.setDisable(true);
                }
            }
        });
    }

    public void startUserBoxListener() {
        userBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                if (!isThereEmptyField()) {
                    createButton.setDisable(false);
                } else {
                    createButton.setDisable(true);
                }
            }
        });
    }

    private void startTextAreaListener() {
        appLocationTextArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                if (!isThereEmptyField()) {
                    createButton.setDisable(false);
                } else {
                    createButton.setDisable(true);
                }
            }
        });
    }

    private void startBrowserFieldListener() {
        browserField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (!isThereEmptyField()) {
                    createButton.setDisable(false);
                } else {
                    createButton.setDisable(true);
                }
            }
        });
    }

    public void startFieldContentListeners() {
        for (TextField tf : fieldsList) {
            addFieldContentListener(tf);
        }
    }

    public boolean isThereEmptyField() {

        boolean isThereAnyFieldNotFilled = checkFields();
        boolean isDatePickerNull = (dateField.getValue() == null);
        boolean isDropdownNull = (comboBox.getValue() == null);
        boolean isAppLocationEmptyAndRequired = isAppLocationEmpty();

        return isThereAnyFieldNotFilled
                || isDatePickerNull
                || isDropdownNull
                || isAppLocationEmptyAndRequired;
    }

    private boolean isAppLocationEmpty() {

        if (isProjectMobilApplication()) {
            return false;
        } else {
            return null == appLocationTextArea.getText() || appLocationTextArea.getText().equalsIgnoreCase("");
        }

    }

    private boolean checkFields() {
        return fieldsList
                .stream()
                .filter(p -> null == p.getText() || p.getText().equalsIgnoreCase(""))
                .collect(Collectors.toList())
                .size() > 0;

    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="MISC UTILS">
    public String createTestDocumentsSavePath() {
        return currentProject.getProjectFolderPath() + AppConfig.TEST_DOCUMENTS_FOLDERNAME;
    }

    private void fillInfoPopupLables(TestDocumentCreationDTO settings) {
        labelPageNumber.setText(settings.getNumberOfPagesRequired() + " db");
        labelFileName.setText(Utils.getFileSystemFriendyName(settings.getAreaUnderTest().toUpperCase()) + ".docm");
        labelSaveLocation.setText(currentProject.getProjectFolderPath() + AppConfig.TEST_DOCUMENTS_FOLDERNAME);
    }

    private void updateProgressLabel() {
        indicator.setVisible(false);
        okSign3.setVisible(true);
        updateProgressPointsLabel("okSign2");
        okSign2.setVisible(true);
        okayButton1.setDisable(false);

    }

    public void setupCreationProgressWindowLabel(TestDocumentCreationDTO formData) {
        String text = "/ " + formData.getNumberOfPagesRequired() + " oldal";
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        totalLabel.setText(text);
                    }
                });
                return null;
            }
        };
        new Thread(task).start();
    }

    public void updateProgressLabel(int value) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(CreateTestDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        doneLabel.setText(String.valueOf(value));
                    }
                });
                return null;
            }
        };
        new Thread(task).start();

    }

    public void turnOnOkaySign1() {
        System.out.println("initiating turn on Okay Sign 1");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                System.out.println("lefut az okay sign GUI modifikáció");
                okSign.setVisible(true);
            }
        });
    }

    private String getTime() {
        long yourmilliseconds = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        Date resultdate = new Date(yourmilliseconds);
        return sdf.format(resultdate);
    }

//</editor-fold>
}
