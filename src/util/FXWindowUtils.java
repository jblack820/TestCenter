package util;

//<editor-fold defaultstate="collapsed" desc="IMPORTS">
import app.Main;
import static config.AppConfig.CONTENT_FADE_IN_DURATION;
import static config.AppConfig.CONTENT_FADE_OUT_DURATION;
import controller.TestCenterController;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
//</editor-fold>

public class FXWindowUtils {

    private static double xOffset;
    private static double yOffset;
    private static Stage currentStage;

    //<editor-fold defaultstate="collapsed" desc="Handle stage">
    public static void initStage(Stage stage, Scene scene) {
        initConfig(stage);
        setupStage(stage, scene);
    }

    private static void initConfig(Stage stage) {
        stage.initStyle(StageStyle.UNDECORATED);
        //stage.setMaximized(true);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
    }

    public static void setupStage(Stage stage, Scene scene) {
        stage.getIcons().add(new Image(Main.class.getResourceAsStream("/images/TEST_LAB_ICON.jpg")));
        stage.setScene(scene);
        disableCloseRequest(stage);
        stage.getScene().getRoot().setEffect(new DropShadow(BlurType.GAUSSIAN, Color.BLACK, 30, 0.01, 0.0, 0.0));
        scene.setFill(Color.TRANSPARENT);
    }

    private static void disableCloseRequest(Stage stage) {
        Platform.setImplicitExit(false);
        stage.setOnCloseRequest((WindowEvent event) -> {
            event.consume();
        });
    }

