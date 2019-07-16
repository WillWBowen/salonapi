package com.salon.www.salonapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    private long id;
    private long customerId;
    private long employeeId;
    private Timestamp bookingTime;
    private Timestamp endTime;

    public Booking(long customerId, long employeeId, Timestamp bookingTime, Timestamp endTime) {

        this.customerId = customerId;
        this.employeeId = employeeId;
        this.bookingTime = bookingTime;
        this.endTime = endTime;
    }

}
