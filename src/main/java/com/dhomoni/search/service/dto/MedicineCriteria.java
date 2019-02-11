package com.dhomoni.search.service.dto;

import java.io.Serializable;
import java.util.Objects;

import com.dhomoni.search.domain.enumeration.Formulation;

import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the Medicine entity. This class is used in MedicineResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /medicines?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MedicineCriteria implements Serializable {
    /**
     * Class for filtering Formulation
     */
    public static class FormulationFilter extends Filter<Formulation> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 6156562705004458913L;
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tradeName;

    private StringFilter unitQuantity;

    private StringFilter genericName;

    private StringFilter chemicalName;

    private FormulationFilter formulation;

    private StringFilter manufacturer;

    private DoubleFilter mrp;

    private StringFilter doseAndAdmin;

    private StringFilter preparation;

    private StringFilter productUrl;

    private BooleanFilter active;

    private LongFilter indicationsId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTradeName() {
        return tradeName;
    }

    public void setTradeName(StringFilter tradeName) {
        this.tradeName = tradeName;
    }

    public StringFilter getUnitQuantity() {
        return unitQuantity;
    }

    public void setUnitQuantity(StringFilter unitQuantity) {
        this.unitQuantity = unitQuantity;
    }

    public StringFilter getGenericName() {
        return genericName;
    }

    public void setGenericName(StringFilter genericName) {
        this.genericName = genericName;
    }

    public StringFilter getChemicalName() {
        return chemicalName;
    }

    public void setChemicalName(StringFilter chemicalName) {
        this.chemicalName = chemicalName;
    }

    public FormulationFilter getFormulation() {
        return formulation;
    }

    public void setFormulation(FormulationFilter formulation) {
        this.formulation = formulation;
    }

    public StringFilter getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(StringFilter manufacturer) {
        this.manufacturer = manufacturer;
    }

    public DoubleFilter getMrp() {
        return mrp;
    }

    public void setMrp(DoubleFilter mrp) {
        this.mrp = mrp;
    }

    public StringFilter getDoseAndAdmin() {
        return doseAndAdmin;
    }

    public void setDoseAndAdmin(StringFilter doseAndAdmin) {
        this.doseAndAdmin = doseAndAdmin;
    }

    public StringFilter getPreparation() {
        return preparation;
    }

    public void setPreparation(StringFilter preparation) {
        this.preparation = preparation;
    }

    public StringFilter getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(StringFilter productUrl) {
        this.productUrl = productUrl;
    }

    public BooleanFilter getActive() {
        return active;
    }

    public void setActive(BooleanFilter active) {
        this.active = active;
    }

    public LongFilter getIndicationsId() {
        return indicationsId;
    }

    public void setIndicationsId(LongFilter indicationsId) {
        this.indicationsId = indicationsId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MedicineCriteria that = (MedicineCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(tradeName, that.tradeName) &&
            Objects.equals(unitQuantity, that.unitQuantity) &&
            Objects.equals(genericName, that.genericName) &&
            Objects.equals(chemicalName, that.chemicalName) &&
            Objects.equals(formulation, that.formulation) &&
            Objects.equals(manufacturer, that.manufacturer) &&
            Objects.equals(mrp, that.mrp) &&
            Objects.equals(doseAndAdmin, that.doseAndAdmin) &&
            Objects.equals(preparation, that.preparation) &&
            Objects.equals(productUrl, that.productUrl) &&
            Objects.equals(active, that.active) &&
            Objects.equals(indicationsId, that.indicationsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        tradeName,
        unitQuantity,
        genericName,
        chemicalName,
        formulation,
        manufacturer,
        mrp,
        doseAndAdmin,
        preparation,
        productUrl,
        active,
        indicationsId
        );
    }

    @Override
    public String toString() {
        return "MedicineCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (tradeName != null ? "tradeName=" + tradeName + ", " : "") +
                (unitQuantity != null ? "unitQuantity=" + unitQuantity + ", " : "") +
                (genericName != null ? "genericName=" + genericName + ", " : "") +
                (chemicalName != null ? "chemicalName=" + chemicalName + ", " : "") +
                (formulation != null ? "formulation=" + formulation + ", " : "") +
                (manufacturer != null ? "manufacturer=" + manufacturer + ", " : "") +
                (mrp != null ? "mrp=" + mrp + ", " : "") +
                (doseAndAdmin != null ? "doseAndAdmin=" + doseAndAdmin + ", " : "") +
                (preparation != null ? "preparation=" + preparation + ", " : "") +
                (productUrl != null ? "productUrl=" + productUrl + ", " : "") +
                (active != null ? "active=" + active + ", " : "") +
                (indicationsId != null ? "indicationsId=" + indicationsId + ", " : "") +
            "}";
    }

}
