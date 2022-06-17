/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.time.LocalDate;
import java.util.Objects;

/**
 *
 * @author takacs.gergely
 */
public class DefectStatusRecord {
    
    private final LocalDate date;
    private String versionNumber;
    private DefectStatus defectStatus;

    public DefectStatusRecord(String productVersionNumber) {
        this.date = LocalDate.now();
        this.versionNumber = productVersionNumber;
        this.defectStatus = DefectStatus.NEW;
    }

    public LocalDate getDate() {
        return date;
    } 

    public String getProductVersionNumber() {
        return versionNumber;
    }

    public void setProductVersionNumber(String productVersionNumber) {
        this.versionNumber = productVersionNumber;
    }

    public DefectStatus getDefectStatus() {
        return defectStatus;
    }

    public void setDefectStatus(DefectStatus defectStatus) {
        this.defectStatus = defectStatus;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.date);
        hash = 17 * hash + Objects.hashCode(this.versionNumber);
        hash = 17 * hash + Objects.hashCode(this.defectStatus);
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
        final DefectStatusRecord other = (DefectStatusRecord) obj;
        if (!Objects.equals(this.date, other.date)) {
            return false;
        }
        if (!Objects.equals(this.versionNumber, other.versionNumber)) {
            return false;
        }
        if (this.defectStatus != other.defectStatus) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DefectStatusRecord{" + "date=" + date + ", productVersionNumber=" + versionNumber + ", defectStatus=" + defectStatus + '}';
    }
    
    
    
    
}

