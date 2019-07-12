package com.salon.www.salonapi.model;

import lombok.Data;

import java.util.Date;

@Data
public class Booking {
    private long id;
    private long customerid;
    private long employeeid;
    private Date bookingTime;
    private Date endTime;
}
