package com.dhomoni.search.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.dhomoni.search.domain.enumeration.Formulation;

/**
 * A DTO for the Medicine entity.
 */
public class MedicineDTO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1968541136144603043L;

	private Long id;

    @NotNull
    private String tradeName;

    private String unitQuantity;

    @NotNull
    private String genericName;

    @NotNull
    private String chemicalName;

    private Formulation formulation;

    @NotNull
    private String manufacturer;

    private Double mrp;

    private String doseAndAdmin;

    private String preparation;

    private String productUrl;

    private Boolean active;

    private Set<IndicationDTO> indications = new HashSet<>();

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

    public Formulation getFormulation() {
        return formulation;
    }

    public void setFormulation(Formulation formulation) {
        this.formulation = formulation;
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

    public Set<IndicationDTO> getIndications() {
        return indications;
    }

    public void setIndications(Set<IndicationDTO> indications) {
        this.indications = indications;
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
            ", formulation='" + getFormulation() + "'" +
            ", manufacturer='" + getManufacturer() + "'" +
            ", mrp=" + getMrp() +
            ", doseAndAdmin='" + getDoseAndAdmin() + "'" +
            ", preparation='" + getPreparation() + "'" +
            ", productUrl='" + getProductUrl() + "'" +
            ", active='" + isActive() + "'" +
            "}";
    }
}
