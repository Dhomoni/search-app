package com.dhomoni.search.service.mapper;

import com.dhomoni.search.domain.*;
import com.dhomoni.search.service.dto.IndicationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Indication and its DTO IndicationDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface IndicationMapper extends EntityMapper<IndicationDTO, Indication> {



    default Indication fromId(Long id) {
        if (id == null) {
            return null;
        }
        Indication indication = new Indication();
        indication.setId(id);
        return indication;
    }
}
