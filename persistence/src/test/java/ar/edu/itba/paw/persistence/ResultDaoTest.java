package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Result;
import ar.edu.itba.paw.persistence.config.TestConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;
import java.sql.Date;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class ResultDaoTest {

    //TABLE NAMES
    private static final String USERS_TABLE_NAME = "users";
    private static final String CLINICS_TABLE_NAME = "clinics";
    private static final String CLINICS_RELATION_TABLE_NAME = "clinic_available_studies";
    private static final String MEDICS_TABLE_NAME = "medics";
    private static final String MEDICS_RELATION_TABLE_NAME = "medic_medical_fields";
    private static final String PATIENTS_TABLE_NAME = "patients";
    private static final String ORDERS_TABLE_NAME = "medical_orders";
    private static final String RESULTS_TABLE_NAME = "results";
    private static final String STUDIES_TABLE_NAME = "medical_studies";
    private static final String CLINIC_HOURS_TABLE_NAME = "clinic_hours";
    private static final String CLINIC_PLANS_TABLE_NAME = "clinic_accepted_plans";

    //Shared Info
    private static final Date DATE = Date.valueOf("2020-10-05");
    private static final String DATA_TYPE = "image/png";
    private static final byte[] DATA = new byte[] { (byte)0xe0, 0x4f, (byte)0xd0,
            0x20, (byte)0xea, 0x3a, 0x69, 0x10, (byte)0xa2, (byte)0xd8, 0x08, 0x00, 0x2b,
            0x30, 0x30, (byte)0x9d };
    private static final String MEDIC_NAME = "Jhon William";
    private static final String MEDIC_LICENCE = "A21-B15";

    //Test Medic Info
    private static final String MEDIC_EMAIL = "jhon@medic.com";

    //Test Clinic Info
    private static final String CLINIC_NAME = "Zero's Clinic";
    private static final String CLINIC_EMAIL = "clinic@zero.com";

    //Test Study Info
    private static final String STUDY_NAME = "MRA";

    //Test Patient Info
    private static final String PATIENT_NAME = "Patient Zero";

    //USER INFO
    private static final String USER_EMAIL = "patient@zero.com";
    private static final String PASSWORD = "GroundZer0";
    private static final int ROLE = 4;

    //Others
    private static final long ZERO_ID_LONG = 0;
    private static final int ZERO_ID_INT = 0;
    private static final boolean TRUE = true;

    @Autowired
    private DataSource ds;

    @Autowired
    private ResultDao dao;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsertResults;
    private SimpleJdbcInsert jdbcInsertOrders;
    private SimpleJdbcInsert jdbcInsertMedics;
    private SimpleJdbcInsert jdbcInsertClinics;
    private SimpleJdbcInsert jdbcInsertStudies;
    private SimpleJdbcInsert jdbcInsertUsers;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsertUsers = new SimpleJdbcInsert(ds)
                .withTableName(USERS_TABLE_NAME)
                .usingGeneratedKeyColumns("id");
        jdbcInsertResults = new SimpleJdbcInsert(ds)
                .withTableName(RESULTS_TABLE_NAME)
                .usingGeneratedKeyColumns("id");
        jdbcInsertOrders = new SimpleJdbcInsert(ds)
                .withTableName(ORDERS_TABLE_NAME)
                .usingGeneratedKeyColumns("id");
        jdbcInsertMedics = new SimpleJdbcInsert(ds)
                .withTableName(MEDICS_TABLE_NAME);
        jdbcInsertClinics = new SimpleJdbcInsert(ds)
                .withTableName(CLINICS_TABLE_NAME);
        jdbcInsertStudies = new SimpleJdbcInsert(ds)
                .withTableName(STUDIES_TABLE_NAME)
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
        JdbcTestUtils.deleteFromTables(jdbcTemplate,STUDIES_TABLE_NAME);
    }

    @Test
    public void testFindByIdExists() {
        long dbkey = insertTestResult();

        final Optional<Result> maybeResult = dao.findById(dbkey);

        Assert.assertNotNull(maybeResult);
        Assert.assertTrue(maybeResult.isPresent());
        Assert.assertEquals(DATE,maybeResult.get().getDate());
    }

    @Test
    public void testFindByIdNotExists() {
        final Optional<Result> maybeResult = dao.findById(ZERO_ID_LONG);

        Assert.assertNotNull(maybeResult);
        Assert.assertFalse(maybeResult.isPresent());
    }

    @Test
    public void testFindByOrderIdExists() {
        long order_id = insertTestOrder();
        long dbkey1 = insertTestResult(order_id);
        long dbkey2 = insertTestResult(order_id);

        final Collection<Result> results = dao.findByOrderId(order_id);

        Assert.assertNotNull(results);
        Assert.assertEquals(2,results.size());
        Assert.assertEquals(DATE, results.stream().findFirst().get().getDate());
    }

    @Test
    public void testFindByOrderIdNotExists() {
        final Collection<Result> results = dao.findByOrderId(ZERO_ID_LONG);

        Assert.assertNotNull(results);
        Assert.assertEquals(0,results.size());
    }

    @Test
    public void testRegisterValid() {
        long order_id = insertTestOrder();

        final Result result = dao.register(order_id,DATA_TYPE,DATA,DATA_TYPE,DATA,DATE,MEDIC_NAME,MEDIC_LICENCE);

        Assert.assertArrayEquals(DATA,result.getData());
        Assert.assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate,RESULTS_TABLE_NAME));
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testRegisterInvalid() {
        dao.register(ZERO_ID_LONG,DATA_TYPE,DATA,DATA_TYPE,DATA,DATE,MEDIC_NAME,MEDIC_LICENCE);
    }

    private long insertTestResult() {
        long order_id = insertTestOrder();

        return insertTestResult(order_id);
    }

    private long insertTestResult(final long order_id) {
        Map<String,Object> insertMap = new HashMap<>();
        insertMap.put("order_id", order_id);
        insertMap.put("result_data_type", DATA_TYPE);
        insertMap.put("result_data", DATA);
        insertMap.put("identification_type", DATA_TYPE);
        insertMap.put("identification", DATA);
        insertMap.put("date", DATE);
        insertMap.put("responsible_name", MEDIC_NAME);
        insertMap.put("responsible_licence_number", MEDIC_LICENCE);

        return jdbcInsertResults.executeAndReturnKey(insertMap).longValue();
    }

    private long insertTestOrder() {
        int user_id = insertTestUser();
        int medic_id = insertTestMedic(user_id);
        int clinic_id = insertTestClinic(user_id);
        int study_id = insertTestStudy();

        Map<String,Object> insertMap = new HashMap<>();
        insertMap.put("medic_id", medic_id);
        insertMap.put("date", DATE);
        insertMap.put("clinic_id", clinic_id);
        insertMap.put("patient_name", PATIENT_NAME);
        insertMap.put("patient_email", USER_EMAIL);
        insertMap.put("study_id", study_id);
        insertMap.put("identification_type", DATA_TYPE);
        insertMap.put("identification", DATA);

        return jdbcInsertOrders.executeAndReturnKey(insertMap).longValue();

    }

    private int insertTestStudy() {
        Map<String,Object> insertMap = new HashMap<>();
        insertMap.put("name", STUDY_NAME);
        return jdbcInsertStudies.executeAndReturnKey(insertMap).intValue();
    }

    private int insertTestClinic(int user_id) {
        Map<String,Object> insertMap = new HashMap<>();
        insertMap.put("user_id", user_id);
        insertMap.put("name", CLINIC_NAME);
        insertMap.put("email", CLINIC_EMAIL);
        insertMap.put("verified", TRUE);
        jdbcInsertClinics.execute(insertMap);
        return user_id;
    }

    private int insertTestMedic(int user_id) {
        Map<String,Object> insertMap = new HashMap<>();
        insertMap.put("user_id", user_id);
        insertMap.put("name", MEDIC_NAME);
        insertMap.put("email", MEDIC_EMAIL);
        insertMap.put("licence_number", MEDIC_LICENCE);
        insertMap.put("identification_type", DATA_TYPE);
        insertMap.put("identification", DATA);
        insertMap.put("verified", TRUE);
        jdbcInsertMedics.execute(insertMap);
        return user_id;

    }

    private int insertTestUser() {
        Map<String, Object> insertMap = new HashMap<>();
        insertMap.put("email", USER_EMAIL);
        insertMap.put("password", PASSWORD);
        insertMap.put("role", ROLE);
        Number key = jdbcInsertUsers.executeAndReturnKey(insertMap);
        return key.intValue();
    }
}