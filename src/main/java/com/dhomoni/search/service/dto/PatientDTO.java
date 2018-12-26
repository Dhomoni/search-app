package com.dhomoni.search.service.dto;

import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import com.dhomoni.search.domain.enumeration.Sex;
import com.dhomoni.search.domain.enumeration.BloodGroup;

/**
 * A DTO for the Patient entity.
 */
public class PatientDTO implements Serializable {

    private Long id;

    private String firstName;

    private String lastName;

    private String phone;

    private String email;

    private Sex sex;

    private Instant birthDate;

    private Double weightInKG;

    private Double heightInInch;

    @Lob
    private byte[] image;
    private String imageContentType;

    private String address;

    private BloodGroup bloodGroup;

    private Boolean activated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public Instant getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Instant birthDate) {
        this.birthDate = birthDate;
    }

    public Double getWeightInKG() {
        return weightInKG;
    }

    public void setWeightInKG(Double weightInKG) {
        this.weightInKG = weightInKG;
    }

    public Double getHeightInInch() {
        return heightInInch;
    }

    public void setHeightInInch(Double heightInInch) {
        this.heightInInch = heightInInch;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BloodGroup getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(BloodGroup bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public Boolean isActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
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

        PatientDTO patientDTO = (PatientDTO) o;
        if (patientDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), patientDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PatientDTO{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", phone='" + getPhone() + "'" +
            ", email='" + getEmail() + "'" +
            ", sex='" + getSex() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            ", weightInKG=" + getWeightInKG() +
            ", heightInInch=" + getHeightInInch() +
            ", image='" + getImage() + "'" +
            ", address='" + getAddress() + "'" +
            ", bloodGroup='" + getBloodGroup() + "'" +
            ", activated='" + isActivated() + "'" +
            "}";
    }
}
