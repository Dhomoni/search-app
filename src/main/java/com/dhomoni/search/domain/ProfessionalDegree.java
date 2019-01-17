package com.dhomoni.search.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Objects;

/**
 * A ProfessionalDegree.
 */
@Entity
@Table(name = "professional_degree")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProfessionalDegree implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Field(type = FieldType.Long, index=false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "institute")
    private String institute;

    @Column(name = "country")
    @Field(type = FieldType.Text, index=false)
    private String country;

    @Column(name = "enrollment_year")
    @Field(type = FieldType.Integer, index=false)
    private Integer enrollmentYear;

    @Column(name = "passing_year")
    @Field(type = FieldType.Integer, index=false)
    private Integer passingYear;

    @ManyToOne
    @JsonIgnoreProperties("professionalDegrees")
    private Doctor doctor;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public ProfessionalDegree name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstitute() {
        return institute;
    }

    public ProfessionalDegree institute(String institute) {
        this.institute = institute;
        return this;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public String getCountry() {
        return country;
    }

    public ProfessionalDegree country(String country) {
        this.country = country;
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getEnrollmentYear() {
        return enrollmentYear;
    }

    public ProfessionalDegree enrollmentYear(Integer enrollmentYear) {
        this.enrollmentYear = enrollmentYear;
        return this;
    }

    public void setEnrollmentYear(Integer enrollmentYear) {
        this.enrollmentYear = enrollmentYear;
    }

    public Integer getPassingYear() {
        return passingYear;
    }

    public ProfessionalDegree passingYear(Integer passingYear) {
        this.passingYear = passingYear;
        return this;
    }

    public void setPassingYear(Integer passingYear) {
        this.passingYear = passingYear;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public ProfessionalDegree doctor(Doctor doctor) {
        this.doctor = doctor;
        return this;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
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
        ProfessionalDegree professionalDegree = (ProfessionalDegree) o;
        if (professionalDegree.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), professionalDegree.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProfessionalDegree{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", institute='" + getInstitute() + "'" +
            ", country='" + getCountry() + "'" +
            ", enrollmentYear=" + getEnrollmentYear() +
            ", passingYear=" + getPassingYear() +
            "}";
    }
}
