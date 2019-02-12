package com.dhomoni.search.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.validation.constraints.NotNull;

import lombok.ToString;

/**
 * A DTO for the Disease entity.
 */
@ToString
public class DiseaseDTO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 9084580373417397559L;

	private Long id;

    @NotNull
    private String medicalName;

    @NotNull
    private String generalName;

    private Set<SymptomDTO> symptoms = new HashSet<>();

//    private Long medicalDepartmentId;
    
    private MedicalDepartmentDTO medicalDepartment;

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

    public Set<SymptomDTO> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(Set<SymptomDTO> symptoms) {
        this.symptoms = symptoms;
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

	public MedicalDepartmentDTO getMedicalDepartment() {
		return medicalDepartment;
	}

	public void setMedicalDepartment(MedicalDepartmentDTO medicalDepartment) {
		this.medicalDepartment = medicalDepartment;
	}
}
