
package model;

import java.io.File;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author takacs.gergely
 */
public class User implements Serializable {
    
    private String userKey;
    private String fullname;
    private UserRole role;

    public User(String fullname) {
        this.userKey = getUserHomePath();
        this.fullname = fullname;
        this.role = UserRole.TESTER;
    }

    public User(String userKey, String fullname, UserRole role) {
        this.userKey = userKey;
        this.fullname = fullname;
        this.role = role;
    }
        
    
    private String getHomeDirName(){
        String path = System.getProperty("user.home");
        String splitter = File.separator.replace("\\","\\\\");
        String [] array = path.split(splitter);
        return array[array.length-1];
    }
    
    private String getUserHomePath (){
        return System.getProperty("user.home");
    }

    public String getUserKey() {
        return userKey;
    }

    public String getFullname() {
        return fullname;
    }

    public UserRole getRole() {
        return role;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.userKey);
        hash = 79 * hash + Objects.hashCode(this.fullname);
        hash = 79 * hash + Objects.hashCode(this.role);
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
        final User other = (User) obj;
        if (!Objects.equals(this.userKey, other.userKey)) {
            return false;
        }
        if (!Objects.equals(this.fullname, other.fullname)) {
            return false;
        }
        if (this.role != other.role) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("User{userKey=").append(userKey);
        sb.append(", fullname=").append(fullname);
        sb.append(", role=").append(role);
        sb.append('}');
        return sb.toString();
    }
    
    
    
}
