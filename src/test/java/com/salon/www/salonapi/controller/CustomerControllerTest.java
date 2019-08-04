package com.salon.www.salonapi.controller;

import com.salon.www.salonapi.model.Customer;
import com.salon.www.salonapi.security.JwtTokenUtil;
import com.salon.www.salonapi.service.itf.CustomerService;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Log4j2
@RunWith(SpringRunner.class)
@SpringBootTest
@WithMockUser(roles = "USER")
public class CustomerControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private CustomerService customerService;

    @Before
    @SneakyThrows
    public void setup() {

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

    }

    @Test
    public void getCustomers_shouldReturnListOfCustomers() throws Exception {
        Customer first = new Customer(1L, "first", "customer");
        Customer second = new Customer(2L, "second", "client");

        given(customerService.getCustomers()).willReturn(Arrays.asList(first, second));

        mockMvc.perform(get("/customers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$[0].userId").value(1L))
                .andExpect(jsonPath("$[0].firstName").value("first"))
                .andExpect(jsonPath("$[0].lastName").value("customer"))
                .andExpect(jsonPath("$[1].userId").value(2L))
                .andExpect(jsonPath("$[1].firstName").value("second"))
                .andExpect(jsonPath("$[1].lastName").value("client"));
        then(customerService).should(times(1)).getCustomers();
    }

    @Test
    public void getCustomers_withNoCustomers_shouldReturnNoContent() throws Exception {
        given(customerService.getCustomers()).willReturn(null);

        mockMvc.perform(get("/customers"))
                .andExpect(status().isNoContent());
        then(customerService).should(times(1)).getCustomers();
    }

    @Test
    public void getCustomer_shouldReturnCustomer() throws Exception {
        Customer customer = new Customer(10L, 55L, "user", "name");

        given(customerService.getCustomer(anyLong())).willReturn(customer);

        mockMvc.perform(get("/customers/10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.userId").value(55L))
                .andExpect(jsonPath("$.firstName").value("user"))
                .andExpect(jsonPath("$.lastName").value("name"));
        then(customerService).should(times(1)).getCustomer(10L);
    }

    @Test
    public void getCustomer_withNoCustomer_shouldReturnNoContent() throws Exception {
        given(customerService.getCustomer(anyLong())).willReturn(null);

        mockMvc.perform(get("/customers/10"))
                .andExpect(status().isNoContent());
        then(customerService).should(times(1)).getCustomer(10L);
    }
}
