package com.salon.www.salonapi.service;

import com.salon.www.salonapi.dao.itf.CustomerDAO;
import com.salon.www.salonapi.exception.CustomerCreationFailedException;
import com.salon.www.salonapi.exception.CustomerNotFoundException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

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

    @Test
    public void createCustomer_callsDAOSaveMethod() {
        Customer customer = new Customer(10L, 4L, "first", "name");
        doNothing().when(customerDAO).save(any());

        customerService.createCustomer(customer);
        then(customerDAO).should(times(1)).save(customer);
    }

    @Test(expected = CustomerCreationFailedException.class)
    public void createShift_shouldThrowCustomerCreationFailedException() {
        Customer customer = new Customer(10L, 4L, "first", "name");
        doThrow(CustomerCreationFailedException.class).when(customerDAO).save(any());

        customerService.createCustomer(customer);
    }

    @Test
    public void updateCustomer_shouldVerifyThatCustomer_exists() {
        Customer customer = new Customer();
        customer.setId(10L);
        given(customerDAO.get(anyLong())).willReturn(Optional.of(customer));
        customerService.updateCustomer(customer);
        then(customerDAO).should(times(1)).get(10L);
    }

    @Test
    public void updateCustomer_shouldCallDAOUpdate_ifCustomerExists() {
        Customer customer = new Customer(10L, 4L, "first", "name");
        given(customerDAO.get(anyLong())).willReturn(Optional.of(customer));
        doNothing().when(customerDAO).update(customer);

        customerService.updateCustomer(customer);

        then(customerDAO).should(times(1)).update(customer);
    }

    @Test(expected = CustomerNotFoundException.class)
    public void updateCustomer_shouldThrowError_ifCustomerDoesNotExist() {
        Customer customer = new Customer();
        customer.setId(1L);
        given(customerDAO.get(anyLong())).willReturn(Optional.empty());

        customerService.updateCustomer(customer);
        then(customerDAO).should(times(1)).get(1L);
        then(customerDAO).should(times(0)).update(customer);
    }
}
