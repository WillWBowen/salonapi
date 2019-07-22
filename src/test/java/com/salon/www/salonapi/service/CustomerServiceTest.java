package com.salon.www.salonapi.service;

import com.salon.www.salonapi.dao.itf.CustomerDAO;
import com.salon.www.salonapi.model.Customer;
import com.salon.www.salonapi.service.impl.CustomerServiceImpl;
import com.salon.www.salonapi.service.itf.CustomerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceTest {

    private CustomerService customerService;

    @Mock
    private CustomerDAO customerDAO;

    @Before
    public void setUp() {
        this.customerService = new CustomerServiceImpl(customerDAO);
    }

    @Test
    public void getCustomer_shouldReturnCustomer_ifPresent() {
        Customer customer = new Customer(10L, 4L, "first", "name");
        given(customerDAO.get(anyLong())).willReturn(Optional.of(customer));

        Customer returnedCustomer = customerService.getCustomer(10L);

        then(customerDAO).should(times(1)).get(10L);
        assertThat(returnedCustomer).isEqualTo(customer);
    }

    @Test
    public void getCustomer_shouldReturnNull_ifNotPresent() {
        given(customerDAO.get(anyLong())).willReturn(Optional.empty());

        Customer returnedCustomer = customerService.getCustomer(10L);

        then(customerDAO).should(times(1)).get(10L);
        assertThat(returnedCustomer).isNull();
    }

    @Test
    public void getCustomers_shouldReturnListOfCustomers_ifPresent() {
        Customer first = new Customer(10L, 4L, "first", "name");
        Customer second = new Customer(14L, 2L, "second", "name");
        given(customerDAO.getAll()).willReturn(Arrays.asList(first, second));

        List<Customer> customers = customerService.getCustomers();

        then(customerDAO).should(times(1)).getAll();
        assertThat(customers).contains(first);
        assertThat(customers).contains(second);
    }

    @Test
    public void getCustomers_shouldReturnNull_ifNoCustomersPresent() {
        given(customerDAO.getAll()).willReturn(null);

        List<Customer> customers = customerService.getCustomers();

        then(customerDAO).should(times(1)).getAll();
        assertThat(customers).isNullOrEmpty();
    }
}
