package com.salon.www.salonapi.dao;

import com.salon.www.salonapi.dao.itf.CustomerDAO;
import com.salon.www.salonapi.exception.CustomerCreationFailedException;
import com.salon.www.salonapi.exception.CustomerDeletionFailedException;
import com.salon.www.salonapi.exception.CustomerUpdateFailedException;
import com.salon.www.salonapi.model.Booking;
import com.salon.www.salonapi.model.Customer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.junit4.SpringRunner;


import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerDaoImplTest {

    private static final String CREATE_CUSTOMER_T_SQL_SCRIPT = "scripts/create/bookings_t.sql";
    private static final String DROP_CUSTOMER_T_SQL_SCRIPT = "scripts/drop/bookings_t.sql";
    private static final String POPULATE_ONE_USER_T_SQL_SCRIPT = "scripts/populate/one_user_t.sql";
    private static final String POPULATE_ONE_CUSTOMER_T_SQL_SCRIPT = "scripts/populate/one_customer_t.sql";
    private static final String POPULATE_TWO_CUSTOMERS_T_SQL_SCRIPT = "scripts/populate/two_customers_t.sql";
    private static final String POPULATE_TWO_BOOKINGS_T_SQL_SCRIPT = "scripts/populate/two_bookings_t.sql";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private CustomerDAO customerDao;


    @Before
    public void setUp() throws SQLException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(CREATE_CUSTOMER_T_SQL_SCRIPT));
        connection.close();
    }

    @After
    public void tearDown() throws SQLException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(DROP_CUSTOMER_T_SQL_SCRIPT));
        connection.close();
    }

    @Test
    public void save_shouldAddCustomerToDatabase() throws SQLException{
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_ONE_USER_T_SQL_SCRIPT));
        connection.close();

        Customer customer = new Customer(1L, "User", "Name");
        customerDao.save(customer);

        Optional<Customer> validCustomer = customerDao.get(1L);

        assertThat(validCustomer.isPresent()).isEqualTo(true);
        assertThat(validCustomer.get().getUserId()).isEqualTo(customer.getUserId());
        assertThat(validCustomer.get().getFirstName()).isEqualTo(customer.getFirstName());
        assertThat(validCustomer.get().getLastName()).isEqualTo(customer.getLastName());

    }

    @Test(expected = CustomerCreationFailedException.class)
    public void save_shouldThrowError_forInvalidCustomerObject() {
        Customer customer = new Customer();
        customer.setFirstName("This string is going to be too long to fit into the database");
        customer.setLastName("Name");
        customer.setUserId(1L);

        customerDao.save(customer);
    }

    @Test
    public void get_shouldReturnValidCustomer_forExistingCustomer() throws Exception {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_ONE_CUSTOMER_T_SQL_SCRIPT));
        connection.close();
        Optional<Customer> validCustomer = customerDao.get(1L);

        assertThat(validCustomer.isPresent()).isEqualTo(true);
        assertThat(validCustomer.get().getFirstName()).isEqualTo("User");
        assertThat(validCustomer.get().getLastName()).isEqualTo("Name");
        assertThat(validCustomer.get().getUserId()).isEqualTo(1L);
    }

    @Test
    public void get_shouldReturnInvalidCustomer_forEmptyDatabase() {
        Optional<Customer> invalid = customerDao.get(new Random().nextLong());

        assertThat(invalid.isPresent()).isFalse();
    }

    @Test
    public void getCustomerByEmail_shouldReturnValidCustomer_forExistingCustomer() throws Exception {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_ONE_CUSTOMER_T_SQL_SCRIPT));
        connection.close();
        Optional<Customer> validCustomer = customerDao.getCustomerByEmail("user.name@test.com");

        assertThat(validCustomer.isPresent()).isEqualTo(true);
        assertThat(validCustomer.get().getFirstName()).isEqualTo("User");
        assertThat(validCustomer.get().getLastName()).isEqualTo("Name");
        assertThat(validCustomer.get().getUserId()).isEqualTo(1L);
    }

    @Test
    public void getCustomerByEmail_shouldReturnInvalidCustomer_forEmptyDatabase() {
        Optional<Customer> invalid = customerDao.getCustomerByEmail("RandomTestEmail@NotRealEmail.com");

        assertThat(invalid.isPresent()).isFalse();
    }

    @Test
    public void getAll_shouldYieldEmptyList_forEmptyDatabase() {
        List<Customer> noCustomers = customerDao.getAll();

        assertThat(noCustomers).isNullOrEmpty();
    }

    @Test
    public void getAll_shouldYieldListOfCustomers_forNonemptyDatabase() throws SQLException{
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_TWO_CUSTOMERS_T_SQL_SCRIPT));

        List<Customer> customers = customerDao.getAll();

        assertThat(customers).isNotNull().hasSize(2);
        assertThat(customers.contains(new Customer(1L, 1L, "User", "Name"))).isTrue();
        assertThat(customers.contains(new Customer(2L, 2L, "Second", "User"))).isTrue();

    }

    @Test
    public void getBookingsForDate_shouldYieldEmptyList_forEmptyDatabase() {
        Timestamp start1 = new Timestamp(new GregorianCalendar(2019, Calendar.JULY, 30, 11, 0).getTimeInMillis());
        List<Booking> noBookings = customerDao.getBookingsForDate(1L, start1);

        assertThat(noBookings).isNullOrEmpty();
    }

    @Test
    public void getBookingsForDate_shouldYieldListOfBookings_forNonemptyDatabase() throws SQLException{
        Timestamp start1 = new Timestamp(new GregorianCalendar(2019, Calendar.JULY, 30, 11, 0).getTimeInMillis());
        Timestamp end1 = new Timestamp(new GregorianCalendar(2019, Calendar.JULY, 30, 12, 30).getTimeInMillis());

        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_TWO_BOOKINGS_T_SQL_SCRIPT));

        List<Booking> bookings = customerDao.getBookingsForDate(1L, start1);


        assertThat(bookings).isNotNull().hasSize(1);
        assertThat(bookings.contains(new Booking(1L,1L,1L, start1, end1, null))).isTrue();

    }

    @Test(expected = CustomerUpdateFailedException.class)
    public void update_shouldThrowException_forNonExistingCustomer() {
        Customer notFound = new Customer();
        notFound.setId(new Random().nextLong());

        customerDao.update(notFound);
    }

    @Test
    public void update_shouldUpdateDatabase_forExistingCustomer() throws SQLException{
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_ONE_CUSTOMER_T_SQL_SCRIPT));
        connection.close();

        customerDao.update(new Customer(1L,1L, "Different", "Name"));

        Optional<Customer> updatedCustomer = customerDao.get(1L);
        assertThat(updatedCustomer).isPresent();
        assertThat(updatedCustomer.get().getFirstName()).isEqualTo("Different");
        assertThat(updatedCustomer.get().getLastName()).isEqualTo(("Name"));
        assertThat(updatedCustomer.get().getUserId()).isEqualTo(1L);
    }

    @Test
    public void delete_shouldRemoveCustomerFromDatabase() throws SQLException{
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_ONE_CUSTOMER_T_SQL_SCRIPT));
        connection.close();
        Customer admin = new Customer();
        admin.setId(1L);
        customerDao.delete(admin);

        assertThat(customerDao.get(1L).isPresent()).isFalse();
        assertThat(customerDao.getAll()).hasSize(0);
    }

    @Test(expected = CustomerDeletionFailedException.class)
    public void delete_shouldFailForNonExistentSkill() {
        Customer customer = new Customer();
        customer.setId(new Random().nextLong());

        customerDao.delete(customer);
    }

}
