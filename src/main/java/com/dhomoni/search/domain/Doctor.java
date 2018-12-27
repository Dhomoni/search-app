package com.dhomoni.search.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vividsolutions.jts.geom.Point;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.dhomoni.search.domain.enumeration.DoctorType;

/**
 * A Doctor.
 */
@Entity
@Table(name = "doctor")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "doctor")
public class Doctor implements Serializable {

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

    @NotNull
    @Column(name = "licence_number", nullable = false)
    private String licenceNumber;

    @Column(name = "national_id")
    private String nationalId;

    @Column(name = "passport_no")
    private String passportNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type")
    private DoctorType type;

    @Column(name = "designation")
    private String designation;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "address")
    private String address;
    
    @Column(name = "GEOM", columnDefinition = "GEOMETRY(Point, 4326)")
    private Point location;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @Column(name = "activated")
    private Boolean activated;

    @OneToOne    @JoinColumn(unique = true)
    private MedicalDepartment medicalDepartment;

    @OneToMany(mappedBy = "doctor")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Chamber> chambers = new HashSet<>();
    @OneToMany(mappedBy = "doctor")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ProfessionalDegree> professionalDegrees = new HashSet<>();
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

    public Doctor registrationId(Long registrationId) {
        this.registrationId = registrationId;
        return this;
    }

    public void setRegistrationId(Long registrationId) {
        this.registrationId = registrationId;
    }

    public String getFirstName() {
        return firstName;
    }

    public Doctor firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Doctor lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public Doctor email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public Doctor phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLicenceNumber() {
        return licenceNumber;
    }

    public Doctor licenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
        return this;
    }

    public void setLicenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
    }

    public String getNationalId() {
        return nationalId;
    }

    public Doctor nationalId(String nationalId) {
        this.nationalId = nationalId;
        return this;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getPassportNo() {
        return passportNo;
    }

    public Doctor passportNo(String passportNo) {
        this.passportNo = passportNo;
        return this;
    }

    public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
    }

    public DoctorType getType() {
        return type;
    }

    public Doctor type(DoctorType type) {
        this.type = type;
        return this;
    }

    public void setType(DoctorType type) {
        this.type = type;
    }

    public String getDesignation() {
        return designation;
    }

    public Doctor designation(String designation) {
        this.designation = designation;
        return this;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDescription() {
        return description;
    }

    public Doctor description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public Doctor address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public byte[] getImage() {
        return image;
    }

    public Doctor image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public Doctor imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Boolean isActivated() {
        return activated;
    }

    public Doctor activated(Boolean activated) {
        this.activated = activated;
        return this;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public MedicalDepartment getMedicalDepartment() {
        return medicalDepartment;
    }

    public Doctor medicalDepartment(MedicalDepartment medicalDepartment) {
        this.medicalDepartment = medicalDepartment;
        return this;
    }

    public void setMedicalDepartment(MedicalDepartment medicalDepartment) {
        this.medicalDepartment = medicalDepartment;
    }

    public Set<Chamber> getChambers() {
        return chambers;
    }

    public Doctor chambers(Set<Chamber> chambers) {
        this.chambers = chambers;
        return this;
    }

    public Doctor addChambers(Chamber chamber) {
        this.chambers.add(chamber);
        chamber.setDoctor(this);
        return this;
    }

    public Doctor removeChambers(Chamber chamber) {
        this.chambers.remove(chamber);
        chamber.setDoctor(null);
        return this;
    }

    public void setChambers(Set<Chamber> chambers) {
        this.chambers = chambers;
    }

    public Set<ProfessionalDegree> getProfessionalDegrees() {
        return professionalDegrees;
    }

    public Doctor professionalDegrees(Set<ProfessionalDegree> professionalDegrees) {
        this.professionalDegrees = professionalDegrees;
        return this;
    }

    public Doctor addProfessionalDegrees(ProfessionalDegree professionalDegree) {
        this.professionalDegrees.add(professionalDegree);
        professionalDegree.setDoctor(this);
        return this;
    }

    public Doctor removeProfessionalDegrees(ProfessionalDegree professionalDegree) {
        this.professionalDegrees.remove(professionalDegree);
        professionalDegree.setDoctor(null);
        return this;
    }

    public void setProfessionalDegrees(Set<ProfessionalDegree> professionalDegrees) {
        this.professionalDegrees = professionalDegrees;
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
        Doctor doctor = (Doctor) o;
        if (doctor.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), doctor.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Doctor{" +
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
            ", imageContentType='" + getImageContentType() + "'" +
            ", activated='" + isActivated() + "'" +
            "}";
    }

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}
}
