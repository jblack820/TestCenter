package testcenterfx;

//<editor-fold defaultstate="collapsed" desc="Imports">
import app.Main;
import static config.AppConfig.CONTENT_FADE_OUT_DURATION;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
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

/**
 * FXML Controller class
 *
 * @author takacs.gergely
 */
public class UsersPageController implements Initializable {

    //<editor-fold defaultstate="collapsed" desc="PROPERTIES">
    
    private Stage stage;
    List<TextField> fieldsList;
    
//</editor-fold>    
    //<editor-fold defaultstate="collapsed" desc="FXML Properties">
    @FXML
    private Pane exitPopup;   
    @FXML
    private Pane hideStagePane;
    @FXML
    private AnchorPane dragPane; 
    @FXML
    private Button createButton;
    @FXML
    private Pane contentPane;
    @FXML
    private Pane infoPopup1;
    @FXML
    private Label infoPopupLabel1; ;
    @FXML
    private Pane logoPane;
    @FXML
    private TextField userHomeField;
    @FXML
    private TextField fullNameField;
    @FXML
    private ComboBox<String> roleSelector;
    @FXML
    private TableColumn<UserPageTableData, String> userKeyColumn;
    @FXML
    private TableColumn<UserPageTableData, String> nameColumn;
    @FXML
    private TableColumn<UserPageTableData, String> roleColumn;
    @FXML
    private TableView<UserPageTableData> userTable;

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
        String role = roleSelector.getValue();
        
        if (isUserAlreadyExist(userHome)){    
            infoPopupLabel1.setText("Felhasználó már létezik!");
            FXWindowUtils.showPopup(infoPopup1, hideStagePane);            
        } else {
            Main.controller.createNewUser(userHome, fullName, role);
            goToListProjectsPage(event);
        }
    }
    @FXML
    private void handleBackButton(ActionEvent event) throws IOException {
        goToListProjectsPage(event);
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
    @FXML
    private void handleOpenFolderButton(ActionEvent event) {
    }
    
    private void goToListProjectsPage(ActionEvent event) throws IOException {
        Parent nextRoot = FXMLLoader.load(getClass().getResource("WelcomePage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXWindowUtils.initNodeFadeOutFX(CONTENT_FADE_OUT_DURATION, contentPane, 1.0, 0.01);
        FXWindowUtils.delayAndFadeInNextRoot(stage, nextRoot, event, CONTENT_FADE_OUT_DURATION);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Setup Page Elements">
    public void setupElements() {
        createButton.setDisable(true);
        FXWindowUtils.setPageTitle(logoPane, "Teszt Labor", "Felhasználók");
        setupUserTable();
        setupDropDownElements();
        setupUserHomeFiled();
        addFieldContentListener(fullNameField);
        startComboBoxListener(roleSelector);
    }

    private void setupUserTable() {

        List<UserPageTableData> userList = Main.controller
                .getUsers()
                .getUserList()
                .stream()
                .map(p -> createUserPageTableDatObjct(p))
                .sorted((UserPageTableData o1, UserPageTableData o2) -> o1.getFullname().compareToIgnoreCase(o2.getFullname()))
                .collect(Collectors.toList());
        userKeyColumn.setCellValueFactory(new PropertyValueFactory<>("userKey"));
        userKeyColumn.setStyle("-fx-alignment: center-left;");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        nameColumn.setStyle("-fx-alignment: center-left;");
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        roleColumn.setStyle("-fx-alignment: center-left;");
        //roleColumn.setCellFactory(D);
        userTable.getItems().addAll(FXCollections.observableArrayList(userList));

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

    public void startComboBoxListener(ComboBox<String> combo) {
        combo.valueProperty().addListener(new ChangeListener<String>() {
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

    public boolean isThereEmptyField() {
        boolean isDropDownEmpty = null == roleSelector.getValue() || roleSelector.getValue().equalsIgnoreCase("");
        boolean isNamefiledEmpty = fullNameField.getText().equalsIgnoreCase("");
        return isDropDownEmpty || isNamefiledEmpty;
    }

    private void setupUserHomeFiled() {
        userHomeField.setText(System.getProperty("user.home"));
        userHomeField.setEditable(false);
    }

    private void initPopup(Pane pane) {
        FXWindowUtils.setupPopupWindow(pane);
    }

    private void initDragPane() {
        FXWindowUtils.makeStageDraggable(dragPane);
    }

    private void setupDropDownElements() {
        List<String> roleList = Arrays.asList(UserRole.values()).stream().map(p -> p.getName()).collect(Collectors.toList());
        roleSelector.getItems().addAll(roleList);
    }
    
    private UserPageTableData createUserPageTableDatObjct(User u) {

        return new UserPageTableData(u.getUserKey(), u.getFullname(), u.getRole().getName());
    }
    
    private boolean isUserAlreadyExist(String userHome) {
        return Main.controller
                .getUsers()
                .getUserList()
                .stream()
                .filter(p->p.getUserKey().equals(userHome))
                .collect(Collectors.toList())
                .size()>0;
    }
//</editor-fold>

}
