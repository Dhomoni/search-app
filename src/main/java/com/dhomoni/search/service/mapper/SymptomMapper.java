package com.dhomoni.search.service.mapper;

import com.dhomoni.search.domain.*;
import com.dhomoni.search.service.dto.SymptomDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Symptom and its DTO SymptomDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SymptomMapper extends EntityMapper<SymptomDTO, Symptom> {



    default Symptom fromId(Long id) {
        if (id == null) {
            return null;
        }
        Symptom symptom = new Symptom();
        symptom.setId(id);
        return symptom;
    }
}
