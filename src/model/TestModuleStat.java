
package model;

import java.util.Objects;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author takacs.gergely
 */
public class TestModuleStat {
    private SimpleIntegerProperty testModuleId;
    private SimpleStringProperty testmoduleName;
    private SimpleIntegerProperty numOfTotalTestCases;
    private SimpleIntegerProperty numOfCompletedTestCases;
    private SimpleIntegerProperty numOfFailedTestCases;

    public TestModuleStat(
            int testModuleId, 
            SimpleStringProperty testmoduleName, 
            int numOfTestCases, 
            int numOfCompletedTestCases, 
            int numOfFailedTestCases) {
        this.testModuleId = new SimpleIntegerProperty(testModuleId);
        this.testmoduleName = testmoduleName;
        this.numOfTotalTestCases = new SimpleIntegerProperty(numOfTestCases);
        this.numOfCompletedTestCases = new SimpleIntegerProperty(numOfCompletedTestCases);
        this.numOfFailedTestCases = new SimpleIntegerProperty(numOfFailedTestCases);
    }

    public SimpleIntegerProperty getTestModuleIdProperty() {
        return testModuleId;
    }

    public Integer getTestModuleId() {
        return testModuleId.get();
    }

    public String getTestmoduleName() {
        return testmoduleName.get();
    }

    public Integer getNumOfTotalTestCases() {
        return numOfTotalTestCases.get();
    }

    public Integer getNumOfCompletedTestCases() {
        return numOfCompletedTestCases.get();
    }

    public Integer getNumOfFailedTestCases() {
        return numOfFailedTestCases.get();
    }
    
    

    public void setTestModuleIdProperty(SimpleIntegerProperty testModuleIdProperty) {
        this.testModuleId = testModuleIdProperty;
    }

    public SimpleStringProperty getTestmoduleNameProperty() {
        return testmoduleName;
    }

    public void setTestmoduleNameProperty(SimpleStringProperty testmoduleNameProperty) {
        this.testmoduleName = testmoduleNameProperty;
    }

    public SimpleIntegerProperty getNumOfTotalTestCasesProperty() {
        return numOfTotalTestCases;
    }

    public void setNumOfTotalTestCasesProperty(SimpleIntegerProperty numOfTotalTestCasesProperty) {
        this.numOfTotalTestCases = numOfTotalTestCasesProperty;
    }

    public SimpleIntegerProperty getNumOfCompletedTestCasesProperty() {
        return numOfCompletedTestCases;
    }

    public void setNumOfCompletedTestCasesProperty(SimpleIntegerProperty numOfCompletedTestCasesProperty) {
        this.numOfCompletedTestCases = numOfCompletedTestCasesProperty;
    }

    public SimpleIntegerProperty getNumOfFailedTestCasesProperty() {
        return numOfFailedTestCases;
    }

    public void setNumOfFailedTestCasesProperty(SimpleIntegerProperty numOfFailedTestCasesProperty) {
        this.numOfFailedTestCases = numOfFailedTestCasesProperty;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.testModuleId);
        hash = 23 * hash + Objects.hashCode(this.testmoduleName);
        hash = 23 * hash + Objects.hashCode(this.numOfTotalTestCases);
        hash = 23 * hash + Objects.hashCode(this.numOfCompletedTestCases);
        hash = 23 * hash + Objects.hashCode(this.numOfFailedTestCases);
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
        final TestModuleStat other = (TestModuleStat) obj;
        if (!Objects.equals(this.testModuleId, other.testModuleId)) {
            return false;
        }
        if (!Objects.equals(this.testmoduleName, other.testmoduleName)) {
            return false;
        }
        if (!Objects.equals(this.numOfTotalTestCases, other.numOfTotalTestCases)) {
            return false;
        }
        if (!Objects.equals(this.numOfCompletedTestCases, other.numOfCompletedTestCases)) {
            return false;
        }
        if (!Objects.equals(this.numOfFailedTestCases, other.numOfFailedTestCases)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TestModuleStat{testModuleIdProperty=").append(testModuleId);
        sb.append(", testmoduleNameProperty=").append(testmoduleName);
        sb.append(", numOfTotalTestCasesProperty=").append(numOfTotalTestCases);
        sb.append(", numOfCompletedTestCasesProperty=").append(numOfCompletedTestCases);
        sb.append(", numOfFailedTestCasesProperty=").append(numOfFailedTestCases);
        sb.append('}');
        return sb.toString();
    }

    
    
    
    
}
