package com.salon.www.salonapi.controller;

import com.salon.www.salonapi.model.Customer;
import com.salon.www.salonapi.security.JwtTokenUtil;
import com.salon.www.salonapi.service.CustomerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminControllerTest {


    private MockMvc mockMvc;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @MockBean
    private CustomerService customerService;

    @Test
    @WithMockUser(roles = "ADMIN")
    public void getCustomers_shouldReturnListOfCustomers_withAdminRole() throws Exception {
        Customer first = new Customer(1L, "first", "customer");
        Customer second = new Customer(2L, "second", "client");

        given(customerService.getCustomers()).willReturn(Arrays.asList(first, second));

        mockMvc.perform(get("/admin/customers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$[0].userId").value(1L))
                .andExpect(jsonPath("$[0].firstName").value("first"))
                .andExpect(jsonPath("$[0].lastName").value("customer"))
                .andExpect(jsonPath("$[1].userId").value(2L))
                .andExpect(jsonPath("$[1].firstName").value("second"))
                .andExpect(jsonPath("$[1].lastName").value("client"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getCustomers_shouldNotBeAccessible_withUserRole() throws Exception {
        Customer first = new Customer(1L, "first", "customer");
        Customer second = new Customer(2L, "second", "client");

        given(customerService.getCustomers()).willReturn(Arrays.asList(first, second));

        mockMvc.perform(get("/admin/customers"))
                .andExpect(status().isForbidden());

    }
}
