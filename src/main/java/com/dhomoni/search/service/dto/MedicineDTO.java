package com.dhomoni.search.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.dhomoni.search.domain.enumeration.MedicineType;

/**
 * A DTO for the Medicine entity.
 */
public class MedicineDTO implements Serializable {

    private Long id;

    @NotNull
    private String tradeName;

    private String unitQuantity;

    @NotNull
    private String genericName;

    @NotNull
    private String chemicalName;

    private MedicineType type;

    @NotNull
    private String manufacturer;

    private Double mrp;

    private String indications;

    private String doseAndAdmin;

    private String preparation;

    private String productUrl;

    private Boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public String getUnitQuantity() {
        return unitQuantity;
    }

    public void setUnitQuantity(String unitQuantity) {
        this.unitQuantity = unitQuantity;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public String getChemicalName() {
        return chemicalName;
    }

    public void setChemicalName(String chemicalName) {
        this.chemicalName = chemicalName;
    }

    public MedicineType getType() {
        return type;
    }

    public void setType(MedicineType type) {
        this.type = type;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Double getMrp() {
        return mrp;
    }

    public void setMrp(Double mrp) {
        this.mrp = mrp;
    }

    public String getIndications() {
        return indications;
    }

    public void setIndications(String indications) {
        this.indications = indications;
    }

    public String getDoseAndAdmin() {
        return doseAndAdmin;
    }

    public void setDoseAndAdmin(String doseAndAdmin) {
        this.doseAndAdmin = doseAndAdmin;
    }

    public String getPreparation() {
        return preparation;
    }

    public void setPreparation(String preparation) {
        this.preparation = preparation;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MedicineDTO medicineDTO = (MedicineDTO) o;
        if (medicineDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), medicineDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MedicineDTO{" +
            "id=" + getId() +
            ", tradeName='" + getTradeName() + "'" +
            ", unitQuantity='" + getUnitQuantity() + "'" +
            ", genericName='" + getGenericName() + "'" +
            ", chemicalName='" + getChemicalName() + "'" +
            ", type='" + getType() + "'" +
            ", manufacturer='" + getManufacturer() + "'" +
            ", mrp=" + getMrp() +
            ", indications='" + getIndications() + "'" +
            ", doseAndAdmin='" + getDoseAndAdmin() + "'" +
            ", preparation='" + getPreparation() + "'" +
            ", productUrl='" + getProductUrl() + "'" +
            ", active='" + isActive() + "'" +
            "}";
    }
}
