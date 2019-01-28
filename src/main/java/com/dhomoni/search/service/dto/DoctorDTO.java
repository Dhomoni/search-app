package com.dhomoni.search.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Lob;
import com.dhomoni.search.domain.enumeration.DoctorType;
import com.vividsolutions.jts.geom.Point;

/**
 * A DTO for the Doctor entity.
 */
public class DoctorDTO implements Serializable {

    private Long id;

    
    private Long registrationId;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    @NotNull
    private String licenceNumber;

    private String nationalId;

    private String passportNo;

    private DoctorType type;

    private String designation;

    @Lob
    private String description;

    private String address;

    private Point location;
    
    @Lob
    private byte[] image;
    
    private String imageContentType;

    private Boolean activated;

//    private Long medicalDepartmentId;
    
    private MedicalDepartmentDTO medicalDepartment;
    
    private Set<ChamberDTO> chambers;
    
    private Set<ProfessionalDegreeDTO> professionalDegrees;

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

    public String getLicenceNumber() {
        return licenceNumber;
    }

    public void setLicenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getPassportNo() {
        return passportNo;
    }

    public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
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
    
	public MedicalDepartmentDTO getMedicalDepartment() {
		return medicalDepartment;
	}

	public void setMedicalDepartment(MedicalDepartmentDTO medicalDepartment) {
		this.medicalDepartment = medicalDepartment;
	}

	public Set<ChamberDTO> getChambers() {
		return chambers;
	}

	public void setChambers(Set<ChamberDTO> chambers) {
		this.chambers = chambers;
	}

	public Set<ProfessionalDegreeDTO> getProfessionalDegrees() {
		return professionalDegrees;
	}

	public void setProfessionalDegrees(Set<ProfessionalDegreeDTO> professionalDegrees) {
		this.professionalDegrees = professionalDegrees;
	}

//    public Long getMedicalDepartmentId() {
//        return medicalDepartmentId;
//    }
//
//    public void setMedicalDepartmentId(Long medicalDepartmentId) {
//        this.medicalDepartmentId = medicalDepartmentId;
//    }

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
            ", registrationId=" + getRegistrationId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", licenceNumber='" + getLicenceNumber() + "'" +
            ", nationalId='" + getNationalId() + "'" +
            ", passportNo='" + getPassportNo() + "'" +
            ", type='" + getType() + "'" +
            ", designation='" + getDesignation() + "'" +
            ", description='" + getDescription() + "'" +
            ", address='" + getAddress() + "'" +
            ", image='" + getImage() + "'" +
            ", activated='" + isActivated() + "'" +
            ", medicalDepartment=" + getMedicalDepartment() +
            "}";
    }
}
