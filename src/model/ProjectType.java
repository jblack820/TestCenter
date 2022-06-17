
package model;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 *
 * @author takacs.gergely
 */
public enum ProjectType {
    WEB_APPLICATION ("Webalkalmazás", "WEB", "WEB"), 
    WINDOWS_DESKTOP_APPLICATION ("Windows desktop alkalmazás", "WIN DESKTOP", "WIN"),
    MACINTOSH_DESKTOP_APPLICATION ("Macintosh desktop alkalmazás", "MAC DESKTOP", "MAC"),
    LINUX_DESKTOP_APPLICATION ("Linux desktop alkalmazás", "LINUX DESKTOP", "LIN"),
    ANDROID_APPLICATION ("Android mobilalkalmazás", "ANDROID", "AND"), 
    IOS_APPLICATION ("iOS mobilalkalmazás", "IOS", "IOS");
    
    private final String projectTypeName;
    private final String shortName;
    private final String codename;

    private ProjectType(String projectTypeName, String shortName, String codename) {
        this.projectTypeName = projectTypeName;
        this.shortName = shortName;
        this.codename = codename;
    }

    

    

    public String getProjectTypeName() {
        return this.projectTypeName;
    }

    public String getProjectTypeShortName() {
        return shortName;
    }

    public String getProjectTypeCodename() {
        return codename;
    }
    
    
    
    public static ProjectType getProjectTypeBasedOnName (String name){
        return Arrays.asList(
                ProjectType.values())
                .stream()                
                .filter((ProjectType p) -> p.getProjectTypeName().equalsIgnoreCase(name))
                .collect(Collectors.toList()).get(0);
    
    }
    
    
    
}
