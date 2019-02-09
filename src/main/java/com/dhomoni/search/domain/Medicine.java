package com.dhomoni.search.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.dhomoni.search.domain.enumeration.Formulation;

/**
 * A Medicine.
 */
@Entity
@Table(name = "medicine")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "medicine")
public class Medicine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "trade_name", nullable = false)
    private String tradeName;

    @Column(name = "unit_quantity")
    private String unitQuantity;

    @NotNull
    @Column(name = "generic_name", nullable = false)
    private String genericName;

    @NotNull
    @Column(name = "chemical_name", nullable = false)
    private String chemicalName;

    @Enumerated(EnumType.STRING)
    @Column(name = "formulation")
    private Formulation formulation;

    @NotNull
    @Column(name = "manufacturer", nullable = false)
    private String manufacturer;

    @Column(name = "mrp")
    private Double mrp;

    @Column(name = "dose_and_admin")
    private String doseAndAdmin;

    @Column(name = "preparation")
    private String preparation;

    @Column(name = "product_url")
    private String productUrl;

    @Column(name = "active")
    private Boolean active;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "medicine_indications",
               joinColumns = @JoinColumn(name = "medicines_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "indications_id", referencedColumnName = "id"))
    @Field(type = FieldType.Nested)
    private Set<Indication> indications = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTradeName() {
        return tradeName;
    }

    public Medicine tradeName(String tradeName) {
        this.tradeName = tradeName;
        return this;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public String getUnitQuantity() {
        return unitQuantity;
    }

    public Medicine unitQuantity(String unitQuantity) {
        this.unitQuantity = unitQuantity;
        return this;
    }

    public void setUnitQuantity(String unitQuantity) {
        this.unitQuantity = unitQuantity;
    }

    public String getGenericName() {
        return genericName;
    }

    public Medicine genericName(String genericName) {
        this.genericName = genericName;
        return this;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public String getChemicalName() {
        return chemicalName;
    }

    public Medicine chemicalName(String chemicalName) {
        this.chemicalName = chemicalName;
        return this;
    }

    public void setChemicalName(String chemicalName) {
        this.chemicalName = chemicalName;
    }

    public Formulation getFormulation() {
        return formulation;
    }

    public Medicine formulation(Formulation formulation) {
        this.formulation = formulation;
        return this;
    }

    public void setFormulation(Formulation formulation) {
        this.formulation = formulation;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public Medicine manufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
        return this;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Double getMrp() {
        return mrp;
    }

    public Medicine mrp(Double mrp) {
        this.mrp = mrp;
        return this;
    }

    public void setMrp(Double mrp) {
        this.mrp = mrp;
    }

    public String getDoseAndAdmin() {
        return doseAndAdmin;
    }

    public Medicine doseAndAdmin(String doseAndAdmin) {
        this.doseAndAdmin = doseAndAdmin;
        return this;
    }

    public void setDoseAndAdmin(String doseAndAdmin) {
        this.doseAndAdmin = doseAndAdmin;
    }

    public String getPreparation() {
        return preparation;
    }

    public Medicine preparation(String preparation) {
        this.preparation = preparation;
        return this;
    }

    public void setPreparation(String preparation) {
        this.preparation = preparation;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public Medicine productUrl(String productUrl) {
        this.productUrl = productUrl;
        return this;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public Boolean isActive() {
        return active;
    }

    public Medicine active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<Indication> getIndications() {
        return indications;
    }

    public Medicine indications(Set<Indication> indications) {
        this.indications = indications;
        return this;
    }

    public Medicine addIndications(Indication indication) {
        this.indications.add(indication);
        return this;
    }

    public Medicine removeIndications(Indication indication) {
        this.indications.remove(indication);
        return this;
    }

    public void setIndications(Set<Indication> indications) {
        this.indications = indications;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Medicine medicine = (Medicine) o;
        if (medicine.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), medicine.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Medicine{" +
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
