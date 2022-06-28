
package testcenterfx;

import java.util.Objects;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleStringProperty;


public class UserPageTableData {

    private SimpleStringProperty userKey;
    private SimpleStringProperty fullname;
    private BooleanProperty isTester;
    private BooleanProperty isManager;
    private BooleanProperty isAdmin;

    public UserPageTableData(SimpleStringProperty userKey, SimpleStringProperty fullname, BooleanProperty isTester, BooleanProperty isManager, BooleanProperty isAdmin) {
        this.userKey = userKey;
        this.fullname = fullname;
        this.isTester = isTester;
        this.isManager = isManager;
        this.isAdmin = isAdmin;
    }

    public SimpleStringProperty getUserKey() {
        return userKey;
    }

    public void setUserKey(SimpleStringProperty userKey) {
        this.userKey = userKey;
    }

    public SimpleStringProperty getFullname() {
        return fullname;
    }

    public void setFullname(SimpleStringProperty fullname) {
        this.fullname = fullname;
    }

    public BooleanProperty getIsTester() {
        return isTester;
    }

    public void setIsTester(BooleanProperty isTester) {
        this.isTester = isTester;
    }

    public BooleanProperty getIsManager() {
        return isManager;
    }

    public void setIsManager(BooleanProperty isManager) {
        this.isManager = isManager;
    }

    public BooleanProperty getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(BooleanProperty isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.userKey);
        hash = 59 * hash + Objects.hashCode(this.fullname);
        hash = 59 * hash + Objects.hashCode(this.isTester);
        hash = 59 * hash + Objects.hashCode(this.isManager);
        hash = 59 * hash + Objects.hashCode(this.isAdmin);
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
        if (!Objects.equals(this.isTester, other.isTester)) {
            return false;
        }
        if (!Objects.equals(this.isManager, other.isManager)) {
            return false;
        }
        if (!Objects.equals(this.isAdmin, other.isAdmin)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "UserPageTableData{" + "userKey=" + userKey + ", fullname=" + fullname + ", isTester=" + isTester + ", isManager=" + isManager + ", isAdmin=" + isAdmin + '}';
    }
 
    
    
    
}

    