package testcenterfx;

//<editor-fold defaultstate="collapsed" desc="Imports">
import app.Main;
import static config.AppConfig.CONTENT_FADE_OUT_DURATION;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
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

//</editor-fold>    
    //<editor-fold defaultstate="collapsed" desc="FXML Properties">
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

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Setup Page Elements">
    public void setupElements() {
        userHomeField.setEditable(true);
        userHomeField.setDisable(false);
        
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
