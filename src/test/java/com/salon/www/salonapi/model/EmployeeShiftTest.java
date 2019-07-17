package com.salon.www.salonapi.model;

import com.salon.www.salonapi.exception.OutOfRangeException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)

public class EmployeeShiftTest {
    @Test(expected = OutOfRangeException.class)
    public void setDay_shouldThrowException_forIntLessThanOne() {
        EmployeeShift shift = new EmployeeShift();
        shift.setDay(0);
    }

    @Test(expected = OutOfRangeException.class)
    public void setDay_shouldThrowException_forIntGreaterThanSeven() {
        EmployeeShift shift = new EmployeeShift();
        shift.setDay(8);
    }

    @Test
    public void setDay_shouldSetDay_forEmployeeShift() {
        EmployeeShift shift = new EmployeeShift();
        shift.setDay(1);
        assertThat(shift.getDay()).isEqualTo(1);
        shift.setDay(7);
        assertThat(shift.getDay()).isEqualTo(7);
    }

}
