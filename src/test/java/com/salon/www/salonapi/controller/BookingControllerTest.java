package com.salon.www.salonapi.controller;

import com.salon.www.salonapi.model.Booking;
import com.salon.www.salonapi.security.JwtTokenUtil;
import com.salon.www.salonapi.service.itf.BookingService;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Log4j2
@RunWith(SpringRunner.class)
@SpringBootTest
@WithMockUser(roles = "USER")
public class BookingControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private BookingService bookingService;

    private static final Timestamp START_1 = new Timestamp(new GregorianCalendar(2019, Calendar.AUGUST, 30, 11, 0).getTimeInMillis());
    private static final Timestamp END_1 = new Timestamp(new GregorianCalendar(2019, Calendar.AUGUST, 30, 12, 0).getTimeInMillis());
    private static final Timestamp START_2 = new Timestamp(new GregorianCalendar(2019, Calendar.SEPTEMBER, 30, 9, 30).getTimeInMillis());
    private static final Timestamp END_2 = new Timestamp(new GregorianCalendar(2019, Calendar.SEPTEMBER, 30, 11, 15).getTimeInMillis());

    @Before
    @SneakyThrows
    public void setup() {

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

    }

    @Test
    public void getBookings_shouldReturnListOfBookingObjects() throws Exception {
        Booking first = new Booking(4L,1L, 4L, START_1, END_1, null);
        Booking second = new Booking(2L, 35L, 32L, START_2, END_2, null);

        given(bookingService.getBookings()).willReturn(Arrays.asList(first, second));

        mockMvc.perform(get("/booking"))
                .andExpect(status().isOk());


        then(bookingService).should(times(1)).getBookings();
    }

    @Test
    public void getBooking_shouldReturnOneBooking() {
        Booking first = new Booking(4L,1L, 4L, START_1, END_1, null);
    }
}
