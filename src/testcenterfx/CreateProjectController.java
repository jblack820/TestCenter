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
import java.util.stream.Collectors;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
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
import util.FXWindowUtils;
//</editor-fold>

/**
 * FXML Controller class
 *
 * @author takacs.gergely
 */
public class CreateProjectController implements Initializable {

    private Stage stage;
    List<TextField> fieldsList;

    //<editor-fold defaultstate="collapsed" desc="FXML Properties">
    @FXML
    private Pane exitPopup;
    @FXML
    private Pane logoPane;
    @FXML
    private Pane basePane;
    @FXML
    private AnchorPane dragPane;
    @FXML
    private ImageView closeIcon;
    @FXML
    private Line minimizeIcon;
    @FXML
    private TextField projectNameField;
    @FXML
    private TextField projectNameShortField;
    @FXML
    private Button createButton;
    @FXML
    private DatePicker dateStartedField;
    @FXML
    private DatePicker dateDeadlineField;
    @FXML
    private TextArea appLocationArea;
    @FXML
    private TextField browserField;
    @FXML
    private TextField versionNumberField;
    @FXML
    private TextField projectTypeShortField;
    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private Label appLocationLabel;
    @FXML
    private Label browserLabel;

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="INIT">
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupElements();
        initDragPane();
        initPopup(exitPopup);
        FXWindowUtils.addUserInfoToDragPane(dragPane);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="HANDLERS">
    @FXML
    private void handleCreateButtonClicked(ActionEvent event) throws IOException {
        createNewProject();
        goToPage(event, "ListProjectsPage.fxml");
    }

    @FXML
    private void handleBackButton(ActionEvent event) throws IOException {
        goToPage(event, "ListProjectsPage.fxml");
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
    public void setupElements() {
        FXWindowUtils.setPageTitle(logoPane, "Új Projekt", "Létrehozása", 8);
        setupDropDownElements();
        startComboBoxListener(comboBox, browserField, projectNameField, browserLabel, projectTypeShortField, appLocationArea, appLocationLabel);
        fieldsList = setupFieldList();
        createButton.setDisable(true);
        startFieldContentListeners();
        startDatePickerListener();
        startAppLocationAreaListener(appLocationArea);
        startBrowserFiledListener(browserField);
        limitModuleShortNameField();
        makeFieldUpperCase(projectNameShortField);
        setupDatePicker();
        addProjectNameContentListener();
    }

    public List<TextField> setupFieldList() {
        List<TextField> list = new ArrayList<>();
        list.add(projectNameField);
        list.add(projectNameShortField);
        return list;
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

    public void startComboBoxListener(ComboBox<String> combo, TextField browserField, TextField projectNameField, Label browserLabel, TextField projectTypeShortField, TextArea appLocationArea, Label appLocationLabel) {
        combo.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (comboBox.getValue() != "") {
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
                    if (!isThereEmptyField()) {
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

    public void addProjectNameContentListener() {
        projectNameField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                comboBox.setValue("");
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
                if (!isThereEmptyField()) {
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
                if (!isThereEmptyField()) {
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
                if (!isThereEmptyField()) {
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
        System.out.println("Checking fields START");
        boolean isRequiredFieldsEmpty = fieldsList.stream().filter(p -> p.getText().equalsIgnoreCase("")).collect(Collectors.toList()).size() > 0;
        boolean isDatePickerEmpty = dateStartedField.getValue() == null;
        boolean isAppLocationEmptyAndRequired = checkifAppLocationEmptyAndRequired();
        boolean isBrowserfieldEmptyAndRequired = checkifBrowserfieldEmpty();
        boolean isComboBoxEmpty = null == comboBox.getValue() || comboBox.getValue().equals("");
        System.out.println("Checking fields END");
        return isRequiredFieldsEmpty || isDatePickerEmpty || isAppLocationEmptyAndRequired || isBrowserfieldEmptyAndRequired || isComboBoxEmpty;
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

    private List<String> loadCBox1Elements() {
        return Arrays.asList(ProjectType.values()).stream().map(p -> p.getProjectTypeName()).sorted().collect(Collectors.toList());
    }

    private void setupDropDownElements() {
        comboBox.getItems().addAll(loadCBox1Elements());
        System.out.println("Combo Box: items");
        comboBox.getItems().stream().sorted().forEach(System.out::println);
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="UTILS">
        private void createNewProject() {
        Main.controller.createNewProject(
                projectNameField.getText(),
                projectNameShortField.getText(),
                dateStartedField.getValue(),
                dateDeadlineField.getValue(),
                comboBox.getValue(),
                versionNumberField.getText(),
                appLocationArea.getText(),
                browserField.getText());
    }

    private void goToPage(ActionEvent event, String page) throws IOException {
        Parent nextRoot = FXMLLoader.load(getClass().getResource(page));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Node contentPane = stage.getScene().getRoot().lookup("#basePane").lookup("#contentPane");
        FXWindowUtils.initNodeFadeOutFX(CONTENT_FADE_OUT_DURATION, contentPane, 1.0, 0.01);
        FXWindowUtils.delayAndFadeInNextRoot(stage, nextRoot, event, CONTENT_FADE_OUT_DURATION);
    
//</editor-fold>

    }

}
