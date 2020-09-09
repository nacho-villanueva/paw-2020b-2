package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Result;
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
public class ResultJdbcDaoTest {

    //Table names
    private static final String RESULTS_TABLE_NAME = "results";
    private static final String ORDERS_TABLE_NAME = "medical_orders";
    private static final String MEDICS_TABLE_NAME = "medics";
    private static final String CLINICS_TABLE_NAME = "clinics";
    private static final String PATIENTS_TABLE_NAME = "patients";
    private static final String STUDIES_TABLE_NAME = "medical_studies";

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
    private static final String PATIENT_EMAIL = "patient@zero.com";
    private static final String PATIENT_NAME = "Patient Zero";

    //Others
    private static final long ZERO_ID_LONG = 0;
    private static final int ZERO_ID_INT = 0;

    @Autowired
    DataSource ds;

    @Autowired
    ResultJdbcDao dao;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsertResults;
    private SimpleJdbcInsert jdbcInsertOrders;
    private SimpleJdbcInsert jdbcInsertMedics;
    private SimpleJdbcInsert jdbcInsertClinics;
    private SimpleJdbcInsert jdbcInsertPatients;
    private SimpleJdbcInsert jdbcInsertStudies;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsertResults = new SimpleJdbcInsert(ds)
                .withTableName(RESULTS_TABLE_NAME)
                .usingGeneratedKeyColumns("id");
        jdbcInsertOrders = new SimpleJdbcInsert(ds)
                .withTableName(ORDERS_TABLE_NAME)
                .usingGeneratedKeyColumns("id");
        jdbcInsertMedics = new SimpleJdbcInsert(ds)
                .withTableName(MEDICS_TABLE_NAME)
                .usingGeneratedKeyColumns("id");
        jdbcInsertClinics = new SimpleJdbcInsert(ds)
                .withTableName(CLINICS_TABLE_NAME)
                .usingGeneratedKeyColumns("id");
        jdbcInsertPatients = new SimpleJdbcInsert(ds)
                .withTableName(PATIENTS_TABLE_NAME)
                .usingGeneratedKeyColumns("id");
        jdbcInsertStudies = new SimpleJdbcInsert(ds)
                .withTableName(STUDIES_TABLE_NAME)
                .usingGeneratedKeyColumns("id");

        JdbcTestUtils.deleteFromTables(jdbcTemplate,RESULTS_TABLE_NAME);
        JdbcTestUtils.deleteFromTables(jdbcTemplate,ORDERS_TABLE_NAME);
        JdbcTestUtils.deleteFromTables(jdbcTemplate,MEDICS_TABLE_NAME);
        JdbcTestUtils.deleteFromTables(jdbcTemplate,CLINICS_TABLE_NAME);
        JdbcTestUtils.deleteFromTables(jdbcTemplate,PATIENTS_TABLE_NAME);
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

        Assert.assertNotNull(result);
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
        int medic_id = insertTestMedic();
        int clinic_id = insertTestClinic();
        int patient_id = insertTestPatient();
        int study_id = insertTestStudy();

        Map<String,Object> insertMap = new HashMap<>();
        insertMap.put("medic_id", medic_id);
        insertMap.put("date", DATE);
        insertMap.put("clinic_id", clinic_id);
        insertMap.put("patient_id", patient_id);
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

    private int insertTestPatient() {
        Map<String,Object> insertMap = new HashMap<>();
        insertMap.put("name", PATIENT_NAME);
        insertMap.put("email", PATIENT_EMAIL);
        return jdbcInsertPatients.executeAndReturnKey(insertMap).intValue();
    }

    private int insertTestClinic() {
        Map<String,Object> insertMap = new HashMap<>();
        insertMap.put("name", CLINIC_NAME);
        insertMap.put("email", CLINIC_EMAIL);
        return jdbcInsertClinics.executeAndReturnKey(insertMap).intValue();
    }

    private int insertTestMedic() {
        Map<String,Object> insertMap = new HashMap<>();
        insertMap.put("name", MEDIC_NAME);
        insertMap.put("email", MEDIC_EMAIL);
        insertMap.put("licence_number", MEDIC_LICENCE);
        return jdbcInsertMedics.executeAndReturnKey(insertMap).intValue();
    }
}
