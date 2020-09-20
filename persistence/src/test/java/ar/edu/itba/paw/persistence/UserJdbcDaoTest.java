package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.persistence.config.TestConfig;
import org.hsqldb.jdbc.JDBCDriver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class UserJdbcDaoTest {

    //TABLE NAMES
    private static final String USERS_TABLE_NAME = "users";
    private static final String CLINICS_TABLE_NAME = "clinics";
    private static final String CLINICS_RELATION_TABLE_NAME = "clinic_available_studies";
    private static final String MEDICS_TABLE_NAME = "medics";
    private static final String MEDICS_RELATION_TABLE_NAME = "medic_medical_fields";
    private static final String PATIENTS_TABLE_NAME = "patients";
    private static final String ORDERS_TABLE_NAME = "medical_orders";
    private static final String RESULTS_TABLE_NAME = "results";


    private static final String EMAIL = "patient@zero.com";
    private static final String PASSWORD = "GroundZer0";
    private static final int ROLE = 2;
    private static final int NEW_ROLE = 4;
    private static final int ZERO_ID = 0;

    @Autowired
    private DataSource ds;

    @Autowired
    private UserJdbcDao dao;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(USERS_TABLE_NAME)
                .usingGeneratedKeyColumns("id");

        JdbcTestUtils.deleteFromTables(jdbcTemplate,PATIENTS_TABLE_NAME);
        JdbcTestUtils.deleteFromTables(jdbcTemplate,RESULTS_TABLE_NAME);
        JdbcTestUtils.deleteFromTables(jdbcTemplate,ORDERS_TABLE_NAME);
        JdbcTestUtils.deleteFromTables(jdbcTemplate,MEDICS_RELATION_TABLE_NAME);
        JdbcTestUtils.deleteFromTables(jdbcTemplate,CLINICS_RELATION_TABLE_NAME);
        JdbcTestUtils.deleteFromTables(jdbcTemplate,MEDICS_TABLE_NAME);
        JdbcTestUtils.deleteFromTables(jdbcTemplate,CLINICS_TABLE_NAME);
        JdbcTestUtils.deleteFromTables(jdbcTemplate,USERS_TABLE_NAME);
    }

    @Test
    public void testRegisterValid() {
        final User user = dao.register(EMAIL,PASSWORD,ROLE);

        Assert.assertNotNull(user);
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate,USERS_TABLE_NAME));
    }

    @Test(expected = DuplicateKeyException.class)
    public void testRegisterInvalid() {
        insertTestUser();

        dao.register(EMAIL,PASSWORD,ROLE);
    }

    @Test
    public void testFindByIdExists() {
        int dbkey = insertTestUser();

        Optional<User> maybeUser = dao.findById(dbkey);

        Assert.assertTrue(maybeUser.isPresent());
        Assert.assertEquals(EMAIL,maybeUser.get().getEmail());
    }

    @Test
    public void testFindByIdNotExists() {
        Optional<User> maybeUser = dao.findById(ZERO_ID);

        Assert.assertFalse(maybeUser.isPresent());
    }

    @Test
    public void testUpdateRoleValidUser() {
        int dbkey = insertTestUser();

        User newUser = dao.updateRole(new User(dbkey,EMAIL,PASSWORD,ROLE),NEW_ROLE);

        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate,USERS_TABLE_NAME));
        Assert.assertEquals(NEW_ROLE,newUser.getRole());
    }

    /*@Test(expected = NullPointerException.class)
    public void testUpdateRoleInvalidUser() {
        dao.updateRole(new User(ZERO_ID,EMAIL,PASSWORD,ROLE),NEW_ROLE);
    }*/

    private int insertTestUser() {
        Map<String, Object> insertMap = new HashMap<>();
        insertMap.put("email", EMAIL);
        insertMap.put("password", PASSWORD);
        insertMap.put("role", ROLE);
        Number key = jdbcInsert.executeAndReturnKey(insertMap);
        return key.intValue();
    }
}
