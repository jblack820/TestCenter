/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author takacs.gergely
 */
public enum OsType {
    
    WINDOWS("Windows"), MAC_OS ("macOS"), IOS ("iOS"), ANDROID ("Android"), LINUX ("Linux");
    
    private String name;

    private OsType(String name) {
        this.name = name;
    }

    public static OsType getWINDOWS() {
        return WINDOWS;
    }

    public static OsType getMAC_OS() {
        return MAC_OS;
    }

    public static OsType getIOS() {
        return IOS;
    }

    public static OsType getANDROID() {
        return ANDROID;
    }

    public static OsType getLINUX() {
        return LINUX;
    }

    public String getName() {
        return name;
    }

    public SimpleStringProperty getSSProperty (){
        return new SimpleStringProperty(getName());
    }
    
    public static OsType getOsTypeByName (String name){
        OsType osType = null;
        for (OsType value : values()) {
            if (value.getName().equalsIgnoreCase(name)){
                osType = value;
            }
        }
        return osType;
    }
    
}
