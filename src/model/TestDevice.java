/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Objects;

/**
 *
 * @author takacs.gergely
 */

public class TestDevice {
    private String name;
    private String brand;
    private String model;
    private OsType osType;
    private String osVersion;
    private String cpu;
    private int ramSize;
    private String notes;

    public TestDevice() {
    }
       

    public TestDevice(String name, String brand, String model, OsType osType, String osVersion, String cpu, int ramSize, String notes) {
        this.name = name;
        this.brand = brand;
        this.model = model;
        this.osType = osType;
        this.osVersion = osVersion;
        this.cpu = cpu;
        this.ramSize = ramSize;
        this.notes = notes;
    }

    public TestDevice(String name, String brand, String model, OsType osType) {
        this.name = name;
        this.brand = brand;
        this.model = model;
        this.osType = osType;
    }

    
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public OsType getOsType() {
        return osType;
    }
    
    public String getOsTypeName (){
        return this.getOsType().getName();
    }

    public void setOsType(OsType osType) {
        this.osType = osType;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public int getRamSize() {
        return ramSize;
    }

    public void setRamSize(int ramSize) {
        this.ramSize = ramSize;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.name);
        hash = 79 * hash + Objects.hashCode(this.brand);
        hash = 79 * hash + Objects.hashCode(this.model);
        hash = 79 * hash + Objects.hashCode(this.osType);
        hash = 79 * hash + Objects.hashCode(this.osVersion);
        hash = 79 * hash + Objects.hashCode(this.cpu);
        hash = 79 * hash + this.ramSize;
        hash = 79 * hash + Objects.hashCode(this.notes);
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
        final TestDevice other = (TestDevice) obj;
        if (this.ramSize != other.ramSize) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.brand, other.brand)) {
            return false;
        }
        if (!Objects.equals(this.model, other.model)) {
            return false;
        }
        if (!Objects.equals(this.osVersion, other.osVersion)) {
            return false;
        }
        if (!Objects.equals(this.cpu, other.cpu)) {
            return false;
        }
        if (!Objects.equals(this.notes, other.notes)) {
            return false;
        }
        if (this.osType != other.osType) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TestDevice{name=").append(name);
        sb.append(", brand=").append(brand);
        sb.append(", model=").append(model);
        sb.append(", osType=").append(osType);
        sb.append(", osVersion=").append(osVersion);
        sb.append(", cpu=").append(cpu);
        sb.append(", ramSize=").append(ramSize);
        sb.append(", notes=").append(notes);
        sb.append('}');
        return sb.toString();
    }



    
}
