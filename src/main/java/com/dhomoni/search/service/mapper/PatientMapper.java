package com.dhomoni.search.service.mapper;

import com.dhomoni.search.domain.*;
import com.dhomoni.search.service.dto.PatientDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Patient and its DTO PatientDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PatientMapper extends EntityMapper<PatientDTO, Patient> {



    default Patient fromId(Long id) {
        if (id == null) {
            return null;
        }
        Patient patient = new Patient();
        patient.setId(id);
        return patient;
    }
}
