package model;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author takacs.gergely
 */
public class User implements Serializable {

    private String userKey;
    private String fullname;
    private List<UserRole> roleList;

    public User(String userKey, String fullname, List<UserRole> roleList) {
        this.userKey = userKey;
        this.fullname = fullname;
        this.roleList = roleList;
    }

    public String getAllRolesInOneString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < this.roleList.size(); i++) {
            sb.append(roleList.get(i).getName());
            if (i != this.roleList.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    private String getHomeDirName() {
        String path = System.getProperty("user.home");
        String splitter = File.separator.replace("\\", "\\\\");
        String[] array = path.split(splitter);
        return array[array.length - 1];
    }

    private String getUserHomePath() {
        return System.getProperty("user.home");
    }
    
    public boolean hasRole (UserRole role){
        return this.roleList.contains(role);
    }

    public String getUserKey() {
        return userKey;
    }

    public String getFullname() {
        return fullname;
    }

    public List<UserRole> getRoleList() {
        return roleList;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setRoleList(List<UserRole> roleList) {
        this.roleList = roleList;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.userKey);
        hash = 79 * hash + Objects.hashCode(this.fullname);
        hash = 79 * hash + Objects.hashCode(this.roleList);
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
        if (!Objects.equals(this.roleList, other.roleList)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("User{userKey=").append(userKey);
        sb.append(", fullname=").append(fullname);
        sb.append(", roleList=").append(roleList);
        sb.append('}');
        return sb.toString();
    }

}
