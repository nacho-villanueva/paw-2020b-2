package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Patient;
import ar.edu.itba.paw.persistence.config.TestConfig;
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
public class PatientJdbcDaoTest {

    private static final String EMAIL = "patient@zero.com";
    private static final String NAME = "Patient Zero";
    private static final String TABLE_NAME = "patients";
    private static final int ZERO_ID = 0;

    @Autowired
    private DataSource ds;

    @Autowired
    private PatientJdbcDao patientDao;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns("id");
        JdbcTestUtils.deleteFromTables(jdbcTemplate,TABLE_NAME);
    }

    @Test
    public void testRegisterValid() {
        final Patient patient = patientDao.register(EMAIL, NAME);

        Assert.assertNotNull(patient);
        Assert.assertEquals(EMAIL,patient.getEmail());
        Assert.assertEquals(NAME,patient.getName());
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate,TABLE_NAME));
    }

    @Test(expected = DuplicateKeyException.class)
    public void testRegisterAlreadyExists() {
        insertTestPatient();

        patientDao.register(EMAIL, NAME);
    }

    @Test
    public void testFindByIdPatientExists() {
        int dbKey = insertTestPatient();

        final Optional<Patient> maybePatient = patientDao.findById(dbKey);

        Assert.assertNotNull(maybePatient);
        Assert.assertTrue(maybePatient.isPresent());
        Assert.assertEquals(EMAIL, maybePatient.get().getEmail());
        Assert.assertEquals(NAME, maybePatient.get().getName());
    }

    @Test
    public void testFindByIdPatientNotExists() {
        final Optional<Patient> maybePatient = patientDao.findById(ZERO_ID);

        Assert.assertNotNull(maybePatient);
        Assert.assertFalse(maybePatient.isPresent());
    }

    @Test
    public void testFindByEmailPatientExists() {
        insertTestPatient();

        final Optional<Patient> maybePatient = patientDao.findByEmail(EMAIL);

        Assert.assertNotNull(maybePatient);
        Assert.assertTrue(maybePatient.isPresent());
        Assert.assertEquals(NAME, maybePatient.get().getName());
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, TABLE_NAME));
    }

    @Test
    public void testFindByEmailPatientNotExists() {
        final Optional<Patient> maybePatient = patientDao.findByEmail(EMAIL);

        Assert.assertNotNull(maybePatient);
        Assert.assertFalse(maybePatient.isPresent());
    }

    @Test
    public void testFindOrRegisterAlreadyExists() {
        int dbKey = insertTestPatient();

        final Patient patient = patientDao.findOrRegister(EMAIL, NAME);

        Assert.assertNotNull(patient);
        Assert.assertEquals(dbKey, patient.getId());
        Assert.assertEquals(EMAIL, patient.getEmail());
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate,TABLE_NAME));
    }

    @Test
    public void testFindOrRegisterNotExists() {
        final Patient patient = patientDao.findOrRegister(EMAIL, NAME);

        Assert.assertNotNull(patient);
        Assert.assertEquals(EMAIL, patient.getEmail());
        Assert.assertEquals(NAME, patient.getName());
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate,TABLE_NAME));
    }

    private int insertTestPatient() {
        Map<String, Object> insertMap = new HashMap<>();
        insertMap.put("email", EMAIL);
        insertMap.put("name", NAME);
        Number key = jdbcInsert.executeAndReturnKey(insertMap);
        return key.intValue();
    }
}
