package com.dhomoni.search.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Symptom entity.
 */
public class SymptomDTO implements Serializable {

    private Long id;

    private String name;

    private Long diseaseId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(Long diseaseId) {
        this.diseaseId = diseaseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SymptomDTO symptomDTO = (SymptomDTO) o;
        if (symptomDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), symptomDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SymptomDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", disease=" + getDiseaseId() +
            "}";
    }
}
