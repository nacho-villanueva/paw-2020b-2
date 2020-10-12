package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Patient;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.persistence.config.TestConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
public class PatientJdbcDaoTest {

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

    //USER INFO
    private static final String EMAIL = "patient@zero.com";
    private static final String PASSWORD = "GroundZer0";
    private static final int ROLE = 1;

    //PATIENT INFO
    private static final String NAME = "Patient Zero";
    private static final int ZERO_ID = 0;

    @Autowired
    private DataSource ds;

    @Autowired
    private PatientJdbcDao patientDao;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsertPatients;
    private SimpleJdbcInsert jdbcInsertUsers;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsertPatients = new SimpleJdbcInsert(ds)
                .withTableName(PATIENTS_TABLE_NAME);
        jdbcInsertUsers = new SimpleJdbcInsert(ds)
                .withTableName(USERS_TABLE_NAME)
                .usingGeneratedKeyColumns("id");

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
        int dbkey = insertTestUser();

        final Patient patient = patientDao.register(new User(dbkey,EMAIL,PASSWORD,ROLE),NAME);

        Assert.assertNotNull(patient);
        Assert.assertEquals(dbkey,patient.getUser_id());
        Assert.assertEquals(NAME,patient.getName());
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate,PATIENTS_TABLE_NAME));
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testRegisterNoSuchUser() {
        patientDao.register(new User(ZERO_ID,EMAIL,PASSWORD,ROLE),NAME);
    }

    @Test(expected = DuplicateKeyException.class)
    public void testRegisterAlreadyExists() {
        int userkey = insertTestPatient();

        patientDao.register(new User(userkey,EMAIL,PASSWORD,ROLE),NAME);
    }

    @Test
    public void testFindByUserIdExists() {
        int userkey = insertTestPatient();

        final Optional<Patient> maybePatient = patientDao.findByUserId(userkey);

        Assert.assertTrue(maybePatient.isPresent());
        Assert.assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate,PATIENTS_TABLE_NAME));
        Assert.assertEquals(NAME,maybePatient.get().getName());
    }

    @Test
    public void testFindByUserIdPatientNotExists() {
        int userkey = insertTestUser(); //Not test patient, test user

        final Optional<Patient> maybePatient = patientDao.findByUserId(userkey);

        Assert.assertFalse(maybePatient.isPresent());
    }

    @Test
    public void testFindByEmailPatientExists() {
        int userkey = insertTestPatient();

        final Optional<Patient> maybePatient = patientDao.findByEmail(EMAIL);

        Assert.assertTrue(maybePatient.isPresent());
        Assert.assertEquals(userkey, maybePatient.get().getUser_id());
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, PATIENTS_TABLE_NAME));
    }

    @Test
    public void testFindByEmailPatientNotExists() {
        final Optional<Patient> maybePatient = patientDao.findByEmail(EMAIL);

        Assert.assertFalse(maybePatient.isPresent());
    }

    private int insertTestPatient() {
        int dbkey = insertTestUser();
        insertTestPatient(dbkey);
        return dbkey;
    }

    private void insertTestPatient(int user_id) {
        Map<String, Object> insertMap = new HashMap<>();
        insertMap.put("user_id",user_id);
        insertMap.put("name", NAME);
        jdbcInsertPatients.execute(insertMap);
    }

    private int insertTestUser() {
        Map<String, Object> insertMap = new HashMap<>();
        insertMap.put("email", EMAIL);
        insertMap.put("password", PASSWORD);
        insertMap.put("role", ROLE);
        Number key = jdbcInsertUsers.executeAndReturnKey(insertMap);
        return key.intValue();
    }
}
