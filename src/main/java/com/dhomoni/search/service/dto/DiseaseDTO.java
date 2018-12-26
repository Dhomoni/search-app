package com.dhomoni.search.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Disease entity.
 */
public class DiseaseDTO implements Serializable {

    private Long id;

    @NotNull
    private String medicalName;

    @NotNull
    private String generalName;

    private String symptoms;

    private Long deptId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMedicalName() {
        return medicalName;
    }

    public void setMedicalName(String medicalName) {
        this.medicalName = medicalName;
    }

    public String getGeneralName() {
        return generalName;
    }

    public void setGeneralName(String generalName) {
        this.generalName = generalName;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long medicalDepartmentId) {
        this.deptId = medicalDepartmentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DiseaseDTO diseaseDTO = (DiseaseDTO) o;
        if (diseaseDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), diseaseDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DiseaseDTO{" +
            "id=" + getId() +
            ", medicalName='" + getMedicalName() + "'" +
            ", generalName='" + getGeneralName() + "'" +
            ", symptoms='" + getSymptoms() + "'" +
            ", dept=" + getDeptId() +
            "}";
    }
}
