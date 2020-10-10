package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Medic;
import ar.edu.itba.paw.model.MedicalField;
import ar.edu.itba.paw.model.StudyType;
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
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class MedicJdbcDaoTest {

    //TABLE NAMES
    private static final String USERS_TABLE_NAME = "users";
    private static final String CLINICS_TABLE_NAME = "clinics";
    private static final String CLINICS_RELATION_TABLE_NAME = "clinic_available_studies";
    private static final String MEDICS_TABLE_NAME = "medics";
    private static final String MEDICS_RELATION_TABLE_NAME = "medic_medical_fields";
    private static final String PATIENTS_TABLE_NAME = "patients";
    private static final String ORDERS_TABLE_NAME = "medical_orders";
    private static final String RESULTS_TABLE_NAME = "results";
    private static final String MEDICAL_FIELDS_TABLE_NAME = "medical_fields";


    private static final String MEDIC_NAME = "Jhon William";
    private static final String MEDIC_NAME_ALT = "Ron Weasly";
    private static final String MEDIC_TELEPHONE = "1111111111";
    private static final String MEDIC_LICENCE = "A21-B15";
    private static final String FIELD_NAME = "Oncology";
    private static final String FIELD_NAME_ALT = "Neurology";
    private static final int ZERO_ID = 0;
    private static final boolean TRUE = true;

    private static final String MEDIC_IDENTIFICATION_TYPE = "image/png";
    private static final byte[] MEDIC_IDENTIFICATION = new byte[] { (byte)0xe0, 0x4f, (byte)0xd0,
            0x20, (byte)0xea, 0x3a, 0x69, 0x10, (byte)0xa2, (byte)0xd8, 0x08, 0x00, 0x2b,
            0x30, 0x30, (byte)0x9d };

    //USER INFO
    private static final String USER_EMAIL = "patient@zero.com";
    private static final String USER_EMAIL_ALT = "patient@one.com";
    private static final String PASSWORD = "GroundZer0";
    private static final int ROLE = 2;


    @Autowired
    private DataSource ds;

    @Autowired
    private MedicJdbcDao dao;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;
    private SimpleJdbcInsert jdbcInsertUsers;
    private SimpleJdbcInsert jdbcInsertMedicalField;
    private SimpleJdbcInsert jdbcInsertRelation;
    Collection<MedicalField> known_fields;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(MEDICS_TABLE_NAME);
        jdbcInsertUsers = new SimpleJdbcInsert(ds)
                .withTableName(USERS_TABLE_NAME)
                .usingGeneratedKeyColumns("id");
        jdbcInsertMedicalField = new SimpleJdbcInsert(ds)
                .withTableName(MEDICAL_FIELDS_TABLE_NAME)
                .usingGeneratedKeyColumns("id");
        jdbcInsertRelation = new SimpleJdbcInsert(ds)
                .withTableName(MEDICS_RELATION_TABLE_NAME);

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
        int dbkey = insertTestMedic();

        final Optional<Medic> maybeMedic = dao.findByUserId(dbkey);

        Assert.assertTrue(maybeMedic.isPresent());
        Assert.assertEquals(USER_EMAIL,maybeMedic.get().getEmail());
    }

    @Test
    public void testFindByIdNotExists() {

        final Optional<Medic> maybeMedic = dao.findByUserId(ZERO_ID);

        Assert.assertFalse(maybeMedic.isPresent());

    }

    @Test
    public void testGetAllExists() {
        insertMultipleTestMedics();

        final Collection<Medic> medics = dao.getAll();

        Assert.assertNotNull(medics);
        Assert.assertEquals(2,medics.size());
        Assert.assertArrayEquals(MEDIC_IDENTIFICATION,medics.stream().findFirst().get().getIdentification());
    }

    @Test
    public void testGetAllNotExists() {
        final Collection<Medic> medics = dao.getAll();

        Assert.assertNotNull(medics);
        Assert.assertEquals(0,medics.size());
    }

    @Test
    public void testRegisterValid() {
        int userkey = insertTestUser(USER_EMAIL);
        known_fields = new ArrayList<>();
        known_fields.add(new MedicalField(ZERO_ID,FIELD_NAME));
        known_fields.add(new MedicalField(ZERO_ID,FIELD_NAME_ALT));

        final Medic medic = dao.register(new User(userkey,USER_EMAIL,PASSWORD,ROLE),MEDIC_NAME, MEDIC_TELEPHONE, MEDIC_IDENTIFICATION_TYPE, MEDIC_IDENTIFICATION, MEDIC_LICENCE, known_fields, false);

        Assert.assertNotNull(medic);
        MedicalField field = medic.getMedical_fields().stream().findFirst().get();
        Assert.assertTrue(field.getName().equals(FIELD_NAME) || field.getName().equals(FIELD_NAME_ALT));
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, MEDICS_TABLE_NAME));
    }

    @Test(expected = DuplicateKeyException.class)
    public void testRegisterAlreadyExists() {
        int userkey = insertTestMedic();

        dao.register(new User(userkey,USER_EMAIL,PASSWORD,ROLE),MEDIC_NAME,MEDIC_TELEPHONE,MEDIC_IDENTIFICATION_TYPE,MEDIC_IDENTIFICATION,MEDIC_LICENCE,new ArrayList<>(), false);
    }

    @Test
    public void testRegisterFieldToMedic() {
        int dbkey = insertTestMedic();
        MedicalField medicalField = new MedicalField(ZERO_ID,FIELD_NAME);

        MedicalField field = dao.registerFieldToMedic(dbkey,medicalField);

        Assert.assertNotNull(field);
        Assert.assertEquals(FIELD_NAME,field.getName());
        Assert.assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate,MEDICS_RELATION_TABLE_NAME));
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testRegisterFieldToMedicNotExist() {
        MedicalField medicalField = new MedicalField(ZERO_ID,FIELD_NAME);

        dao.registerFieldToMedic(ZERO_ID,medicalField);
    }

    @Test
    public void testKnowsField() {
        int userkey = insertTestMedic();
        int fieldkey = insertTestMedicalField(FIELD_NAME);
        insertTestRelation(userkey,fieldkey);

        boolean ret = dao.knowsField(userkey,fieldkey);

        Assert.assertTrue(ret);
    }

    @Test
    public void testKnowsFieldFalse() {
        int userkey = insertTestMedic();
        int fieldkey = insertTestMedicalField(FIELD_NAME);

        boolean ret = dao.knowsField(userkey,fieldkey);

        Assert.assertFalse(ret);
    }

    @Test
    public void testUpdateMedicInfo() {
        int userkey = insertTestMedic();
        int fieldkey = insertTestMedicalField(FIELD_NAME);
        int fieldkeyalt = insertTestMedicalField(FIELD_NAME_ALT);
        insertTestRelation(userkey,fieldkey);

        known_fields = new ArrayList<>();
        known_fields.add(new MedicalField(fieldkey,FIELD_NAME));
        known_fields.add(new MedicalField(fieldkeyalt, FIELD_NAME_ALT));

        Medic medic = dao.updateMedicInfo(new User(userkey,USER_EMAIL,PASSWORD,ROLE),MEDIC_NAME_ALT,MEDIC_TELEPHONE,MEDIC_IDENTIFICATION_TYPE,MEDIC_IDENTIFICATION,MEDIC_LICENCE,known_fields,TRUE);

        Assert.assertEquals(known_fields.size(), medic.getMedical_fields().size());
        Assert.assertEquals(1,JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,MEDICS_TABLE_NAME,"name = '" + MEDIC_NAME_ALT + "' AND licence_number = '" + MEDIC_LICENCE + "' AND user_id = " + userkey));
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,MEDICS_RELATION_TABLE_NAME, "medic_id = " + userkey + " AND field_id = " + fieldkey));
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,MEDICS_RELATION_TABLE_NAME, "medic_id = " + userkey + " AND field_id = " + fieldkeyalt));
    }

    private int insertTestMedicalField(final String fieldName) {
        Map<String, Object> insertMap = new HashMap<>();
        insertMap.put("name", fieldName);
        Number key = jdbcInsertMedicalField.executeAndReturnKey(insertMap);
        return key.intValue();
    }

    private void insertTestRelation(final int medic_id, final int field_id) {
        Map<String, Object> insertMap = new HashMap<>();
        insertMap.put("medic_id", medic_id);
        insertMap.put("field_id",field_id);
        jdbcInsertRelation.execute(insertMap);
    }

    private int insertTestMedic() {
        int userkey = insertTestUser(USER_EMAIL);
        insertMedic(userkey);
        return userkey;
    }

    private void insertMultipleTestMedics() {
        int userkey = insertTestUser(USER_EMAIL);
        insertMedic(userkey);
        userkey = insertTestUser(USER_EMAIL_ALT);
        insertMedic(userkey);
    }

    private void insertMedic(int user_id) {
        Map<String, Object> insertMap = new HashMap<>();
        insertMap.put("user_id",user_id);
        insertMap.put("name", MEDIC_NAME);
        insertMap.put("telephone", MEDIC_TELEPHONE);
        insertMap.put("identification_type",MEDIC_IDENTIFICATION_TYPE);
        insertMap.put("identification",MEDIC_IDENTIFICATION);
        insertMap.put("licence_number", MEDIC_LICENCE);
        insertMap.put("verified", TRUE);

        jdbcInsert.execute(insertMap);
    }

    private int insertTestUser(final String email) {
        Map<String, Object> insertMap = new HashMap<>();
        insertMap.put("email", email);
        insertMap.put("password", PASSWORD);
        insertMap.put("role", ROLE);
        Number key = jdbcInsertUsers.executeAndReturnKey(insertMap);
        return key.intValue();
    }
}
