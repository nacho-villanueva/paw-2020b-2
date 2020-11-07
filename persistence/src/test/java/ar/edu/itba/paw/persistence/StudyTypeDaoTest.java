package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.StudyType;
import ar.edu.itba.paw.persistence.config.TestConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class StudyTypeDaoTest {

    //TABLE NAMES
    private static final String STUDIES_TABLE_NAME = "medical_studies";

    //TEST DATA
    private static final StudyType studyTypeTest = new StudyType(10,"TEST STUDY TEST");

    private static final StudyType studyTypeZero = new StudyType(2,"Vaccine");

    private static final List<String> studyTypeNames = new ArrayList<>(Arrays.asList("X-ray", "Vaccine", "Electrocardiography", "Allergy Test", "Rehab", "Surgery"));

    private static final int STUDY_TYPES_COUNT = studyTypeNames.size();

    // shouldn't exist
    private static final int ZERO_ID = 0;
    private static final String STUDY_NAME = "MRA";

    @Autowired
    private DataSource ds;

    @Autowired
    private StudyTypeDao dao;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);

    }

    @Test
    @Transactional
    @Rollback
    public void testFindByIdExists() {

        Optional<StudyType> maybeStudy = dao.findById(studyTypeZero.getId());

        Assert.assertNotNull(maybeStudy);
        Assert.assertTrue(maybeStudy.isPresent());
        Assert.assertEquals(studyTypeZero.getName(), maybeStudy.get().getName());
    }
    @Test
    @Transactional
    @Rollback
    public void testFindByIdNotExists() {
        final Optional<StudyType> maybeStudy = dao.findById(ZERO_ID);

        Assert.assertNotNull(maybeStudy);
        Assert.assertFalse(maybeStudy.isPresent());
    }

    @Test
    @Transactional
    @Rollback
    public void testFindByNameExists() {

        final Optional<StudyType> maybeStudy = dao.findByName(studyTypeZero.getName());

        Assert.assertNotNull(maybeStudy);
        Assert.assertTrue(maybeStudy.isPresent());
        Assert.assertEquals(studyTypeZero.getId().intValue(),maybeStudy.get().getId().intValue());
    }

    @Test
    @Transactional
    @Rollback
    public void testFindByNameNotExists() {
        final Optional<StudyType> maybeStudy = dao.findByName(STUDY_NAME);

        Assert.assertNotNull(maybeStudy);
        Assert.assertFalse(maybeStudy.isPresent());
    }

    @Test
    @Transactional
    @Rollback
    public void testGetAll() {

        Collection<StudyType> studies = dao.getAll();

        Assert.assertNotNull(studies);
        Assert.assertEquals(STUDY_TYPES_COUNT, studies.size());
        Assert.assertTrue(studies.stream().findFirst().isPresent());
        StudyType study = studies.stream().findFirst().get();
        Assert.assertTrue(studyTypeNames.contains(study.getName()));
    }
    /*
    // Currently not useful
    @Test
    @Transactional
    @Rollback
    public void testGetAllNone() {
        Collection<StudyType> studies = dao.getAll();

        Assert.assertNotNull(studies);
        Assert.assertEquals(0,studies.size());
    }
    */

    @Test
    @Transactional
    @Rollback
    public void testFindOrRegisterExists() {

        final StudyType maybeType = dao.findOrRegister(studyTypeZero.getName());

        Assert.assertNotNull(maybeType);
        Assert.assertEquals(studyTypeZero.getId().intValue(),maybeType.getId().intValue());
        Assert.assertEquals(studyTypeZero.getName(),maybeType.getName());
        Assert.assertEquals(STUDY_TYPES_COUNT,JdbcTestUtils.countRowsInTable(jdbcTemplate,STUDIES_TABLE_NAME));
    }

    @Test
    @Transactional
    @Rollback
    public void testFindOrRegisterNotExists() {
        final StudyType maybeType = dao.findOrRegister(STUDY_NAME);

        Assert.assertNotNull(maybeType);
        Assert.assertEquals(STUDY_NAME,maybeType.getName());
        Assert.assertEquals(1+STUDY_TYPES_COUNT,JdbcTestUtils.countRowsInTable(jdbcTemplate,STUDIES_TABLE_NAME));
    }

}
