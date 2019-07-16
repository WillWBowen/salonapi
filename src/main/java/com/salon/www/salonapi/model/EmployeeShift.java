package com.salon.www.salonapi.model;

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
    private String day;
    private LocalTime startTime;
    private LocalTime endTime;

    public EmployeeShift(Long employeeId, String day, LocalTime startTime, LocalTime endTime){

        this.employeeId = employeeId;
        this.day = day;
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
                day.equals(that.day) &&
                startTime.getHour() == that.startTime.getHour() &&
                startTime.getMinute() == that.startTime.getMinute() &&
                endTime.getHour() == that.endTime.getHour() &&
                endTime.getMinute() == that.endTime.getMinute();
    }
}
