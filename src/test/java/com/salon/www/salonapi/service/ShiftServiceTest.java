package com.salon.www.salonapi.service;

import com.salon.www.salonapi.dao.itf.EmployeeShiftDAO;
import com.salon.www.salonapi.exception.EmployeeCreationFailedException;
import com.salon.www.salonapi.exception.EmployeeShiftCreationFailedException;
import com.salon.www.salonapi.exception.EmployeeShiftNotFoundException;
import com.salon.www.salonapi.model.EmployeeShift;
import com.salon.www.salonapi.service.impl.ShiftServiceImpl;
import com.salon.www.salonapi.service.itf.ShiftService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ShiftServiceTest {

    private ShiftService shiftService;

    @Mock
    private EmployeeShiftDAO shiftDao;

    @Before
    public void setUp() {
        this.shiftService = new ShiftServiceImpl(shiftDao);
    }

    @Test
    public void getShifts_should_returnNull_inNoShiftsInDatabase() {
        List<EmployeeShift> shifts = shiftService.getShifts();
        then(shiftDao).should(times(1)).getAll();
        assertThat(shifts).isNullOrEmpty();
    }

    @Test
    public void getShifts_shouldReturnListOfShifts_forNonEmptyDatabase() {
        EmployeeShift first = new EmployeeShift(1L, 1L, Calendar.THURSDAY, null, null);
        EmployeeShift second = new EmployeeShift(2L, 1L, Calendar.MONDAY, null, null);

        given(shiftDao.getAll()).willReturn(Arrays.asList(first, second));

        List<EmployeeShift> shifts = shiftService.getShifts();

        then(shiftDao).should(times(1)).getAll();
        assertThat(shifts).contains(first);
        assertThat(shifts).contains(second);
    }

    @Test
    public void updateShift_shouldVerifyThatShift_exists() {
        EmployeeShift shift = new EmployeeShift();
        shift.setId(10L);
        given(shiftDao.get(anyLong())).willReturn(Optional.of(shift));
        shiftService.updateShift(shift);
        then(shiftDao).should(times(1)).get(10L);
    }

    @Test
    public void updateShift_shouldCallDAOUpdate_ifShiftExists() {
        EmployeeShift shift = new EmployeeShift(1L, 1L, Calendar.MONDAY, null, null);
        given(shiftDao.get(1L)).willReturn(Optional.of(shift));
        doNothing().when(shiftDao).update(shift);

        shiftService.updateShift(shift);

        then(shiftDao).should(times(1)).update(shift);
    }

    @Test(expected = EmployeeShiftNotFoundException.class)
    public void updateShift_shouldThrowError_ifShiftDoesNotExist() {
        EmployeeShift shift = new EmployeeShift();
        shift.setId(1L);
        given(shiftDao.get(1L)).willReturn(Optional.empty());

        shiftService.updateShift(shift);
        then(shiftDao).should(times(1)).get(1L);
        then(shiftDao).should(times(0)).update(shift);
    }

    @Test
    public void getShift_returnsNull_forEmptyOptional() {
        given(shiftDao.get(anyLong())).willReturn(Optional.empty());

        EmployeeShift shift = shiftService.getShift(14L);

        then(shiftDao).should(times(1)).get(14L);
        assertThat(shift).isNull();
    }

    @Test
    public void getShift_returnsShift_forNonEmptyOptional() {
        EmployeeShift shift = new EmployeeShift(14L, 12L, Calendar.WEDNESDAY, null, null);
        given(shiftDao.get(anyLong())).willReturn(Optional.of(shift));

        EmployeeShift storedShift = shiftService.getShift(14L);

        then(shiftDao).should(times(1)).get(14L);
        assertThat(storedShift).isNotNull();
        assertThat(storedShift).isEqualTo(shift);
    }

    @Test
    public void createShift_callsDAOSaveMethod() {
        EmployeeShift shift = new EmployeeShift(12L, Calendar.WEDNESDAY, null, null);
        doNothing().when(shiftDao).save(any());

        shiftService.createShift(shift);
        then(shiftDao).should(times(1)).save(shift);
    }

    @Test(expected = EmployeeShiftCreationFailedException.class)
    public void createShift_shouldThrowEmployeeShiftCreationFailedException() {
        EmployeeShift shift = new EmployeeShift(12L, Calendar.WEDNESDAY, null, null);
        doThrow(EmployeeShiftCreationFailedException.class).when(shiftDao).save(any());

        shiftService.createShift(shift);
    }


}
