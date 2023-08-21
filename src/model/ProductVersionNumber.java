
package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 *
 * @author takacs.gergely
 */
public class ProductVersionNumber implements Serializable, Comparable<ProductVersionNumber> {
    
    private LocalDate dateVersionIntroduced;
    private String versionNumber;

    public ProductVersionNumber(String versionNumber) {
        
        this.versionNumber = versionNumber;
        this.dateVersionIntroduced = LocalDate.now();
    }

    public ProductVersionNumber(String versionNumber, LocalDate date) {
        
        this.versionNumber = versionNumber;
        this.dateVersionIntroduced = date;
    }
    

    public LocalDate getDateTimeVersionIntroduced() {
        return dateVersionIntroduced;
    }

    public String getStringNumber() {
        return versionNumber;
    }

    public void setVersionNumver(String versionNumver) {
        this.versionNumber = versionNumver;
    }

    public void setDateVersionIntroduced(LocalDate dateVersionIntroduced) {
        this.dateVersionIntroduced = dateVersionIntroduced;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        //hash = 23 * hash + Objects.hashCode(this.dateVersionIntroduced);
        hash = 23 * hash + Objects.hashCode(this.versionNumber);
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
        final ProductVersionNumber other = (ProductVersionNumber) obj;
        if (!Objects.equals(this.versionNumber, other.versionNumber)) {
            return false;
        }
//        if (!Objects.equals(this.dateVersionIntroduced, other.dateVersionIntroduced)) {
//            return false;
//        }
        return true;
    }
    
    
    


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ProductVersionNumber{dateTimeVersionIntroduced=").append(dateVersionIntroduced);
        sb.append(", versionNumver=").append(versionNumber);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int compareTo(ProductVersionNumber pvn) {
        return this.getStringNumber().compareTo(pvn.getStringNumber());
    }
    
    
    
    
    
    
}
