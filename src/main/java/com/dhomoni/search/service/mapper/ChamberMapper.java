package com.dhomoni.search.service.mapper;

import com.dhomoni.search.domain.*;
import com.dhomoni.search.service.dto.ChamberDTO;

import java.util.Set;

import org.mapstruct.*;

/**
 * Mapper for the entity Chamber and its DTO ChamberDTO.
 */
@Mapper(componentModel = "spring", uses = {DoctorMapper.class, WeeklyVisitingHourMapper.class})
public interface ChamberMapper extends EntityMapper<ChamberDTO, Chamber> {

    @Mapping(source = "doctor.id", target = "doctorId")
    ChamberDTO toDto(Chamber chamber);
    
    @IterableMapping(qualifiedByName="mapWithoutDoctor")
    Set<ChamberDTO> toDtos(Set<Chamber> chambers);
    
    @Named("mapWithoutDoctor")
    @Mapping(target = "doctorId", ignore = true)
    ChamberDTO toDtoWithoutDoctor(Chamber chamber);

    @Mapping(source = "doctorId", target = "doctor")
    @Mapping(target = "weeklyVisitingHours", ignore = true)
    Chamber toEntity(ChamberDTO chamberDTO);

    default Chamber fromId(Long id) {
        if (id == null) {
            return null;
        }
        Chamber chamber = new Chamber();
        chamber.setId(id);
        return chamber;
    }
}
