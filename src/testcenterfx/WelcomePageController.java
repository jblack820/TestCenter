package testcenterfx;

//<editor-fold defaultstate="collapsed" desc="IMPORTS">
import app.Main;
import config.AppConfig;
import config.FolderStructure;
import controller.TestCenterController;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import model.ArchivedProjectName;
import model.TestProject;
import model.User;
import model.UserRole;
import model.Users;
import util.FXWindowUtils;
//</editor-fold>

/**
 *
 * @author takacs.gergely
 */
public class WelcomePageController implements Initializable {

    //<editor-fold defaultstate="collapsed" desc="ATTRIBUTES">
    public static boolean isDocumentsScanned;
    private Stage stage;
    public static Stage staticStage;
    private List<CheckBox> checkBoxList;
    public static boolean isReactivateProcessFailed;
    public static final List<TestProject> FAILED_TO_ARCHIVE_PROJECTS_LIST = new ArrayList<>();
    @FXML
    private Pane mypopup;
    @FXML
    private Pane reactivateErrorPane;
    @FXML
    private TextArea archiveErrorTextArea;
    @FXML
    private Pane archiveErrorPane;
    @FXML
    private Pane scanningDocsPane;
    @FXML
    private Label installPath;
    @FXML
    private Label adminLabel;
    @FXML
    private Label invaliPasswordLabel;
    @FXML
    private Button enterAdminButton;
    @FXML
    private Label redLabel;
    @FXML
    private Button okayButton;
    @FXML
    private Button createButton;
    @FXML
    private Pane installPathPane;
    @FXML
    private Pane newUserPane;
    @FXML
    private Pane userNotExistWarningPane;
    @FXML
    private Pane basePane;
    @FXML
    private Pane logoPane;
    @FXML
    private AnchorPane dragPane;
    @FXML
    private AnchorPane welcomeStage;
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
    private CheckBox testerBox;
    @FXML
    private CheckBox managerBox;
    @FXML
    private CheckBox adminBox;
    @FXML
    private ToggleGroup toggleGroup;
    @FXML
    private TextField userHomeField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField fullNameField;

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="INIT">
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initStageElements();

        if (isTestCenterLocationNeeded()) {
            initInstallScreenListeners();
            FXWindowUtils.showPopup(installPathPane, basePane, closeIcon, minimizeIcon, logoPane, bigLogo);
        }

        if (!isDocumentsScanned && !isTestCenterLocationNeeded()) {
            scanDocumentsAndReloadPage();
        }

        if (isDocumentsScanned && isUserCreationNeeded()) {
            initUserNotExistWarning();
        }

        if (isCurrentUserExist()) {
            TestCenterController.userLoggedIn = Main.controller.getUsers().getUserByUserKey(System.getProperty("user.home"));
            FXWindowUtils.addUserInfoToDragPane(dragPane);
        }

        if (isReactivateProcessFailed && isDocumentsScanned) {
            System.out.println("\n\nFailed reactivation");
            isReactivateProcessFailed = false;
            reactivateErrorPane.setVisible(true);
        }

