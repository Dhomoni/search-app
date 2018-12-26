package com.dhomoni.search.service.mapper;

import com.dhomoni.search.domain.*;
import com.dhomoni.search.service.dto.DiseaseDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Disease and its DTO DiseaseDTO.
 */
@Mapper(componentModel = "spring", uses = {MedicalDepartmentMapper.class})
public interface DiseaseMapper extends EntityMapper<DiseaseDTO, Disease> {

    @Mapping(source = "dept.id", target = "deptId")
    DiseaseDTO toDto(Disease disease);

    @Mapping(source = "deptId", target = "dept")
    Disease toEntity(DiseaseDTO diseaseDTO);

    default Disease fromId(Long id) {
        if (id == null) {
            return null;
        }
        Disease disease = new Disease();
        disease.setId(id);
        return disease;
    }
}
