package com.dhomoni.search.service.mapper;

import com.dhomoni.search.domain.*;
import com.dhomoni.search.service.dto.WeeklyVisitingHourDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity WeeklyVisitingHour and its DTO WeeklyVisitingHourDTO.
 */
@Mapper(componentModel = "spring", uses = {ChamberMapper.class})
public interface WeeklyVisitingHourMapper extends EntityMapper<WeeklyVisitingHourDTO, WeeklyVisitingHour> {

    @Mapping(source = "chamber.id", target = "chamberId")
    WeeklyVisitingHourDTO toDto(WeeklyVisitingHour weeklyVisitingHour);

    @Mapping(source = "chamberId", target = "chamber")
    WeeklyVisitingHour toEntity(WeeklyVisitingHourDTO weeklyVisitingHourDTO);

    default WeeklyVisitingHour fromId(Long id) {
        if (id == null) {
            return null;
        }
        WeeklyVisitingHour weeklyVisitingHour = new WeeklyVisitingHour();
        weeklyVisitingHour.setId(id);
        return weeklyVisitingHour;
    }
}
