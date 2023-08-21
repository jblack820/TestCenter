package app;


import controller.TestCenterController;
import javafx.application.Application;
import javafx.stage.Stage;
import testcenterfx.LauncherPreloader;
import testcenterfx.PreloaderFXMLController;

/**
 *
 * @author takacs.gergely
 */
public class Main extends Application {
    
    public static TestCenterController controller = new TestCenterController();
    
    public static Stage primaryStage = null;
    @Override
    public void init() throws Exception {        
        PreloaderFXMLController init = new PreloaderFXMLController();
        init.checkFunctions();
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
    }

    public static void main(String[] args) {
        System.setProperty("javafx.preloader", LauncherPreloader.class.getCanonicalName());
        Application.launch(Main.class, args);
    }
}
