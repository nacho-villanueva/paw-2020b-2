package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.*;
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

import javax.persistence.CollectionTable;
import javax.persistence.PersistenceException;
import javax.sql.DataSource;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class ClinicDaoTest {

    //TABLE NAMES
    private static final String CLINICS_TABLE_NAME = "clinics";
    private static final String CLINIC_HOURS_TABLE_NAME = "clinic_hours";
    private static final String CLINIC_PLANS_TABLE_NAME = "clinic_accepted_plans";
    private static final String CLINICS_RELATION_TABLE_NAME = "clinic_available_studies";

    private static final User userTest = new User(0,"test@test.com","testPass",User.UNDEFINED_ROLE_ID,"es-AR");

    private static final User userTwo = new User(3,"two@two.com","passTwo",User.CLINIC_ROLE_ID);
    private static final ClinicDayHours clinicDaysHoursTwoSun = new ClinicDayHours(0, LocalTime.parse("08:00:00"), LocalTime.parse("22:00:00"));
    private static final ClinicDayHours clinicDaysHoursTwoMon = new ClinicDayHours(1, LocalTime.parse("09:00:00"), LocalTime.parse("21:00:00"));
    private static final ClinicDayHours clinicDaysHoursTwoTue = new ClinicDayHours(2, LocalTime.parse("10:00:00"), LocalTime.parse("20:00:00"));
    private static final ClinicDayHours clinicDaysHoursTwoWed = new ClinicDayHours(3, LocalTime.parse("11:00:00"), LocalTime.parse("19:00:00"));
    private static final ClinicHours clinicHourTwo = new ClinicHours(new ArrayList<>(Arrays.asList(
            clinicDaysHoursTwoSun,
            clinicDaysHoursTwoMon,
            clinicDaysHoursTwoTue,
            clinicDaysHoursTwoWed)));
    private static final Clinic clinicTwo = new Clinic(userTwo, "Clinic two", null, true);
    private static final String CLINIC_TWO_ACCEPTED_PLAN = "Osde";
    private static final StudyType studyTypeOne = new StudyType(1,"X-ray");
    private static final StudyType studyTypeSix = new StudyType(6,"Surgery");

    private static final User userSeven = new User(8,"seven@seven.com","passSeven",User.UNDEFINED_ROLE_ID);

    private static final List<String> clinicMails = new ArrayList<>(Arrays.asList("two@two.com","five@five.com","eight@eight.com","nine@nine.com","ten@ten.com","twelve@twelve.com",
            "thirteen@thirteen.com","fourteen@fourteen.com","fifteen@fifteen.com","sixteen@sixteen.com","seventeen@seventeen.com","eighteen@eighteen.com", "nineteen@nineteen.com","twenty@twenty.com"));

    private static final int ZERO_ID = 1;
    private static final String NAME_ZERO = "Zero's Clinic";
    private static final StudyType studyTypeTest = new StudyType(10,"TEST STUDY TEST");
    private static final String TELEPHONE = "+011-00000000";
    private static final String OPEN_TIME = "08:00:00";
    private static final String OPEN_TIME_ALT = "09:00:00";
    private static final String CLOSE_TIME = "18:00:00";
    private static final String CLOSE_TIME_ALT = "15:00:00";

    private static final String MEDIC_PLAN = "OSDE";
    private static final String MEDIC_PLAN_ALT = "Swiss Medical";

    private static final String NEW_NAME_TWO = "Refurbished Two's Clinic";


    @Autowired
    private DataSource ds;

    @Autowired
    private ClinicDao dao;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Test
    @Transactional
    @Rollback
    public void testFindByUserIdExists() {

        final Optional<Clinic> maybeClinic = dao.findByUserId(clinicTwo.getUser().getId());

        Assert.assertNotNull(maybeClinic);
        Assert.assertTrue(maybeClinic.isPresent());
        Assert.assertEquals(clinicTwo.getName(), maybeClinic.get().getName());
        Assert.assertTrue(maybeClinic.get().getHours().getDays()[ClinicHours.SUNDAY]);
        Assert.assertTrue(maybeClinic.get().getAccepted_plans().contains(CLINIC_TWO_ACCEPTED_PLAN));
    }

    @Test
    @Transactional
    @Rollback
    public void testFindByUserIdNotExists() {
        final Optional<Clinic> maybeClinic = dao.findByUserId(ZERO_ID);

        Assert.assertNotNull(maybeClinic);
        Assert.assertFalse(maybeClinic.isPresent());
    }

    @Test
    @Transactional
    @Rollback
    public void testGetAll() {

        final Collection<Clinic> clinics = dao.getAll();

        Assert.assertNotNull(clinics);
        Assert.assertEquals(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,CLINICS_TABLE_NAME,"verified = true"),clinics.size());
        Clinic clinic = clinics.stream().findFirst().get();
        Assert.assertTrue(clinicMails.contains(clinic.getUser().getEmail()));
    }
    /*
    // Temporally disabled
    @Test
    @Transactional
    @Rollback
    public void testGetAllNone() {
        final Collection<Clinic> clinics = dao.getAll();

        Assert.assertNotNull(clinics);
        Assert.assertEquals(0,clinics.size());
    }
    */

    @Test
    @Transactional
    @Rollback
    public void testRegisterStudyToClinic() {

        int clinicMedicalStudies = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,CLINICS_RELATION_TABLE_NAME,"clinic_id = "+clinicTwo.getUser().getId());

        StudyType study = dao.registerStudyToClinic(clinicTwo.getUser().getId(), studyTypeSix);

        Assert.assertNotNull(study);
        Assert.assertEquals(studyTypeSix.getId(), study.getId());
        Assert.assertEquals(1+clinicMedicalStudies, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,CLINICS_RELATION_TABLE_NAME,"clinic_id = "+clinicTwo.getUser().getId()));
    }

    @Test
    @Transactional
    @Rollback
    public void testRegisterStudyToClinicNotExists() {
        int numberOfRelations = JdbcTestUtils.countRowsInTable(jdbcTemplate,CLINICS_RELATION_TABLE_NAME);

        StudyType studyType = dao.registerStudyToClinic(ZERO_ID, studyTypeOne);

        Assert.assertNull(studyType);
        Assert.assertEquals(numberOfRelations,JdbcTestUtils.countRowsInTable(jdbcTemplate,CLINICS_RELATION_TABLE_NAME));
    }

    @Test
    @Transactional
    @Rollback
    public void testRegisterValid() {
        Collection<StudyType> available_studies = new ArrayList<>();
        available_studies.add(studyTypeOne);
        available_studies.add(studyTypeSix);
        Set<String> plans = new HashSet<>();
        plans.add(MEDIC_PLAN);
        plans.add(MEDIC_PLAN_ALT);

        int rowsClinicTable = JdbcTestUtils.countRowsInTable(jdbcTemplate, CLINICS_TABLE_NAME);
        int rowsClinicPlansTable = JdbcTestUtils.countRowsInTable(jdbcTemplate, CLINIC_PLANS_TABLE_NAME);
        int rowsClinicHoursTable = JdbcTestUtils.countRowsInTable(jdbcTemplate, CLINIC_HOURS_TABLE_NAME);


        final Clinic clinic = dao.register(userSeven, NAME_ZERO,TELEPHONE, available_studies,plans,getClinicHours(),false);

        Assert.assertEquals(available_studies.size(),clinic.getMedical_studies().size());
        Assert.assertEquals(plans.size(),clinic.getAccepted_plans().size());
        Assert.assertTrue(clinic.getHours().getDays()[ClinicHours.MONDAY]);
        Assert.assertEquals(LocalTime.parse(OPEN_TIME),clinic.getHours().getOpen_hours()[ClinicHours.MONDAY]);
        Assert.assertEquals(LocalTime.parse(CLOSE_TIME_ALT),clinic.getHours().getClose_hours()[ClinicHours.SATURDAY]);
        StudyType study = clinic.getMedical_studies().stream().findFirst().get();
        Assert.assertTrue(study.getName().equals(studyTypeOne.getName()) || study.getName().equals(studyTypeSix.getName()));
        Assert.assertEquals(1+rowsClinicTable, JdbcTestUtils.countRowsInTable(jdbcTemplate, CLINICS_TABLE_NAME));
        Assert.assertEquals(plans.size()+rowsClinicPlansTable, JdbcTestUtils.countRowsInTable(jdbcTemplate, CLINIC_PLANS_TABLE_NAME));
        Assert.assertEquals(5+rowsClinicHoursTable, JdbcTestUtils.countRowsInTable(jdbcTemplate, CLINIC_HOURS_TABLE_NAME));
    }

    @Test(expected = PersistenceException.class)
    @Transactional
    @Rollback
    public void testRegisterAlreadyExists() {
        Collection<StudyType> available_studies = new ArrayList<>();
        available_studies.add(studyTypeOne);
        available_studies.add(studyTypeSix);
        Set<String> plans = new HashSet<>();
        plans.add(MEDIC_PLAN);
        plans.add(MEDIC_PLAN_ALT);

        dao.register(userTwo,NAME_ZERO,null,available_studies,plans,getClinicHours(),false);
    }

    @Test
    @Transactional
    @Rollback
    public void testHasStudy() {

        boolean ret = dao.hasStudy(clinicTwo.getUser().getId(),studyTypeOne.getId());

        Assert.assertTrue(ret);
    }

    @Test
    @Transactional
    @Rollback
    public void testHasStudyFalse() {

        boolean ret = dao.hasStudy(clinicTwo.getUser().getId(),studyTypeTest.getId());

        Assert.assertFalse(ret);
    }

    @Test
    @Transactional
    @Rollback
    public void testUpdateClinicInfo() {

        //New studies
        Collection<StudyType> available_studies = new ArrayList<>();
        available_studies.add(studyTypeSix);

        //New plans
        Set<String> new_plans = new HashSet<>();
        new_plans.add(MEDIC_PLAN_ALT);

        //New hours
        ClinicHours new_hours = getClinicHoursAlt();

        int amountofPlansEntiresBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, CLINIC_PLANS_TABLE_NAME);

        Clinic clinic = dao.updateClinicInfo(userTwo, NEW_NAME_TWO,TELEPHONE,available_studies,new_plans,new_hours,true);

        Assert.assertEquals(available_studies.size(), clinic.getMedical_studies().size());
        Assert.assertEquals(1,JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,CLINICS_TABLE_NAME,"name = '" + NEW_NAME_TWO.replace("'","''")  + "' AND telephone = '" + TELEPHONE + "'"));
        Assert.assertEquals(0,JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,CLINICS_RELATION_TABLE_NAME,"clinic_id = " + userTwo.getId() + " AND study_id = " + studyTypeOne.getId()));
        Assert.assertEquals(1,JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,CLINICS_RELATION_TABLE_NAME,"clinic_id = " + userTwo.getId() + " AND study_id = " + studyTypeSix.getId()));
        Assert.assertFalse(clinic.getHours().getDays()[ClinicHours.MONDAY]);
        Assert.assertEquals(LocalTime.parse(OPEN_TIME),clinic.getHours().getOpen_hours()[ClinicHours.SUNDAY]);
        Assert.assertEquals(LocalTime.parse(CLOSE_TIME_ALT),clinic.getHours().getClose_hours()[ClinicHours.TUESDAY]);
        Assert.assertEquals(amountofPlansEntiresBefore,JdbcTestUtils.countRowsInTable(jdbcTemplate, CLINIC_PLANS_TABLE_NAME));
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, CLINIC_PLANS_TABLE_NAME,"clinic_id = " + userTwo.getId() + " AND lower(medic_plan) = lower('" + MEDIC_PLAN_ALT + "')"));
        Assert.assertEquals(3, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, CLINIC_HOURS_TABLE_NAME,"clinic_id = " + userTwo.getId() + " AND close_time = '" + CLOSE_TIME_ALT + "'"));
    }

    @Test
    @Transactional
    @Rollback
    public void testSearchNoParams() {

        //Search params
        String clinic_name = null;
        ClinicHours hours = null;
        String accepted_plan = null;
        String study_name = null;

        Collection<Clinic> clinics = dao.searchClinicsBy(clinic_name,hours,accepted_plan,study_name);

        Assert.assertEquals(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,CLINICS_TABLE_NAME,"verified = true"),clinics.size());
        clinics.forEach(clinic -> {
            Assert.assertTrue(clinicMails.contains(clinic.getUser().getEmail()));
        });
    }

    @Test
    @Transactional
    @Rollback
    public void testSearchNoName() {

        //Search params
        String clinic_name = null;
        ClinicHours hours = new ClinicHours();
        hours.setDayHour(ClinicHours.TUESDAY, LocalTime.parse("08:00:00"),LocalTime.parse("23:00:00"));
        hours.setDayHour(ClinicHours.WEDNESDAY, LocalTime.parse("00:00:00"),LocalTime.parse("23:00:00"));
        hours.setDayHour(ClinicHours.THURSDAY, LocalTime.parse("00:00:00"),LocalTime.parse("23:00:00"));
        hours.setDayHour(ClinicHours.FRIDAY, LocalTime.parse("00:00:00"),LocalTime.parse("23:00:00"));
        String accepted_plan = "OSDE";
        String study_name = "allergy";

        Collection<Clinic> clinics = dao.searchClinicsBy(clinic_name,hours,accepted_plan,study_name);

        Assert.assertEquals(1,clinics.size());
        Assert.assertEquals(clinicTwo.getUser().getId().intValue(),clinics.stream().findFirst().get().getUser().getId().intValue());
    }

    @Test
    @Transactional
    @Rollback
    public void testSearchNoHours() {
        //Search params
        String clinic_name = null;
        ClinicHours hours = new ClinicHours();
        String accepted_plan = null;
        String study_name = null;

        Collection<Clinic> clinics = dao.searchClinicsBy(clinic_name,hours,accepted_plan,study_name);

        Assert.assertEquals(0,clinics.size());
    }

    @Test
    @Transactional
    @Rollback
    public void testSearchPlanWithLike() {
        //Search params
        String clinic_name = null;
        ClinicHours hours = null;
        String accepted_plan = "oSdE";
        String study_name = null;

        Collection<Clinic> clinics = dao.searchClinicsBy(clinic_name,hours,accepted_plan,study_name);

        Collection<Integer> clinicIds = clinics.stream().map(clinic -> clinic.getUser().getId()).collect(Collectors.toSet());

        Assert.assertTrue(clinicIds.contains(3));
        Assert.assertTrue(clinicIds.contains(6));
        Assert.assertTrue(clinicIds.contains(9));
        Assert.assertTrue(clinicIds.contains(11));
        Assert.assertTrue(clinicIds.contains(16));
        Assert.assertFalse(clinicIds.contains(18));     //18 has osde but it's not a verified clinic
        Assert.assertTrue(clinicIds.contains(19));
        Assert.assertTrue(clinicIds.contains(20));
    }

    @Test
    @Transactional
    @Rollback
    public void testSearchJustHours() {
        String name = null;
        ClinicHours availableHours = new ClinicHours();
        availableHours.setDayHour(ClinicHours.MONDAY, LocalTime.parse("00:00:00"), LocalTime.parse("23:59:59"));        //TODO: See why 24:00:00 breaks the test
        String medic_plan = null;
        String study_name = null;

        Collection<Clinic> clinics = dao.searchClinicsBy(name,availableHours,medic_plan,study_name);

        Collection<Integer> clinicIds = clinics.stream().map(clinic -> clinic.getUser().getId()).collect(Collectors.toSet());
        Assert.assertTrue(clinicIds.contains(11));
        Assert.assertTrue(clinicIds.contains(6));
        Assert.assertTrue(clinicIds.contains(9));
        Assert.assertFalse(clinicIds.contains(10));
        Assert.assertFalse(clinicIds.contains(12));
        Assert.assertEquals(7,clinics.size());
    }


    private ClinicHours getClinicHours() {
        ClinicHours hours = new ClinicHours();
        hours.setDayHour(ClinicHours.MONDAY, LocalTime.parse(OPEN_TIME), LocalTime.parse(CLOSE_TIME));
        hours.setDayHour(ClinicHours.TUESDAY, LocalTime.parse(OPEN_TIME), LocalTime.parse(CLOSE_TIME));
        hours.setDayHour(ClinicHours.THURSDAY, LocalTime.parse(OPEN_TIME), LocalTime.parse(CLOSE_TIME));
        hours.setDayHour(ClinicHours.FRIDAY, LocalTime.parse(OPEN_TIME), LocalTime.parse(CLOSE_TIME));
        hours.setDayHour(ClinicHours.SATURDAY, LocalTime.parse(OPEN_TIME_ALT), LocalTime.parse(CLOSE_TIME_ALT));

        return hours;
    }

    private ClinicHours getClinicHoursAlt() {
        ClinicHours hours = new ClinicHours();
        hours.setDayHour(ClinicHours.TUESDAY, LocalTime.parse(OPEN_TIME_ALT), LocalTime.parse(CLOSE_TIME_ALT));
        hours.setDayHour(ClinicHours.THURSDAY, LocalTime.parse(OPEN_TIME_ALT), LocalTime.parse(CLOSE_TIME_ALT));
        hours.setDayHour(ClinicHours.FRIDAY, LocalTime.parse(OPEN_TIME_ALT), LocalTime.parse(CLOSE_TIME_ALT));
        hours.setDayHour(ClinicHours.SUNDAY, LocalTime.parse(OPEN_TIME), LocalTime.parse(CLOSE_TIME));

        return hours;
    }


}
