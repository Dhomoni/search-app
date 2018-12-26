package com.dhomoni.search.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Disease.
 */
@Entity
@Table(name = "disease")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "disease")
public class Disease implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "medical_name", nullable = false)
    private String medicalName;

    @NotNull
    @Column(name = "general_name", nullable = false)
    private String generalName;

    @Column(name = "symptoms")
    private String symptoms;

    @ManyToOne
    @JsonIgnoreProperties("diseases")
    private MedicalDepartment dept;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMedicalName() {
        return medicalName;
    }

    public Disease medicalName(String medicalName) {
        this.medicalName = medicalName;
        return this;
    }

    public void setMedicalName(String medicalName) {
        this.medicalName = medicalName;
    }

    public String getGeneralName() {
        return generalName;
    }

    public Disease generalName(String generalName) {
        this.generalName = generalName;
        return this;
    }

    public void setGeneralName(String generalName) {
        this.generalName = generalName;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public Disease symptoms(String symptoms) {
        this.symptoms = symptoms;
        return this;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public MedicalDepartment getDept() {
        return dept;
    }

    public Disease dept(MedicalDepartment medicalDepartment) {
        this.dept = medicalDepartment;
        return this;
    }

    public void setDept(MedicalDepartment medicalDepartment) {
        this.dept = medicalDepartment;
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
        Disease disease = (Disease) o;
        if (disease.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), disease.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Disease{" +
            "id=" + getId() +
            ", medicalName='" + getMedicalName() + "'" +
            ", generalName='" + getGeneralName() + "'" +
            ", symptoms='" + getSymptoms() + "'" +
            "}";
    }
}
