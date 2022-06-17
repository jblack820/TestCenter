
package model;

/**
 *
 * @author takacs.gergely
 */
public enum UserRole {
    
    TESTER ("Tesztelő"), MANAGER ("Vezető");
    
    private String name;

    private UserRole(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    public static UserRole getUserRoleByName(String name){
        UserRole ur = null;
        for (UserRole value : values()) {
            if (value.getName().equalsIgnoreCase(name)){
                ur = value;
            }
        }
        return ur;
    }
    

    @Override
    public String toString() {
        return "UserRole{" + "name=" + name + '}';
    }
    
    
    
}
