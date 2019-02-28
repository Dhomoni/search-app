package com.dhomoni.search.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the Chamber entity. This class is used in ChamberResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /chambers?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ChamberCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter address;

    private StringFilter phone;

    private DoubleFilter fee;

    private BooleanFilter isSuspended;

    private StringFilter notice;

    private IntegerFilter appointmentLimit;

    private IntegerFilter adviceDurationInMinute;

    private LongFilter doctorId;

    private LongFilter weeklyVisitingHoursId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getAddress() {
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public DoubleFilter getFee() {
        return fee;
    }

    public void setFee(DoubleFilter fee) {
        this.fee = fee;
    }

    public BooleanFilter getIsSuspended() {
        return isSuspended;
    }

    public void setIsSuspended(BooleanFilter isSuspended) {
        this.isSuspended = isSuspended;
    }

    public StringFilter getNotice() {
        return notice;
    }

    public void setNotice(StringFilter notice) {
        this.notice = notice;
    }

    public IntegerFilter getAppointmentLimit() {
        return appointmentLimit;
    }

    public void setAppointmentLimit(IntegerFilter appointmentLimit) {
        this.appointmentLimit = appointmentLimit;
    }

    public IntegerFilter getAdviceDurationInMinute() {
        return adviceDurationInMinute;
    }

    public void setAdviceDurationInMinute(IntegerFilter adviceDurationInMinute) {
        this.adviceDurationInMinute = adviceDurationInMinute;
    }

    public LongFilter getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(LongFilter doctorId) {
        this.doctorId = doctorId;
    }

    public LongFilter getWeeklyVisitingHoursId() {
        return weeklyVisitingHoursId;
    }

    public void setWeeklyVisitingHoursId(LongFilter weeklyVisitingHoursId) {
        this.weeklyVisitingHoursId = weeklyVisitingHoursId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ChamberCriteria that = (ChamberCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(address, that.address) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(fee, that.fee) &&
            Objects.equals(isSuspended, that.isSuspended) &&
            Objects.equals(notice, that.notice) &&
            Objects.equals(appointmentLimit, that.appointmentLimit) &&
            Objects.equals(adviceDurationInMinute, that.adviceDurationInMinute) &&
            Objects.equals(doctorId, that.doctorId) &&
            Objects.equals(weeklyVisitingHoursId, that.weeklyVisitingHoursId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        address,
        phone,
        fee,
        isSuspended,
        notice,
        appointmentLimit,
        adviceDurationInMinute,
        doctorId,
        weeklyVisitingHoursId
        );
    }

    @Override
    public String toString() {
        return "ChamberCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (address != null ? "address=" + address + ", " : "") +
                (phone != null ? "phone=" + phone + ", " : "") +
                (fee != null ? "fee=" + fee + ", " : "") +
                (isSuspended != null ? "isSuspended=" + isSuspended + ", " : "") +
                (notice != null ? "notice=" + notice + ", " : "") +
                (appointmentLimit != null ? "appointmentLimit=" + appointmentLimit + ", " : "") +
                (adviceDurationInMinute != null ? "adviceDurationInMinute=" + adviceDurationInMinute + ", " : "") +
                (doctorId != null ? "doctorId=" + doctorId + ", " : "") +
                (weeklyVisitingHoursId != null ? "weeklyVisitingHoursId=" + weeklyVisitingHoursId + ", " : "") +
            "}";
    }

}
