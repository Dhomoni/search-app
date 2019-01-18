package com.dhomoni.search.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vividsolutions.jts.geom.Point;

/**
 * A Chamber.
 */
@Entity
@Table(name = "chamber")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Chamber implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Field(type = FieldType.Long, index = false)
    private Long id;

    @Column(name = "address")
    private String address;
    
    @Column(name = "GEOM", columnDefinition = "GEOMETRY(Point, 4326)")
    private Point location;

    @Column(name = "phone")
    private String phone;

    @Column(name = "fee")
    private Double fee;

    @ManyToOne
    @JsonIgnoreProperties("chambers")
    private Doctor doctor;

    @OneToMany(mappedBy = "chamber")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @Field(type = FieldType.Nested, includeInParent = true, ignoreFields = {"chamber"})
    private Set<WeeklyVisitingHour> weeklyVisitingHours = new HashSet<>();
    
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public Chamber address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public Chamber phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getFee() {
        return fee;
    }

    public Chamber fee(Double fee) {
        this.fee = fee;
        return this;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public Chamber doctor(Doctor doctor) {
        this.doctor = doctor;
        return this;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Set<WeeklyVisitingHour> getWeeklyVisitingHours() {
        return weeklyVisitingHours;
    }

    public Chamber weeklyVisitingHours(Set<WeeklyVisitingHour> weeklyVisitingHours) {
        this.weeklyVisitingHours = weeklyVisitingHours;
        return this;
    }

    public Chamber addWeeklyVisitingHours(WeeklyVisitingHour weeklyVisitingHour) {
        this.weeklyVisitingHours.add(weeklyVisitingHour);
        weeklyVisitingHour.setChamber(this);
        return this;
    }

    public Chamber removeWeeklyVisitingHours(WeeklyVisitingHour weeklyVisitingHour) {
        this.weeklyVisitingHours.remove(weeklyVisitingHour);
        weeklyVisitingHour.setChamber(null);
        return this;
    }

    public void setWeeklyVisitingHours(Set<WeeklyVisitingHour> weeklyVisitingHours) {
        this.weeklyVisitingHours = weeklyVisitingHours;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Chamber chamber = (Chamber) o;
        if (chamber.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), chamber.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Chamber{" +
            "id=" + getId() +
            ", address='" + getAddress() + "'" +
            ", phone='" + getPhone() + "'" +
            ", fee=" + getFee() +
            "}";
    }

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}
}
