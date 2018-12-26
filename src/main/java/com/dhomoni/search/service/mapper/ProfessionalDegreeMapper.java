package com.dhomoni.search.service.mapper;

import com.dhomoni.search.domain.*;
import com.dhomoni.search.service.dto.ProfessionalDegreeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ProfessionalDegree and its DTO ProfessionalDegreeDTO.
 */
@Mapper(componentModel = "spring", uses = {DoctorMapper.class})
public interface ProfessionalDegreeMapper extends EntityMapper<ProfessionalDegreeDTO, ProfessionalDegree> {

    @Mapping(source = "doctor.id", target = "doctorId")
    ProfessionalDegreeDTO toDto(ProfessionalDegree professionalDegree);

    @Mapping(source = "doctorId", target = "doctor")
    ProfessionalDegree toEntity(ProfessionalDegreeDTO professionalDegreeDTO);

    default ProfessionalDegree fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProfessionalDegree professionalDegree = new ProfessionalDegree();
        professionalDegree.setId(id);
        return professionalDegree;
    }
}
