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

    private LongFilter registrationId;

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter email;

    private StringFilter phone;

    private StringFilter licenceNumber;

    private StringFilter nationalId;

    private StringFilter passportNo;

    private DoctorTypeFilter type;

    private StringFilter designation;

    private StringFilter address;

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

    public LongFilter getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(LongFilter registrationId) {
        this.registrationId = registrationId;
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

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public StringFilter getLicenceNumber() {
        return licenceNumber;
    }

    public void setLicenceNumber(StringFilter licenceNumber) {
        this.licenceNumber = licenceNumber;
    }

    public StringFilter getNationalId() {
        return nationalId;
    }

    public void setNationalId(StringFilter nationalId) {
        this.nationalId = nationalId;
    }

    public StringFilter getPassportNo() {
        return passportNo;
    }

    public void setPassportNo(StringFilter passportNo) {
        this.passportNo = passportNo;
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

    public StringFilter getAddress() {
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
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
            Objects.equals(registrationId, that.registrationId) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(email, that.email) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(licenceNumber, that.licenceNumber) &&
            Objects.equals(nationalId, that.nationalId) &&
            Objects.equals(passportNo, that.passportNo) &&
            Objects.equals(type, that.type) &&
            Objects.equals(designation, that.designation) &&
            Objects.equals(address, that.address) &&
            Objects.equals(activated, that.activated) &&
            Objects.equals(medicalDepartmentId, that.medicalDepartmentId) &&
            Objects.equals(chambersId, that.chambersId) &&
            Objects.equals(professionalDegreesId, that.professionalDegreesId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        registrationId,
        firstName,
        lastName,
        email,
        phone,
        licenceNumber,
        nationalId,
        passportNo,
        type,
        designation,
        address,
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
                (registrationId != null ? "registrationId=" + registrationId + ", " : "") +
                (firstName != null ? "firstName=" + firstName + ", " : "") +
                (lastName != null ? "lastName=" + lastName + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (phone != null ? "phone=" + phone + ", " : "") +
                (licenceNumber != null ? "licenceNumber=" + licenceNumber + ", " : "") +
                (nationalId != null ? "nationalId=" + nationalId + ", " : "") +
                (passportNo != null ? "passportNo=" + passportNo + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (designation != null ? "designation=" + designation + ", " : "") +
                (address != null ? "address=" + address + ", " : "") +
                (activated != null ? "activated=" + activated + ", " : "") +
                (medicalDepartmentId != null ? "medicalDepartmentId=" + medicalDepartmentId + ", " : "") +
                (chambersId != null ? "chambersId=" + chambersId + ", " : "") +
                (professionalDegreesId != null ? "professionalDegreesId=" + professionalDegreesId + ", " : "") +
            "}";
    }

}
