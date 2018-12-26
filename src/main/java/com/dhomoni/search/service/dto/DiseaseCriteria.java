package com.dhomoni.search.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the Disease entity. This class is used in DiseaseResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /diseases?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DiseaseCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter medicalName;

    private StringFilter generalName;

    private StringFilter symptoms;

    private LongFilter deptId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getMedicalName() {
        return medicalName;
    }

    public void setMedicalName(StringFilter medicalName) {
        this.medicalName = medicalName;
    }

    public StringFilter getGeneralName() {
        return generalName;
    }

    public void setGeneralName(StringFilter generalName) {
        this.generalName = generalName;
    }

    public StringFilter getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(StringFilter symptoms) {
        this.symptoms = symptoms;
    }

    public LongFilter getDeptId() {
        return deptId;
    }

    public void setDeptId(LongFilter deptId) {
        this.deptId = deptId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DiseaseCriteria that = (DiseaseCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(medicalName, that.medicalName) &&
            Objects.equals(generalName, that.generalName) &&
            Objects.equals(symptoms, that.symptoms) &&
            Objects.equals(deptId, that.deptId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        medicalName,
        generalName,
        symptoms,
        deptId
        );
    }

    @Override
    public String toString() {
        return "DiseaseCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (medicalName != null ? "medicalName=" + medicalName + ", " : "") +
                (generalName != null ? "generalName=" + generalName + ", " : "") +
                (symptoms != null ? "symptoms=" + symptoms + ", " : "") +
                (deptId != null ? "deptId=" + deptId + ", " : "") +
            "}";
    }

}
