package testcenterfx;

//<editor-fold defaultstate="collapsed" desc="IMPORTS">
import app.Main;
import config.AppConfig;
import config.FolderStructure;
import controller.TestCenterController;
import java.io.File;
import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import model.UserRole;
import util.FXWindowUtils;
//</editor-fold>

/**
 *
 * @author takacs.gergely
 */
public class WelcomePageController implements Initializable {


    //<editor-fold defaultstate="collapsed" desc="INIT">
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initStageElements();

        if (isTestCenterLocationNeeded()) {
            initInstallScreenListeners();
            FXWindowUtils.showPopup(installPathPane, basePane, closeIcon, minimizeIcon, logoPane, bigLogo);
        }

        if (!isDocumentsScanned && !isTestCenterLocationNeeded()) {
            Task<Void> scanDocsTask = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    scanDocuments();
                    return null;
                }

            };
            scanDocsTask.setOnSucceeded((event) -> {
                isDocumentsScanned = true;
            });
            new Thread(scanDocsTask).start();

        }

        if (isDocumentsScanned && isUserCreationNeeded()) {            
            initAddUserProcess();
        }

        if (isCurrentUserExist()) {
            TestCenterController.userLoggedIn = Main.controller.getUsers().getUserByUserKey(System.getProperty("user.home"));
            FXWindowUtils.addUserInfoToDragPane(dragPane);
        }
    }

    private void initStageElements() {
        FXWindowUtils.makeStageDraggable(dragPane);
        FXWindowUtils.setupPopupWindow(mypopup);
        FXWindowUtils.setupPopupWindow(installPathPane);
        FXWindowUtils.addVersionInfoToDragPane(dragPane);
        newUserPane.setVisible(false);
        addUserButton.setDisable(true);
    }

    private void initInstallScreenListeners() {
        toggleGroup.selectedToggleProperty().addListener((ov, oldToggle, newToggle) -> {
            installPath.setText("");
            redLabel.setVisible(false);
            okayButton.setDisable(true);
        });
    }

    private void initfiledListeners() {
        addFieldContentListener(fullNameField);
        startComboBoxListener(roleSelector);
    }

    public void addFieldContentListener(final TextField tf) {
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (!isThereEmptyField()) {
                    addUserButton.setDisable(false);
                } else {
                    addUserButton.setDisable(true);
                }
            }
        });
    }

    public void startComboBoxListener(ComboBox<String> combo) {
        combo.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (!isThereEmptyField()) {
                    addUserButton.setDisable(false);
                } else {
                    addUserButton.setDisable(true);
                }

            }
        });
    }

    public boolean isThereEmptyField() {
        boolean isDropDownEmpty = null == roleSelector.getValue() || roleSelector.getValue().equalsIgnoreCase("");
        boolean isNamefiledEmpty = fullNameField.getText().equalsIgnoreCase("");
        return isDropDownEmpty || isNamefiledEmpty;
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="ATTRIBUTES">
public static boolean isDocumentsScanned;
    private Stage stage;
    @FXML
    private Pane mypopup;
    @FXML
    private Pane scanningDocsPane;
    @FXML
    private Label installPath;
    @FXML
    private Label redLabel;
    @FXML
    private Button okayButton;
    @FXML
    private Button addUserButton;
    @FXML
    private Pane installPathPane;
    @FXML
    private Pane newUserPane;
    @FXML
    private Pane basePane;
    @FXML
    private Pane logoPane;
    @FXML
    private AnchorPane dragPane;
    @FXML
    private ImageView closeIcon;
    @FXML
    private Line minimizeIcon;
    @FXML
    private ImageView bigLogo;
    @FXML
    private ImageView foldericon;
    @FXML
    private RadioButton radioButton1;
    @FXML
    private ToggleGroup toggleGroup;
    @FXML
    private TextField userHomeField;
    @FXML
    private TextField fullNameField;
    @FXML
    private ComboBox<String> roleSelector;

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TEST CENTER EXIST CHECK">
    private boolean isTestCenterFolderPresentInFolder(Label installPath) {
        boolean answer = false;
        File targetDir = new File(installPath.getText() + File.separator);

        File[] subDirectories = targetDir.listFiles(File::isDirectory);

        if (null != subDirectories) {
            answer = Arrays
                    .asList(subDirectories)
                    .stream()
                    .filter(p -> p.getName()
                    .equalsIgnoreCase(AppConfig.TEST_CENTER_FOLDERNAME))
                    .collect(Collectors.toList())
                    .size() > 0;
        }
        return answer;
    }

    private boolean isSelectingExistingfolder() {
        return radioButton1.isSelected();
    }

    private boolean isValidFolderSelected(String fullInstallPath) {
        boolean answer = false;
        File targetDir = new File(fullInstallPath);
        File[] subDirectories = targetDir.listFiles(File::isDirectory);

        if (null != subDirectories) {
            answer = Arrays
                    .asList(subDirectories)
                    .stream()
                    .filter(p -> p.getName().equalsIgnoreCase(AppConfig.ACTIVE_PROJECTS_FOLDERNAME)
                    || p.getName().equalsIgnoreCase(AppConfig.ARCHIVED_PROJECTS_FOLDERNAME))
                    .collect(Collectors.toList())
                    .size() == 2;
        }
        return answer;

    }

    private void creatTestCenter(Label installPath) {

        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                String fullInstallPath = installPath.getText() + AppConfig.TEST_CENTER_FOLDERNAME + File.separator;
                
                
                Main.controller.getTestCenter().setFolderStructure(new FolderStructure(fullInstallPath));
                
                
                Main.controller.setupTestCenterApp(installPath.getText());
                
                //saving location to txt
                Main.controller.getSaveSettings().saveSettingsToUserHome(fullInstallPath);
                controller.TestCenterController.isTestCenterFolderLocationNeeded = false;
                FXWindowUtils.hidePopup(installPathPane, basePane, closeIcon, minimizeIcon, logoPane, bigLogo);
                return null;
            }
        };
        sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                TestCenterController.isUserCreationNeeded = true;
                controller.TestCenterController.isTestCenterFolderLocationNeeded = false;
                System.out.println("Refreshing page...");
                refreshPage();
            }
        });
        new Thread(sleeper).start();

    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="USER CHECK">
    private boolean isCurrentUserExist() {
        return Main.controller.getUsers().isUserExist(System.getProperty("user.home"));
    }

    private void initRoleSelector() {
        List<String> roleList = Arrays.asList(UserRole.values()).stream().map(p -> p.getName()).collect(Collectors.toList());
        roleSelector.getItems().addAll(roleList);
    }

    private void initAddUserProcess() {
        System.out.println("init add user");
        FXWindowUtils.showPopup(newUserPane, basePane, closeIcon, minimizeIcon, logoPane, bigLogo);
        userHomeField.setText(System.getProperty("user.home"));
        userHomeField.setDisable(true);
        initfiledListeners();
        initRoleSelector();
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="HANDLERS">
    @FXML
    private void handleListActiveProjectsButton(ActionEvent event) throws IOException {
        Parent nextRoot = FXMLLoader.load(getClass().getResource("ListProjectsPage.fxml"));
        FXWindowUtils.initTransitionToNextPage(event, stage, nextRoot);
    }
    
    @FXML
    private void handleGoToArchivedProjectsButton(ActionEvent event) throws IOException {
        Parent nextRoot = FXMLLoader.load(getClass().getResource("ReActivateProjectsPage.fxml"));
        FXWindowUtils.initTransitionToNextPage(event, stage, nextRoot);
    }

    @FXML
    private void handleGoTouserPageButton(ActionEvent event) throws IOException {
        Parent nextRoot = FXMLLoader.load(getClass().getResource("UsersPage.fxml"));
        FXWindowUtils.initTransitionToNextPage(event, stage, nextRoot);
    }
    
    @FXML
    private void handleGoToClonePage (ActionEvent event) throws IOException {
        Parent nextRoot = FXMLLoader.load(getClass().getResource("CloneProjectPage.fxml"));
        FXWindowUtils.initTransitionToNextPage(event, stage, nextRoot);
    }

    @FXML
    private void handleGoToDevicesPageButton(ActionEvent event) throws IOException {
        Parent nextRoot = FXMLLoader.load(getClass().getResource("DevicesPage.fxml"));
        FXWindowUtils.initTransitionToNextPage(event, stage, nextRoot);
    }

    @FXML
    private void minimizeStage(MouseEvent event) {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void handleExitPressed() {
        FXWindowUtils.showPopup(mypopup, basePane, closeIcon, minimizeIcon, logoPane, bigLogo);
    }

    @FXML
    public void handleCancelExit() {
        FXWindowUtils.hidePopup(mypopup, basePane, closeIcon, minimizeIcon, logoPane, bigLogo);
    }

    @FXML
    public void handleDoExit() {
        System.exit(0);
    }

    @FXML
    private void handleCreateNewProjectButton(ActionEvent event) throws IOException {
        Parent nextRoot = FXMLLoader.load(getClass().getResource("CreateProjectPage.fxml"));
        FXWindowUtils.initTransitionToNextPage(event, stage, nextRoot);
    }

    @FXML
    private void handleFileChooserClicked() {
        redLabel.setVisible(false);
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog((Stage) basePane.getScene().getWindow());
        String selectedDirPath = selectedDirectory.getAbsolutePath() + File.separator;
        installPath.setText(selectedDirPath);
        String fullInstallPath;

        if (isSelectingExistingfolder()) {
            fullInstallPath = installPath.getText();
            if (isValidFolderSelected(fullInstallPath)) {
                enableContinue();
            } else {
                disableContinue();
            }
        } else {
            if (isLabelFilled(installPath)) {
                redLabel.setVisible(false);
                okayButton.setDisable(false);
            }
        }
    }

    private boolean isLabelFilled(Label label) {
        return !label.getText().equalsIgnoreCase("");
    }

    private void enableContinue() {
        redLabel.setStyle("-fx-text-fill: green");
        redLabel.setText("Érvényes TestCenter mappa. Kattintson a Tovább gombra a folytatáshoz");
        redLabel.setVisible(true);
        okayButton.setDisable(false);
    }

    @FXML
    private void clearPathLabel() {
        installPath.setText("");
    }

    @FXML
    private void setFolderIconOpacityToMax() {
        foldericon.setOpacity(1.0);
    }

    @FXML
    private void reduceFolderIconOpacity() {
        foldericon.setOpacity(0.6);
    }

    @FXML
    private void handleCommitButton(ActionEvent event) {

        if (isSelectingExistingfolder()) {

            Task<Void> showPopup = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            installPathPane.setVisible(false);
                            FXWindowUtils.showPopup(scanningDocsPane, basePane, closeIcon, minimizeIcon, logoPane, bigLogo);
                        }
                    });
                    return null;
                }
            };

            showPopup.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent t) {
                    Task<Void> task = new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            String fullInstallPath = installPath.getText();
                            try {
                                controller.TestCenterController.isTestCenterFolderLocationNeeded = false;
                                loadTestCenter(fullInstallPath, event);
                            } catch (IOException ex) {
                                Logger.getLogger(WelcomePageController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            return null;
                        }

                    };
                    new Thread(task).start();
                }
            });
            new Thread(showPopup).start();

        } else {
            creatTestCenter(installPath);
        }

    }

    private void loadTestCenter(String fullInstallPath, ActionEvent actionEvent) throws IOException {
        Main.controller.getTestCenter().setFolderStructure(new FolderStructure(fullInstallPath));
        Main.controller.getSaveSettings().saveSettingsToUserHome(fullInstallPath);
        Main.controller.initApplication();
        FXWindowUtils.hidePopup(installPathPane, basePane, closeIcon, minimizeIcon, logoPane, bigLogo);
        try {
            scanDocsAndRefrehPage(actionEvent);
        } catch (IOException ex) {
            Logger.getLogger(WelcomePageController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void handleAddUser(ActionEvent event) throws IOException {

        TestCenterController.isUserCreationNeeded = false;
        createUser();

        System.out.println("\nRefreshing starting...");
        Parent nextRoot = FXMLLoader.load(getClass().getResource("WelcomePage.fxml"));
        Stage currentStage = (Stage) basePane.getScene().getWindow();
        System.out.println("\nevent null?:"  + (null==event));
        System.out.println("\nstage null?:"  + (null==stage));
        System.out.println("\nnextRoozt null?:"  + (null==nextRoot));
        
        FXWindowUtils.initTransitionToNextPage(event, currentStage, nextRoot);
        System.out.println("\nRefreshing ended!!");

    }

    private void refreshPage(ActionEvent event) throws IOException {
        Parent nextRoot = FXMLLoader.load(getClass().getResource("WelcomePage.fxml"));
        Stage currentStage = (Stage) basePane.getScene().getWindow();
        FXWindowUtils.initTransitionToNextPage(event, currentStage, nextRoot);
    }

    private void createUser() {
        Main.controller.createNewUser(userHomeField.getText(), fullNameField.getText(), roleSelector.getValue());
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="UTILS">
    private void refreshPage() {
        stage = (Stage) basePane.getScene().getWindow();
        Parent nextRoot = null;
        try {
            nextRoot = FXMLLoader.load(getClass().getResource("WelcomePage.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(WelcomePageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        FXWindowUtils.initTransitionToNextPage(stage, nextRoot);
    }

    private void disableContinue() {
        redLabel.setStyle("-fx-text-fill: red");
        redLabel.setText("Érvénytelen TestCenter mappa");
        redLabel.setVisible(true);
        okayButton.setDisable(true);

    }

    private void scanDocsAndRefrehPage(ActionEvent event) throws IOException {
        Main.controller.scanDocumentsAndCreateProjectObjects();
        isDocumentsScanned = true;
        refreshPage(event);
    }

    private void scanDocuments() {

        Task<Void> showProgressPane = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(() -> {
                    FXWindowUtils.showPopup(scanningDocsPane, basePane, closeIcon, minimizeIcon, logoPane, bigLogo);
                });
                return null;
            }
        };
        showProgressPane.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                System.out.println("Scanning documents started...");
                Task<Void> scanDocs = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        Main.controller.scanDocumentsAndCreateProjectObjects();
                        System.out.println("Scanning documents finished");
                        return null;
                    }
                };
                scanDocs.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        isDocumentsScanned = true;
                        refreshPage();
                    }
                });
                new Thread(scanDocs).start();
            }
        });
        new Thread(showProgressPane).start();
    }

    private boolean isTestCenterLocationNeeded() {
        return controller.TestCenterController.isTestCenterFolderLocationNeeded;
    }

    private boolean isUserCreationNeeded() {
        return controller.TestCenterController.isUserCreationNeeded;
    }

//</editor-fold>
}
