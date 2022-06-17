/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testcenterfx;

import app.Main;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import util.FXWindowUtils;

/**
 * FXML Controller class
 *
 * @author takacs.gergely
 */
public class PreloaderFXMLController implements Initializable {

    @FXML
    private Label lbllLoading;
    
    @FXML
    private Label versionLabel;
    public static Label labelLoading;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        labelLoading = lbllLoading;
        versionLabel.setText(Main.class.getPackage().getImplementationVersion());
    }

    public String checkFunctions() {
       
        
        final String[] message = {""};

        try {
            Thread t1 = new Thread(() -> {
                message[0] = "Initalizing...";
                Platform.runLater(() -> {
                    labelLoading.setText(message[0]);
                });
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(PreloaderFXMLController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            Thread t2 = new Thread(() -> {
                message[0] = "Loading resources...";
                Platform.runLater(() -> {
                    labelLoading.setText(message[0]);
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(PreloaderFXMLController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            Thread t3 = new Thread(() -> {
                message[0] = "Opening application...";
                Platform.runLater(() -> {
                    labelLoading.setText(message[0]);
                });

                try {
                    Thread.sleep(1000);
                    Main.controller.initApplication();

                } catch (InterruptedException ex) {
                    Logger.getLogger(PreloaderFXMLController.class.getName()).log(Level.SEVERE, null, ex);
                }

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            labelLoading.setText(message[0]);
                            Stage stage = new Stage();
                            Parent root = FXMLLoader.load(getClass().getResource("WelcomePage.fxml"));
                            FXWindowUtils.initStage(stage, new Scene(root));
                            stage.show();
                            FXWindowUtils.initFadeInFX(700, root);
                            
                        } catch (IOException ex) {
                            Logger.getLogger(PreloaderFXMLController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });

            });

            t1.start();
            t1.join();
            t2.start();
            t2.join();
            t3.start();
            t3.join();

        } catch (InterruptedException ex) {
            Logger.getLogger(PreloaderFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return message[0];
    }

}
