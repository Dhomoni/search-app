package com.dhomoni.search.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the MedicalDepartment entity.
 */
public class MedicalDepartmentDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MedicalDepartmentDTO medicalDepartmentDTO = (MedicalDepartmentDTO) o;
        if (medicalDepartmentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), medicalDepartmentDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MedicalDepartmentDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
