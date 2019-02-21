package com.dhomoni.search.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

import com.vividsolutions.jts.geom.Point;

/**
 * A DTO for the Chamber entity.
 */
public class ChamberDTO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

    private String address;
    
    private Point location;

    private String phone;

    private Double fee;
    
    private Double distanceInKM;

    private Set<WeeklyVisitingHourDTO> weeklyVisitingHours;
    
    private Long doctorId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChamberDTO chamberDTO = (ChamberDTO) o;
        if (chamberDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), chamberDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ChamberDTO{" +
            "id=" + getId() +
            ", address='" + getAddress() + "'" +
            ", phone='" + getPhone() + "'" +
            ", fee=" + getFee() +
            ", doctor=" + getDoctorId() +
            "}";
    }

	public Double getDistanceInKM() {
		return distanceInKM;
	}

	public void setDistanceInKM(Double distanceInKM) {
		this.distanceInKM = distanceInKM;
	}

	public Set<WeeklyVisitingHourDTO> getWeeklyVisitingHours() {
		return weeklyVisitingHours;
	}

	public void setWeeklyVisitingHours(Set<WeeklyVisitingHourDTO> weeklyVisitingHours) {
		this.weeklyVisitingHours = weeklyVisitingHours;
	}
}
