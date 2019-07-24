package com.salon.www.salonapi.dao.itf;

import com.salon.www.salonapi.model.Booking;

import java.sql.Timestamp;
import java.util.List;

public interface BookingDAO extends Dao<Booking> {
    List<Booking> getForRange(Timestamp start, Timestamp end);
}
