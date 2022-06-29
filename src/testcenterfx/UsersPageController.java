package testcenterfx;

//<editor-fold defaultstate="collapsed" desc="Imports">
import app.Main;
import static config.AppConfig.CONTENT_FADE_OUT_DURATION;
import controller.TestCenterController;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import model.User;
import model.UserRole;
import util.FXWindowUtils;
//</editor-fold>

public class UsersPageController implements Initializable {

    //<editor-fold defaultstate="collapsed" desc="PROPERTIES">
    private Stage stage;
    List<TextField> fieldsList;
    private List<CheckBox> checkBoxList;
    static User userEdited;

//</editor-fold>    
    //<editor-fold defaultstate="collapsed" desc="FXML Properties">
    @FXML
    private ImageView closeIcon;
    @FXML
    private Line minimizeIcon;
    @FXML
    private Pane basePane;
    @FXML
    private AnchorPane editPane;
    @FXML
    private Button deleteUserButton;
    @FXML
    private Button modifyUserButton;
    @FXML
    private Pane exitPopup;
    @FXML
    private Pane hideStagePane;
    @FXML
    private AnchorPane dragPane;
    @FXML
    private AnchorPane addUserPane;
    @FXML
    private Button createButton;
    @FXML
    private Pane contentPane;
    @FXML
    private Pane infoPopup1;
    @FXML
    private Label infoPopupLabel1;
    @FXML
    private CheckBox testerBox;
    @FXML
    private CheckBox managerBox;
    @FXML
    private CheckBox adminBox;
    @FXML
    private Pane logoPane;
    @FXML
    private TextField userHomeField;
    @FXML
    private TextField fullNameField;
    @FXML
    private TableColumn<User, String> userKeyColumn;
    @FXML
    private TableColumn<User, String> nameColumn;
    @FXML
    private TableColumn<User, String> roleColumn;
    @FXML
    private TableView<User> userTable;

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="INIT">
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Main.controller.reScanUsersJSON();
        setupElements();
        initDragPane();
        initPopup(exitPopup);
        initPopup(infoPopup1);
        FXWindowUtils.addUserInfoToDragPane(dragPane);
        FXWindowUtils.addVersionInfoToDragPane(dragPane);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="HANDLERS">
    @FXML
    private void handleCreateButtonClicked(ActionEvent event) throws IOException {
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

        if (isUserAlreadyExist(userHome)) {
            infoPopupLabel1.setText("Felhasználó már létezik!");
            FXWindowUtils.showPopup(infoPopup1, hideStagePane);
        } else {
            Main.controller.createNewUser(userHome, fullName, roles);
            goToUsersPage(event);
        }
    }

    @FXML
    private void handleBackButton(ActionEvent event) throws IOException {
        goToWelcomePage(event);
    }

