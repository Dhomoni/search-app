package com.dhomoni.search.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

import com.dhomoni.search.domain.enumeration.WeekDay;

/**
 * A WeeklyVisitingHours.
 */
@Entity
@Table(name = "weekly_visiting_hours")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "weeklyvisitinghours")
public class WeeklyVisitingHours implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "week_day")
    private WeekDay weekDay;

    @Min(value = 0)
    @Max(value = 23)
    @Column(name = "start_hour")
    private Integer startHour;

    @Min(value = 0)
    @Max(value = 59)
    @Column(name = "start_minute")
    private Integer startMinute;

    @Min(value = 0)
    @Max(value = 23)
    @Column(name = "end_hour")
    private Integer endHour;

    @Min(value = 0)
    @Max(value = 59)
    @Column(name = "end_minute")
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

    public WeeklyVisitingHours weekDay(WeekDay weekDay) {
        this.weekDay = weekDay;
        return this;
    }

    public void setWeekDay(WeekDay weekDay) {
        this.weekDay = weekDay;
    }

    public Integer getStartHour() {
        return startHour;
    }

    public WeeklyVisitingHours startHour(Integer startHour) {
        this.startHour = startHour;
        return this;
    }

    public void setStartHour(Integer startHour) {
        this.startHour = startHour;
    }

    public Integer getStartMinute() {
        return startMinute;
    }

    public WeeklyVisitingHours startMinute(Integer startMinute) {
        this.startMinute = startMinute;
        return this;
    }

    public void setStartMinute(Integer startMinute) {
        this.startMinute = startMinute;
    }

    public Integer getEndHour() {
        return endHour;
    }

    public WeeklyVisitingHours endHour(Integer endHour) {
        this.endHour = endHour;
        return this;
    }

    public void setEndHour(Integer endHour) {
        this.endHour = endHour;
    }

    public Integer getEndMinute() {
        return endMinute;
    }

    public WeeklyVisitingHours endMinute(Integer endMinute) {
        this.endMinute = endMinute;
        return this;
    }

    public void setEndMinute(Integer endMinute) {
        this.endMinute = endMinute;
    }

    public Chamber getChamber() {
        return chamber;
    }

    public WeeklyVisitingHours chamber(Chamber chamber) {
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
        WeeklyVisitingHours weeklyVisitingHours = (WeeklyVisitingHours) o;
        if (weeklyVisitingHours.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), weeklyVisitingHours.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WeeklyVisitingHours{" +
            "id=" + getId() +
            ", weekDay='" + getWeekDay() + "'" +
            ", startHour=" + getStartHour() +
            ", startMinute=" + getStartMinute() +
            ", endHour=" + getEndHour() +
            ", endMinute=" + getEndMinute() +
            "}";
    }
}
