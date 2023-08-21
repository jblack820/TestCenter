/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author takacs.gergely
 */
public class TestProjectTableData {
    
    private BooleanProperty isSelected;
    private SimpleStringProperty dateStarted;
    private SimpleStringProperty progressRate;
    private SimpleStringProperty testProjectName;

    public TestProjectTableData(SimpleStringProperty testProjectName, SimpleStringProperty dateStarted,SimpleStringProperty progressRate) {
        this.isSelected = new SimpleBooleanProperty();
        this.isSelected.set(false);
        this.testProjectName = testProjectName;
        this.dateStarted = dateStarted;
        this.progressRate = progressRate;
    }
    
    public TestProjectTableData(SimpleStringProperty testProjectName) {
        this.isSelected = new SimpleBooleanProperty();
        this.isSelected.set(false);
        this.testProjectName = testProjectName;
        this.dateStarted = new SimpleStringProperty("");
        this.progressRate = new SimpleStringProperty("");
    }

    public BooleanProperty getIsSelectedBooleanProperty() {
        return isSelected;
    }

    public void setIsSelectedBooleanProperty(Boolean isSelected) {
        this.isSelected.setValue(isSelected);
    }

    public SimpleStringProperty getDateStarted() {
        return dateStarted;
    }

    public void setDateStarted(SimpleStringProperty dateStarted) {
        this.dateStarted = dateStarted;
    }

    public SimpleStringProperty getProgressRate() {
        return progressRate;
    }

    public void setProgressRate(SimpleStringProperty progressRate) {
        this.progressRate = progressRate;
    }

    public SimpleStringProperty getTestProjectName() {
        return testProjectName;
    }

    public void setTestProjectName(SimpleStringProperty testProjectName) {
        this.testProjectName = testProjectName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TestProjectTableData{isSelected=").append(isSelected);
        sb.append(", dateStarted=").append(dateStarted);
        sb.append(", progressRate=").append(progressRate);
        sb.append(", testProjectName=").append(testProjectName);
        sb.append('}');
        return sb.toString();
    }

    
   

    
    
}

    