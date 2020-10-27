package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.persistence.config.TestConfig;
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

import javax.sql.DataSource;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class OrderDaoTest {

    //Others
    private static final long ZERO_ID_LONG = 0;

    //Known Data
    private static final User userMedic = new User(2,"one@one.com","onePass",User.MEDIC_ROLE_ID);
    private static final Medic medic = new Medic(userMedic, "Medic one", null, "image/png", new byte[0], "1234567", true);
    private static final User userClinic = new User(3,"two@two.com","passTwo",User.CLINIC_ROLE_ID);
    private static final Clinic clinic = new Clinic(userClinic, "Clinic two", null, true);
    private static final StudyType studyType = new StudyType(1, "X-ray");

    private static final Order order = new Order(1, medic, Date.valueOf("2020-10-05"), clinic, studyType, "Description 1", "image/png",new byte[0], "insurance plan one", "insurance123", "patient@patient.com", "Patient one");

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
        final Optional<Order> maybeOrder = dao.findById(order.getOrder_id());

        Assert.assertTrue(maybeOrder.isPresent());
        Assert.assertEquals(order.getMedic().getName(),maybeOrder.get().getMedic().getName());
        Assert.assertEquals(order.getClinic().getName(),maybeOrder.get().getClinic().getName());
        Assert.assertEquals(order.getStudy().getName(),maybeOrder.get().getStudy().getName());
        Assert.assertEquals(order.getPatient_name(),maybeOrder.get().getPatient_name());
        Assert.assertEquals(order.getDate(),maybeOrder.get().getDate());
    }

    @Transactional
    @Rollback
    @Test
    public void testFindByIdNotExists() {
        final Optional<Order> maybeOrder = dao.findById(ZERO_ID_LONG);

        Assert.assertFalse(maybeOrder.isPresent());
    }

//    @Test
//    public void testRegisterValid() {
//        int medic_id = insertTestUser();
//        new User(user_id,USER_EMAIL,PASSWORD,User.MEDIC_ROLE_ID,"en-us");
//        int medic_id = insertTestMedic(user_id);
//        int user_id = insertTestUser();
//        int clinic_id = insertTestClinic(user_id);
//        int study_id = insertTestStudy();
//        Medic medic = new Medic(new User(medic_id,USER_EMAIL,PASSWORD,User.MEDIC_ROLE_ID,"en-us"),MEDIC_NAME,"",ORDER_IDENTIFICATION_TYPE,ORDER_IDENTIFICATION,MEDIC_LICENCE,TRUE);
//        Clinic clinic = new Clinic(new User(clinic_id,USER_EMAIL,PASSWORD,User.CLINIC_ROLE_ID,"en-us"),CLINIC_NAME,USER_EMAIL,"",TRUE);
//        StudyType studyType = new StudyType(study_id,STUDY_NAME);
//
//        final Order order = dao.register(medic,ORDER_DATE,clinic,PATIENT_NAME,USER_EMAIL,studyType,"",ORDER_IDENTIFICATION_TYPE,ORDER_IDENTIFICATION,"","");
//
//        Assert.assertEquals(PATIENT_NAME,order.getPatient_name());
//        Assert.assertArrayEquals(ORDER_IDENTIFICATION,order.getIdentification());
//        Assert.assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate,ORDERS_TABLE_NAME));
//    }

//    @Test (expected = DataIntegrityViolationException.class)
//    public void testRegisterInvalid() {
//        Medic medic = new Medic(new User(ZERO_ID_INT,USER_EMAIL,PASSWORD,User.MEDIC_ROLE_ID,"en-us"),MEDIC_NAME,"",ORDER_IDENTIFICATION_TYPE,ORDER_IDENTIFICATION,MEDIC_LICENCE,TRUE);
//        Clinic clinic = new Clinic(ZERO_ID_INT,CLINIC_NAME,USER_EMAIL,"",TRUE);
//        StudyType studyType = new StudyType(ZERO_ID_INT,STUDY_NAME);
//
//        dao.register(medic,ORDER_DATE,clinic,PATIENT_NAME,USER_EMAIL,studyType,"",ORDER_IDENTIFICATION_TYPE,ORDER_IDENTIFICATION,"","");
//    }
}
