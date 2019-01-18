package com.dhomoni.search.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A MedicalDepartment.
 */
@Entity
@Table(name = "medical_department")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MedicalDepartment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Field(type = FieldType.Long, index = false)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "medicalDepartment")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @Field(type = FieldType.Nested, includeInParent = true, ignoreFields = {"medicalDepartment"})
    private Set<Disease> diseases = new HashSet<>();
    
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

    public MedicalDepartment name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Disease> getDiseases() {
        return diseases;
    }

    public MedicalDepartment diseases(Set<Disease> diseases) {
        this.diseases = diseases;
        return this;
    }

    public MedicalDepartment addDiseases(Disease disease) {
        this.diseases.add(disease);
        disease.setMedicalDepartment(this);
        return this;
    }

    public MedicalDepartment removeDiseases(Disease disease) {
        this.diseases.remove(disease);
        disease.setMedicalDepartment(null);
        return this;
    }

    public void setDiseases(Set<Disease> diseases) {
        this.diseases = diseases;
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
        MedicalDepartment medicalDepartment = (MedicalDepartment) o;
        if (medicalDepartment.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), medicalDepartment.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MedicalDepartment{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
