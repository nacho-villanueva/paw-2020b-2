package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Clinic;
import ar.edu.itba.paw.model.ClinicHours;
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
import java.sql.Time;
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
    private static final String CLINIC_HOURS_TABLE_NAME = "clinic_hours";
    private static final String CLINIC_PLANS_TABLE_NAME = "clinic_accepted_plans";
    private static final String STUDIES_TABLE_NAME = "medical_studies";

    private static final String NAME_ZERO = "Zero's Clinic";
    private static final String NAME_ONE = "One's Clinic";
    private static final String NAME_TWO = "Two's Clinic";
    private static final String NAME_THREE = "Three's Clinic";
    private static final String TELEPHONE = "+011-00000000";
    private static final String STUDY_NAME = "MRA";
    private static final String STUDY_NAME_ALT = "Colonoscopy";
    private static final String OPEN_TIME = "08:00:00";
    private static final String OPEN_TIME_ALT = "09:00:00";
    private static final String CLOSE_TIME = "18:00:00";
    private static final String CLOSE_TIME_ALT = "15:00:00";
    private static final String MEDIC_PLAN = "OSDE";
    private static final String MEDIC_PLAN_ALT = "Swiss Medical";
    private static final int ZERO_ID = 0;
    private static final boolean TRUE = true;

    //USER INFO
    private static final String USER_EMAIL_ZERO = "patient@zero.com";
    private static final String USER_EMAIL_ONE = "patient@one.com";
    private static final String USER_EMAIL_TWO = "patient@two.com";
    private static final String USER_EMAIL_THREE = "patient@three.com";
    private static final String PASSWORD = "GroundZer0";
    private static final int ROLE = 3;


    @Autowired
    private DataSource ds;

    @Autowired
    private ClinicJdbcDao dao;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;
    private SimpleJdbcInsert jdbcInsertUsers;
    private SimpleJdbcInsert jdbcInsertHours;
    private SimpleJdbcInsert jdbcInsertPlans;
    private SimpleJdbcInsert jdbcInsertRelation;
    private SimpleJdbcInsert jdbcInsertStudyType;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(CLINICS_TABLE_NAME);
        jdbcInsertUsers = new SimpleJdbcInsert(ds)
                .withTableName(USERS_TABLE_NAME)
                .usingGeneratedKeyColumns("id");
        jdbcInsertHours = new SimpleJdbcInsert(ds)
                .withTableName(CLINIC_HOURS_TABLE_NAME);
        jdbcInsertPlans = new SimpleJdbcInsert(ds)
                .withTableName(CLINIC_PLANS_TABLE_NAME);
        jdbcInsertRelation = new SimpleJdbcInsert(ds)
                .withTableName(CLINICS_RELATION_TABLE_NAME);
        jdbcInsertStudyType = new SimpleJdbcInsert(ds)
                .withTableName(STUDIES_TABLE_NAME)
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
        JdbcTestUtils.deleteFromTables(jdbcTemplate,STUDIES_TABLE_NAME);
    }

    @Test
    public void testFindByUserIdExists() {
        int dbkey = insertTestClinic();
        insertTestClinicHours(dbkey);
        insertTestPlans(dbkey);

        final Optional<Clinic> maybeClinic = dao.findByUserId(dbkey);

        Assert.assertNotNull(maybeClinic);
        Assert.assertTrue(maybeClinic.isPresent());
        Assert.assertEquals(NAME_ZERO, maybeClinic.get().getName());
        Assert.assertTrue(maybeClinic.get().getHours().getDays()[ClinicHours.SATURDAY]);
        Assert.assertTrue(maybeClinic.get().getAccepted_plans().contains(MEDIC_PLAN_ALT));
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
        Assert.assertTrue(clinic.getEmail().equals(USER_EMAIL_ZERO) || clinic.getEmail().equals(USER_EMAIL_ONE));
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
        int userkey = insertTestUser(USER_EMAIL_ZERO);
        Collection<StudyType> available_studies = new ArrayList<>();
        available_studies.add(new StudyType(ZERO_ID, STUDY_NAME));
        available_studies.add(new StudyType(ZERO_ID, STUDY_NAME_ALT));
        Set<String> plans = new HashSet<>();
        plans.add(MEDIC_PLAN);
        plans.add(MEDIC_PLAN_ALT);


        final Clinic clinic = dao.register(new User(userkey, USER_EMAIL_ZERO,PASSWORD,ROLE), NAME_ZERO,TELEPHONE, available_studies,plans,getClinicHours(),false);

        Assert.assertEquals(2,clinic.getMedical_studies().size());
        Assert.assertEquals(2,clinic.getAccepted_plans().size());
        Assert.assertTrue(clinic.getHours().getDays()[ClinicHours.MONDAY]);
        Assert.assertEquals(Time.valueOf(OPEN_TIME),clinic.getHours().getOpen_hours()[ClinicHours.MONDAY]);
        Assert.assertEquals(Time.valueOf(CLOSE_TIME_ALT),clinic.getHours().getClose_hours()[ClinicHours.SATURDAY]);
        StudyType study = clinic.getMedical_studies().stream().findFirst().get();
        Assert.assertTrue(study.getName().equals(STUDY_NAME) || study.getName().equals(STUDY_NAME_ALT));
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, CLINICS_TABLE_NAME));
        Assert.assertEquals(2, JdbcTestUtils.countRowsInTable(jdbcTemplate, CLINIC_PLANS_TABLE_NAME));
        Assert.assertEquals(5, JdbcTestUtils.countRowsInTable(jdbcTemplate, CLINIC_HOURS_TABLE_NAME));
    }

    @Test(expected = DuplicateKeyException.class)
    public void testRegisterAlreadyExists() {
        int userkey = insertTestClinic();

        dao.register(new User(userkey, USER_EMAIL_ZERO,PASSWORD,ROLE), NAME_ZERO,TELEPHONE,new ArrayList<>(),new HashSet<>(),new ClinicHours(),false);
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
        insertTestPlans(userkey);
        insertTestClinicHours(userkey);

        //New studies
        Collection<StudyType> available_studies = new ArrayList<>();
        available_studies.add(new StudyType(studykeyalt, STUDY_NAME_ALT));

        //New plans
        Set<String> new_plans = new HashSet<>();
        new_plans.add(MEDIC_PLAN_ALT);

        //New hours
        ClinicHours new_hours = getClinicHoursAlt();

        Clinic clinic = dao.updateClinicInfo(new User(userkey, USER_EMAIL_ZERO,PASSWORD,ROLE), NAME_ONE,TELEPHONE,available_studies,new_plans,new_hours,TRUE);

        Assert.assertEquals(available_studies.size(), clinic.getMedical_studies().size());
        Assert.assertEquals(1,JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,CLINICS_TABLE_NAME,"name = '" + NAME_ONE.replace("'","''")  + "' AND telephone = '" + TELEPHONE + "'"));
        Assert.assertEquals(0,JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,CLINICS_RELATION_TABLE_NAME,"clinic_id = " + userkey + " AND study_id = " + studykey));
        Assert.assertEquals(1,JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,CLINICS_RELATION_TABLE_NAME,"clinic_id = " + userkey + " AND study_id = " + studykeyalt));
        Assert.assertFalse(clinic.getHours().getDays()[ClinicHours.MONDAY]);
        Assert.assertEquals(Time.valueOf(OPEN_TIME),clinic.getHours().getOpen_hours()[ClinicHours.SUNDAY]);
        Assert.assertEquals(Time.valueOf(CLOSE_TIME_ALT),clinic.getHours().getClose_hours()[ClinicHours.TUESDAY]);
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, CLINIC_PLANS_TABLE_NAME));
        Assert.assertEquals(4, JdbcTestUtils.countRowsInTable(jdbcTemplate, CLINIC_HOURS_TABLE_NAME));
    }

    @Test
    public void testSearchNoParams() {
        int[] keys = insertTestSearchPreconditions();

        //Search params
        String clinic_name = null;
        ClinicHours hours = null;
        String accepted_plan = null;
        String study_name = null;

        Collection<Clinic> clinics = dao.searchClinicsBy(clinic_name,hours,accepted_plan,study_name);

        Assert.assertEquals(keys.length,clinics.size());
    }

    @Test
    public void testSearchNoName() {
        int[] keys = insertTestSearchPreconditions();

        //Search params
        String clinic_name = null;
        ClinicHours hours = new ClinicHours();
        hours.setDayHour(ClinicHours.MONDAY,Time.valueOf("16:00:00"),Time.valueOf("18:00:00"));
        hours.setDayHour(ClinicHours.WEDNESDAY,Time.valueOf("15:00:00"),Time.valueOf("18:00:00"));
        hours.setDayHour(ClinicHours.SATURDAY,Time.valueOf("19:00:00"),Time.valueOf("21:00:00"));
        String accepted_plan = "OSDE";
        String study_name = "mra";

        Collection<Clinic> clinics = dao.searchClinicsBy(clinic_name,hours,accepted_plan,study_name);

        Assert.assertEquals(1,clinics.size());
        Assert.assertEquals(keys[3],clinics.stream().findFirst().get().getUser_id());
    }

    private int[] insertTestSearchPreconditions() {
        int userkey = insertTestUser(USER_EMAIL_ZERO);
        int userkey1 = insertTestUser(USER_EMAIL_ONE);
        int userkey2 = insertTestUser(USER_EMAIL_TWO);
        int userkey3 = insertTestUser(USER_EMAIL_THREE);
        int studykey = insertTestStudyType(STUDY_NAME);
        int studykeyalt = insertTestStudyType(STUDY_NAME_ALT);
        ClinicHours hours;

        /* Zero's Clinic
           studies: MRA
           plans: Osde, Pami
           Hours: mon-fri from 9-15
        */
        insertClinic(userkey, NAME_ZERO);
        insertTestRelation(userkey,studykey);

        insertTestPlan(userkey,"Osde");
        insertTestPlan(userkey,"Pami");
        hours = new ClinicHours();
        hours.setDayHour(ClinicHours.MONDAY,Time.valueOf("09:00:00"),Time.valueOf("15:00:00"));
        hours.setDayHour(ClinicHours.TUESDAY,Time.valueOf("09:00:00"),Time.valueOf("15:00:00"));
        hours.setDayHour(ClinicHours.WEDNESDAY,Time.valueOf("09:00:00"),Time.valueOf("15:00:00"));
        hours.setDayHour(ClinicHours.THURSDAY,Time.valueOf("09:00:00"),Time.valueOf("15:00:00"));
        hours.setDayHour(ClinicHours.FRIDAY,Time.valueOf("09:00:00"),Time.valueOf("15:00:00"));
        insertClinicHours(userkey,hours);

        /* One's Clinic
           studies: MRA,Colonoscopy
           plans: none
           Hours: sat-sun from 11-18 and wed 11-16
        */
        insertClinic(userkey1, NAME_ONE);
        insertTestRelation(userkey1,studykey);
        insertTestRelation(userkey1,studykeyalt);

        hours = new ClinicHours();
        hours.setDayHour(ClinicHours.SATURDAY,Time.valueOf("11:00:00"),Time.valueOf("18:00:00"));
        hours.setDayHour(ClinicHours.SUNDAY,Time.valueOf("11:00:00"),Time.valueOf("18:00:00"));
        hours.setDayHour(ClinicHours.WEDNESDAY,Time.valueOf("11:00:00"),Time.valueOf("16:00:00"));
        insertClinicHours(userkey1,hours);


        /* Two's Clinic
           studies: Colonoscopy
           plans: Swiss, osde
           Hours: mon-wed from 17-24 and fri 19-24
        */
        insertClinic(userkey2, NAME_TWO);
        insertTestRelation(userkey2,studykeyalt);

        insertTestPlan(userkey2,"Swiss");
        insertTestPlan(userkey2,"osde");
        hours = new ClinicHours();
        hours.setDayHour(ClinicHours.MONDAY,Time.valueOf("17:00:00"),Time.valueOf("24:00:00"));
        hours.setDayHour(ClinicHours.TUESDAY,Time.valueOf("17:00:00"),Time.valueOf("24:00:00"));
        hours.setDayHour(ClinicHours.WEDNESDAY,Time.valueOf("17:00:00"),Time.valueOf("24:00:00"));
        hours.setDayHour(ClinicHours.FRIDAY,Time.valueOf("19:00:00"),Time.valueOf("24:00:00"));
        insertClinicHours(userkey2,hours);

        /* Three's Clinic
           studies: MRA
           plans: swiss medical, osde 310
           Hours: sat-sun from 9-18
        */
        insertClinic(userkey3, NAME_THREE);
        insertTestRelation(userkey3,studykey);

        insertTestPlan(userkey3,"swiss medical");
        insertTestPlan(userkey3,"osde 310");
        hours = new ClinicHours();
        hours.setDayHour(ClinicHours.SUNDAY,Time.valueOf("09:00:00"),Time.valueOf("18:00:00"));
        hours.setDayHour(ClinicHours.MONDAY,Time.valueOf("09:00:00"),Time.valueOf("18:00:00"));
        hours.setDayHour(ClinicHours.TUESDAY,Time.valueOf("09:00:00"),Time.valueOf("18:00:00"));
        hours.setDayHour(ClinicHours.WEDNESDAY,Time.valueOf("09:00:00"),Time.valueOf("18:00:00"));
        hours.setDayHour(ClinicHours.THURSDAY,Time.valueOf("09:00:00"),Time.valueOf("18:00:00"));
        hours.setDayHour(ClinicHours.FRIDAY,Time.valueOf("09:00:00"),Time.valueOf("18:00:00"));
        hours.setDayHour(ClinicHours.SATURDAY,Time.valueOf("09:00:00"),Time.valueOf("18:00:00"));
        insertClinicHours(userkey3,hours);

        return new int[]{userkey,userkey1,userkey2,userkey3};
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
        int userkey = insertTestUser(USER_EMAIL_ZERO);
        return insertClinic(userkey, NAME_ZERO);
    }

    private void insertMultipleTestClinic() {
        int userkey = insertTestUser(USER_EMAIL_ZERO);
        insertClinic(userkey, NAME_ZERO);
        userkey = insertTestUser(USER_EMAIL_ONE);
        insertClinic(userkey, NAME_ONE);
    }

    private int insertTestUser(final String email) {
        Map<String, Object> insertMap = new HashMap<>();
        insertMap.put("email", email);
        insertMap.put("password", PASSWORD);
        insertMap.put("role", ROLE);
        Number key = jdbcInsertUsers.executeAndReturnKey(insertMap);
        return key.intValue();
    }

    private ClinicHours getClinicHours() {
        ClinicHours hours = new ClinicHours();
        hours.setDayHour(ClinicHours.MONDAY, Time.valueOf(OPEN_TIME), Time.valueOf(CLOSE_TIME));
        hours.setDayHour(ClinicHours.TUESDAY, Time.valueOf(OPEN_TIME), Time.valueOf(CLOSE_TIME));
        hours.setDayHour(ClinicHours.THURSDAY, Time.valueOf(OPEN_TIME), Time.valueOf(CLOSE_TIME));
        hours.setDayHour(ClinicHours.FRIDAY, Time.valueOf(OPEN_TIME), Time.valueOf(CLOSE_TIME));
        hours.setDayHour(ClinicHours.SATURDAY, Time.valueOf(OPEN_TIME_ALT), Time.valueOf(CLOSE_TIME_ALT));

        return hours;
    }

    private ClinicHours getClinicHoursAlt() {
        ClinicHours hours = new ClinicHours();
        hours.setDayHour(ClinicHours.TUESDAY, Time.valueOf(OPEN_TIME_ALT), Time.valueOf(CLOSE_TIME_ALT));
        hours.setDayHour(ClinicHours.THURSDAY, Time.valueOf(OPEN_TIME_ALT), Time.valueOf(CLOSE_TIME_ALT));
        hours.setDayHour(ClinicHours.FRIDAY, Time.valueOf(OPEN_TIME_ALT), Time.valueOf(CLOSE_TIME_ALT));
        hours.setDayHour(ClinicHours.SUNDAY, Time.valueOf(OPEN_TIME), Time.valueOf(CLOSE_TIME));

        return hours;
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

    private void insertTestClinicHours(int clinic_id) {
        ClinicHours hours = getClinicHours();
        insertClinicHours(clinic_id,hours);
    }

    private void insertTestPlans(int clinic_id) {
        insertTestPlan(clinic_id,MEDIC_PLAN);
        insertTestPlan(clinic_id,MEDIC_PLAN_ALT);
    }

    private void insertTestPlan(int clinic_id, String name) {
        Map<String, Object> insertMap = new HashMap<>();
        insertMap.put("clinic_id",clinic_id);
        insertMap.put("medic_plan", name);
        jdbcInsertPlans.execute(insertMap);
    }

    private void insertClinicHours(int clinic_id, ClinicHours hours) {
        Map<String, Object> insertMap = new HashMap<>();
        insertMap.put("clinic_id", clinic_id);
        for (int i = 0; i < hours.getDays().length; i++) {
            if(hours.getDays()[i]) {
                insertMap.put("day_of_week",i);
                insertMap.put("open_time",hours.getOpen_hours()[i]);
                insertMap.put("close_time",hours.getClose_hours()[i]);
                jdbcInsertHours.execute(insertMap);
            }
        }
    }
}
