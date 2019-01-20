package com.dhomoni.search.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.dhomoni.search.domain.enumeration.WeekDay;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A WeeklyVisitingHour.
 */
@Entity
@Table(name = "weekly_visiting_hour")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class WeeklyVisitingHour implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Field(type = FieldType.Long)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "week_day")
    private WeekDay weekDay;

    @Min(value = 0)
    @Max(value = 23)
    @Column(name = "start_hour")
    @Field(type = FieldType.Integer, index = false)
    private Integer startHour;

    @Min(value = 0)
    @Max(value = 59)
    @Column(name = "start_minute")
    @Field(type = FieldType.Integer, index = false)
    private Integer startMinute;

    @Min(value = 0)
    @Max(value = 23)
    @Column(name = "end_hour")
    @Field(type = FieldType.Integer, index = false)
    private Integer endHour;

    @Min(value = 0)
    @Max(value = 59)
    @Column(name = "end_minute")
    @Field(type = FieldType.Integer, index = false)
    private Integer endMinute;

    @ManyToOne
    @JsonIgnoreProperties("weeklyVisitingHours")
    private Chamber chamber;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WeekDay getWeekDay() {
        return weekDay;
    }

    public WeeklyVisitingHour weekDay(WeekDay weekDay) {
        this.weekDay = weekDay;
        return this;
    }

    public void setWeekDay(WeekDay weekDay) {
        this.weekDay = weekDay;
    }

    public Integer getStartHour() {
        return startHour;
    }

    public WeeklyVisitingHour startHour(Integer startHour) {
        this.startHour = startHour;
        return this;
    }

    public void setStartHour(Integer startHour) {
        this.startHour = startHour;
    }

    public Integer getStartMinute() {
        return startMinute;
    }

    public WeeklyVisitingHour startMinute(Integer startMinute) {
        this.startMinute = startMinute;
        return this;
    }

    public void setStartMinute(Integer startMinute) {
        this.startMinute = startMinute;
    }

    public Integer getEndHour() {
        return endHour;
    }

    public WeeklyVisitingHour endHour(Integer endHour) {
        this.endHour = endHour;
        return this;
    }

    public void setEndHour(Integer endHour) {
        this.endHour = endHour;
    }

    public Integer getEndMinute() {
        return endMinute;
    }

    public WeeklyVisitingHour endMinute(Integer endMinute) {
        this.endMinute = endMinute;
        return this;
    }

    public void setEndMinute(Integer endMinute) {
        this.endMinute = endMinute;
    }

    public Chamber getChamber() {
        return chamber;
    }

    public WeeklyVisitingHour chamber(Chamber chamber) {
        this.chamber = chamber;
        return this;
    }

    public void setChamber(Chamber chamber) {
        this.chamber = chamber;
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
        WeeklyVisitingHour weeklyVisitingHour = (WeeklyVisitingHour) o;
        if (weeklyVisitingHour.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), weeklyVisitingHour.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WeeklyVisitingHour{" +
            "id=" + getId() +
            ", weekDay='" + getWeekDay() + "'" +
            ", startHour=" + getStartHour() +
            ", startMinute=" + getStartMinute() +
            ", endHour=" + getEndHour() +
            ", endMinute=" + getEndMinute() +
            "}";
    }
}
