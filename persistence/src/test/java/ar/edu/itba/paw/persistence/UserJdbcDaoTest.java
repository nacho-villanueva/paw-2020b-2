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
    private static final String CLINIC_HOURS_TABLE_NAME = "clinic_hours";
    private static final String CLINIC_PLANS_TABLE_NAME = "clinic_accepted_plans";


    private static final String EMAIL = "patient@zero.com";
    private static final String PASSWORD = "GroundZer0";
    private static final int ROLE = 4;
    private static final int ZERO_ID = 0;
    private static final String DEFAULT_LOCALE = "en-US";

    private static final String MEDIC_IDENTIFICATION_TYPE = "image/png";
    private static final byte[] MEDIC_IDENTIFICATION = new byte[] { (byte)0xe0, 0x4f, (byte)0xd0,
            0x20, (byte)0xea, 0x3a, 0x69, 0x10, (byte)0xa2, (byte)0xd8, 0x08, 0x00, 0x2b,
            0x30, 0x30, (byte)0x9d };
    private static final String MEDIC_NAME = "Jhon William";
    private static final String MEDIC_TELEPHONE = "1111111111";
    private static final String MEDIC_LICENCE = "A21-B15";

    @Autowired
    private DataSource ds;

    @Autowired
    private UserJdbcDao dao;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;
    private SimpleJdbcInsert jdbcInsertMedic;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(USERS_TABLE_NAME)
                .usingGeneratedKeyColumns("id");
        jdbcInsertMedic = new SimpleJdbcInsert(ds)
                .withTableName(MEDICS_TABLE_NAME);

        JdbcTestUtils.deleteFromTables(jdbcTemplate,CLINIC_HOURS_TABLE_NAME);
        JdbcTestUtils.deleteFromTables(jdbcTemplate,CLINIC_PLANS_TABLE_NAME);
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
        final User user = dao.register(EMAIL,PASSWORD,ROLE,DEFAULT_LOCALE);

        Assert.assertNotNull(user);
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate,USERS_TABLE_NAME));
    }

    @Test(expected = DuplicateKeyException.class)
    public void testRegisterInvalid() {
        insertTestUser(User.PATIENT_ROLE_ID);
        dao.register(EMAIL,PASSWORD,ROLE,DEFAULT_LOCALE);
    }

    @Test
    public void testFindByIdExists() {
        int dbkey = insertTestUser(User.PATIENT_ROLE_ID);

        Optional<User> maybeUser = dao.findById(dbkey);

        Assert.assertTrue(maybeUser.isPresent());
        Assert.assertEquals(EMAIL,maybeUser.get().getEmail());
        Assert.assertFalse(maybeUser.get().isRegistered());
        Assert.assertEquals(User.PATIENT_ROLE_ID,maybeUser.get().getRole());
    }

    @Test
    public void testFindByIdNotExists() {
        Optional<User> maybeUser = dao.findById(ZERO_ID);

        Assert.assertFalse(maybeUser.isPresent());
    }

    @Test
    public void testFlagsRegisteredNotVerified() {
        int dbkey = insertTestUser(User.MEDIC_ROLE_ID);
        insertTestMedic(dbkey,false);

        Optional<User> maybeUser = dao.findById(dbkey);

        Assert.assertTrue(maybeUser.isPresent());
        Assert.assertTrue(maybeUser.get().isRegistered());
        Assert.assertTrue(maybeUser.get().isVerifying());
    }

    @Test
    public void testFlagsRegisteredVerified() {
        int dbkey = insertTestUser(User.MEDIC_ROLE_ID);
        insertTestMedic(dbkey,true);

        Optional<User> maybeUser = dao.findById(dbkey);

        Assert.assertTrue(maybeUser.isPresent());
        Assert.assertTrue(maybeUser.get().isRegistered());
        Assert.assertFalse(maybeUser.get().isVerifying());
    }

    /*@Test
    public void testUpdateRoleInvalidUser() {
        dao.updateRole(new User(ZERO_ID,EMAIL,PASSWORD,ROLE),NEW_ROLE);  //Does not fail, it updates nothing
    }*/

    private int insertTestUser(int role) {
        Map<String, Object> insertMap = new HashMap<>();
        insertMap.put("email", EMAIL);
        insertMap.put("password", PASSWORD);
        insertMap.put("role", role);
        Number key = jdbcInsert.executeAndReturnKey(insertMap);
        return key.intValue();
    }

    private void insertTestMedic(int user_id, boolean verified) {
        Map<String, Object> insertMap = new HashMap<>();
        insertMap.put("user_id",user_id);
        insertMap.put("name", MEDIC_NAME);
        insertMap.put("telephone", MEDIC_TELEPHONE);
        insertMap.put("identification_type",MEDIC_IDENTIFICATION_TYPE);
        insertMap.put("identification",MEDIC_IDENTIFICATION);
        insertMap.put("licence_number", MEDIC_LICENCE);
        insertMap.put("verified", verified);

        jdbcInsertMedic.execute(insertMap);
    }
}