    public static void makeStageDraggable(Pane dragPane) {

        dragPane.setOnMousePressed((event) -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        dragPane.setOnMouseDragged((event) -> {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
            stage.setOpacity(0.95f);
        });

        dragPane.setOnDragDone((event) -> {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setOpacity(1.0f);
        });
        dragPane.setOnMouseReleased((event) -> {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setOpacity(1.0f);
        });

    }

    public static FadeTransition getFadeInFx(double durationMillis, Parent root) {
        FadeTransition fade = new FadeTransition();
        fade.setDuration(Duration.millis(durationMillis));
        fade.setFromValue(0.01);
        fade.setToValue(1.0);
        fade.setNode(root);
        return fade;
    }

    public static FadeTransition getNodeFadeInFx(double durationMillis, Node node, double fromValue, double toValue) {
        FadeTransition fade = new FadeTransition();
        fade.setDuration(Duration.millis(durationMillis));
        fade.setFromValue(fromValue);
        fade.setToValue(toValue);
        fade.setNode(node);
        return fade;
    }

    public static FadeTransition getNodeFadeOutFx(double durationMillis, Node node, double fromValue, double toValue) {
        FadeTransition fade = new FadeTransition();
        fade.setDuration(Duration.millis(durationMillis));
        fade.setFromValue(fromValue);
        fade.setToValue(toValue);
        fade.setNode(node);
        return fade;
    }

    public static FadeTransition getFadeOutFx(double durationMillis, Parent root) {
        FadeTransition fade = new FadeTransition();
        fade.setDuration(Duration.millis(durationMillis));
        fade.setFromValue(0.9);
        fade.setToValue(0.02);
        fade.setNode(root);
        return fade;
    }

    public static void initNodeFadeInFX(double durationMillis, Node node, double fromValue, double toValue) {
        FadeTransition fade = getNodeFadeInFx(durationMillis, node, fromValue, toValue);
        fade.play();
    }

    public static void initNodeFadeOutFX(double durationMillis, Node node, double fromValue, double toValue) {
        FadeTransition fade = getNodeFadeInFx(durationMillis, node, fromValue, toValue);
        fade.play();
    }

    public static void initFadeInFX(double durationMillis, Parent root) {
        FadeTransition fade = getFadeInFx(durationMillis, root);
        fade.play();
    }

    public static void delayAndFadeInNextRoot(Stage stage, Parent nextRoot, ActionEvent event, double delayTime) {
        currentStage = stage;
        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Thread.sleep((long) delayTime);
                } catch (InterruptedException e) {
                }
                return null;
            }
        };
        sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                currentStage.getScene().setRoot(nextRoot);
                nextRoot.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.BLACK, 30, 0.01, 0.0, 0.0));
                Node newContentPane = currentStage.getScene().getRoot().lookup("#basePane").lookup("#contentPane");
                currentStage.show();
                initNodeFadeInFX(CONTENT_FADE_IN_DURATION, newContentPane, 0.01, 1.0);
            }
        });
        new Thread(sleeper).start();
    }

    public static void delayAndFadeInNextRoot(Stage stage, Parent nextRoot, double delayTime) {
        currentStage = stage;
        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Thread.sleep((long) delayTime);
                } catch (InterruptedException e) {
                }
                return null;
            }
        };
        sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                currentStage.getScene().setRoot(nextRoot);
                nextRoot.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.BLACK, 30, 0.01, 0.0, 0.0));
                Node newContentPane = currentStage.getScene().getRoot().lookup("#basePane").lookup("#contentPane");
                currentStage.show();
                initNodeFadeInFX(CONTENT_FADE_IN_DURATION, newContentPane, 0.01, 1.0);
            }
        });
        new Thread(sleeper).start();
    }

    public static void initTransitionToNextPage(ActionEvent event, Stage stage, Parent nextRoot) {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Node contentPane = stage.getScene().getRoot().lookup("#basePane").lookup("#contentPane");
        FXWindowUtils.initNodeFadeOutFX(CONTENT_FADE_OUT_DURATION, contentPane, 1.0, 0.01);
        FXWindowUtils.delayAndFadeInNextRoot(stage, nextRoot, event, CONTENT_FADE_OUT_DURATION);
    }

    public static void initTransitionToNextPage(Stage stage, Parent nextRoot) {

        Node contentPane = stage.getScene().getRoot().lookup("#basePane").lookup("#contentPane");
        FXWindowUtils.initNodeFadeOutFX(CONTENT_FADE_OUT_DURATION, contentPane, 1.0, 0.01);
        FXWindowUtils.delayAndFadeInNextRoot(stage, nextRoot, CONTENT_FADE_OUT_DURATION);
    }

    public static void addUserInfoToDragPane(AnchorPane dragPane) {
        Label userLabel = new Label("Belépve: " + TestCenterController.userLoggedIn.getFullname());
        dragPane.getChildren().add(userLabel);
        userLabel.setLayoutX(200);
        userLabel.setLayoutY(6);
        userLabel.setPrefWidth(350);
        userLabel.setStyle("-fx-font-family: \"Perpetua\"; -fx-text-fill: #A6A6A6; -fx-font-size: 16px; -fx-backgroud-color: transparent;");
    }

    public static void addVersionInfoToDragPane(AnchorPane dragPane) {
        Label versionLabel = new Label("v." + Main.class.getPackage().getImplementationVersion());
        dragPane.getChildren().add(versionLabel);
        versionLabel.setLayoutX(110);
        versionLabel.setLayoutY(6);
        versionLabel.setPrefWidth(50);
        versionLabel.setOpacity(1.0);
        versionLabel.setStyle("-fx-font-family: \"Perpetua\"; -fx-text-fill: #A6A6A6; -fx-font-size: 16px; -fx-backgroud-color: transparent;");
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Handle popups">
    public static void showPopup(Pane popup, Pane basePane, ImageView closeIcon, Line minimizeIcon, Pane logoPane) {
        initShowPopup(popup);
        disableStage(basePane, closeIcon, minimizeIcon, logoPane);
    }

    public static void showPopup(Pane popup, Pane basePane, ImageView closeIcon, Line minimizeIcon, Pane logoPane, ImageView bigLogo) {        
        initShowPopup(popup);
        disableStage(basePane, closeIcon, minimizeIcon, logoPane, bigLogo);
    }
    
    public static void showPopup(Pane popup, Pane hideStagePane) {        
        initShowPopup(popup);
        hideStagePane.setVisible(true);
    }
    

    public static void hidePopup(Pane mypopup, Pane basePane, ImageView closeIcon, Line minimizeIcon, Pane logoPane) {
        initHidePopup(mypopup);
        enableStage(basePane, closeIcon, minimizeIcon, logoPane);
    }

    public static void hidePopup(Pane mypopup, Pane basePane, ImageView closeIcon, Line minimizeIcon, Pane logoPane, ImageView bigLogo) {
        initHidePopup(mypopup);
        enableStage(basePane, closeIcon, minimizeIcon, logoPane, bigLogo);
    }
    
    public static void hidePopup(Pane mypopup, Pane hideStagePane) {
        initHidePopup(mypopup);
        hideStagePane.setVisible(false);
    }

    private static void initShowPopup(Pane popup) {
        popup.setVisible(true);
        popup.setDisable(false);
        popup.setOpacity(1);
    }

    private static void disableStage(Pane basePane, ImageView closeIcon, Line minimizeIcon, Pane logoPane) {
        basePane.setDisable(true);
        closeIcon.setOpacity(0.3);
        minimizeIcon.setOpacity(0.6);
        logoPane.setOpacity(0.6);
    }

    private static void disableStage(Pane basePane, ImageView closeIcon, Line minimizeIcon, Pane logoPane, ImageView bigLogo) {
        basePane.setDisable(true);
        closeIcon.setOpacity(0.3);
        minimizeIcon.setOpacity(0.6);
        logoPane.setOpacity(0.6);
        bigLogo.setOpacity(0.5);
    }

    private static void initHidePopup(Pane popup) {
        popup.setVisible(false);
        popup.setDisable(true);
        popup.setOpacity(0);
    }

    private static void enableStage(Pane basePane, ImageView closeIcon, Line minimizeIcon, Pane logoPane) {
        basePane.setDisable(false);
        closeIcon.setOpacity(0.5);
        minimizeIcon.setOpacity(0.86);
        logoPane.setOpacity(1);
    }

    private static void enableStage(Pane basePane, ImageView closeIcon, Line minimizeIcon, Pane logoPane, ImageView bigLogo) {
        basePane.setDisable(false);
        closeIcon.setOpacity(0.5);
        minimizeIcon.setOpacity(0.86);
        logoPane.setOpacity(1);
        bigLogo.setOpacity(1);
    }

    public static void setupPopupWindow(Pane mypopup) {
        mypopup.setDisable(true);
        mypopup.setVisible(false);
        mypopup.setOpacity(0);
        mypopup.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.BLACK, 30, 0.05, 0.0, 0.0));
    }

 

    public static void setPageTitle(Pane logoPane, String text1, String text2) {
        Font font1 = Font.loadFont(Main.class.getResourceAsStream("/font/NunitoSans-Regular.ttf"), 25);
        Font font2 = Font.loadFont(Main.class.getResourceAsStream("/font/NunitoSans-Black.ttf"), 50);
        Label firstLabel = new Label(text1.toUpperCase());        
        Label secondLabel = new Label(text2.toUpperCase());
        firstLabel.setFont(font1);
        secondLabel.setFont(font2);
        setPageTitleColorAndPosition(logoPane, firstLabel);
        setPageTitleColorAndPosition(logoPane, secondLabel);    
        firstLabel.setLayoutY(15);
        secondLabel.setLayoutY(30);
        logoPane.getChildren().add(firstLabel);        
        logoPane.getChildren().add(secondLabel);
    }
    
        public static void setPageTitle(Pane logoPane, String text1, String text2, double extraGap) {
        Font font1 = Font.loadFont(Main.class.getResourceAsStream("/font/NunitoSans-Regular.ttf"), 25);
        Font font2 = Font.loadFont(Main.class.getResourceAsStream("/font/NunitoSans-Black.ttf"), 50);
        Label firstLabel = new Label(text1.toUpperCase());        
        Label secondLabel = new Label(text2.toUpperCase());
        firstLabel.setFont(font1);
        secondLabel.setFont(font2);
        setPageTitleColorAndPosition(logoPane, firstLabel);
        setPageTitleColorAndPosition(logoPane, secondLabel);    
        firstLabel.setLayoutY(15);
        secondLabel.setLayoutY(30+extraGap);
        logoPane.getChildren().add(firstLabel);        
        logoPane.getChildren().add(secondLabel);
    }
    
    
    private static void setPageTitleColorAndPosition(Pane logoPane, Label label){
        label.setStyle("-fx-text-fill: #D2D2D2;");
        label.setLayoutX(700    );
    }
    
    
    

}
//</editor-fold>

