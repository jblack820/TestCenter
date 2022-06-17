
package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author takacs.gergely
 */
public class TestCenterSnapshot implements Serializable {
    
    private final LocalDateTime TIME_OF_SNAPSHOT;
    TestCenter centerObject;

    public TestCenterSnapshot(TestCenter centerObject) {
        this.centerObject = centerObject;
        TIME_OF_SNAPSHOT = LocalDateTime.now();
    }

    public LocalDateTime getTIME_OF_SNAPSHOT() {
        return TIME_OF_SNAPSHOT;
    }

    public TestCenter getCenterObject() {
        return centerObject;
    }

    public void setCenterObject(TestCenter centerObject) {
        this.centerObject = centerObject;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.TIME_OF_SNAPSHOT);
        hash = 79 * hash + Objects.hashCode(this.centerObject);
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
        final TestCenterSnapshot other = (TestCenterSnapshot) obj;
        if (!Objects.equals(this.TIME_OF_SNAPSHOT, other.TIME_OF_SNAPSHOT)) {
            return false;
        }
        if (!Objects.equals(this.centerObject, other.centerObject)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TestCenterSnapshot{TIME_OF_SNAPSHOT=").append(TIME_OF_SNAPSHOT);
        sb.append(", centerObject=").append(centerObject);
        sb.append('}');
        return sb.toString();
    }
    
    
    
    
    
    
    
}
