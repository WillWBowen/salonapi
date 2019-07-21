package com.salon.www.salonapi.dao;

import com.salon.www.salonapi.dao.itf.AuthorityDAO;
import com.salon.www.salonapi.exception.*;
import com.salon.www.salonapi.model.security.Authority;
import com.salon.www.salonapi.model.security.AuthorityName;
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
public class AuthorityDaoImplTest {

    private static final String CREATE_CAPABILITY_T_SQL_SCRIPT = "scripts/create/authorities_t.sql";
    private static final String DROP_CAPABILITY_T_SQL_SCRIPT = "scripts/drop/authorities_t.sql";
    private static final String POPULATE_ONE_CAPABILITY_T_SQL_SCRIPT = "scripts/populate/one_authority_t.sql";
    private static final String POPULATE_TWO_CAPABILITIES_T_SQL_SCRIPT = "scripts/populate/two_authorities_t.sql";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private AuthorityDAO authorityDao;


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
    public void save_shouldAddAuthorityToDatabase() {
        Authority authority = new Authority(AuthorityName.ROLE_USER);
        authorityDao.save(authority);

        Optional<Authority> validAuthority = authorityDao.get(1L);

        assertThat(validAuthority.isPresent()).isEqualTo(true);
        assertThat(validAuthority.get().getName()).isEqualTo(authority.getName());
    }

    @Test(expected = AuthorityCreationFailedException.class)
    public void save_shouldThrowError_forInvalidAuthorityObject() {
        Authority authority = new Authority();

        authorityDao.save(authority);
    }

    @Test
    public void get_shouldReturnValidAuthority_forExistingAuthority() throws Exception {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_ONE_CAPABILITY_T_SQL_SCRIPT));
        connection.close();
        Optional<Authority> validAuthority = authorityDao.get(1L);

        assertThat(validAuthority.isPresent()).isEqualTo(true);
        assertThat(validAuthority.get().getName()).isEqualTo(AuthorityName.ROLE_USER);
    }

    @Test
    public void get_shouldReturnInvalidAuthority_forEmptyDatabase() {
        Optional<Authority> invalid = authorityDao.get(new Random().nextLong());

        assertThat(invalid.isPresent()).isFalse();
    }

    @Test
    public void getAll_shouldYieldEmptyList_forEmptyDatabase() {
        List<Authority> noAuthorities = authorityDao.getAll();

        assertThat(noAuthorities).isNullOrEmpty();
    }

    @Test
    public void getAll_shouldYieldListOfAuthorities_forNonemptyDatabase() throws SQLException{
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_TWO_CAPABILITIES_T_SQL_SCRIPT));

        List<Authority> authorities = authorityDao.getAll();

        assertThat(authorities).isNotNull().hasSize(2);
        assertThat(authorities.contains(new Authority(1L, AuthorityName.ROLE_USER, null))).isTrue();
        assertThat(authorities.contains(new Authority(2L, AuthorityName.ROLE_ADMIN, null))).isTrue();

    }

    @Test(expected = AuthorityUpdateFailedException.class)
    public void update_shouldThrowException_forNonExistingAuthority() {
        Authority notFound = new Authority();
        notFound.setId(new Random().nextLong());

        authorityDao.update(notFound);
    }

    @Test
    public void update_shouldUpdateDatabase_forExistingAuthority() throws SQLException{
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_ONE_CAPABILITY_T_SQL_SCRIPT));
        connection.close();

        authorityDao.update(new Authority(1L, AuthorityName.ROLE_USER, null));

        Optional<Authority> updatedAuthority = authorityDao.get(1L);
        assertThat(updatedAuthority).isPresent();
        assertThat(updatedAuthority.get().getName()).isEqualTo(AuthorityName.ROLE_USER);
    }

    @Test
    public void delete_shouldRemoveAuthorityFromDatabase() throws SQLException{
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ScriptUtils.executeSqlScript(connection, new ClassPathResource(POPULATE_ONE_CAPABILITY_T_SQL_SCRIPT));
        connection.close();

        authorityDao.delete(new Authority(1L, AuthorityName.ROLE_USER, null));

        assertThat(authorityDao.get(1L).isPresent()).isFalse();
        assertThat(authorityDao.getAll()).hasSize(0);
    }

    @Test(expected = AuthorityDeletionFailedException.class)
    public void delete_shouldFailForNonExistentSkill() {
        Authority authority = new Authority();
        authority.setId(new Random().nextLong());

        authorityDao.delete(authority);
    }

}
