package com.salon.www.salonapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.salon.www.salonapi.model.Customer;
import com.salon.www.salonapi.model.Employee;
import com.salon.www.salonapi.model.EmployeeShift;
import com.salon.www.salonapi.model.Skill;
import com.salon.www.salonapi.security.JwtTokenUtil;
import com.salon.www.salonapi.service.itf.CustomerService;
import com.salon.www.salonapi.service.itf.EmployeeService;
import com.salon.www.salonapi.service.itf.ShiftService;
import com.salon.www.salonapi.service.itf.SkillService;
import lombok.extern.log4j.Log4j2;
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

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Calendar;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Log4j2
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

    @MockBean
    private SkillService skillService;

    @MockBean
    private ShiftService shiftService;

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
        then(customerService).should(times(1)).getCustomers();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void getCustomers_withAdminRole_withNoCustomers_shouldReturnNoContent() throws Exception {
        given(customerService.getCustomers()).willReturn(null);

        mockMvc.perform(get("/admin/customers"))
                .andExpect(status().isNoContent());
        then(customerService).should(times(1)).getCustomers();
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getCustomers_withUserRole_shouldNotBeAccessible() throws Exception {
        mockMvc.perform(get("/admin/customers"))
                .andExpect(status().isForbidden());

        then(customerService).should(times(0)).getCustomers();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void getCustomer_withAdminRole_shouldReturnCustomer() throws Exception {
        Customer customer = new Customer(10L, 55L, "user", "name");

        given(customerService.getCustomer(anyLong())).willReturn(customer);

        mockMvc.perform(get("/admin/customers/10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.userId").value(55L))
                .andExpect(jsonPath("$.firstName").value("user"))
                .andExpect(jsonPath("$.lastName").value("name"));
        then(customerService).should(times(1)).getCustomer(10L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void getCustomer_withAdminRole_withNoCustomer_shouldReturnNoContent() throws Exception {
        given(customerService.getCustomer(anyLong())).willReturn(null);

        mockMvc.perform(get("/admin/customers/10"))
                .andExpect(status().isNoContent());
        then(customerService).should(times(1)).getCustomer(10L);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getCustomer_withUserRole_shouldDenyAccess() throws Exception {
        mockMvc.perform(get("/admin/customers/10"))
                .andExpect(status().isForbidden());
        then(customerService).should(times(0)).getCustomer(10L);
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
        then(employeeService).should(times(1)).getEmployees();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void getEmployees_withAdminRole_withNoEmployees_shouldReturnNoContent() throws Exception {
        given(employeeService.getEmployees()).willReturn(null);

        mockMvc.perform(get("/admin/employees"))
                .andExpect(status().isNoContent());
        then(employeeService).should(times(1)).getEmployees();
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getEmployees_withUserRole_shouldNotBeAccessible() throws Exception {
        Employee first = new Employee(1L, "first", "employee");
        Employee second = new Employee(2L, "second", "technician");

        given(employeeService.getEmployees()).willReturn(Arrays.asList(first, second));

        mockMvc.perform(get("/admin/employees"))
                .andExpect(status().isForbidden());
        then(employeeService).should(times(0)).getEmployees();

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

        then(employeeService).should(times(1)).getEmployee(10L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void getEmployee_withAdminRole_withNoEmployee_shouldReturnNoContent() throws Exception {
        given(employeeService.getEmployee(anyLong())).willReturn(null);

        mockMvc.perform(get("/admin/employees/10"))
                .andExpect(status().isNoContent());
        then(employeeService).should(times(1)).getEmployee(10L);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getEmployee_withUserRole_shouldDenyAccess() throws Exception {
        mockMvc.perform(get("/admin/employees/10"))
                .andExpect(status().isForbidden());
        then(employeeService).should(times(0)).getEmployee(10L);
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
        then(employeeService).should(times(1)).createEmployee(employee);
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
        then(employeeService).should(times(0)).createEmployee(employee);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void createEmployee_withUserRole_shouldDenyAccess() throws Exception {
        mockMvc.perform(post("/admin/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isForbidden());
        then(employeeService).should(times(0)).createEmployee(any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void updateEmployee_withAdminRole_shouldReturnNoContent() throws Exception {
        Employee employee = new Employee(1L, "first", "name");
        doNothing().when(employeeService).updateEmployee(employee);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(employee);
        mockMvc.perform(put("/admin/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNoContent());
        then(employeeService).should(times(1)).updateEmployee(employee);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void updateEmployee_withAdminRole_shouldReturnBadRequest_withBadInput() throws Exception {
        Employee employee = new Employee();
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(employee);
        mockMvc.perform(put("/admin/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
        then(employeeService).should(times(0)).updateEmployee(employee);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void updateEmployee_withUserRole_shouldDenyAccess() throws Exception {
        mockMvc.perform(put("/admin/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isForbidden());
        then(employeeService).should(times(0)).updateEmployee(any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void createSkill_withAdminRole_shouldReturnCreated() throws Exception {
        Skill skill = new Skill("Manicure", 20);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(skill);
        doNothing().when(skillService).createSkill(skill);
        mockMvc.perform(post("/admin/skills")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());
        then(skillService).should(times(1)).createSkill(skill);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void createSkill_withAdminRole_shouldReturnBadRequest_withBadInput() throws Exception {
        Skill skill = new Skill();
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(skill);
        mockMvc.perform(post("/admin/skills")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
        then(skillService).should(times(0)).createSkill(skill);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void createSkill_withUserRole_shouldDenyAccess() throws Exception {
        mockMvc.perform(post("/admin/skills")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isForbidden());

        then(skillService).should(times(0)).createSkill(any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void getSkill_withAdminRole_shouldReturnSkill() throws Exception {
        Skill skill = new Skill(12L, "manicure", 20);
        given(skillService.getSkill(anyLong())).willReturn(skill);
        mockMvc.perform(get("/admin/skills/12")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(12L))
                .andExpect(jsonPath("$.name").value("manicure"))
                .andExpect(jsonPath("$.price").value(20));
        then(skillService).should(times(1)).getSkill(12L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void getSkill_withAdminRole_withNoCustomer_shouldReturnNoContent() throws Exception {
        given(skillService.getSkill(anyLong())).willReturn(null);

        mockMvc.perform(get("/admin/skills/10"))
                .andExpect(status().isNoContent());
        then(skillService).should(times(1)).getSkill(10L);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getSkill_withUserRole_shouldDenyAccess() throws Exception {
        mockMvc.perform(get("/admin/skills/10"))
                .andExpect(status().isForbidden());
        then(skillService).should(times(0)).getSkill(anyLong());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void getSkills_withAdminRole_shouldReturnListOfSkills() throws Exception {
        Skill first = new Skill(10L, "manicure", 20);
        Skill second = new Skill(12L, "pedicure", 30);

        given(skillService.getSkills()).willReturn(Arrays.asList(first, second));

        mockMvc.perform(get("/admin/skills"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$[0].id").value(10L))
                .andExpect(jsonPath("$[0].name").value("manicure"))
                .andExpect(jsonPath("$[0].price").value(20))
                .andExpect(jsonPath("$[1].id").value(12L))
                .andExpect(jsonPath("$[1].name").value("pedicure"))
                .andExpect(jsonPath("$[1].price").value(30));
        then(skillService).should(times(1)).getSkills();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void getSkills_withAdminRole_withNoSkills_shouldReturnNoContent() throws Exception {
        given(skillService.getSkills()).willReturn(null);

        mockMvc.perform(get("/admin/skills"))
                .andExpect(status().isNoContent());
        then(skillService).should(times(1)).getSkills();
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getSkills_withUserRole_shouldNotBeAccessible() throws Exception {
        mockMvc.perform(get("/admin/skills"))
                .andExpect(status().isForbidden());
        then(skillService).should(times(0)).getSkills();

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void updateSkill_withAdminRole_shouldReturnNoContent() throws Exception {
        Skill skill = new Skill(2L, "pedicure", 35);
        doNothing().when(skillService).updateSkill(skill);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(skill);
        mockMvc.perform(put("/admin/skills")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNoContent());
        then(skillService).should(times(1)).updateSkill(skill);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void updateSkill_withAdminRole_shouldReturnBadRequest_withBadInput() throws Exception {
        Skill skill = new Skill();
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(skill);
        mockMvc.perform(put("/admin/skills")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
        then(skillService).should(times(0)).updateSkill(skill);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void updateSkill_withUserRole_shouldDenyAccess() throws Exception {
        mockMvc.perform(put("/admin/skills")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isForbidden());
        then(skillService).should(times(0)).updateSkill(any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void createShift_withAdminRole_shouldReturnCreated() throws Exception {
        EmployeeShift shift = new EmployeeShift(1L, Calendar.THURSDAY, LocalTime.of(9, 0), LocalTime.of(17,0));
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(shift);

        log.info(json);
        doNothing().when(shiftService).createShift(shift);
        given(employeeService.getEmployee(anyLong())).willReturn(new Employee(1L, 1L, "employee", "name"));

        mockMvc.perform(post("/admin/shifts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());
        then(shiftService).should(times(1)).createShift(shift);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void createShift_withAdminRole_shouldReturnBadRequest_withBadInput() throws Exception {
        EmployeeShift shift = new EmployeeShift();
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(shift);
        mockMvc.perform(post("/admin/shifts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
        then(shiftService).should(times(0)).createShift(shift);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void createShift_withUserRole_shouldDenyAccess() throws Exception {
        mockMvc.perform(post("/admin/shifts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isForbidden());

        then(shiftService).should(times(0)).createShift(any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void getShift_withAdminRole_shouldReturnShift() throws Exception {
        EmployeeShift shift = new EmployeeShift(12L, 5L, Calendar.MONDAY, LocalTime.of(9, 0), LocalTime.of(5, 0));
        given(shiftService.getShift(anyLong())).willReturn(shift);
        mockMvc.perform(get("/admin/shifts/12")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(12L))
                .andExpect(jsonPath("$.employeeId").value(5L))
                .andExpect(jsonPath("$.day").value(Calendar.MONDAY));
        then(shiftService).should(times(1)).getShift(12L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void getShift_withAdminRole_withNoCustomer_shouldReturnNoContent() throws Exception {
        given(shiftService.getShift(anyLong())).willReturn(null);

        mockMvc.perform(get("/admin/shifts/10"))
                .andExpect(status().isNoContent());
        then(shiftService).should(times(1)).getShift(10L);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getShift_withUserRole_shouldDenyAccess() throws Exception {
        mockMvc.perform(get("/admin/shifts/10"))
                .andExpect(status().isForbidden());
        then(shiftService).should(times(0)).getShift(anyLong());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void getShifts_withAdminRole_shouldReturnListOfShifts() throws Exception {
        EmployeeShift first = new EmployeeShift(10L, Calendar.MONDAY, LocalTime.of(9, 0), LocalTime.of(5, 0));
        EmployeeShift second = new EmployeeShift(12L, Calendar.THURSDAY, LocalTime.of(8, 0), LocalTime.of(4, 0));

        given(shiftService.getShifts()).willReturn(Arrays.asList(first, second));

        mockMvc.perform(get("/admin/shifts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$[0].employeeId").value(10L))
                .andExpect(jsonPath("$[1].employeeId").value(12L));
        then(shiftService).should(times(1)).getShifts();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void getShifts_withAdminRole_withNoShifts_shouldReturnNoContent() throws Exception {
        given(shiftService.getShifts()).willReturn(null);

        mockMvc.perform(get("/admin/shifts"))
                .andExpect(status().isNoContent());
        then(shiftService).should(times(1)).getShifts();
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getShifts_withUserRole_shouldNotBeAccessible() throws Exception {
        mockMvc.perform(get("/admin/shifts"))
                .andExpect(status().isForbidden());
        then(shiftService).should(times(0)).getShifts();

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void updateShift_withAdminRole_shouldReturnNoContent() throws Exception {
        EmployeeShift shift = new EmployeeShift(1L, Calendar.MONDAY, LocalTime.of(9, 0), LocalTime.of(17, 0));
        doNothing().when(shiftService).updateShift(shift);
        given(employeeService.getEmployee(anyLong())).willReturn(new Employee());
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(shift);
        mockMvc.perform(put("/admin/shifts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNoContent());
        then(shiftService).should(times(1)).updateShift(shift);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void updateShift_withAdminRole_shouldReturnBadRequest_withBadInput() throws Exception {
        EmployeeShift shift = new EmployeeShift();
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(shift);
        mockMvc.perform(put("/admin/shifts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
        then(shiftService).should(times(0)).updateShift(shift);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void updateShift_withUserRole_shouldDenyAccess() throws Exception {
        mockMvc.perform(put("/admin/shifts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isForbidden());
        then(shiftService).should(times(0)).updateShift(any());
    }
}
