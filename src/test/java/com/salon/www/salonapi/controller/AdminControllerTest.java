package com.salon.www.salonapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.salon.www.salonapi.model.Customer;
import com.salon.www.salonapi.model.Employee;
import com.salon.www.salonapi.security.JwtTokenUtil;
import com.salon.www.salonapi.service.CustomerService;
import com.salon.www.salonapi.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @MockBean
    private EmployeeService employeeService;

    @Test
    @WithMockUser(roles = "ADMIN")
    public void getCustomers_withAdminRole_shouldReturnListOfCustomers() throws Exception {
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
    @WithMockUser(roles = "ADMIN")
    public void getCustomers_withAdminRole_withNoCustomers_shouldReturn404() throws Exception {
        given(customerService.getCustomers()).willReturn(null);

        mockMvc.perform(get("/admin/customers"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getCustomers_withUserRole_shouldNotBeAccessible() throws Exception {
        mockMvc.perform(get("/admin/customers"))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void getCustomer_withAdminRole_shouldReturnCustomer() throws Exception {
        Customer customer = new Customer(10L, 55L, "user", "name");

        given(customerService.getCustomer(10L)).willReturn(customer);

        mockMvc.perform(get("/admin/customers/10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.userId").value(55L))
                .andExpect(jsonPath("$.firstName").value("user"))
                .andExpect(jsonPath("$.lastName").value("name"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void getCustomer_withAdminRole_withNoCustomer_shouldReturn404() throws Exception {
        given(customerService.getCustomer(anyLong())).willReturn(null);

        mockMvc.perform(get("/admin/customers/10"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getCustomer_withUserRole_shouldDenyAccess() throws Exception {
        mockMvc.perform(get("/admin/customers/10"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void getEmployees_withAdminRole_shouldReturnListOfEmployees() throws Exception {
        Employee first = new Employee(1L, "first", "employee");
        Employee second = new Employee(2L, "second", "technician");

        given(employeeService.getEmployees()).willReturn(Arrays.asList(first, second));

        mockMvc.perform(get("/admin/employees"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$[0].userId").value(1L))
                .andExpect(jsonPath("$[0].firstName").value("first"))
                .andExpect(jsonPath("$[0].lastName").value("employee"))
                .andExpect(jsonPath("$[1].userId").value(2L))
                .andExpect(jsonPath("$[1].firstName").value("second"))
                .andExpect(jsonPath("$[1].lastName").value("technician"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void getEmployees_withAdminRole_withNoEmployees_shouldReturn404() throws Exception {
        given(employeeService.getEmployees()).willReturn(null);

        mockMvc.perform(get("/admin/employees"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getEmployees_withUserRole_shouldNotBeAccessible() throws Exception {
        Employee first = new Employee(1L, "first", "employee");
        Employee second = new Employee(2L, "second", "technician");

        given(employeeService.getEmployees()).willReturn(Arrays.asList(first, second));

        mockMvc.perform(get("/admin/employees"))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void getEmployee_withAdminRole_shouldReturnEmployee() throws Exception {
        Employee employee = new Employee(10L, 45L, "first", "name");
        given(employeeService.getEmployee(anyLong())).willReturn(employee);

        mockMvc.perform(get("/admin/employees/10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.userId").value(45L))
                .andExpect(jsonPath("$.firstName").value("first"))
                .andExpect(jsonPath("$.lastName").value("name"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void getEmployee_withAdminRole_withNoEmployee_shouldReturn404() throws Exception {
        given(employeeService.getEmployee(anyLong())).willReturn(null);

        mockMvc.perform(get("/admin/employees/10"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getEmployee_withUserRole_shouldDenyAccess() throws Exception {
        mockMvc.perform(get("/admin/employees/10"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void createEmployee_withAdminRole_shouldReturnCreated() throws Exception {
        Employee employee = new Employee(1L, "first", "name");
        doNothing().when(employeeService).createEmployee(employee);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(employee);
        mockMvc.perform(post("/admin/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void createEmployee_withAdminRole_shouldReturnBadRequest_withBadInput() throws Exception {
        Employee employee = new Employee();
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(employee);
        mockMvc.perform(post("/admin/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void createEmployee_withUserRole_shouldDenyAccess() throws Exception {
        mockMvc.perform(post("/admin/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isForbidden());
    }

}
