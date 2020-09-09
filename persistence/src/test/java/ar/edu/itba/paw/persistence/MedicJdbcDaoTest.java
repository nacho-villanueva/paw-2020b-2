package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Medic;
import ar.edu.itba.paw.model.MedicalField;
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

    private static final String MEDICS_TABLE_NAME = "medics";
    private static final String RELATION_TABLE_NAME = "medic_medical_fields";
    private static final String MEDIC_NAME = "Jhon William";
    private static final String MEDIC_EMAIL = "jhon@medic.com";
    private static final String MEDIC_EMAIL_ALT = "gus@medic.com";
    private static final String MEDIC_TELEPHONE = "1111111111";
    private static final String MEDIC_LICENCE = "A21-B15";
    private static final String FIELD_NAME = "Oncology";
    private static final String FIELD_NAME_ALT = "Neurology";
    private static final int ZERO_ID = 0;

    @Autowired
    DataSource ds;

    @Autowired
    MedicJdbcDao dao;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;
    Collection<MedicalField> known_fields;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(MEDICS_TABLE_NAME)
                .usingGeneratedKeyColumns("id");

        JdbcTestUtils.deleteFromTables(jdbcTemplate,RELATION_TABLE_NAME);
        JdbcTestUtils.deleteFromTables(jdbcTemplate,MEDICS_TABLE_NAME);
    }

    @Test
    public void testFindByIdExists() {
        int dbkey = insertTestMedic();

        final Optional<Medic> maybeMedic = dao.findById(dbkey);

        Assert.assertNotNull(maybeMedic);
        Assert.assertTrue(maybeMedic.isPresent());
        Assert.assertEquals(MEDIC_EMAIL,maybeMedic.get().getEmail());
    }

    @Test
    public void testFindByIdNotExists() {

        final Optional<Medic> maybeMedic = dao.findById(ZERO_ID);

        Assert.assertNotNull(maybeMedic);
        Assert.assertFalse(maybeMedic.isPresent());

    }

    @Test
    public void testGetAllExists() {
        insertMultipleTestMedics();

        final Collection<Medic> medics = dao.getAll();

        Assert.assertNotNull(medics);
        Assert.assertEquals(2,medics.size());
        Assert.assertEquals(MEDIC_NAME,medics.stream().findFirst().get().getName());
    }

    @Test
    public void testGetAllNotExists() {
        final Collection<Medic> medics = dao.getAll();

        Assert.assertNotNull(medics);
        Assert.assertEquals(0,medics.size());
    }

    @Test
    public void testRegisterValid() {
        known_fields = new ArrayList<>();
        known_fields.add(new MedicalField(ZERO_ID,FIELD_NAME));
        known_fields.add(new MedicalField(ZERO_ID,FIELD_NAME_ALT));

        final Medic medic = dao.register(MEDIC_NAME, MEDIC_EMAIL, MEDIC_TELEPHONE, MEDIC_LICENCE, known_fields);

        Assert.assertNotNull(medic);
        Assert.assertEquals(MEDIC_EMAIL, medic.getEmail());
        Assert.assertFalse(medic.getMedical_fields().isEmpty());
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, MEDICS_TABLE_NAME));
    }

    @Test(expected = DuplicateKeyException.class)
    public void testRegisterAlreadyExists() {
        insertTestMedic();

        dao.register(MEDIC_NAME,MEDIC_EMAIL,MEDIC_TELEPHONE,MEDIC_LICENCE,new ArrayList<>());
    }

    @Test
    public void testRegisterFieldToMedic() {
        int dbkey = insertTestMedic();
        MedicalField medicalField = new MedicalField(ZERO_ID,FIELD_NAME);

        MedicalField field = dao.registerFieldToMedic(dbkey,medicalField);

        Assert.assertNotNull(field);
        Assert.assertEquals(FIELD_NAME,field.getName());
        Assert.assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate,RELATION_TABLE_NAME));
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testRegisterFieldToMedicNotExist() {
        MedicalField medicalField = new MedicalField(ZERO_ID,FIELD_NAME);

        dao.registerFieldToMedic(ZERO_ID,medicalField);
    }

    private int insertTestMedic() {
        return insertMedic(MEDIC_EMAIL);
    }

    private void insertMultipleTestMedics() {
        insertMedic(MEDIC_EMAIL);
        insertMedic(MEDIC_EMAIL_ALT);
    }

    private int insertMedic(String email) {
        Map<String, Object> insertMap = new HashMap<>();
        insertMap.put("name", MEDIC_NAME);
        insertMap.put("email", email);
        insertMap.put("telephone", MEDIC_TELEPHONE);
        insertMap.put("licence_number", MEDIC_LICENCE);

        return jdbcInsert.executeAndReturnKey(insertMap).intValue();
    }
}
