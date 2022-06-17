
package model;

import java.io.Serializable;


public enum DefectPriority implements Serializable {
    LOW ("Alacsony"), MEDIUM ("Közepes"), HIGH ("Magas");
    
    private final String desc;

    private DefectPriority(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
    
    
}
