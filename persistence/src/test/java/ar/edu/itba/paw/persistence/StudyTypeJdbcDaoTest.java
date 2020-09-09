package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.StudyType;
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
public class StudyTypeJdbcDaoTest {

    private static final String STUDIES_TABLE_NAME = "medical_studies";
    private static final String CLINICS_TABLE_NAME = "clinics";
    private static final String RELATION_TABLE_NAME = "clinic_available_studies";
    private static final String STUDY_NAME = "MRA";
    private static final String STUDY_NAME_ALT = "Colonoscopy";
    private static final String CLINIC_NAME = "Zero's clinic";
    private static final String CLINIC_EMAIL = "zero@clinic.com";
    private static final int ZERO_ID = 0;

    @Autowired
    DataSource ds;

    @Autowired
    StudyTypeJdbcDao dao;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsertStudies;
    private SimpleJdbcInsert jdbcInsertClinics;
    private SimpleJdbcInsert jdbcInsertRelation;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);

        jdbcInsertStudies = new SimpleJdbcInsert(ds)
                .withTableName(STUDIES_TABLE_NAME)
                .usingGeneratedKeyColumns("id");

        jdbcInsertClinics = new SimpleJdbcInsert(ds)
                .withTableName(CLINICS_TABLE_NAME)
                .usingGeneratedKeyColumns("id");

        jdbcInsertRelation = new SimpleJdbcInsert(ds)
                .withTableName(RELATION_TABLE_NAME);

        JdbcTestUtils.deleteFromTables(jdbcTemplate,RELATION_TABLE_NAME);
        JdbcTestUtils.deleteFromTables(jdbcTemplate,CLINICS_TABLE_NAME);
        JdbcTestUtils.deleteFromTables(jdbcTemplate,STUDIES_TABLE_NAME);
    }

    @Test
    public void testFindByIdExists() {
        int dbkey = insertTestStudy();

        final Optional<StudyType> maybeStudy = dao.findById(dbkey);

        Assert.assertNotNull(maybeStudy);
        Assert.assertTrue(maybeStudy.isPresent());
        Assert.assertEquals(STUDY_NAME, maybeStudy.get().getName());
    }

    @Test
    public void testFindByIdNotExists() {
        final Optional<StudyType> maybeStudy = dao.findById(ZERO_ID);

        Assert.assertNotNull(maybeStudy);
        Assert.assertFalse(maybeStudy.isPresent());
    }

    @Test
    public void testFindByNameExists() {
        int dbkey = insertTestStudy();

        final Optional<StudyType> maybeStudy = dao.findByName(STUDY_NAME);

        Assert.assertNotNull(maybeStudy);
        Assert.assertTrue(maybeStudy.isPresent());
        Assert.assertEquals(dbkey,maybeStudy.get().getId());
    }

    @Test
    public void testFindByNameNotExists() {
        final Optional<StudyType> maybeStudy = dao.findByName(STUDY_NAME);

        Assert.assertNotNull(maybeStudy);
        Assert.assertFalse(maybeStudy.isPresent());
    }

    @Test
    public void testGetAll() {
        insertMultipleStudies();

        Collection<StudyType> studies = dao.getAll();

        Assert.assertNotNull(studies);
        Assert.assertEquals(2, studies.size());
        StudyType study = studies.stream().findFirst().get();
        Assert.assertTrue(study.getName().equals(STUDY_NAME) || study.getName().equals(STUDY_NAME_ALT));
    }

    @Test
    public void testGetAllNone() {
        Collection<StudyType> studies = dao.getAll();

        Assert.assertNotNull(studies);
        Assert.assertEquals(0,studies.size());
    }

    @Test
    public void testFindByClinicIdExists() {
        int clinic_id = insertClinic();
        insertStudiesIntoClinic(clinic_id);

        final Collection<StudyType> studiesForClinic = dao.findByClinicId(clinic_id);

        Assert.assertNotNull(studiesForClinic);
        Assert.assertEquals(2,studiesForClinic.size());
        StudyType studyType = studiesForClinic.stream().findFirst().get();
        Assert.assertTrue(studyType.getName().equals(STUDY_NAME) || studyType.getName().equals(STUDY_NAME_ALT));
    }

    @Test
    public void testFindByClinicIdNotExists() {
        final Collection<StudyType> studiesForClinic = dao.findByClinicId(ZERO_ID);

        Assert.assertNotNull(studiesForClinic);
        Assert.assertEquals(0,studiesForClinic.size());
    }

    @Test
    public void testFindOrRegisterExists() {
        int dbkey = insertTestStudy();

        final StudyType studyType = dao.findOrRegister(STUDY_NAME);

        Assert.assertNotNull(studyType);
        Assert.assertEquals(dbkey,studyType.getId());
        Assert.assertEquals(STUDY_NAME,studyType.getName());
    }

    @Test
    public void testFindOrRegisterNotExists() {
        final StudyType studyType = dao.findOrRegister(STUDY_NAME);

        Assert.assertNotNull(studyType);
        Assert.assertEquals(STUDY_NAME,studyType.getName());
        Assert.assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate,STUDIES_TABLE_NAME));
    }

    @Test
    public void testRegisterValid() {
        final StudyType studyType = dao.register(STUDY_NAME);

        Assert.assertNotNull(studyType);
        Assert.assertEquals(STUDY_NAME,studyType.getName());
        Assert.assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate,STUDIES_TABLE_NAME));
    }

    @Test(expected = DuplicateKeyException.class)
    public void testRegisterAlreadyExists() {
        insertTestStudy();

        dao.register(STUDY_NAME);
    }

    private int insertTestStudy() {
        return insertStudy(STUDY_NAME);
    }

    private int insertStudy(final String name) {
        Map<String,Object> insertMap = new HashMap<>();
        insertMap.put("name", name);
        return jdbcInsertStudies.executeAndReturnKey(insertMap).intValue();
    }

    private void insertMultipleStudies() {
        insertStudy(STUDY_NAME);
        insertStudy(STUDY_NAME_ALT);
    }

    private int insertClinic() {
        Map<String,Object> insertMap = new HashMap<>();
        insertMap.put("name", CLINIC_NAME);
        insertMap.put("email", CLINIC_EMAIL);
        return jdbcInsertClinics.executeAndReturnKey(insertMap).intValue();
    }

    private void insertStudiesIntoClinic(final int clinic_id) {
        int studykey = insertStudy(STUDY_NAME);
        Map<String,Object> insertMap = new HashMap<>();
        insertMap.put("clinic_id",clinic_id);
        insertMap.put("study_id",studykey);
        jdbcInsertRelation.execute(insertMap);

        studykey = insertStudy(STUDY_NAME_ALT);
        insertMap.put("study_id",studykey);
        jdbcInsertRelation.execute(insertMap);
    }

}
