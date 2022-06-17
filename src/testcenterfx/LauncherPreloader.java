package testcenterfx;

import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.FXWindowUtils;

/**
 *
 * @author takacs.gergely
 */
public class LauncherPreloader extends Preloader {

    private Stage preloaderStage;

    @Override
    public void start(Stage stage) throws Exception {
        this.preloaderStage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("PreloaderFXML.fxml"));
        FXWindowUtils.initStage(stage, new Scene(root));
        stage.show();
        FXWindowUtils.initFadeInFX(700, root);
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification info) {
        if (info.getType() == StateChangeNotification.Type.BEFORE_START) {
            preloaderStage.hide();
        }
    }

}
