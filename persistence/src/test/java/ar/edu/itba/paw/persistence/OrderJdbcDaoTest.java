package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.*;
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
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class OrderJdbcDaoTest {

    //Table names
    private static final String ORDERS_TABLE_NAME = "medical_orders";
    private static final String MEDICS_TABLE_NAME = "medics";
    private static final String CLINICS_TABLE_NAME = "clinics";
    private static final String PATIENTS_TABLE_NAME = "patients";
    private static final String STUDIES_TABLE_NAME = "medical_studies";

    //Test Order Info
    private static final Date ORDER_DATE = Date.valueOf("2020-10-05");
    private static final String ORDER_IDENTIFICATION_TYPE = "image/png";
    private static final byte[] ORDER_IDENTIFICATION = new byte[] { (byte)0xe0, 0x4f, (byte)0xd0,
            0x20, (byte)0xea, 0x3a, 0x69, 0x10, (byte)0xa2, (byte)0xd8, 0x08, 0x00, 0x2b,
            0x30, 0x30, (byte)0x9d };

    //Test Medic Info
    private static final String MEDIC_NAME = "Jhon William";
    private static final String MEDIC_EMAIL = "jhon@medic.com";
    private static final String MEDIC_LICENCE = "A21-B15";

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
    OrderJdbcDao dao;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsertOrders;
    private SimpleJdbcInsert jdbcInsertMedics;
    private SimpleJdbcInsert jdbcInsertClinics;
    private SimpleJdbcInsert jdbcInsertPatients;
    private SimpleJdbcInsert jdbcInsertStudies;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
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

        JdbcTestUtils.deleteFromTables(jdbcTemplate,ORDERS_TABLE_NAME);
        JdbcTestUtils.deleteFromTables(jdbcTemplate,MEDICS_TABLE_NAME);
        JdbcTestUtils.deleteFromTables(jdbcTemplate,CLINICS_TABLE_NAME);
        JdbcTestUtils.deleteFromTables(jdbcTemplate,PATIENTS_TABLE_NAME);
        JdbcTestUtils.deleteFromTables(jdbcTemplate,STUDIES_TABLE_NAME);
    }

    @Test
    public void testFindByIdExists() {
        long dbkey = insertTestOrder();

        final Optional<Order> maybeOrder = dao.findById(dbkey);

        Assert.assertNotNull(maybeOrder);
        Assert.assertTrue(maybeOrder.isPresent());
        Assert.assertEquals(MEDIC_NAME,maybeOrder.get().getMedic().getName());
        Assert.assertEquals(CLINIC_EMAIL,maybeOrder.get().getClinic().getEmail());
        Assert.assertEquals(STUDY_NAME,maybeOrder.get().getStudy().getName());
        Assert.assertEquals(PATIENT_NAME,maybeOrder.get().getPatient().getName());
        Assert.assertEquals(ORDER_DATE,maybeOrder.get().getDate());
    }

    @Test
    public void testFindByIdNotExists() {
        final Optional<Order> maybeOrder = dao.findById(ZERO_ID_LONG);

        Assert.assertNotNull(maybeOrder);
        Assert.assertFalse(maybeOrder.isPresent());
    }

    @Test
    public void testRegisterValid() {
        int medic_id = insertTestMedic();
        int clinic_id = insertTestClinic();
        int study_id = insertTestStudy();
        Medic medic = new Medic(medic_id,MEDIC_NAME,MEDIC_EMAIL,"",MEDIC_LICENCE);
        Clinic clinic = new Clinic(clinic_id,CLINIC_NAME,CLINIC_EMAIL,"");
        StudyType studyType = new StudyType(study_id,STUDY_NAME);
        Patient patient = new Patient(ZERO_ID_INT,PATIENT_EMAIL,PATIENT_NAME);

        final Order order = dao.register(medic,ORDER_DATE,clinic,patient,studyType,"",ORDER_IDENTIFICATION_TYPE,ORDER_IDENTIFICATION,"","");

        Assert.assertNotNull(order);
        Assert.assertEquals(PATIENT_NAME,order.getPatient().getName());
        Assert.assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate,ORDERS_TABLE_NAME));
    }

    @Test (expected = DataIntegrityViolationException.class)
    public void testRegisterInvalid() {
        Medic medic = new Medic(ZERO_ID_INT,MEDIC_NAME,MEDIC_EMAIL,"",MEDIC_LICENCE);
        Clinic clinic = new Clinic(ZERO_ID_INT,CLINIC_NAME,CLINIC_EMAIL,"");
        StudyType studyType = new StudyType(ZERO_ID_INT,STUDY_NAME);
        Patient patient = new Patient(ZERO_ID_INT,PATIENT_EMAIL,PATIENT_NAME);

        dao.register(medic,ORDER_DATE,clinic,patient,studyType,"",ORDER_IDENTIFICATION_TYPE,ORDER_IDENTIFICATION,"","");
    }

    private long insertTestOrder() {
        int medic_id = insertTestMedic();
        int clinic_id = insertTestClinic();
        int patient_id = insertTestPatient();
        int study_id = insertTestStudy();

        Map<String,Object> insertMap = new HashMap<>();
        insertMap.put("medic_id", medic_id);
        insertMap.put("date", ORDER_DATE);
        insertMap.put("clinic_id", clinic_id);
        insertMap.put("patient_id", patient_id);
        insertMap.put("study_id", study_id);
        insertMap.put("identification_type", ORDER_IDENTIFICATION_TYPE);
        insertMap.put("identification", ORDER_IDENTIFICATION);

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
