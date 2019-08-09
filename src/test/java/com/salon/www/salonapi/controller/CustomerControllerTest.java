package com.salon.www.salonapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.salon.www.salonapi.model.Customer;
import com.salon.www.salonapi.model.NewUser;
import com.salon.www.salonapi.model.security.User;
import com.salon.www.salonapi.security.JwtTokenUtil;
import com.salon.www.salonapi.service.itf.CustomerService;
import com.salon.www.salonapi.service.itf.UserService;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Log4j2
@RunWith(SpringRunner.class)
@SpringBootTest
@WithMockUser(roles = "USER")
public class CustomerControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper mapper;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private CustomerService customerService;
    @MockBean
    private UserService userService;

    @Before
    @SneakyThrows
    public void setup() {

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());

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

    @Test
    public void createCustomer_shouldReturnCreated() throws Exception {
        NewUser newUser = new NewUser("First", "Last", "First.Last@test.com", "password", "testUser", "5555555555");
        User user = new User(newUser);
        Customer customer = new Customer(newUser);
        when(userService.getUser(any())).thenReturn(null, user);
        when(customerService.getCustomerByEmail(anyString())).thenReturn(null, customer);
        doNothing().when(userService).createUser(any());
        doNothing().when(customerService).createCustomer(any());
        String json = mapper.writeValueAsString(newUser);
        mockMvc.perform(post("/customers/new")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());
        then(customerService).should(times(1)).createCustomer(customer);
    }

    @Test
    public void createCustomer_shouldReturnBadRequest_withBadInput() throws Exception {
        NewUser newUser = new NewUser();
        String json = mapper.writeValueAsString(newUser);
        mockMvc.perform(post("/customers/new")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
        then(customerService).should(times(0)).createCustomer(any());
    }

    @Test
    public void updateCustomer_shouldReturnNoContent() throws Exception {
        Customer customer = new Customer(10L, 55L, "user", "name");
        given(customerService.getCustomer(anyLong())).willReturn(customer);
        doNothing().when(customerService).updateCustomer(customer);
        String json = mapper.writeValueAsString(customer);
        mockMvc.perform(put("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNoContent());
        then(customerService).should(times(1)).updateCustomer(customer);
    }

    @Test
    public void updateCustomer_shouldReturnBadRequest_withMissingId() throws Exception {
        Customer customer = new Customer(55L, "user", "name");
        given(customerService.getCustomer(anyLong())).willReturn(customer);
        doNothing().when(customerService).updateCustomer(customer);
        String json = mapper.writeValueAsString(customer);
        mockMvc.perform(put("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
        then(customerService).should(times(0)).updateCustomer(customer);
    }

    @Test
    public void updateCustomer_shouldReturnBadRequest_withBadInput() throws Exception {
        Customer customer = new Customer();
        String json = mapper.writeValueAsString(customer);
        mockMvc.perform(put("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
        then(customerService).should(times(0)).updateCustomer(customer);
    }
}
