package com.dhomoni.search.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import com.dhomoni.search.domain.enumeration.DoctorType;

/**
 * A DTO for the Doctor entity.
 */
public class DoctorDTO implements Serializable {

    private Long id;

    @NotNull
    private String licenceNumber;

    private String firstName;

    private String lastName;

    private String phone;

    private String email;

    private DoctorType type;

    private String designation;

    @Lob
    private String description;

    @Lob
    private byte[] image;
    private String imageContentType;

    private Boolean activated;

    private Long medicalDepartmentId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLicenceNumber() {
        return licenceNumber;
    }

    public void setLicenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
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

    public DoctorType getType() {
        return type;
    }

    public void setType(DoctorType type) {
        this.type = type;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Boolean isActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public Long getMedicalDepartmentId() {
        return medicalDepartmentId;
    }

    public void setMedicalDepartmentId(Long medicalDepartmentId) {
        this.medicalDepartmentId = medicalDepartmentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DoctorDTO doctorDTO = (DoctorDTO) o;
        if (doctorDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), doctorDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DoctorDTO{" +
            "id=" + getId() +
            ", licenceNumber='" + getLicenceNumber() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", phone='" + getPhone() + "'" +
            ", email='" + getEmail() + "'" +
            ", type='" + getType() + "'" +
            ", designation='" + getDesignation() + "'" +
            ", description='" + getDescription() + "'" +
            ", image='" + getImage() + "'" +
            ", activated='" + isActivated() + "'" +
            ", medicalDepartment=" + getMedicalDepartmentId() +
            "}";
    }
}
