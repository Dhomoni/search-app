package com.dhomoni.search.service.dto;

import java.io.Serializable;
import java.util.Objects;
import com.dhomoni.search.domain.enumeration.DoctorType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the Doctor entity. This class is used in DoctorResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /doctors?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DoctorCriteria implements Serializable {
    /**
     * Class for filtering DoctorType
     */
    public static class DoctorTypeFilter extends Filter<DoctorType> {
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter licenceNumber;

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter phone;

    private StringFilter email;

    private DoctorTypeFilter type;

    private StringFilter designation;

    private BooleanFilter activated;

    private LongFilter medicalDepartmentId;

    private LongFilter chambersId;

    private LongFilter professionalDegreesId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getLicenceNumber() {
        return licenceNumber;
    }

    public void setLicenceNumber(StringFilter licenceNumber) {
        this.licenceNumber = licenceNumber;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public DoctorTypeFilter getType() {
        return type;
    }

    public void setType(DoctorTypeFilter type) {
        this.type = type;
    }

    public StringFilter getDesignation() {
        return designation;
    }

    public void setDesignation(StringFilter designation) {
        this.designation = designation;
    }

    public BooleanFilter getActivated() {
        return activated;
    }

    public void setActivated(BooleanFilter activated) {
        this.activated = activated;
    }

    public LongFilter getMedicalDepartmentId() {
        return medicalDepartmentId;
    }

    public void setMedicalDepartmentId(LongFilter medicalDepartmentId) {
        this.medicalDepartmentId = medicalDepartmentId;
    }

    public LongFilter getChambersId() {
        return chambersId;
    }

    public void setChambersId(LongFilter chambersId) {
        this.chambersId = chambersId;
    }

    public LongFilter getProfessionalDegreesId() {
        return professionalDegreesId;
    }

    public void setProfessionalDegreesId(LongFilter professionalDegreesId) {
        this.professionalDegreesId = professionalDegreesId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DoctorCriteria that = (DoctorCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(licenceNumber, that.licenceNumber) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(email, that.email) &&
            Objects.equals(type, that.type) &&
            Objects.equals(designation, that.designation) &&
            Objects.equals(activated, that.activated) &&
            Objects.equals(medicalDepartmentId, that.medicalDepartmentId) &&
            Objects.equals(chambersId, that.chambersId) &&
            Objects.equals(professionalDegreesId, that.professionalDegreesId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        licenceNumber,
        firstName,
        lastName,
        phone,
        email,
        type,
        designation,
        activated,
        medicalDepartmentId,
        chambersId,
        professionalDegreesId
        );
    }

    @Override
    public String toString() {
        return "DoctorCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (licenceNumber != null ? "licenceNumber=" + licenceNumber + ", " : "") +
                (firstName != null ? "firstName=" + firstName + ", " : "") +
                (lastName != null ? "lastName=" + lastName + ", " : "") +
                (phone != null ? "phone=" + phone + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (designation != null ? "designation=" + designation + ", " : "") +
                (activated != null ? "activated=" + activated + ", " : "") +
                (medicalDepartmentId != null ? "medicalDepartmentId=" + medicalDepartmentId + ", " : "") +
                (chambersId != null ? "chambersId=" + chambersId + ", " : "") +
                (professionalDegreesId != null ? "professionalDegreesId=" + professionalDegreesId + ", " : "") +
            "}";
    }

}
