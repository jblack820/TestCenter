package dummy;


import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import model.User;
import model.UserRole;
import util.JsonUtils;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author takacs.gergely
 */
public class DummyScripts {
    
    private void addUsers() {
        Set<User> users = new LinkedHashSet<>();
        users.addAll(Arrays.asList(
                new User("Takács Gergely"),
                new User("C:\\Users\\kovacs.erika1", "Kovács Erika", UserRole.TESTER),
                new User("C:\\Users\\zombori.ferenc", "Zombori Ferenc", UserRole.TESTER),
                new User("C:\\Users\\szekszardi.zoltan", "Szekszárdi Zoltán", UserRole.MANAGER), 
                new User("C:\\Users\\zsiga.sandor", "Zsiga Sándor", UserRole.MANAGER)
        ));
        JsonUtils.saveSetofUsersToJSON(users);        
    }
    
    
    
    
}
