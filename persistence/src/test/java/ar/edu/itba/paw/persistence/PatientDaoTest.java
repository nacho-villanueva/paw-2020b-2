package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.MedicPlan;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.config.TestConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.PersistenceException;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class PatientDaoTest {

    //TABLE NAMES
    private static final String PATIENTS_TABLE_NAME = "patients";

    //Known users
    private static final User userSix = new User(7,"six@six.com","passSix",User.UNDEFINED_ROLE_ID);
    private static final User userZero = new User(1,"zero@zero.com","passZero",User.PATIENT_ROLE_ID);
    private static final Patient patientZero = new Patient(userZero,"Patient Zero");

    //Known Medic Plans
    private static final MedicPlan medicPlanOne = new MedicPlan(1,"Osde");
    private static final MedicPlan medicPlanTwo = new MedicPlan(2,"Swiss Medical");

    //PATIENT INFO
    private static final Patient patientTest = new Patient(userSix,"Test Patient",medicPlanOne,"12345678");
    private static final User userTest = new User(0,"test@test.com","testPass",User.UNDEFINED_ROLE_ID,"es-AR");

    @Autowired
    private DataSource ds;

    @Autowired
    private PatientDao dao;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Transactional
    @Rollback
    @Test
    public void testRegisterValid() {
        final Patient patient = dao.register(patientTest.getUser(),patientTest.getName());

        Assert.assertNotNull(patient);
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,PATIENTS_TABLE_NAME,"user_id = " + patientTest.getUser().getId() + " AND name = '" + patientTest.getName() + "'"));
    }

    @Transactional
    @Rollback
    @Test(expected = PersistenceException.class)
    public void testRegisterNoSuchUser() {
        dao.register(userTest,patientTest.getName());
    }

    @Transactional
    @Rollback
    @Test(expected = PersistenceException.class)
    public void testRegisterAlreadyExists() {
        dao.register(patientZero.getUser(),patientTest.getName());
    }

    @Transactional
    @Rollback
    @Test
    public void testFindByUserIdExists() {
        final Optional<Patient> maybePatient = dao.findByUserId(patientZero.getUser().getId());

        Assert.assertTrue(maybePatient.isPresent());
        Assert.assertEquals(patientZero.getName(),maybePatient.get().getName());
        Assert.assertEquals(patientZero.getUser().getId(),maybePatient.get().getUserId());
    }

    @Transactional
    @Rollback
    @Test
    public void testFindByUserIdPatientNotExists() {
        final Optional<Patient> maybePatient = dao.findByUserId(patientTest.getUser().getId());

        Assert.assertFalse(maybePatient.isPresent());
    }

    @Transactional
    @Rollback
    @Test
    public void testFindByEmailPatientExists() {
        final Optional<Patient> maybePatient = dao.findByEmail(patientZero.getUser().getEmail());

        Assert.assertTrue(maybePatient.isPresent());
        Assert.assertEquals(patientZero.getUser().getId(), maybePatient.get().getUserId());
        Assert.assertEquals(patientZero.getName(), maybePatient.get().getName());
    }

    @Transactional
    @Rollback
    @Test
    public void testFindByEmailPatientNotExists() {
        final Optional<Patient> maybePatient = dao.findByEmail(patientTest.getUser().getEmail());

        Assert.assertFalse(maybePatient.isPresent());
    }

    @Transactional
    @Rollback
    @Test
    public void testUpdatePatientInfo() {
        Patient patient = dao.updatePatientInfo(patientZero.getUser(),patientTest.getName(),patientTest.getMedicPlan(),patientTest.getMedicPlanNumber());

        Assert.assertNotNull(patient);
        Assert.assertEquals(1,JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,PATIENTS_TABLE_NAME,"name = '" + patientTest.getName() + "' AND plan_id = " + patientTest.getMedicPlan().getId() + " AND medic_plan_number = '" + patientTest.getMedicPlanNumber() + "' AND user_id = " + patientZero.getUser().getId()));
    }

    @Transactional
    @Rollback
    @Test
    public void testUpdatePatientInfoNonExistentPatient() {
        Patient patient = dao.updatePatientInfo(patientTest.getUser(),patientTest.getName(),patientTest.getMedicPlan(),patientTest.getMedicPlanNumber());

        Assert.assertNull(patient);
        Assert.assertEquals(0,JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,PATIENTS_TABLE_NAME,"user_id = " + patientTest.getUser().getId()));
    }
}
