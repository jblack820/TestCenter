
package model;

import java.util.Objects;

/**
 *
 * @author takacs.gergely
 */
public class ArchivedProjectName {
    
    private String folderName;
    private String projectName;


    public ArchivedProjectName(String folderName, String projectName) {
        this.folderName = folderName;
        this.projectName = projectName;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.folderName);
        hash = 29 * hash + Objects.hashCode(this.projectName);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ArchivedProjectName other = (ArchivedProjectName) obj;
        if (!Objects.equals(this.folderName, other.folderName)) {
            return false;
        }
        if (!Objects.equals(this.projectName, other.projectName)) {
            return false;
        }
        return true;
    }
    
    
    
}
