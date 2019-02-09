package com.dhomoni.search.service.mapper;

import com.dhomoni.search.domain.*;
import com.dhomoni.search.service.dto.MedicineDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Medicine and its DTO MedicineDTO.
 */
@Mapper(componentModel = "spring", uses = {IndicationMapper.class})
public interface MedicineMapper extends EntityMapper<MedicineDTO, Medicine> {



    default Medicine fromId(Long id) {
        if (id == null) {
            return null;
        }
        Medicine medicine = new Medicine();
        medicine.setId(id);
        return medicine;
    }
}
