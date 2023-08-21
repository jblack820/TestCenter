/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testcenterfx;


import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author takacs.gergely
 */
public class TestCenterCreatorController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private AnchorPane anchorBase;
    
    @FXML
    private Label installPath;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }



    private void handleFileChooserClicked (){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog((Stage) anchorBase.getScene().getWindow());
        installPath.setText(selectedDirectory.getAbsolutePath());
        
    }
    
}
