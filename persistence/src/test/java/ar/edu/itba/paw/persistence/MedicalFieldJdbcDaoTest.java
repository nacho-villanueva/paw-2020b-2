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

    private static final String MEDICAL_FIELDS_TABLE_NAME = "medical_fields";
    private static final String MEDIC_TABLE_NAME = "medics";
    private static final String FIELDS_MEDIC_TABLE_NAME = "medic_medical_fields";
    private static final String MEDIC_NAME = "Jhon William";
    private static final String MEDIC_EMAIL = "Jhon@medic.com";
    private static final String MEDIC_LICENCE = "A21-41512";
    private static final String FIELD_NAME = "Oncology";
    private static final String FIELD_NAME_ALT = "Neurology";
    private static final int ZERO_ID = 0;

    @Autowired
    DataSource ds;

    @Autowired
    MedicalFieldJdbcDao dao;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;
    private SimpleJdbcInsert jdbcInsertMedic;
    private SimpleJdbcInsert jdbcInsertFieldsForMedic;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(MEDICAL_FIELDS_TABLE_NAME)
                .usingGeneratedKeyColumns("id");

        jdbcInsertMedic = new SimpleJdbcInsert(ds)
                .withTableName(MEDIC_TABLE_NAME)
                .usingGeneratedKeyColumns("id");

        jdbcInsertFieldsForMedic = new SimpleJdbcInsert(ds)
                .withTableName(FIELDS_MEDIC_TABLE_NAME);

        JdbcTestUtils.deleteFromTables(jdbcTemplate, FIELDS_MEDIC_TABLE_NAME);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, MEDICAL_FIELDS_TABLE_NAME);
        JdbcTestUtils.deleteFromTables(jdbcTemplate,MEDIC_TABLE_NAME);
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
        int medic_id = insertMedic();
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

    @Test
    public void testRegisterValid() {
        final MedicalField medicalField = dao.register(FIELD_NAME);

        Assert.assertNotNull(medicalField);
        Assert.assertEquals(FIELD_NAME,medicalField.getName());
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate,MEDICAL_FIELDS_TABLE_NAME));
    }

    @Test(expected = DuplicateKeyException.class)
    public void testRegisterAlreadyExists() {
        insertTestField();

        dao.register(FIELD_NAME);
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

    private int insertMedic() {
        Map<String,Object> insertMap = new HashMap<>();
        insertMap.put("name", MEDIC_NAME);
        insertMap.put("email", MEDIC_EMAIL);
        insertMap.put("licence_number", MEDIC_LICENCE);
        return jdbcInsertMedic.executeAndReturnKey(insertMap).intValue();
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
}
