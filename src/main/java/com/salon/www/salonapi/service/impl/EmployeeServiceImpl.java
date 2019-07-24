package com.salon.www.salonapi.service.impl;

import com.salon.www.salonapi.dao.itf.EmployeeDAO;
import com.salon.www.salonapi.dao.itf.SkillDAO;
import com.salon.www.salonapi.exception.EmployeeNotFoundException;
import com.salon.www.salonapi.model.Booking;
import com.salon.www.salonapi.model.Employee;
import com.salon.www.salonapi.model.EmployeeShift;
import com.salon.www.salonapi.model.Skill;
import com.salon.www.salonapi.service.itf.BookingService;
import com.salon.www.salonapi.service.itf.EmployeeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

@Service("employeeService")
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeDAO employeeDAO;
    private SkillDAO skillDAO;
    private BookingService bookingService;

    public EmployeeServiceImpl(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    public List<Employee> getEmployees() {
        return employeeDAO.getAll();
    }

    public Employee getEmployee(Long id) {
        return employeeDAO.get(id).orElse(null);
    }

    public void createEmployee(Employee employee) {
        employeeDAO.save(employee);
    }

    public void updateEmployee(Employee employee) {
        employeeDAO.get(employee.getId()).orElseThrow(EmployeeNotFoundException::new);
        employeeDAO.update(employee);
    }

    public EmployeeShift getEmployeeShiftForDay(Long employeeId, int day) {
        return employeeDAO.getShiftForDay(employeeId, day).orElse(null);
    }

    public List<Booking> getEmployeeBookingsForDate(Long employeeId, Timestamp date) {
        return employeeDAO.getBookingsForDate(employeeId, date);
    }

    @Override
    public Boolean employeeIsAvailable(long employeeId, Timestamp bookingTime, Timestamp endTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(bookingTime.getTime());
        int day = cal.get(Calendar.DAY_OF_WEEK);

        // Make sure that booking is not outside employees shift
        EmployeeShift shift = getEmployeeShiftForDay(employeeId, day);
        if(shift == null) return false;
        // set calendar to beginning of employees shift
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), shift.getStartTime().getHour(), shift.getStartTime().getMinute());
        if(bookingTime.before(cal.getTime())) return false;
        // set calendar to end of employees shift
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), shift.getEndTime().getHour(), shift.getEndTime().getMinute());
        if(endTime.after(cal.getTime())) return false;

        // Check no other bookings overlap this booking
        List<Booking> bookings = getEmployeeBookingsForDate(employeeId, bookingTime);
        return !bookingService.bookingTimeHasConflict(bookings, bookingTime, endTime);
    }

    @Override
    public List<Skill> getEmployeeSkills(long employeeId) {
        return skillDAO.getForEmployee(employeeId);
    }

    @Override
    public Boolean employeeHasSkills(long employeeId, List<Skill> skills) {

        List<Skill> employeeSkills = getEmployeeSkills(employeeId);
        return employeeSkills.containsAll(skills);
    }
}
