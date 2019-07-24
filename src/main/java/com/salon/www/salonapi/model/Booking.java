package com.salon.www.salonapi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    private long id;
    private long customerId;
    private long employeeId;
    private Timestamp bookingTime;
    private Timestamp endTime;
    private List<Skill> skills;

    public Booking(long customerId, long employeeId, Timestamp bookingTime, Timestamp endTime, List<Skill> skills) {

        this.customerId = customerId;
        this.employeeId = employeeId;
        this.bookingTime = bookingTime;
        this.endTime = endTime;
        this.skills = skills;
    }

}
