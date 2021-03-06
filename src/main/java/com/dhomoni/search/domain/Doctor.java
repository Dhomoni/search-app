package com.dhomoni.search.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.dhomoni.search.domain.enumeration.DoctorType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vividsolutions.jts.geom.Point;

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
    @Field(type = FieldType.Long)
    private Long id;

    @Column(name = "registration_id", unique = true)
    @Field(type = FieldType.Long, index = false)
    private Long registrationId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    @Field(type = FieldType.Keyword)
    private String email;

    @Column(name = "phone")
    @Field(type = FieldType.Keyword)
    private String phone;

    @NotNull
    @Column(name = "licence_number", nullable = false)
    @Field(type = FieldType.Keyword, index = false)
    private String licenceNumber;

    @Column(name = "national_id")
    @Field(type = FieldType.Keyword, index = false)
    private String nationalId;

    @Column(name = "passport_no")
    @Field(type = FieldType.Keyword, index = false)
    private String passportNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    @Field(type = FieldType.Keyword)
    private DoctorType type;

    @Column(name = "designation")
    private String designation;

    @Lob
    @Column(name = "description")
    @Field(type = FieldType.Text, index = false)
    private String description;

    @Column(name = "address")
    @Field(type = FieldType.Keyword, index = false)
    private String address;
    
    @Column(name = "GEOM", columnDefinition = "GEOMETRY(Point, 4326)")
    @Field(type = FieldType.Object, index = false)
    private Point location;

    @Lob
    @Column(name = "image")
    @Field(type = FieldType.Text, index = false)
    private byte[] image;

    @Column(name = "image_content_type")
    @Field(type = FieldType.Keyword, index = false)
    private String imageContentType;

    @Column(name = "activated")
    @Field(type = FieldType.Boolean, index = false)
    private Boolean activated;

    @Column(name = "institute")
    private String institute;

    @Column(name = "speciality")
    private String speciality;

    @ManyToOne
    @JsonIgnoreProperties("doctor")
    @Field(type = FieldType.Object)
    private MedicalDepartment medicalDepartment;

    @OneToMany(mappedBy = "doctor")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnoreProperties({"doctor"})
    @Field(type = FieldType.Nested, ignoreFields = {"doctor"})
    private Set<Chamber> chambers = new HashSet<>();
    
    @OneToMany(mappedBy = "doctor")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnoreProperties({"doctor"})
    @Field(type = FieldType.Nested, ignoreFields = {"doctor"})
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
    
	public Point getLocation() {
		return location;
	}
	
	public Doctor location(Point location) {
        this.location = location;
        return this;
    }

	public void setLocation(Point location) {
		this.location = location;
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
    
    public String getInstitute() {
        return institute;
    }

    public Doctor institute(String institute) {
        this.institute = institute;
        return this;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public String getSpeciality() {
        return speciality;
    }

    public Doctor speciality(String speciality) {
        this.speciality = speciality;
        return this;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
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
            ", institute='" + getInstitute() + "'" +
            ", speciality='" + getSpeciality() + "'" +
            ", description='" + getDescription() + "'" +
            ", address='" + getAddress() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            ", activated='" + isActivated() + "'" +
            "}";
    }
}
