package com.dhomoni.search.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import javax.persistence.Lob;

import com.dhomoni.search.domain.enumeration.BloodGroup;
import com.dhomoni.search.domain.enumeration.Sex;

/**
 * A DTO for the Patient entity.
 */
public class PatientDTO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8938996626792769046L;


	private Long id;

    
    private Long registrationId;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private Sex sex;

    private Instant birthTimestamp;

    private BloodGroup bloodGroup;

    private Double weightInKG;

    private Double heightInInch;

    @Lob
    private byte[] image;
    private String imageContentType;

    private String address;

    private Boolean activated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(Long registrationId) {
        this.registrationId = registrationId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public Instant getBirthTimestamp() {
        return birthTimestamp;
    }

    public void setBirthTimestamp(Instant birthTimestamp) {
        this.birthTimestamp = birthTimestamp;
    }

    public BloodGroup getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(BloodGroup bloodGroup) {
        this.bloodGroup = bloodGroup;
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
            ", registrationId=" + getRegistrationId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", sex='" + getSex() + "'" +
            ", birthTimestamp='" + getBirthTimestamp() + "'" +
            ", bloodGroup='" + getBloodGroup() + "'" +
            ", weightInKG=" + getWeightInKG() +
            ", heightInInch=" + getHeightInInch() +
            ", image='" + getImage() + "'" +
            ", address='" + getAddress() + "'" +
            ", activated='" + isActivated() + "'" +
            "}";
    }
}
