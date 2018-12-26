package com.dhomoni.search.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the ProfessionalDegree entity.
 */
public class ProfessionalDegreeDTO implements Serializable {

    private Long id;

    private String name;

    private String institute;

    private Integer passingYear;

    private Long doctorId;

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

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public Integer getPassingYear() {
        return passingYear;
    }

    public void setPassingYear(Integer passingYear) {
        this.passingYear = passingYear;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProfessionalDegreeDTO professionalDegreeDTO = (ProfessionalDegreeDTO) o;
        if (professionalDegreeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), professionalDegreeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProfessionalDegreeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", institute='" + getInstitute() + "'" +
            ", passingYear=" + getPassingYear() +
            ", doctor=" + getDoctorId() +
            "}";
    }
}
