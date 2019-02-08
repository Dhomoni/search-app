package com.dhomoni.search.service.mapper;

import com.dhomoni.search.domain.*;
import com.dhomoni.search.service.dto.SymptomDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Symptom and its DTO SymptomDTO.
 */
@Mapper(componentModel = "spring", uses = {DiseaseMapper.class})
public interface SymptomMapper extends EntityMapper<SymptomDTO, Symptom> {

    @Mapping(source = "disease.id", target = "diseaseId")
    SymptomDTO toDto(Symptom symptom);

    @Mapping(source = "diseaseId", target = "disease")
    Symptom toEntity(SymptomDTO symptomDTO);

    default Symptom fromId(Long id) {
        if (id == null) {
            return null;
        }
        Symptom symptom = new Symptom();
        symptom.setId(id);
        return symptom;
    }
}
