package com.salon.www.salonapi.service.itf;

import com.salon.www.salonapi.model.EmployeeShift;

import java.util.List;

public interface ShiftService {
    void createShift(EmployeeShift shift);

    EmployeeShift getShift(long shiftId);

    List<EmployeeShift> getShifts();

    void updateShift(EmployeeShift shift);
}
