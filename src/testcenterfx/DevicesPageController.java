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
import java.util.stream.IntStream;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import model.OsType;
import model.TestDevice;
import model.TestDocumentCreationDTO;
import model.User;
import util.FXWindowUtils;
import util.JsonUtils;
//</editor-fold>

/**
 * FXML Controller class
 *
 * @author takacs.gergely
 */
public class DevicesPageController implements Initializable {

    private Stage stage;
    List<TextField> fieldsList;
    static TestDevice td;

    //<editor-fold defaultstate="collapsed" desc="FXML Properties">
    @FXML
    private Pane exitPopup;
    @FXML
    private Pane infoPopup;
    @FXML
    private Pane hidePane;
    @FXML
    private Pane basePane;
    @FXML
    private Pane hideStagePane;
    @FXML
    private Pane notespane;
    @FXML
    private Pane deletePane;
    @FXML
    private Label infoLabel;
    @FXML
    private Label alertDeviceNameLabel;
    @FXML
    private Pane logoPane;
    @FXML
    private AnchorPane anchorPaneBase;
    @FXML
    private AnchorPane editPane;
    @FXML
    private AnchorPane dragPane;
    @FXML
    private ImageView closeIcon;
    @FXML
    private Line minimizeIcon;
    @FXML
    private AnchorPane cb2;
    @FXML
    private Button createButton;
    @FXML
    private Pane contentPane;
    @FXML
    private Button backButton;
    @FXML
    private Button modifyButton;
    @FXML
    private Pane closePane;
    @FXML
    private Pane minimizePane;
    @FXML
    private Pane infoPopup1;
    @FXML
    private Button okayButton;
    @FXML
    private Label infoLabel1;
    @FXML
    private Pane infoPopup2;
    @FXML
    private Button okayButton1;
    @FXML
    private TextArea notesArea;
    @FXML
    private Label notesDeviceNameLabel;
    @FXML
    private TextField deviceNameField;
    @FXML
    private TextField brandField;
    @FXML
    private TextField modelField;
    @FXML
    private ComboBox<String> osTypeSelector;
    @FXML
    private TextField cpuField;
    @FXML
    private ComboBox<String> ramSizeSelector;
    
    @FXML
    private TextField editDeviceNameField;
    
    @FXML
    private TextField editBrandField;
    @FXML
    private TextField editModelField;
    @FXML
    private ComboBox<String> editOsTypeSelector;
    @FXML
    private TextField editOsVersionField;
    @FXML
    private TableColumn<TestDevice, String> deviceNameColumn;
    @FXML
    private TableColumn<TestDevice, String> brandColumn;
    @FXML
    private TableColumn<TestDevice, String> modelColumn;
    @FXML
    private TableColumn<TestDevice, String> osTypeColumn;
    @FXML
    private TableView<TestDevice> deviceTable;

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Initalize Page">
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Main.controller.reScanDevicesJSON();
        setupElements();
        initDragPane();
        initPopup(exitPopup);
        initPopup(infoPopup);
        FXWindowUtils.addUserInfoToDragPane(dragPane);
        FXWindowUtils.addVersionInfoToDragPane(dragPane);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="HANDLERS">
    @FXML
    private void handleCreateButtonClicked(ActionEvent event) throws IOException {
        createDevice();
        refreshDevicesPage(event);
    }

    private void createDevice() {
        Main.controller.createNewDevice(deviceNameField.getText(), brandField.getText(), modelField.getText(), osTypeSelector.getValue());
    }

    @FXML
    private void handleBackButton(ActionEvent event) throws IOException {
        goToWelcomePage(event);
    }

    @FXML
    private void handleEditPaneBackButton(ActionEvent event) throws IOException {
        FXWindowUtils.hidePopup(editPane, basePane, closeIcon, minimizeIcon, logoPane);
    }

    @FXML
    private void handleEditPaneModifyButton(ActionEvent event) throws IOException {
        Main.controller.getDevices().getDevicesList().stream().forEach(System.out::println);
        td.setName(editDeviceNameField.getText());
        td.setBrand(editBrandField.getText());
        td.setModel(editModelField.getText());
        td.setOsType(OsType.getOsTypeByName(editOsTypeSelector.getValue()));
        td.setOsVersion(editOsVersionField.getText());
        td.setCpu(cpuField.getText());
        td.setRamSize(Integer.parseInt(ramSizeSelector.getValue()));
        JsonUtils.saveDevicesToJSON(Main.controller.getDevices().getDevicesList());
        FXWindowUtils.hidePopup(editPane, basePane, closeIcon, minimizeIcon, logoPane);
        refreshDevicesPage(event);
    }

