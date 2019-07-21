package com.salon.www.salonapi.service.impl;

import com.salon.www.salonapi.model.EmployeeShift;
import com.salon.www.salonapi.service.itf.ShiftService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("shiftService")
public class ShiftServiceImpl implements ShiftService {
    @Override
    public void createShift(EmployeeShift shift) {

    }

    @Override
    public EmployeeShift getShift(long shiftId) {
        return null;
    }

    @Override
    public List<EmployeeShift> getShifts() {
        return null;
    }

    @Override
    public void updateShift(EmployeeShift shift) {

    }
}
