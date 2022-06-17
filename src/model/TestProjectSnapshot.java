
package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author takacs.gergely
 */
public class TestProjectSnapshot implements Serializable {
       
    private LocalDateTime dateTimeOfSnapshot;
    private List<TestDocument> testModules;    
    private ProductVersionNumber currentProjectVersionNumber;

    public TestProjectSnapshot(List<TestDocument> testModules, ProductVersionNumber currentProjectVersionNumber) {
        this.dateTimeOfSnapshot = LocalDateTime.now();
        this.testModules = testModules;
        this.currentProjectVersionNumber = currentProjectVersionNumber;
    }

    public LocalDateTime getDateTimeOfSnapshot() {
        return dateTimeOfSnapshot;
    }

    public List<TestDocument> getTestModules() {
        return testModules;
    }

    public ProductVersionNumber getCurrentProjectVersionNumber() {
        return currentProjectVersionNumber;
    }

    public void setDateTimeOfSnapshot(LocalDateTime dateTimeOfSnapshot) {
        this.dateTimeOfSnapshot = dateTimeOfSnapshot;
    }

    public void setTestModules(List<TestDocument> testModules) {
        this.testModules = testModules;
    }

    public void setCurrentProjectVersionNumber(ProductVersionNumber currentProjectVersionNumber) {
        this.currentProjectVersionNumber = currentProjectVersionNumber;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.dateTimeOfSnapshot);
        hash = 83 * hash + Objects.hashCode(this.testModules);
        hash = 83 * hash + Objects.hashCode(this.currentProjectVersionNumber);
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
        final TestProjectSnapshot other = (TestProjectSnapshot) obj;
        if (!Objects.equals(this.dateTimeOfSnapshot, other.dateTimeOfSnapshot)) {
            return false;
        }
        if (!Objects.equals(this.testModules, other.testModules)) {
            return false;
        }
        if (!Objects.equals(this.currentProjectVersionNumber, other.currentProjectVersionNumber)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        sb.append("TestProjectSnapshot{dateTimeOfSnapshot=").append(dateTimeOfSnapshot.format(formatter));
        sb.append(", testModules=").append(testModules);
        sb.append(", currentProjectVersionNumber=").append(currentProjectVersionNumber);
        sb.append('}');
        return sb.toString();
    }

    
        
    
    
}
