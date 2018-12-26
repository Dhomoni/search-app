package com.dhomoni.search.service.mapper;

import com.dhomoni.search.domain.*;
import com.dhomoni.search.service.dto.DoctorDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Doctor and its DTO DoctorDTO.
 */
@Mapper(componentModel = "spring", uses = {MedicalDepartmentMapper.class})
public interface DoctorMapper extends EntityMapper<DoctorDTO, Doctor> {

    @Mapping(source = "medicalDepartment.id", target = "medicalDepartmentId")
    DoctorDTO toDto(Doctor doctor);

    @Mapping(source = "medicalDepartmentId", target = "medicalDepartment")
    @Mapping(target = "chambers", ignore = true)
    @Mapping(target = "professionalDegrees", ignore = true)
    Doctor toEntity(DoctorDTO doctorDTO);

    default Doctor fromId(Long id) {
        if (id == null) {
            return null;
        }
        Doctor doctor = new Doctor();
        doctor.setId(id);
        return doctor;
    }
}
