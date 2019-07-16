package com.salon.www.salonapi.dao;

import com.salon.www.salonapi.exception.*;
import com.salon.www.salonapi.model.Capability;
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
import java.util.List;
import java.util.Optional;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CapabilityDaoImplTest {

    private static final String CREATE_CAPABILITY_T_SQL_SCRIPT = "scripts/create/capabilities_t.sql";
    private static final String DROP_CAPABILITY_T_SQL_SCRIPT = "scripts/drop/capabilities_t.sql";
    private static final String POPULATE_ONE_CAPABILITY_T_SQL_SCRIPT = "scripts/populate/one_capability_t.sql";
    private static final String POPULATE_TWO_CAPABILITIES_T_SQL_SCRIPT = "scripts/populate/two_capabilities_t.sql";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private CapabilityDAO capabilityDao;


    @Before
    public void setUp() throws SQLException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(CREATE_CAPABILITY_T_SQL_SCRIPT));
        connection.close();
    }

    @After
    public void tearDown() throws SQLException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(DROP_CAPABILITY_T_SQL_SCRIPT));
        connection.close();
    }

    @Test
    public void save_shouldAddCapabilityToDatabase() {
        Capability capability = new Capability("add_customer");
        capabilityDao.save(capability);

        Optional<Capability> validCapability = capabilityDao.get(1L);

        assertThat(validCapability.isPresent()).isEqualTo(true);
        assertThat(validCapability.get().getName()).isEqualTo(capability.getName());
    }

    @Test(expected = CapabilityCreationFailedException.class)
    public void save_shouldThrowError_forInvalidCapabilityObject() {
        Capability capability = new Capability();
        capability.setName("This string is going to be too long to fit into the database");

        capabilityDao.save(capability);
    }

    @Test
    public void get_shouldReturnValidCapability_forExistingCapability() throws Exception {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_ONE_CAPABILITY_T_SQL_SCRIPT));
        connection.close();
        Optional<Capability> validCapability = capabilityDao.get(1L);

        assertThat(validCapability.isPresent()).isEqualTo(true);
        assertThat(validCapability.get().getName()).isEqualTo("add_customer");
    }

    @Test
    public void get_shouldReturnInvalidCapability_forEmptyDatabase() {
        Optional<Capability> invalid = capabilityDao.get(new Random().nextLong());

        assertThat(invalid.isPresent()).isFalse();
    }

    @Test
    public void getAll_shouldYieldEmptyList_forEmptyDatabase() {
        List<Capability> noCapabilities = capabilityDao.getAll();

        assertThat(noCapabilities).isNullOrEmpty();
    }

    @Test
    public void getAll_shouldYieldListOfCapabilities_forNonemptyDatabase() throws SQLException{
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_TWO_CAPABILITIES_T_SQL_SCRIPT));

        List<Capability> capabilities = capabilityDao.getAll();

        assertThat(capabilities).isNotNull().hasSize(2);
        assertThat(capabilities.contains(new Capability(1L, "add_customer"))).isTrue();
        assertThat(capabilities.contains(new Capability(2L, "update_customer"))).isTrue();

    }

    @Test(expected = CapabilityUpdateFailedException.class)
    public void update_shouldThrowException_forNonExistingCapability() {
        Capability notFound = new Capability();
        notFound.setId(new Random().nextLong());

        capabilityDao.update(notFound);
    }

    @Test
    public void update_shouldUpdateDatabase_forExistingCapability() throws SQLException{
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_ONE_CAPABILITY_T_SQL_SCRIPT));
        connection.close();

        capabilityDao.update(new Capability(1L,"get_booking"));

        Optional<Capability> updatedCapability = capabilityDao.get(1L);
        assertThat(updatedCapability).isPresent();
        assertThat(updatedCapability.get().getName()).isEqualTo("get_booking");
    }

    @Test
    public void delete_shouldRemoveCapabilityFromDatabase() throws SQLException{
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_ONE_CAPABILITY_T_SQL_SCRIPT));
        connection.close();

        capabilityDao.delete(new Capability(1L, ""));

        assertThat(capabilityDao.get(1L).isPresent()).isFalse();
        assertThat(capabilityDao.getAll()).hasSize(0);
    }

    @Test(expected = CapabilityDeletionFailedException.class)
    public void delete_shouldFailForNonExistentSkill() {
        Capability capability = new Capability();
        capability.setId(new Random().nextLong());

        capabilityDao.delete(capability);
    }

}
