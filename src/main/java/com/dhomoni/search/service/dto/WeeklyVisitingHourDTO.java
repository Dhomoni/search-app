package com.dhomoni.search.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.dhomoni.search.domain.enumeration.WeekDay;

/**
 * A DTO for the WeeklyVisitingHour entity.
 */
public class WeeklyVisitingHourDTO implements Serializable {

    private Long id;

    private WeekDay weekDay;

    @Min(value = 0)
    @Max(value = 23)
    private Integer startHour;

    @Min(value = 0)
    @Max(value = 59)
    private Integer startMinute;

    @Min(value = 0)
    @Max(value = 23)
    private Integer endHour;

    @Min(value = 0)
    @Max(value = 59)
    private Integer endMinute;

    private Long chamberId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WeekDay getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(WeekDay weekDay) {
        this.weekDay = weekDay;
    }

    public Integer getStartHour() {
        return startHour;
    }

    public void setStartHour(Integer startHour) {
        this.startHour = startHour;
    }

    public Integer getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(Integer startMinute) {
        this.startMinute = startMinute;
    }

    public Integer getEndHour() {
        return endHour;
    }

    public void setEndHour(Integer endHour) {
        this.endHour = endHour;
    }

    public Integer getEndMinute() {
        return endMinute;
    }

    public void setEndMinute(Integer endMinute) {
        this.endMinute = endMinute;
    }

    public Long getChamberId() {
        return chamberId;
    }

    public void setChamberId(Long chamberId) {
        this.chamberId = chamberId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WeeklyVisitingHourDTO weeklyVisitingHourDTO = (WeeklyVisitingHourDTO) o;
        if (weeklyVisitingHourDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), weeklyVisitingHourDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WeeklyVisitingHourDTO{" +
            "id=" + getId() +
            ", weekDay='" + getWeekDay() + "'" +
            ", startHour=" + getStartHour() +
            ", startMinute=" + getStartMinute() +
            ", endHour=" + getEndHour() +
            ", endMinute=" + getEndMinute() +
            ", chamber=" + getChamberId() +
            "}";
    }
}
