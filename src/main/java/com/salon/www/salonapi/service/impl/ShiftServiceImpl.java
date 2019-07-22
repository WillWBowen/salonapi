package com.salon.www.salonapi.service.impl;

import com.salon.www.salonapi.dao.itf.EmployeeShiftDAO;
import com.salon.www.salonapi.exception.EmployeeShiftNotFoundException;
import com.salon.www.salonapi.model.EmployeeShift;
import com.salon.www.salonapi.service.itf.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service("shiftService")
@Transactional
public class ShiftServiceImpl implements ShiftService {

    private EmployeeShiftDAO shiftDao;

    @Autowired
    public ShiftServiceImpl(EmployeeShiftDAO shiftDao) {
        this.shiftDao = shiftDao;
    }

    @Override
    public void createShift(EmployeeShift shift) {
        shiftDao.save(shift);
    }

    @Override
    public EmployeeShift getShift(long shiftId) {
        return shiftDao.get(shiftId).orElse(null);
    }

    @Override
    public List<EmployeeShift> getShifts() {
        return shiftDao.getAll();
    }

    @Override
    public void updateShift(EmployeeShift shift) {
        //verify object exists
        Optional<EmployeeShift> oldShift = shiftDao.get(shift.getId());
        oldShift.orElseThrow(EmployeeShiftNotFoundException::new);
        shiftDao.update(shift);
    }
}
