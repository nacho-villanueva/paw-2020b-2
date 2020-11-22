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

import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class ResultDaoTest {

    //TABLE NAMES
    private static final String RESULTS_TABLE_NAME = "results";
    //Others
    private static final long ZERO_ID_LONG = 0;
    private static final long INVALID_ORDER_ID = -1;

    //Known Data
    private static final User userMedic = new User(2,"one@one.com","onePass",User.MEDIC_ROLE_ID);
    private static final Medic medic = new Medic(userMedic, "Medic one", null, "image/png", new byte[0], "1234567", true);
    private static final User userClinic = new User(3,"two@two.com","passTwo",User.CLINIC_ROLE_ID);
    private static final Clinic clinic = new Clinic(userClinic, "Clinic two", null, true);
    private static final StudyType studyType = new StudyType(1, "X-ray");

    private static final Order order = new Order(1, medic, LocalDate.parse("2020-10-05"), clinic, studyType, "Description 1", "image/png",new byte[0], "insurance plan one", "insurance123", "patient@patient.com", "Patient one");

    //Test data
    private static final Result result = new Result(1L, order, LocalDate.parse("2020-10-05"), "responsible_one","licence1234", "image/png", new byte[0],"image/png", new byte[0]);
    private static final Result testResultTwo = new Result(20L, order, LocalDate.parse("2020-10-05"), "responsible_test","licence1234", "image/png", new byte[0],"image/png", new byte[0]);


    @Autowired
    private DataSource ds;

    @Autowired
    private ResultDao dao;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Transactional
    @Rollback
    @Test
    public void testFindByIdExists() {
        final Optional<Result> maybeResult = dao.findById(result.getId());

        Assert.assertNotNull(maybeResult);
        Assert.assertTrue(maybeResult.isPresent());
        Assert.assertEquals(result.getDate(),maybeResult.get().getDate());
    }

    @Transactional
    @Rollback
    @Test
    public void testFindByIdNotExists() {
        final Optional<Result> maybeResult = dao.findById(ZERO_ID_LONG);

        Assert.assertNotNull(maybeResult);
        Assert.assertFalse(maybeResult.isPresent());
    }

    @Transactional
    @Rollback
    @Test
    public void testFindByOrderIdExists() {
        final Collection<Result> results = dao.findByOrderId(result.getOrder_id());

        Assert.assertNotNull(results);
        Assert.assertTrue(results.stream().findFirst().isPresent());

        Assert.assertEquals(2,results.size());
        Assert.assertEquals(result.getDate(), results.stream().findFirst().get().getDate());
    }

    @Transactional
    @Rollback
    @Test
    public void testFindByOrderIdNotExists() {
        final Collection<Result> results = dao.findByOrderId(ZERO_ID_LONG);

        Assert.assertNotNull(results);
        Assert.assertEquals(0,results.size());
    }

    @Transactional
    @Rollback
    @Test
    public void testRegisterValid() {

        final Result testResult = dao.register(testResultTwo.getOrder_id(),testResultTwo.getData_type(),testResultTwo.getData(),testResultTwo.getIdentification_type(),testResultTwo.getIdentification(),testResultTwo.getDate(),testResultTwo.getResponsible_name(),testResultTwo.getResponsible_licence_number());

        Assert.assertEquals(testResultTwo.getResponsible_name(), testResult.getResponsible_name());
        Assert.assertEquals(1,JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,RESULTS_TABLE_NAME, "responsible_name='" + testResult.getResponsible_name() +"'"));
    }

    @Transactional
    @Rollback
    @Test(expected = PersistenceException.class)
    public void testRegisterInvalid() {
        dao.register(INVALID_ORDER_ID, result.getData_type(),result.getData(),result.getIdentification_type(),result.getIdentification(),result.getDate(),result.getResponsible_name(),result.getResponsible_licence_number());
    }

}
