package com.dhomoni.search.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Indication entity.
 */
public class IndicationDTO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8824275396178669374L;

	private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        IndicationDTO indicationDTO = (IndicationDTO) o;
        if (indicationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), indicationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "IndicationDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
