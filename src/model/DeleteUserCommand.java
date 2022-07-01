package model;

import app.Main;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import testcenterfx.UsersPageController;
import util.FXWindowUtils;

/**
 *
 * @author takacs.gergely
 */
public class DeleteUserCommand implements Command {
    
    private final Stage stage;
    private final ActionEvent event;
    private final User user;

    public DeleteUserCommand(Stage stage, ActionEvent event, User user) {
        this.stage = stage;
        this.event = event;
        this.user = user;
    }

    public Stage getStage() {
        return stage;
    }

    public ActionEvent getEvent() {
        return event;
    }

    public User getUser() {
        return user;
    }
    
    

    @Override
    public void execute() {
        Boolean isUserRemoveSuccesful = Main.controller.getUsers().removeUser(user);
        
        if (isUserRemoveSuccesful){
            Main.controller.updateUsersJsonFile();
            try {
                UsersPageController.loadUserPage(event);
            } catch (IOException ex) {
                Logger.getLogger(UsersPageController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            FXWindowUtils.showSmallAlert(stage, "Hiba", "Nincs ilyen felhasználó");
        }
        
        if (isUserRemoveSuccesful){
            FXWindowUtils.showSmallAlert(stage, "Felhasználó törölve", "");
        }
    }
    
    
    
}