    @FXML
    private void minimizeStage(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void handleExitPressed(MouseEvent event) {
        FXWindowUtils.showPopup(exitPopup, hideStagePane);
    }

    @FXML
    public void handleCancelExit() {
        FXWindowUtils.hidePopup(exitPopup, hideStagePane);
    }

    @FXML
    public void handleDoExit() {
        System.exit(0);
    }

    @FXML
    private void handlePopupOkayButton(ActionEvent event) {
        FXWindowUtils.hidePopup(infoPopup1, hideStagePane);
    }

    private void goToUsersPage(ActionEvent event) throws IOException {
        Parent nextRoot = FXMLLoader.load(getClass().getResource("UsersPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXWindowUtils.initNodeFadeOutFX(CONTENT_FADE_OUT_DURATION, contentPane, 1.0, 0.01);
        FXWindowUtils.delayAndFadeInNextRoot(stage, nextRoot, event, CONTENT_FADE_OUT_DURATION);
    }

    private void goToWelcomePage(ActionEvent event) throws IOException {
        Parent nextRoot = FXMLLoader.load(getClass().getResource("WelcomePage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXWindowUtils.initNodeFadeOutFX(CONTENT_FADE_OUT_DURATION, contentPane, 1.0, 0.01);
        FXWindowUtils.delayAndFadeInNextRoot(stage, nextRoot, event, CONTENT_FADE_OUT_DURATION);
    }

    static void handleEditRequest(String userKey, TableCell tc) {
        System.out.println("edit panel requested!");
        Main.controller.reScanUsersJSON();
        userEdited = Main.controller.getUsers().getUserByUserKey(userKey);
        AnchorPane editPane = (AnchorPane) tc.getScene().lookup("#editPane");

        TextField editUserKey = (TextField) editPane.getScene().lookup("#editUserKey");
        TextField editUserName = (TextField) editPane.getScene().lookup("#editUserName");
        CheckBox editTesterBox = (CheckBox) editPane.getScene().lookup("#editTesterBox");
        CheckBox editManagerBox = (CheckBox) editPane.getScene().lookup("#editManagerBox");
        CheckBox editAdminBox = (CheckBox) editPane.getScene().lookup("#editAdminBox");
        Button modifyUserButton = (Button) editPane.getScene().lookup("#modifyUserButton");
        Button deleteUserButton = (Button) editPane.getScene().lookup("#deleteUserButton");

        editUserKey.setText(userEdited.getUserKey());
        editUserName.setText(userEdited.getFullname());
        editTesterBox.setSelected(userEdited.hasRole(UserRole.TESTER));
        editManagerBox.setSelected(userEdited.hasRole(UserRole.MANAGER));
        editAdminBox.setSelected(userEdited.hasRole(UserRole.ADMIN));

        if (userKey.equalsIgnoreCase(TestCenterController.getUserLoggedIn().getUserKey())
                && TestCenterController.getUserLoggedIn().hasRole(UserRole.ADMIN)) {
            editAdminBox.setDisable(true);
            editUserKey.setDisable(true);
            deleteUserButton.setDisable(true);
        } else {
            editAdminBox.setDisable(false);
            editUserKey.setDisable(false);
            deleteUserButton.setDisable(false);
        }

        if (!TestCenterController.userLoggedIn.hasRole(UserRole.ADMIN)) {
            editUserKey.setDisable(true);
            editUserName.setDisable(true);
            editTesterBox.setDisable(true);
            editManagerBox.setDisable(true);
            editAdminBox.setDisable(true);
            deleteUserButton.setDisable(true);
            modifyUserButton.setDisable(true);
        }

        Pane basePane = (Pane) tc.getScene().lookup("#basePane");
        ImageView closeIcon = (ImageView) tc.getScene().lookup("#closeIcon");
        Line minimizeIcon = (Line) tc.getScene().lookup("#minimizeIcon");
        Pane logoPane = (Pane) tc.getScene().lookup("#logoPane");
        FXWindowUtils.showPopup(editPane, basePane, closeIcon, minimizeIcon, logoPane);

    }

    @FXML
    private void handleModifyButtonClicked(ActionEvent event) {
        TextField editUserKey = (TextField) editPane.getScene().lookup("#editUserKey");
        TextField editUserName = (TextField) editPane.getScene().lookup("#editUserName");
        CheckBox editTesterBox = (CheckBox) editPane.getScene().lookup("#editTesterBox");
        CheckBox editManagerBox = (CheckBox) editPane.getScene().lookup("#editManagerBox");
        CheckBox editAdminBox = (CheckBox) editPane.getScene().lookup("#editAdminBox");

        userEdited.setUserKey(editUserKey.getText());
        userEdited.setFullname(editUserName.getText());
        userEdited.getRoleList().clear();
        if (editTesterBox.isSelected()) {
            userEdited.getRoleList().add(UserRole.TESTER);
        }
        if (editManagerBox.isSelected()) {
            userEdited.getRoleList().add(UserRole.MANAGER);
        }
        if (editAdminBox.isSelected()) {
            userEdited.getRoleList().add(UserRole.ADMIN);
        }

        Main.controller.updateUsersJsonFile();
        Main.controller.reScanUsersJSON();
        TestCenterController.setUserLoggedIn(Main.controller.getUsers().getUserByUserKey(System.getProperty("user.home")));
        try {
            goToUsersPage(event);
        } catch (IOException ex) {
            Logger.getLogger(UsersPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleEditPaneBackButton(ActionEvent event) throws IOException {
        FXWindowUtils.hidePopup(editPane, basePane, closeIcon, minimizeIcon, logoPane);
    }
    
    @FXML
    private void handleDeleteUserButtonClicked(ActionEvent event) {
        TextField userKeyFiled = (TextField) editPane.getScene().lookup("#editUserKey");
        Boolean isUserRemoveSuccesful = Main.controller.getUsers().removeUser(userEdited);
        
        if (isUserRemoveSuccesful){
            Main.controller.updateUsersJsonFile();
            try {
                goToUsersPage(event);
            } catch (IOException ex) {
                Logger.getLogger(UsersPageController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("Nincs ilyen felhasznalo");
        }
        
    }
    

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Setup Page Elements">
    public void setupElements() {
        userHomeField.setEditable(true);
        userHomeField.setDisable(false);
        editPane.setVisible(false);
        if (isUserAdmin()) {
            addUserPane.setDisable(false);
        } else {
            addUserPane.setDisable(true);
        }
        checkBoxList = new ArrayList<>(Arrays.asList(new CheckBox[]{testerBox, adminBox, managerBox}));
        createButton.setDisable(true);
        FXWindowUtils.setPageTitle(logoPane, "Teszt Labor", "Felhasználók");
        setupUserTable();
        addNewUserFieldsListener();

    }

    private void setupUserTable() {

        List<User> userList = Main.controller
                .getUsers()
                .getUserList()
                .stream()
                .collect(Collectors.toList());
        userKeyColumn.setCellValueFactory(new PropertyValueFactory<>("userKey"));
        userKeyColumn.setCellFactory(CustomUserTableCell.forTableColumn());
        userKeyColumn.setStyle("-fx-alignment: center-left;");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        nameColumn.setStyle("-fx-alignment: center-left;");
        //roleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().))
        roleColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getAllRolesInOneString()));
        roleColumn.setStyle("-fx-alignment: center-left;");
        //roleColumn.setCellFactory(D);
        userTable.getItems().addAll(FXCollections.observableArrayList(userList));

    }

    public void addFieldContentListener(final TextField tf) {
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (!isThereRquiredElementsNotFilled()) {
                    createButton.setDisable(false);
                } else {
                    createButton.setDisable(true);
                }
            }
        });
    }

    public void startComboBoxListener(ComboBox<String> combo) {
        combo.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (!isThereRquiredElementsNotFilled()) {
                    createButton.setDisable(false);
                } else {
                    createButton.setDisable(true);
                }

            }
        });
    }

    public boolean isThereRquiredElementsNotFilled() {
        boolean isNamefiledEmpty = fullNameField.getText().equalsIgnoreCase("");
        boolean isUserHomeFieldEmpty = userHomeField.getText().equalsIgnoreCase("");
        boolean isNoRoleSelected = isNoBoxSelected();
        return isUserHomeFieldEmpty || isNamefiledEmpty || isNoRoleSelected;
    }

    private void initPopup(Pane pane) {
        FXWindowUtils.setupPopupWindow(pane);
    }

    private void initDragPane() {
        FXWindowUtils.makeStageDraggable(dragPane);
    }

    private boolean isUserAlreadyExist(String userHome) {
        return Main.controller
                .getUsers()
                .getUserList()
                .stream()
                .filter(p -> p.getUserKey().equals(userHome))
                .collect(Collectors.toList())
                .size() > 0;
    }
//</editor-fold>

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

    private boolean isUserAdmin() {
        return controller.TestCenterController.userLoggedIn.getRoleList().contains(UserRole.ADMIN);
    }

}
