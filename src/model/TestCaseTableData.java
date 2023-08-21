/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.time.LocalDate;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author takacs.gergely
 */
public class TestCaseTableData {
    
    private BooleanProperty isSelected;
    private SimpleStringProperty testCaseId;
    private SimpleStringProperty testCaseName;
    private ObjectProperty<LocalDate> date;

    public TestCaseTableData(String testCaseId, String testCaseName, LocalDate dateProperty) {
        this.isSelected = new SimpleBooleanProperty();
        this.isSelected.set(false);
        this.testCaseId = new SimpleStringProperty(testCaseId);
        this.testCaseName = new SimpleStringProperty(testCaseName);
        this.date = new SimpleObjectProperty<LocalDate>();
        this.date.set(dateProperty);
    }

    public boolean getIsSelected() {
        return isSelected.get();
    }
    
    public BooleanProperty getisSelectedBooleanProperty (){
        return isSelected;
    }
    
    public void setgetisSelectedBooleanProperty (Boolean b){
        this.isSelected.setValue(b);
    }
    

    public String getTestCaseId() {
        return testCaseId.get();
    }

    public String getTestCaseName() {
        return testCaseName.get();
    }

    public String getDate() {
        return handleDateToString(date);
    }

    public void setIsSelected(BooleanProperty isSelected) {
        this.isSelected = isSelected;
    }

    public void setTestCaseId(SimpleStringProperty testCaseId) {
        this.testCaseId = testCaseId;
    }

    public void setTestCaseName(SimpleStringProperty testCaseName) {
        this.testCaseName = testCaseName;
    }

    public void setDate(ObjectProperty<LocalDate> date) {
        this.date = date;
    }

    private String handleDateToString(ObjectProperty<LocalDate> date) {
        if (null==date.get()) {
            return "üres";
        } else {
            return util.Utils.convertDateToHungarianTextFormat(date.get());
        }
    }

    @Override
    public String toString() {
        return "\nTestCaseTableData{" + "\nisSelected=" + isSelected + ", \ntestCaseId=" + testCaseId + ", \ntestCaseName=" + testCaseName + ", \ndate=" + date + '}';
    }

    
    
    
    
}
