
package model;

import java.io.Serializable;

/**
 *
 * @author takacs.gergely
 */
public enum DefectStatus implements Serializable {
    
    NEW ("Új"), 
    NOT_A_BUG ("Nem hiba"), 
    REJECTED ("Visszautasítva"), 
    TODO_FIXING ("Javítás alatt"), 
    RE_TESTING ("Újratesztelés alatt"), 
    FIXED ("Kijavítva");
    
    private final String name;

    private DefectStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
}
