package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Clinic;
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
public class ClinicJdbcDaoTest {

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

    private static final String NAME = "Zero's Clinic";
    private static final String NAME_ALT = "One's Clinic";
    private static final String TELEPHONE = "+011-00000000";
    private static final String STUDY_NAME = "MRA";
    private static final String STUDY_NAME_ALT = "Colonoscopy";
    private static final int ZERO_ID = 0;
    private static final boolean TRUE = true;

    //USER INFO
    private static final String USER_EMAIL = "patient@zero.com";
    private static final String USER_EMAIL_ALT = "patient@one.com";
    private static final String PASSWORD = "GroundZer0";
    private static final int ROLE = 3;


    @Autowired
    private DataSource ds;

    @Autowired
    private ClinicJdbcDao dao;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;
    private SimpleJdbcInsert jdbcInsertUsers;
    private SimpleJdbcInsert jdbcInsertRelation;
    private SimpleJdbcInsert jdbcInsertStudyType;
    private Collection<StudyType> available_studies;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(CLINICS_TABLE_NAME);
        jdbcInsertUsers = new SimpleJdbcInsert(ds)
                .withTableName(USERS_TABLE_NAME)
                .usingGeneratedKeyColumns("id");
        jdbcInsertRelation = new SimpleJdbcInsert(ds)
                .withTableName(CLINICS_RELATION_TABLE_NAME);
        jdbcInsertStudyType = new SimpleJdbcInsert(ds)
                .withTableName(STUDIES_TABLE_NAME)
                .usingGeneratedKeyColumns("id");

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
    public void testFindByUserIdExists() {
        int dbkey = insertTestClinic();

        final Optional<Clinic> maybeClinic = dao.findByUserId(dbkey);

        Assert.assertNotNull(maybeClinic);
        Assert.assertTrue(maybeClinic.isPresent());
        Assert.assertEquals(NAME, maybeClinic.get().getName());
    }

    @Test
    public void testFindByUserIdNotExists() {
        final Optional<Clinic> maybeClinic = dao.findByUserId(ZERO_ID);

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
        Assert.assertTrue(clinic.getEmail().equals(USER_EMAIL) || clinic.getEmail().equals(USER_EMAIL_ALT));
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
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate,CLINICS_RELATION_TABLE_NAME));
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testRegisterStudyToClinicNotExists() {
        StudyType studyType = new StudyType(ZERO_ID, STUDY_NAME);

        dao.registerStudyToClinic(ZERO_ID, studyType);
    }

    @Test
    public void testRegisterValid() {
        int userkey = insertTestUser(USER_EMAIL);
        available_studies = new ArrayList<>();
        available_studies.add(new StudyType(ZERO_ID, STUDY_NAME));
        available_studies.add(new StudyType(ZERO_ID, STUDY_NAME_ALT));

        final Clinic clinic = dao.register(new User(userkey,USER_EMAIL,PASSWORD,ROLE),NAME,TELEPHONE,available_studies,false);

        Assert.assertFalse(clinic.getMedical_studies().isEmpty());
        StudyType study = clinic.getMedical_studies().stream().findFirst().get();
        Assert.assertTrue(study.getName().equals(STUDY_NAME) || study.getName().equals(STUDY_NAME_ALT));
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, CLINICS_TABLE_NAME));
    }

    @Test(expected = DuplicateKeyException.class)
    public void testRegisterAlreadyExists() {
        int userkey = insertTestClinic();

        dao.register(new User(userkey,USER_EMAIL,PASSWORD,ROLE),NAME,TELEPHONE,new ArrayList<>(), false);
    }

    @Test
    public void testHasStudy() {
        int userkey = insertTestClinic();
        int studykey = insertTestStudyType(STUDY_NAME);
        insertTestRelation(userkey,studykey);

        boolean ret = dao.hasStudy(userkey,studykey);

        Assert.assertTrue(ret);
    }

    @Test
    public void testHasStudyFalse() {
        int userkey = insertTestClinic();
        int studykey = insertTestStudyType(STUDY_NAME);

        boolean ret = dao.hasStudy(userkey,studykey);

        Assert.assertFalse(ret);
    }

    @Test
    public void testUpdateClinicInfo() {
        int userkey = insertTestClinic();
        int studykey = insertTestStudyType(STUDY_NAME);
        int studykeyalt = insertTestStudyType(STUDY_NAME_ALT);
        insertTestRelation(userkey,studykey);
        available_studies = new ArrayList<>();
        available_studies.add(new StudyType(studykey, STUDY_NAME));
        available_studies.add(new StudyType(studykeyalt, STUDY_NAME_ALT));

        Clinic clinic = dao.updateClinicInfo(new User(userkey,USER_EMAIL,PASSWORD,ROLE),NAME_ALT,TELEPHONE,available_studies,TRUE);

        Assert.assertEquals(available_studies.size(), clinic.getMedical_studies().size());
        Assert.assertEquals(1,JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,CLINICS_TABLE_NAME,"name = '" + NAME_ALT.replace("'","''")  + "' AND telephone = '" + TELEPHONE + "'"));
        Assert.assertEquals(1,JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,CLINICS_RELATION_TABLE_NAME,"clinic_id = " + userkey + " AND study_id = " + studykey));
        Assert.assertEquals(1,JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,CLINICS_RELATION_TABLE_NAME,"clinic_id = " + userkey + " AND study_id = " + studykeyalt));
    }


    private int insertClinic(int user_id, String name) {
        Map<String,Object> insertMap = new HashMap<>();
        insertMap.put("user_id", user_id);
        insertMap.put("name", name);
        insertMap.put("telephone", TELEPHONE);
        insertMap.put("verified", TRUE);
        jdbcInsert.execute(insertMap);
        return user_id;
    }

    private int insertTestClinic() {
        int userkey = insertTestUser(USER_EMAIL);
        return insertClinic(userkey, NAME);
    }

    private void insertMultipleTestClinic() {
        int userkey = insertTestUser(USER_EMAIL);
        insertClinic(userkey, NAME);
        userkey = insertTestUser(USER_EMAIL_ALT);
        insertClinic(userkey, NAME_ALT);
    }

    private int insertTestUser(final String email) {
        Map<String, Object> insertMap = new HashMap<>();
        insertMap.put("email", email);
        insertMap.put("password", PASSWORD);
        insertMap.put("role", ROLE);
        Number key = jdbcInsertUsers.executeAndReturnKey(insertMap);
        return key.intValue();
    }

    private void insertTestRelation(final int clinic_id, final int study_id) {
        Map<String, Object> insertMap = new HashMap<>();
        insertMap.put("clinic_id", clinic_id);
        insertMap.put("study_id", study_id);
        jdbcInsertRelation.execute(insertMap);
    }

    private int insertTestStudyType(final String name) {
        Map<String, Object> insertMap = new HashMap<>();
        insertMap.put("name", name);
        Number key = jdbcInsertStudyType.executeAndReturnKey(insertMap);
        return key.intValue();
    }
}
