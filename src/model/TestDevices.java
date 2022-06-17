/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import config.AppConfig;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import util.JsonUtils;

/**
 *
 * @author takacs.gergely
 */
public class TestDevices {
    
    private final List<TestDevice> devicesList;

    public TestDevices() {
        this.devicesList = new ArrayList<>();
    }

    public List<TestDevice> getDevicesList() {
        return devicesList;
    }
    
    public void saveDevicesToJSON (List<TestDevice> devices){
        JsonUtils.saveDevicesToJSON(devices);
    }
    
    public void loadDevicesFromJSON (){
        if (isDevicesJSONExist()) {
                  devicesList.clear();
                  devicesList.addAll(JsonUtils.getTestDevicesFromJson());
        } else {
            System.out.println("No devices JSON found!");
        }
    }
    
    public TestDevice getDeviceByName (String deviceName){
        return devicesList.stream().filter(p->p.getName().equalsIgnoreCase(deviceName)).findFirst().get();
    }

    public void addDevice(TestDevice device) {
        this.devicesList.add(device);
    }
    
    private boolean isDevicesJSONExist() {
        return new File(app.Main.controller
                .getTestCenter()
                .getFolderStructure()
                .getTestCenterLocation()
                + AppConfig.TEST_DEVICES_JSON_FILENAME)
                .exists();
    }

    public void removeDevice(TestDevice td) {
        devicesList.remove(td);
    }
    
    public void copyDevicesListToClipboard () {
        StringBuilder sb = new StringBuilder();
        
        for (TestDevice td : devicesList) {
            sb.append(td.getOsType().getName()).append(" kliens");
            sb.append(System.lineSeparator());
            sb.append("Eszköz:\t\t").append(td.getName());
            sb.append(System.lineSeparator());
            sb.append("Op rendszer:\t").append(td.getOsVersion());
            sb.append(System.lineSeparator());
            sb.append("CPU:\t\t").append(td.getCpu());
            sb.append(System.lineSeparator());
            sb.append("RAM:\t\t").append(td.getRamSize());                        
            sb.append(System.lineSeparator());
            sb.append(System.lineSeparator());
        }   
        
        
        final ClipboardContent content = new ClipboardContent();
        content.putString(sb.toString());
        Clipboard.getSystemClipboard().setContent(content);
        
    }
    
}   
