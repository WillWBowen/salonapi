package com.salon.www.salonapi.controller;

import com.salon.www.salonapi.model.Booking;
import com.salon.www.salonapi.service.itf.BookingService;
import com.salon.www.salonapi.service.itf.CustomerService;
import com.salon.www.salonapi.service.itf.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@AllArgsConstructor
@RequestMapping("/booking")
public class BookingController {

    private BookingService bookingService;
    private CustomerService customerService;
    private EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<List<Booking>> getBookings() {
        List<Booking> bookings = bookingService.getBookings();
        if (bookings == null) return ResponseEntity.noContent().build();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(bookings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBooking(@PathVariable("id") Long bookingId) {
        Booking booking = bookingService.getBooking(bookingId);
        if(booking == null) return ResponseEntity.noContent().build();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(booking);
    }

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody Booking booking) {
        validate(booking);
        bookingService.createBooking(booking);
        return ResponseEntity.created(null).build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBooking(@PathVariable("id") Long bookingId) {
        bookingService.deleteBooking(bookingId);
        return ResponseEntity.noContent().build();
    }

    private void validate(Booking booking) {
        Objects.requireNonNull(customerService.getCustomer(booking.getCustomerId()));
        Objects.requireNonNull(employeeService.getEmployee(booking.getEmployeeId()));
        Objects.requireNonNull(booking.getBookingTime());
        Objects.requireNonNull(booking.getEndTime());
        employeeService.employeeIsAvailable(booking.getEmployeeId(), booking.getBookingTime(), booking.getEndTime());
        employeeService.employeeHasSkills(booking.getEmployeeId(), booking.getSkills());
        customerService.customerIsAvailable(booking.getCustomerId(), booking.getBookingTime(), booking.getEndTime());

    }

}
