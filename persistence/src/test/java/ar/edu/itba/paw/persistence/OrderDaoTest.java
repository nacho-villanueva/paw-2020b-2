package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.persistence.config.TestConfig;
import org.hibernate.TransientPropertyValueException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import javax.sql.DataSource;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class OrderDaoTest {

    // TABLES
    private static final String ORDERS_TABLE_NAME = "medical_orders";


    //Others
    private static final long ZERO_ID_LONG = 0;

    // Invalid Data
    private static final User invalidUserMedic = new User(-1,"invalid@medic.com","invalid",User.MEDIC_ROLE_ID);
    private static final Medic invalidMedic = new Medic(invalidUserMedic, "Medic Ivalid", null, "image/png", new byte[0], "1234567", true);
    private static final User invalidUserClinic = new User(-2,"invalid@clinic.com","invalid",User.CLINIC_ROLE_ID);
    private static final Clinic invalidClinic = new Clinic(invalidUserClinic, "Clinic Invalid", null, true);
    private static final StudyType invalidStudyType = new StudyType(1, "Invalid");


    //Known Data
    private static final User userMedic = new User(2,"one@one.com","onePass",User.MEDIC_ROLE_ID);
    private static final Medic medic = new Medic(userMedic, "Medic one", null, "image/png", new byte[0], "1234567", true);
    private static final User userClinic = new User(3,"two@two.com","passTwo",User.CLINIC_ROLE_ID);
    private static final Clinic clinic = new Clinic(userClinic, "Clinic two", null, true);
    private static final StudyType studyType = new StudyType(1, "X-ray");

    private static final Order order = new Order(1, medic, Date.valueOf("2020-10-05"), clinic, studyType, "Description 1", "image/png",new byte[0], "insurance plan one", "insurance123", "patient@patient.com", "Patient one");
    private static final Order newOrder = new Order(2, medic, Date.valueOf("2020-10-05"), clinic, studyType, "Description 1", "image/png",new byte[0], "insurance plan one", "insurance123", "patientnew@patient.com", "Patient New");

    @Autowired
    private DataSource ds;

    @Autowired
    private OrderDao dao;

    private JdbcTemplate jdbcTemplate;


    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Transactional
    @Rollback
    @Test
    public void testFindByIdExists() {
        final Optional<Order> maybeOrder = dao.findById(order.getOrderId());

        Assert.assertTrue(maybeOrder.isPresent());
        Assert.assertEquals(order.getMedic().getName(),maybeOrder.get().getMedic().getName());
        Assert.assertEquals(order.getClinic().getName(),maybeOrder.get().getClinic().getName());
        Assert.assertEquals(order.getStudy().getName(),maybeOrder.get().getStudy().getName());
        Assert.assertEquals(order.getPatientName(),maybeOrder.get().getPatientName());
        Assert.assertEquals(order.getDate(),maybeOrder.get().getDate());
    }

    @Transactional
    @Rollback
    @Test
    public void testFindByIdNotExists() {
        final Optional<Order> maybeOrder = dao.findById(ZERO_ID_LONG);

        Assert.assertFalse(maybeOrder.isPresent());
    }

    @Transactional
    @Rollback
    @Test
    public void testRegisterValid() {
        final Order testOrder = dao.register(newOrder.getMedic(), newOrder.getDate(),clinic, newOrder.getPatientEmail(), newOrder.getPatientName(),newOrder.getStudy(),"",newOrder.getIdentificationType(),newOrder.getIdentification(),newOrder.getPatientInsurancePlan(),newOrder.getPatientInsuranceNumber());

        Assert.assertEquals(newOrder.getPatientName(), testOrder.getPatientName());
        Assert.assertArrayEquals(newOrder.getIdentification(), testOrder.getIdentification());
        Assert.assertEquals(1,JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,ORDERS_TABLE_NAME, "patient_name='" + newOrder.getPatientName() +"'"));
    }

    @Transactional
    @Rollback
    @Test (expected = PersistenceException.class)
    public void testRegisterInvalid() {
        dao.register(invalidMedic,newOrder.getDate(),invalidClinic,newOrder.getPatientName(), newOrder.getPatientEmail(),invalidStudyType,"",newOrder.getIdentificationType(),newOrder.getIdentification(),"","");
    }
}
