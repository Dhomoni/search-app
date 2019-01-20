package com.dhomoni.search.service.mapper;

import java.util.Set;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.dhomoni.search.domain.ProfessionalDegree;
import com.dhomoni.search.service.dto.ProfessionalDegreeDTO;

/**
 * Mapper for the entity ProfessionalDegree and its DTO ProfessionalDegreeDTO.
 */
@Mapper(componentModel = "spring", uses = {DoctorMapper.class})
public interface ProfessionalDegreeMapper extends EntityMapper<ProfessionalDegreeDTO, ProfessionalDegree> {

    @Mapping(source = "doctor.id", target = "doctorId")
    ProfessionalDegreeDTO toDto(ProfessionalDegree professionalDegree);

    @IterableMapping(qualifiedByName="mapWithoutDoctor")
    Set<ProfessionalDegreeDTO> toDtos(Set<ProfessionalDegree> professionalDegrees);
    
    @Named("mapWithoutDoctor")
    @Mapping(target = "doctorId", ignore = true)
    ProfessionalDegreeDTO toDtoWithoutDoctor(ProfessionalDegree professionalDegree);
    
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
