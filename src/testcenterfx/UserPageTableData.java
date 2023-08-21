/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testcenterfx;

import java.util.Objects;

/**
 *
 * @author takacs.gergely
 */
public class UserPageTableData {

    private String userKey;
    private String fullname;
    private String role;

    public UserPageTableData(String userKey, String fullname, String role) {
        this.userKey = userKey;
        this.fullname = fullname;
        this.role = role;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.userKey);
        hash = 29 * hash + Objects.hashCode(this.fullname);
        hash = 29 * hash + Objects.hashCode(this.role);
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
        final UserPageTableData other = (UserPageTableData) obj;
        if (!Objects.equals(this.userKey, other.userKey)) {
            return false;
        }
        if (!Objects.equals(this.fullname, other.fullname)) {
            return false;
        }
        if (!Objects.equals(this.role, other.role)) {
            return false;
        }
        return true;
    }
    
    
    

}
