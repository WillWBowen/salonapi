package com.salon.www.salonapi.model;

import com.salon.www.salonapi.exception.OutOfRangeException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeShift {
    private Long id;
    private Long employeeId;
    private int day;  // This number represents a Calendar.DAY and should be between 1(Sunday) and 7(Saturday)
    private LocalTime startTime;
    private LocalTime endTime;

    public EmployeeShift(Long employeeId, int day, LocalTime startTime, LocalTime endTime){

        // calling the setter to check that day is valid
        this.setDay(day);

        this.employeeId = employeeId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeShift that = (EmployeeShift) o;
        return Objects.equals(id, that.id) &&
                employeeId.equals(that.employeeId) &&
                day == that.day &&
                startTime.getHour() == that.startTime.getHour() &&
                startTime.getMinute() == that.startTime.getMinute() &&
                endTime.getHour() == that.endTime.getHour() &&
                endTime.getMinute() == that.endTime.getMinute();
    }

    public void setDay(int day) {
        if(day < 1 || day > 7) {
            throw new OutOfRangeException("Invalid value for day: " + day + ". Value must be between 1 (Sunday) and 7 (Saturday)");
        }
        this.day = day;
    }
}
