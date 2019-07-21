package com.salon.www.salonapi.service.impl;

import com.salon.www.salonapi.model.EmployeeShift;
import com.salon.www.salonapi.service.itf.ShiftService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("shiftService")
public class ShiftServiceImpl implements ShiftService {
    @Override
    public void createShift(EmployeeShift shift) {
        // TODO: implement createShift method
    }

    @Override
    public EmployeeShift getShift(long shiftId) {
        // TODO: implement getShift method
        return null;
    }

    @Override
    public List<EmployeeShift> getShifts() {
        // TODO: implement getShifts method
        return null;
    }

    @Override
    public void updateShift(EmployeeShift shift) {
        // TODO: implement updateShift method
    }
}
