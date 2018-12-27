package com.dhomoni.search.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import com.dhomoni.search.domain.enumeration.Sex;

import com.dhomoni.search.domain.enumeration.BloodGroup;

/**
 * A Patient.
 */
@Entity
@Table(name = "patient")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "patient")
public class Patient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    
    @Column(name = "registration_id", unique = true)
    private Long registrationId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "sex")
    private Sex sex;

    @Column(name = "birth_timestamp")
    private Instant birthTimestamp;

    @Enumerated(EnumType.STRING)
    @Column(name = "blood_group")
    private BloodGroup bloodGroup;

    @Column(name = "weight_in_kg")
    private Double weightInKG;

    @Column(name = "height_in_inch")
    private Double heightInInch;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @Column(name = "address")
    private String address;

    @Column(name = "activated")
    private Boolean activated;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRegistrationId() {
        return registrationId;
    }

    public Patient registrationId(Long registrationId) {
        this.registrationId = registrationId;
        return this;
    }

    public void setRegistrationId(Long registrationId) {
        this.registrationId = registrationId;
    }

    public String getFirstName() {
        return firstName;
    }

    public Patient firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Patient lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public Patient email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public Patient phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Sex getSex() {
        return sex;
    }

    public Patient sex(Sex sex) {
        this.sex = sex;
        return this;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public Instant getBirthTimestamp() {
        return birthTimestamp;
    }

    public Patient birthTimestamp(Instant birthTimestamp) {
        this.birthTimestamp = birthTimestamp;
        return this;
    }

    public void setBirthTimestamp(Instant birthTimestamp) {
        this.birthTimestamp = birthTimestamp;
    }

    public BloodGroup getBloodGroup() {
        return bloodGroup;
    }

    public Patient bloodGroup(BloodGroup bloodGroup) {
        this.bloodGroup = bloodGroup;
        return this;
    }

    public void setBloodGroup(BloodGroup bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public Double getWeightInKG() {
        return weightInKG;
    }

    public Patient weightInKG(Double weightInKG) {
        this.weightInKG = weightInKG;
        return this;
    }

    public void setWeightInKG(Double weightInKG) {
        this.weightInKG = weightInKG;
    }

    public Double getHeightInInch() {
        return heightInInch;
    }

    public Patient heightInInch(Double heightInInch) {
        this.heightInInch = heightInInch;
        return this;
    }

    public void setHeightInInch(Double heightInInch) {
        this.heightInInch = heightInInch;
    }

    public byte[] getImage() {
        return image;
    }

    public Patient image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public Patient imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public String getAddress() {
        return address;
    }

    public Patient address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean isActivated() {
        return activated;
    }

    public Patient activated(Boolean activated) {
        this.activated = activated;
        return this;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
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
        Patient patient = (Patient) o;
        if (patient.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), patient.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Patient{" +
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
            ", imageContentType='" + getImageContentType() + "'" +
            ", address='" + getAddress() + "'" +
            ", activated='" + isActivated() + "'" +
            "}";
    }
}