    @FXML
    private void handleEditPaneDeleteButton(ActionEvent event) throws IOException {
        alertDeviceNameLabel.setText(td.getName());
        hidePane.setVisible(true);
        deletePane.setVisible(true);
    }

    @FXML
    private void handleDeleteConfirm(ActionEvent event) throws IOException {
        hidePane.setVisible(false);
        deletePane.setVisible(false);
        Main.controller.removeTestDeviceFromJSON(td);
        FXWindowUtils.hidePopup(editPane, basePane, closeIcon, minimizeIcon, logoPane);
        refreshDevicesPage(event);
    }

    @FXML
    private void cancelDelete(ActionEvent event) throws IOException {
        hidePane.setVisible(false);
        deletePane.setVisible(false);
    }

    @FXML
    private void handleNotesButtonPressed(ActionEvent event) throws IOException {
        notesDeviceNameLabel.setText(td.getName());
        notesArea.setText(td.getNotes());
        notespane.setVisible(true);
        hidePane.setVisible(true);

    }

    @FXML
    private void saveNotesButtonPressed(ActionEvent event) throws IOException {
        td.setNotes(notesArea.getText().replaceAll("\n", System.getProperty("line.separator")));
        JsonUtils.saveDevicesToJSON(Main.controller.getDevices().getDevicesList());
        notespane.setVisible(false);
        hidePane.setVisible(false);
    }

    @FXML
    private void cancelModifyNotes(ActionEvent event) throws IOException {
        notespane.setVisible(false);
        hidePane.setVisible(false);
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

    private void refreshDevicesPage(ActionEvent event) throws IOException {
        goToPage("DevicesPage.fxml", event);
    }

    private void goToWelcomePage(ActionEvent event) throws IOException {
        goToPage("WelcomePage.fxml", event);
    }

    private void openEditPane() {
        FXWindowUtils.showPopup(editPane, basePane, closeIcon, minimizeIcon, logoPane);
    }

    @FXML
    private void hideInfoPane() {
        FXWindowUtils.hidePopup(infoPopup, hideStagePane);
    }

    public static void handleEditRequest(String deviceName, TableCell tc) {
        Main.controller.getDevices().loadDevicesFromJSON();
        td = Main.controller.getDevices().getDeviceByName(tc.getText());
        System.out.println("\ntest device" + td.toString());
        AnchorPane editPane = (AnchorPane) tc.getScene().lookup("#editPane");
        
        TextField editDeviceNameField = (TextField) editPane.getScene().lookup("#editDeviceNameField");
        TextField editBrandFieldText = (TextField) editPane.getScene().lookup("#editBrandField");
        TextField editModelFieldText = (TextField) editPane.getScene().lookup("#editModelField");        
        ComboBox<String> editOsTypeSelector = (ComboBox<String>) editPane.getScene().lookup("#editOsTypeSelector");
        TextField editOsVersionField = (TextField) editPane.getScene().lookup("#editOsVersionField");        
        TextField cpuEditField = (TextField) editPane.getScene().lookup("#cpuField");
        ComboBox<String> editRamSizeSelector = (ComboBox<String>) editPane.getScene().lookup("#ramSizeSelector");

        
        
        editDeviceNameField.setText(td.getName());
        editBrandFieldText.setText(td.getBrand());
        editModelFieldText.setText(td.getModel());
        editOsVersionField.setText(td.getOsVersion());
        editOsTypeSelector.setValue(td.getOsType().getName());
        cpuEditField.setText(td.getCpu());
        editRamSizeSelector.setValue(String.valueOf(td.getRamSize()));

        Pane basePane = (Pane) tc.getScene().lookup("#basePane");
        ImageView closeIcon = (ImageView) tc.getScene().lookup("#closeIcon");
        Line minimizeIcon = (Line) tc.getScene().lookup("#minimizeIcon");
        Pane logoPane = (Pane) tc.getScene().lookup("#logoPane");
        FXWindowUtils.showPopup(editPane, basePane, closeIcon, minimizeIcon, logoPane);
    }

    public void handleCopyToClipboardRequest() {
        Main.controller.getDevices().copyDevicesListToClipboard();
        FXWindowUtils.showPopup(infoPopup, hideStagePane);

    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Setup Page Elements">
    public void setupElements() {
        FXWindowUtils.setPageTitle(logoPane, "Teszt", "Eszközök");
        createButton.setDisable(true);
        setupDeviceTable();
        setupDropDownElements();
        setupEditPageDropDownElements();
        startComboBoxListener(osTypeSelector);
        addFieldContentListener(brandField);
        addFieldContentListener(modelField);
        addFieldContentListener(deviceNameField);
        addEditFieldContentListener(editOsVersionField);
        addEditFieldContentListener(editDeviceNameField);
        hidePane.setVisible(false);
        editPane.setVisible(false);
        deletePane.setVisible(false);
        notespane.setVisible(false);

        //startEditComboBoxListener(editOsTypeSelector);
    }

    private void setupDeviceTable() {

        List<TestDevice> deviceList = Main.controller.getDevices().getDevicesList();
        deviceNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        deviceNameColumn.setCellFactory(CustomTestDeviceTableCell.forTableColumn());
        deviceNameColumn.setStyle("-fx-alignment: center-left;");
        brandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
        brandColumn.setStyle("-fx-alignment: center-left;");
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        modelColumn.setStyle("-fx-alignment: center-left;");
        osTypeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOsTypeName()));
        osTypeColumn.setStyle("-fx-alignment: center-right");
        deviceTable.getItems().addAll(FXCollections.observableArrayList(deviceList));

    }

