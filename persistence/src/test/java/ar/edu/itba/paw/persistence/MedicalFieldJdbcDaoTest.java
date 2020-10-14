package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.MedicalField;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class MedicalFieldJdbcDaoTest {

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

    private static final String MEDICAL_FIELDS_TABLE_NAME = "medical_fields";
    private static final String MEDIC_NAME = "Jhon William";
    private static final String MEDIC_EMAIL = "Jhon@medic.com";
    private static final String MEDIC_LICENCE = "A21-41512";
    private static final String FIELD_NAME = "Oncology";
    private static final String FIELD_NAME_ALT = "Neurology";
    private static final int ZERO_ID = 0;

    private static final String MEDIC_IDENTIFICATION_TYPE = "image/png";
    private static final byte[] MEDIC_IDENTIFICATION = new byte[] { (byte)0xe0, 0x4f, (byte)0xd0,
            0x20, (byte)0xea, 0x3a, 0x69, 0x10, (byte)0xa2, (byte)0xd8, 0x08, 0x00, 0x2b,
            0x30, 0x30, (byte)0x9d };
    private static final boolean TRUE = true;

    //USER INFO
    private static final String USER_EMAIL = "patient@zero.com";
    private static final String USER_EMAIL_ALT = "patient@one.com";
    private static final String PASSWORD = "GroundZer0";
    private static final int ROLE = 2;

    @Autowired
    private DataSource ds;

    @Autowired
    private MedicalFieldJdbcDao dao;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;
    private SimpleJdbcInsert jdbcInsertMedic;
    private SimpleJdbcInsert jdbcInsertFieldsForMedic;
    private SimpleJdbcInsert jdbcInsertUsers;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(MEDICAL_FIELDS_TABLE_NAME)
                .usingGeneratedKeyColumns("id");

        jdbcInsertMedic = new SimpleJdbcInsert(ds)
                .withTableName(MEDICS_TABLE_NAME);

        jdbcInsertFieldsForMedic = new SimpleJdbcInsert(ds)
                .withTableName(MEDICS_RELATION_TABLE_NAME);

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
        JdbcTestUtils.deleteFromTables(jdbcTemplate,MEDICAL_FIELDS_TABLE_NAME);
    }

    @Test
    public void testFindByIdExists() {
        int dbkey = insertTestField();

        final Optional<MedicalField> maybeField = dao.findById(dbkey);

        Assert.assertNotNull(maybeField);
        Assert.assertTrue(maybeField.isPresent());
        Assert.assertEquals(FIELD_NAME, maybeField.get().getName());
    }

    @Test
    public void testFindByIdNotExists() {
        final Optional<MedicalField> maybeField = dao.findById(ZERO_ID);

        Assert.assertNotNull(maybeField);
        Assert.assertFalse(maybeField.isPresent());
    }

    @Test
    public void testFindByNameExists() {
        int dbkey = insertTestField();

        final Optional<MedicalField> maybeField = dao.findByName(FIELD_NAME);

        Assert.assertNotNull(maybeField);
        Assert.assertTrue(maybeField.isPresent());
        Assert.assertEquals(dbkey,maybeField.get().getId());
    }

    @Test
    public void testFindByNameNotExists() {
        final Optional<MedicalField> maybeField = dao.findByName(FIELD_NAME);

        Assert.assertNotNull(maybeField);
        Assert.assertFalse(maybeField.isPresent());
    }

    @Test
    public void testGetAllExists() {
        insertMultipleFields();

        Collection<MedicalField> fields = dao.getAll();

        Assert.assertNotNull(fields);
        Assert.assertEquals(2, fields.size());
        MedicalField field = fields.stream().findFirst().get();
        Assert.assertTrue(field.getName().equals(FIELD_NAME) || field.getName().equals(FIELD_NAME_ALT));
    }

    @Test
    public void testGetAllEmpty() {
        Collection<MedicalField> fields = dao.getAll();

        Assert.assertNotNull(fields);
        Assert.assertEquals(0, fields.size());
    }

    @Test
    public void findByMedicIdExists() {
        int userkey = insertTestUser();
        int medic_id = insertMedic(userkey);
        insertFieldsIntoMedic(medic_id);

        final Collection<MedicalField> fieldsForMedic = dao.findByMedicId(medic_id);

        Assert.assertNotNull(fieldsForMedic);
        Assert.assertEquals(2,fieldsForMedic.size());
        MedicalField medicalField = fieldsForMedic.stream().findFirst().get();
        Assert.assertTrue(medicalField.getName().equals(FIELD_NAME) || medicalField.getName().equals(FIELD_NAME_ALT));
    }

    @Test
    public void findByMedicIdBadId() {
        final Collection<MedicalField> fieldsForMedic = dao.findByMedicId(ZERO_ID);

        Assert.assertNotNull(fieldsForMedic);
        Assert.assertEquals(0, fieldsForMedic.size());
    }

    @Test
    public void testFindOrRegisterExists() {
        int dbkey = insertTestField();

        final MedicalField medicalField = dao.findOrRegister(FIELD_NAME);

        Assert.assertNotNull(medicalField);
        Assert.assertEquals(dbkey,medicalField.getId());
        Assert.assertEquals(FIELD_NAME,medicalField.getName());
    }

    @Test
    public void testFindOrRegisterNotExists() {
        final MedicalField medicalField = dao.findOrRegister(FIELD_NAME);

        Assert.assertNotNull(medicalField);
        Assert.assertEquals(FIELD_NAME, medicalField.getName());
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, MEDICAL_FIELDS_TABLE_NAME));
    }

    private int insertTestField() {
        return insertField(FIELD_NAME);
    }

    private int insertField(final String name) {
        Map<String,Object> insertMap = new HashMap<>();
        insertMap.put("name", name);
        return jdbcInsert.executeAndReturnKey(insertMap).intValue();
    }

    private void insertMultipleFields() {
        insertField(FIELD_NAME);
        insertField(FIELD_NAME_ALT);
    }

    private int insertMedic(final int user_id) {
        Map<String,Object> insertMap = new HashMap<>();
        insertMap.put("user_id", user_id);
        insertMap.put("name", MEDIC_NAME);
        insertMap.put("email", MEDIC_EMAIL);
        insertMap.put("identification_type", MEDIC_IDENTIFICATION_TYPE);
        insertMap.put("identification", MEDIC_IDENTIFICATION);
        insertMap.put("licence_number", MEDIC_LICENCE);
        insertMap.put("verified", TRUE);
        jdbcInsertMedic.execute(insertMap);
        return user_id;
    }

    private void insertFieldsIntoMedic(final int medic_id) {
        int fieldkey = insertField(FIELD_NAME);
        Map<String,Object> insertMap = new HashMap<>();
        insertMap.put("medic_id",medic_id);
        insertMap.put("field_id",fieldkey);
        jdbcInsertFieldsForMedic.execute(insertMap);

        fieldkey = insertField(FIELD_NAME_ALT);
        insertMap.put("field_id",fieldkey);
        jdbcInsertFieldsForMedic.execute(insertMap);
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