        if (!FAILED_TO_ARCHIVE_PROJECTS_LIST.isEmpty()) {
            String text = getProjectNamesForArea();
            archiveErrorTextArea.setText(text);
            archiveErrorPane.setVisible(true);
            removeFailedToArchiveProjectsFromArchive(FAILED_TO_ARCHIVE_PROJECTS_LIST);
            FAILED_TO_ARCHIVE_PROJECTS_LIST.clear();
        }

    }

    private void initStageElements() {
        FXWindowUtils.makeStageDraggable(dragPane);
        FXWindowUtils.setupPopupWindow(mypopup);
        FXWindowUtils.setupPopupWindow(installPathPane);
        FXWindowUtils.addVersionInfoToDragPane(dragPane);
        reactivateErrorPane.setVisible(false);
        archiveErrorPane.setVisible(false);
        userNotExistWarningPane.setVisible(false);
        invaliPasswordLabel.setVisible(false);
        newUserPane.setVisible(false);
        checkBoxList = new ArrayList<>(Arrays.asList(new CheckBox[]{testerBox, adminBox, managerBox}));
    }

    private void initInstallScreenListeners() {
        toggleGroup.selectedToggleProperty().addListener((ov, oldToggle, newToggle) -> {
            installPath.setText("");
            redLabel.setVisible(false);
            okayButton.setDisable(true);
        });
    }

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

    private void creatTestCenter(Label installPath, ActionEvent event) {
        staticStage = (Stage) basePane.getScene().getWindow();
        Task<Void> installTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                String fullInstallPath = installPath.getText() + AppConfig.TEST_CENTER_FOLDERNAME + File.separator;
                Main.controller.getTestCenter().setFolderStructure(new FolderStructure(fullInstallPath));
                Main.controller.setupTestCenterApp(installPath.getText());
                Main.controller.getSaveSettings().saveSettingsToUserHome(fullInstallPath);
                controller.TestCenterController.isTestCenterFolderLocationNeeded = false;
                FXWindowUtils.hidePopup(installPathPane, basePane, closeIcon, minimizeIcon, logoPane, bigLogo);
                return null;
            }
        };
        installTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                TestCenterController.isUserCreationNeeded = true;
                TestCenterController.isTestCenterFolderLocationNeeded = false;
                scanDocumentsAndReloadPage();
            }
        });
        Thread installThread = new Thread(installTask);
        installThread.start();
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="USER CHECK">
    private boolean isCurrentUserExist() {
        return Main.controller.getUsers().isUserExist(System.getProperty("user.home"));
    }

    private void initUserNotExistWarning() {
        List<String> adminList = getAdminStringList();
        if (adminList.isEmpty()) {
            adminLabel.setText("Még nincsenek adminisztrátorok a rendszerben. Lépjen be jelszőval!");
        } else {
            adminLabel.setText(getCommaSeparatedStringList(adminList));
        }
        startPasswordFieldListener();
        FXWindowUtils.showPopup(userNotExistWarningPane, basePane, closeIcon, minimizeIcon, logoPane, bigLogo);
    }

    private void initAddUserProcess() {
        addNewUserFieldsListener();
        adminBox.setSelected(true);
        adminBox.setDisable(true);
        userHomeField.setText(System.getProperty("user.home"));
        FXWindowUtils.showPopup(newUserPane, basePane, closeIcon, minimizeIcon, logoPane, bigLogo);

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
    private void handleGoToClonePage(ActionEvent event) throws IOException {
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
    public void handleAdminButtonPressed() {
        if (passwordField.getText().equals(AppConfig.ADMIN_PASSWORD)) {
            initAddUserProcess();
        } else {
            invaliPasswordLabel.setVisible(true);
        }
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

    @FXML
    public void handleHideReactivateErrorPane() {
        reactivateErrorPane.setVisible(false);
    }

    @FXML
    public void handleHideArchiveErrorPane() {
        archiveErrorPane.setVisible(false);
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
            creatTestCenter(installPath, event);
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
        Parent nextRoot = FXMLLoader.load(getClass().getResource("WelcomePage.fxml"));
        Stage currentStage = (Stage) basePane.getScene().getWindow();
        FXWindowUtils.initTransitionToNextPage(event, currentStage, nextRoot);
    }

    private void refreshPage(ActionEvent event) throws IOException {
        Parent nextRoot = FXMLLoader.load(getClass().getResource("WelcomePage.fxml"));
        Stage currentStage = (Stage) basePane.getScene().getWindow();
        FXWindowUtils.initTransitionToNextPage(event, currentStage, nextRoot);
    }

    private void createUser() {
        String userHome = userHomeField.getText();
        String fullName = fullNameField.getText();

        List<UserRole> roles = new ArrayList<>();
        if (testerBox.isSelected()) {
            roles.add(UserRole.TESTER);
        }
        if (adminBox.isSelected()) {
            roles.add(UserRole.ADMIN);
        }
        if (managerBox.isSelected()) {
            roles.add(UserRole.MANAGER);
        }
        Main.controller.createNewUser(userHome, fullName, roles);
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="UTILS">
    private void refreshPage() {
        Stage currentStage = (Stage) welcomeStage.getScene().getWindow();
        Parent nextRoot = null;
        try {
            nextRoot = FXMLLoader.load(getClass().getResource("WelcomePage.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(WelcomePageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        FXWindowUtils.initTransitionToNextPage(currentStage, nextRoot);
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

    private void scanDocumentsAndReloadPage() {
        Task<Void> startScanning = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(() -> {
                    FXWindowUtils.showPopup(scanningDocsPane, basePane, closeIcon, minimizeIcon, logoPane, bigLogo);
                });
                return null;
            }
        };
        startScanning.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                Task<Void> scanDocs = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        System.out.println("Scanning starts...");
                        Main.controller.scanDocumentsAndCreateProjectObjects();
                        return null;
                    }
                };
                scanDocs.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        System.out.println("Scanning ended!");
                        isDocumentsScanned = true;
                        refreshPage();
                    }
                });
                new Thread(scanDocs).start();
            }
        });
        new Thread(startScanning).start();
    }

    private boolean isTestCenterLocationNeeded() {
        return controller.TestCenterController.isTestCenterFolderLocationNeeded;
    }

    private boolean isUserCreationNeeded() {
        return controller.TestCenterController.isUserCreationNeeded;
    }
    
    private List<String> getAdminStringList() {
        return Main.controller
                .getUsers()
                .getAdminList()
                .stream()
                .map(p -> p.getFullname())
                .collect(Collectors.toList());
    }

    private String getCommaSeparatedStringList(List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i != list.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    private void startPasswordFieldListener() {
        passwordField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                invaliPasswordLabel.setVisible(false);
                if (passwordField.getText().equals("")) {
                    enterAdminButton.setDisable(true);
                } else {
                    enterAdminButton.setDisable(false);
                }
            }
        });
    }

    private void addNewUserFieldsListener() {

        userHomeField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (!isThereRquiredElementsNotFilled()) {
                    createButton.setDisable(false);
                } else {
                    createButton.setDisable(true);
                }
            }
        });

        fullNameField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (!isThereRquiredElementsNotFilled()) {
                    createButton.setDisable(false);
                } else {
                    createButton.setDisable(true);
                }
            }
        });

        for (CheckBox checkBox : checkBoxList) {
            checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (!isThereRquiredElementsNotFilled()) {
                        createButton.setDisable(false);
                    } else {
                        createButton.setDisable(true);
                    }
                }
            });

        }
    }

    public boolean isThereRquiredElementsNotFilled() {
        boolean isNamefiledEmpty = fullNameField.getText().equalsIgnoreCase("");
        boolean isUserHomeFieldEmpty = userHomeField.getText().equalsIgnoreCase("");
        boolean isNoRoleSelected = isNoBoxSelected();
        return isUserHomeFieldEmpty || isNamefiledEmpty || isNoRoleSelected;
    }

    private boolean isNoBoxSelected() {
        boolean answer = true;

        for (CheckBox checkBox : checkBoxList) {
            if (checkBox.isSelected() == true) {
                answer = false;
                break;
            }
        }
        return answer;
    }

    private String getProjectNamesForArea() {
        StringBuilder sb = new StringBuilder();

        for (TestProject testProject : FAILED_TO_ARCHIVE_PROJECTS_LIST) {
            sb.append(testProject.getProjectName()).append(System.lineSeparator());
        }
        return sb.toString();
    }

    private void removeFailedToArchiveProjectsFromArchive(List<TestProject> FAILED_TO_ARCHIVE_PROJECTS_LIST) {

        for (TestProject tp : FAILED_TO_ARCHIVE_PROJECTS_LIST) {            
            String projectPath = tp.getArchivedProjectFolderPath();
            File file = new File(projectPath);
            
            if (file.exists()) {
                
                String projectName = tp.getProjectName();
                
                ArchivedProjectName archivedProjectName = Main.controller.getTestCenter().getArchivedProjectNameByProjectName(projectName);
                if (null!=archivedProjectName){
                    Main.controller.getTestCenter().getARCHIVED_PROJECT_NAMES().remove(archivedProjectName);
                }
                
                try {
                    Files.delete(file.toPath());
                } catch (IOException ex) {
                    Logger.getLogger(WelcomePageController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }

    }

//</editor-fold>
}
