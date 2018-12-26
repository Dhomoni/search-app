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
 * Criteria class for the ProfessionalDegree entity. This class is used in ProfessionalDegreeResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /professional-degrees?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProfessionalDegreeCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter institute;

    private IntegerFilter passingYear;

    private LongFilter doctorId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getInstitute() {
        return institute;
    }

    public void setInstitute(StringFilter institute) {
        this.institute = institute;
    }

    public IntegerFilter getPassingYear() {
        return passingYear;
    }

    public void setPassingYear(IntegerFilter passingYear) {
        this.passingYear = passingYear;
    }

    public LongFilter getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(LongFilter doctorId) {
        this.doctorId = doctorId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProfessionalDegreeCriteria that = (ProfessionalDegreeCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(institute, that.institute) &&
            Objects.equals(passingYear, that.passingYear) &&
            Objects.equals(doctorId, that.doctorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        institute,
        passingYear,
        doctorId
        );
    }

    @Override
    public String toString() {
        return "ProfessionalDegreeCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (institute != null ? "institute=" + institute + ", " : "") +
                (passingYear != null ? "passingYear=" + passingYear + ", " : "") +
                (doctorId != null ? "doctorId=" + doctorId + ", " : "") +
            "}";
    }

}
