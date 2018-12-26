package com.dhomoni.search.service.dto;

import java.io.Serializable;
import java.util.Objects;
import com.dhomoni.search.domain.enumeration.WeekDay;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the WeeklyVisitingHours entity. This class is used in WeeklyVisitingHoursResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /weekly-visiting-hours?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class WeeklyVisitingHoursCriteria implements Serializable {
    /**
     * Class for filtering WeekDay
     */
    public static class WeekDayFilter extends Filter<WeekDay> {
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private WeekDayFilter weekDay;

    private IntegerFilter startHour;

    private IntegerFilter startMinute;

    private IntegerFilter endHour;

    private IntegerFilter endMinute;

    private LongFilter chamberId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public WeekDayFilter getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(WeekDayFilter weekDay) {
        this.weekDay = weekDay;
    }

    public IntegerFilter getStartHour() {
        return startHour;
    }

    public void setStartHour(IntegerFilter startHour) {
        this.startHour = startHour;
    }

    public IntegerFilter getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(IntegerFilter startMinute) {
        this.startMinute = startMinute;
    }

    public IntegerFilter getEndHour() {
        return endHour;
    }

    public void setEndHour(IntegerFilter endHour) {
        this.endHour = endHour;
    }

    public IntegerFilter getEndMinute() {
        return endMinute;
    }

    public void setEndMinute(IntegerFilter endMinute) {
        this.endMinute = endMinute;
    }

    public LongFilter getChamberId() {
        return chamberId;
    }

    public void setChamberId(LongFilter chamberId) {
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
        final WeeklyVisitingHoursCriteria that = (WeeklyVisitingHoursCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(weekDay, that.weekDay) &&
            Objects.equals(startHour, that.startHour) &&
            Objects.equals(startMinute, that.startMinute) &&
            Objects.equals(endHour, that.endHour) &&
            Objects.equals(endMinute, that.endMinute) &&
            Objects.equals(chamberId, that.chamberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        weekDay,
        startHour,
        startMinute,
        endHour,
        endMinute,
        chamberId
        );
    }

    @Override
    public String toString() {
        return "WeeklyVisitingHoursCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (weekDay != null ? "weekDay=" + weekDay + ", " : "") +
                (startHour != null ? "startHour=" + startHour + ", " : "") +
                (startMinute != null ? "startMinute=" + startMinute + ", " : "") +
                (endHour != null ? "endHour=" + endHour + ", " : "") +
                (endMinute != null ? "endMinute=" + endMinute + ", " : "") +
                (chamberId != null ? "chamberId=" + chamberId + ", " : "") +
            "}";
    }

}
