package com.dhomoni.search.service.dto;

import java.io.Serializable;
import java.util.Objects;
import com.dhomoni.search.domain.enumeration.Sex;
import com.dhomoni.search.domain.enumeration.BloodGroup;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the Patient entity. This class is used in PatientResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /patients?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PatientCriteria implements Serializable {
    /**
     * Class for filtering Sex
     */
    public static class SexFilter extends Filter<Sex> {
    }
    /**
     * Class for filtering BloodGroup
     */
    public static class BloodGroupFilter extends Filter<BloodGroup> {
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter phone;

    private StringFilter email;

    private SexFilter sex;

    private InstantFilter birthDate;

    private DoubleFilter weightInKG;

    private DoubleFilter heightInInch;

    private StringFilter address;

    private BloodGroupFilter bloodGroup;

    private BooleanFilter activated;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
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

    public SexFilter getSex() {
        return sex;
    }

    public void setSex(SexFilter sex) {
        this.sex = sex;
    }

    public InstantFilter getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(InstantFilter birthDate) {
        this.birthDate = birthDate;
    }

    public DoubleFilter getWeightInKG() {
        return weightInKG;
    }

    public void setWeightInKG(DoubleFilter weightInKG) {
        this.weightInKG = weightInKG;
    }

    public DoubleFilter getHeightInInch() {
        return heightInInch;
    }

    public void setHeightInInch(DoubleFilter heightInInch) {
        this.heightInInch = heightInInch;
    }

    public StringFilter getAddress() {
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public BloodGroupFilter getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(BloodGroupFilter bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public BooleanFilter getActivated() {
        return activated;
    }

    public void setActivated(BooleanFilter activated) {
        this.activated = activated;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PatientCriteria that = (PatientCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(email, that.email) &&
            Objects.equals(sex, that.sex) &&
            Objects.equals(birthDate, that.birthDate) &&
            Objects.equals(weightInKG, that.weightInKG) &&
            Objects.equals(heightInInch, that.heightInInch) &&
            Objects.equals(address, that.address) &&
            Objects.equals(bloodGroup, that.bloodGroup) &&
            Objects.equals(activated, that.activated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        firstName,
        lastName,
        phone,
        email,
        sex,
        birthDate,
        weightInKG,
        heightInInch,
        address,
        bloodGroup,
        activated
        );
    }

    @Override
    public String toString() {
        return "PatientCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (firstName != null ? "firstName=" + firstName + ", " : "") +
                (lastName != null ? "lastName=" + lastName + ", " : "") +
                (phone != null ? "phone=" + phone + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (sex != null ? "sex=" + sex + ", " : "") +
                (birthDate != null ? "birthDate=" + birthDate + ", " : "") +
                (weightInKG != null ? "weightInKG=" + weightInKG + ", " : "") +
                (heightInInch != null ? "heightInInch=" + heightInInch + ", " : "") +
                (address != null ? "address=" + address + ", " : "") +
                (bloodGroup != null ? "bloodGroup=" + bloodGroup + ", " : "") +
                (activated != null ? "activated=" + activated + ", " : "") +
            "}";
    }

}
