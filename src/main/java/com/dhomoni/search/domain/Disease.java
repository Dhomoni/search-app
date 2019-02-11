package com.dhomoni.search.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
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

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "disease_symptoms",
               joinColumns = @JoinColumn(name = "diseases_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "symptoms_id", referencedColumnName = "id"))
    @Field(type = FieldType.Nested, index = false)
    private Set<Symptom> symptoms = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("diseases")
    private MedicalDepartment medicalDepartment;

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

    public Set<Symptom> getSymptoms() {
        return symptoms;
    }

    public Disease symptoms(Set<Symptom> symptoms) {
        this.symptoms = symptoms;
        return this;
    }

    public Disease addSymptoms(Symptom symptom) {
        this.symptoms.add(symptom);
        return this;
    }

    public Disease removeSymptoms(Symptom symptom) {
        this.symptoms.remove(symptom);
        return this;
    }

    public void setSymptoms(Set<Symptom> symptoms) {
        this.symptoms = symptoms;
    }

    public MedicalDepartment getMedicalDepartment() {
        return medicalDepartment;
    }

    public Disease medicalDepartment(MedicalDepartment medicalDepartment) {
        this.medicalDepartment = medicalDepartment;
        return this;
    }

    public void setMedicalDepartment(MedicalDepartment medicalDepartment) {
        this.medicalDepartment = medicalDepartment;
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
            "}";
    }
}
