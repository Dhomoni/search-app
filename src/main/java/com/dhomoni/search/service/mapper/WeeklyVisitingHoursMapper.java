package com.dhomoni.search.service.mapper;

import com.dhomoni.search.domain.*;
import com.dhomoni.search.service.dto.WeeklyVisitingHoursDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity WeeklyVisitingHours and its DTO WeeklyVisitingHoursDTO.
 */
@Mapper(componentModel = "spring", uses = {ChamberMapper.class})
public interface WeeklyVisitingHoursMapper extends EntityMapper<WeeklyVisitingHoursDTO, WeeklyVisitingHours> {

    @Mapping(source = "chamber.id", target = "chamberId")
    WeeklyVisitingHoursDTO toDto(WeeklyVisitingHours weeklyVisitingHours);

    @Mapping(source = "chamberId", target = "chamber")
    WeeklyVisitingHours toEntity(WeeklyVisitingHoursDTO weeklyVisitingHoursDTO);

    default WeeklyVisitingHours fromId(Long id) {
        if (id == null) {
            return null;
        }
        WeeklyVisitingHours weeklyVisitingHours = new WeeklyVisitingHours();
        weeklyVisitingHours.setId(id);
        return weeklyVisitingHours;
    }
}