    private void setupDropDownElements() {
        List<String> osTypeList = Arrays.asList(OsType.values()).stream().map(p -> p.getName()).collect(Collectors.toList());
        osTypeSelector.getItems().addAll(osTypeList);
    }

    private void setupEditPageDropDownElements() {
        List<String> osTypeList = Arrays.asList(OsType.values()).stream().map(p -> p.getName()).collect(Collectors.toList());
        editOsTypeSelector.getItems().addAll(osTypeList);
        ramSizeSelector
                .getItems()
                .addAll(IntStream.range(1, 129)
                        .boxed()
                        .collect(Collectors.toList())
                        .stream()
                        .map(p -> String.valueOf(p))
                        .collect(Collectors.toList()));
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

    public void addEditFieldContentListener(final TextField tf) {
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (!isThereEditPaneEmptyField()) {
                    modifyButton.setDisable(false);
                } else {
                    modifyButton.setDisable(true);
                }
            }
        });
    }

    public boolean isThereEmptyField() {
        return isOsTypeDropDownEmpty() || isBrandNameFieldEmpty() || isModelNameFieldEmpty() || isDeviceNamefiledEmpty();
    }

    public boolean isThereEditPaneEmptyField() {
        return isEditOsVersionFiledEmpty() || isEditDeviceNamefiledEmpty();
    }

    private boolean isOsTypeDropDownEmpty() {
        return null == osTypeSelector.getValue() || osTypeSelector.getValue().equalsIgnoreCase("");
    }

    private boolean isBrandNameFieldEmpty() {
        return brandField.getText().equalsIgnoreCase("");
    }

    private boolean isModelNameFieldEmpty() {
        return modelField.getText().equalsIgnoreCase("");
    }

    private boolean isDeviceNamefiledEmpty() {
        return deviceNameField.getText().equalsIgnoreCase("");
    }

    private boolean isEditOsVersionFiledEmpty() {
        return editOsVersionField.getText().equalsIgnoreCase("");
    }

    private boolean isEditDeviceNamefiledEmpty() {
        return editDeviceNameField.getText().equalsIgnoreCase("");
    }

    private void initPopup(Pane pane) {
        FXWindowUtils.setupPopupWindow(pane);
    }

    private void initDragPane() {
        FXWindowUtils.makeStageDraggable(dragPane);
    }

    private UserPageTableData createUserPageTableDatObjct(User u) {

        return new UserPageTableData(u.getUserKey(), u.getFullname(), u.getRole().getName());
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Misc Utils">

    private void fillInfoPopupLables(Pane infoPopup, TestDocumentCreationDTO settings) {
//        labelPageNumber.setText(settings.getNumberOfPagesRequired() + " db");
//        labelFileName.setText(Utils.removeHunagrianLettersAndSpaces(settings.getAppSectionUnderTest().toUpperCase()) + ".docm");
//        labelSaveLocation.setText(saveLocation.getText());
    }

    private void goToPage(String page, ActionEvent event) throws IOException {
        Parent nextRoot = FXMLLoader.load(getClass().getResource(page));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXWindowUtils.initNodeFadeOutFX(CONTENT_FADE_OUT_DURATION, contentPane, 1.0, 0.01);
        FXWindowUtils.delayAndFadeInNextRoot(stage, nextRoot, event, CONTENT_FADE_OUT_DURATION);
    }

//</editor-fold>
}
