package com.dhomoni.search.service.mapper;

import com.dhomoni.search.domain.*;
import com.dhomoni.search.service.dto.MedicalDepartmentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity MedicalDepartment and its DTO MedicalDepartmentDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MedicalDepartmentMapper extends EntityMapper<MedicalDepartmentDTO, MedicalDepartment> {


    @Mapping(target = "diseases", ignore = true)
    MedicalDepartment toEntity(MedicalDepartmentDTO medicalDepartmentDTO);

    default MedicalDepartment fromId(Long id) {
        if (id == null) {
            return null;
        }
        MedicalDepartment medicalDepartment = new MedicalDepartment();
        medicalDepartment.setId(id);
        return medicalDepartment;
    }
}
