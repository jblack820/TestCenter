package model;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Greg
 */
public class TestStep implements Serializable {
    
    String description;
    boolean isFailed;

    public TestStep(String description, boolean isFailed) {
        this.description = description;
        this.isFailed = isFailed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isIsFailed() {
        return isFailed;
    }

    public void setIsFailed(boolean isFailed) {
        this.isFailed = isFailed;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.description);
        hash = 41 * hash + (this.isFailed ? 1 : 0);
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
        final TestStep other = (TestStep) obj;
        if (this.isFailed != other.isFailed) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nTestStep{description=").append(description);
        sb.append(", isFailed=").append(isFailed);
        sb.append('}');
        return sb.toString();
    }
    
    
    
}
