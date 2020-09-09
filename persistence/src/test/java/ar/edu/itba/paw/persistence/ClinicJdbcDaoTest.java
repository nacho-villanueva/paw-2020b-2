package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Clinic;
import ar.edu.itba.paw.model.StudyType;
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
public class ClinicJdbcDaoTest {

    private static final String TABLE_NAME = "clinics";
    private static final String RELATION_TABLE_NAME = "clinic_available_studies";
    private static final String NAME = "Zero's Clinic";
    private static final String NAME_ALT = "One's Clinic";
    private static final String EMAIL = "clinic@zero.com";
    private static final String EMAIL_ALT = "clinic@one.com";
    private static final String TELEPHONE = "+011-00000000";
    private static final String STUDY_NAME = "MRA";
    private static final String STUDY_NAME_ALT = "Colonoscopy";
    private static final int ZERO_ID = 0;


    @Autowired
    private DataSource ds;

    @Autowired
    private ClinicJdbcDao dao;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;
    private Collection<StudyType> available_studies;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns("id");

        JdbcTestUtils.deleteFromTables(jdbcTemplate,RELATION_TABLE_NAME);
        JdbcTestUtils.deleteFromTables(jdbcTemplate,TABLE_NAME);
    }

    @Test
    public void testFindByIdExists() {
        int dbkey = insertTestClinic();

        final Optional<Clinic> maybeClinic = dao.findById(dbkey);

        Assert.assertNotNull(maybeClinic);
        Assert.assertTrue(maybeClinic.isPresent());
        Assert.assertEquals(NAME, maybeClinic.get().getName());
    }

    @Test
    public void testFindByIdNotExists() {
        final Optional<Clinic> maybeClinic = dao.findById(ZERO_ID);

        Assert.assertNotNull(maybeClinic);
        Assert.assertFalse(maybeClinic.isPresent());
    }

    @Test
    public void testGetAll() {
        insertMultipleTestClinic();

        final Collection<Clinic> clinics = dao.getAll();

        Assert.assertNotNull(clinics);
        Assert.assertEquals(2,clinics.size());
        Clinic clinic = clinics.stream().findFirst().get();
        Assert.assertTrue(clinic.getEmail().equals(EMAIL) || clinic.getEmail().equals(EMAIL_ALT));
    }

    @Test
    public void testGetAllNone() {
        final Collection<Clinic> clinics = dao.getAll();

        Assert.assertNotNull(clinics);
        Assert.assertEquals(0,clinics.size());
    }

    @Test
    public void testRegisterStudyToClinic() {
        int dbkey = insertTestClinic();
        StudyType studyType = new StudyType(ZERO_ID, STUDY_NAME);

        StudyType study = dao.registerStudyToClinic(dbkey, studyType);

        Assert.assertNotNull(study);
        Assert.assertEquals(STUDY_NAME, study.getName());
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate,RELATION_TABLE_NAME));
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testRegisterStudyToClinicNotExists() {
        StudyType studyType = new StudyType(ZERO_ID, STUDY_NAME);

        dao.registerStudyToClinic(ZERO_ID, studyType);
    }

    @Test
    public void testRegisterValid() {
        available_studies = new ArrayList<>();
        available_studies.add(new StudyType(ZERO_ID, STUDY_NAME));
        available_studies.add(new StudyType(ZERO_ID, STUDY_NAME_ALT));

        final Clinic clinic = dao.register(NAME,EMAIL,TELEPHONE,available_studies);

        Assert.assertNotNull(clinic);
        Assert.assertEquals(NAME, clinic.getName());
        Assert.assertFalse(clinic.getMedical_studies().isEmpty());
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, TABLE_NAME));
    }

    @Test(expected = DuplicateKeyException.class)
    public void testRegisterAlreadyExists() {
        insertTestClinic();

        dao.register(NAME, EMAIL, TELEPHONE, new ArrayList<>());
    }

    private int insertClinic(String name, String email) {
        Map<String,Object> insertMap = new HashMap<>();
        insertMap.put("name", name);
        insertMap.put("email", email);
        insertMap.put("telephone", TELEPHONE);
        return jdbcInsert.executeAndReturnKey(insertMap).intValue();
    }

    private int insertTestClinic() {
        return insertClinic(NAME, EMAIL);
    }

    private void insertMultipleTestClinic() {
        insertClinic(NAME, EMAIL);
        insertClinic(NAME_ALT, EMAIL_ALT);
    }
}
