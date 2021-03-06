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

import javax.persistence.PersistenceException;
import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class OrderDaoTest {

    // TABLES
    private static final String ORDERS_TABLE_NAME = "medical_orders";
    private static final String MEDICAL_ORDERS_USERS_TABLE_NAME = "medical_orders_users";


    //Others
    private static final long ZERO_ID_LONG = 0;
    private static final String DATE_A = "2020-10-05";

    // Invalid Data
    private static final User invalidUserMedic = new User(-1,"invalid@medic.com","invalid",User.MEDIC_ROLE_ID);
    private static final Medic invalidMedic = new Medic(invalidUserMedic, "Medic Ivalid", null, "image/png", new byte[0], "1234567", true);
    private static final User invalidUserClinic = new User(-2,"invalid@clinic.com","invalid",User.CLINIC_ROLE_ID);
    private static final Clinic invalidClinic = new Clinic(invalidUserClinic, "Clinic Invalid", null, true);
    private static final StudyType invalidStudyType = new StudyType(1, "Invalid");


    //Known Data
    private static final User userMedic = new User(2,"one@one.com","onePass",User.MEDIC_ROLE_ID);
    private static final Medic medic = new Medic(userMedic, "Medic one", null, "image/png", new byte[0], "1234567", true);
    private static final User userClinic = new User(3,"two@two.com","passTwo",User.CLINIC_ROLE_ID);
    private static final Clinic clinic = new Clinic(userClinic, "Clinic two", null, true);
    private static final StudyType studyType = new StudyType(1, "X-ray");

    private static final Order order = new Order(1, medic, LocalDate.parse("2020-10-05"), clinic, studyType, "Description 1", "image/png",new byte[0], "insurance plan one", "insurance123", "patient1@patient.com", "Patient one");
    private static final Order newOrder = new Order(2, medic, LocalDate.parse("2020-10-05"), clinic, studyType, "Description 1", "image/png",new byte[0], "insurance plan one", "insurance123", "patientnew@patient.com", "Patient New");

    private static final List<String> descriptionListAll = new ArrayList<>(Arrays.asList("Description 1","Description 2","Description 3"));
    private static final List<String> descriptionListDateA = new ArrayList<>(Arrays.asList("Description 1","Description 2"));
    private static final List<String> descriptionListOwnedByMedic = new ArrayList<>(Arrays.asList("Description 1","Description 2"));
    private static final List<String> descriptionListSharedToSharedMedic = new ArrayList<>(Arrays.asList("Description 2"));
    private static final List<String> descriptionListParticipatedByMedic = new ArrayList<>(Arrays.asList("Description 1","Description 2","Description 3"));

    private static final List<Integer> studyIdListWithOrders = new ArrayList<>(Arrays.asList(1));
    private static final List<String> patientEmailsAssingedToMedic = new ArrayList<>(Arrays.asList("patient1@patient.com", "patient2@patient.com"));
    private static final List<Integer> clinicIdListWithOrders = new ArrayList<>(Arrays.asList(3));
    private static final List<Integer> medicIdListWithOrders = new ArrayList<>(Arrays.asList(2, 22));

    private static final User sharedUserMedic = new User(22,"twentyone@twentyone.com","twentyonePass",User.MEDIC_ROLE_ID);
    private static final Medic sharedMedic = new Medic(sharedUserMedic, "Medic twentyone", null, "image/png", new byte[0], "1234567", true);

    //Pagination related
    private static final int PAGE_SIZE_WITH_ALL_ORDERS = 99;
    private static final int DEFAULT_PAGE = 1;

    @Autowired
    private DataSource ds;

    @Autowired
    private OrderDao dao;

    private JdbcTemplate jdbcTemplate;


    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Transactional
    @Rollback
    @Test
    public void testFindByIdExists() {
        final Optional<Order> maybeOrder = dao.findById(order.getOrderId());

        Assert.assertTrue(maybeOrder.isPresent());
        Assert.assertEquals(order.getMedic().getName(),maybeOrder.get().getMedic().getName());
        Assert.assertEquals(order.getClinic().getName(),maybeOrder.get().getClinic().getName());
        Assert.assertEquals(order.getStudy().getName(),maybeOrder.get().getStudy().getName());
        Assert.assertEquals(order.getPatientName(),maybeOrder.get().getPatientName());
        Assert.assertEquals(order.getDate(),maybeOrder.get().getDate());
    }

    @Transactional
    @Rollback
    @Test
    public void testFindByIdNotExists() {
        final Optional<Order> maybeOrder = dao.findById(ZERO_ID_LONG);

        Assert.assertFalse(maybeOrder.isPresent());
    }

    @Transactional
    @Rollback
    @Test
    public void testRegisterValid() {
        final Order testOrder = dao.register(newOrder.getMedic(), newOrder.getDate(),clinic, newOrder.getPatientName(), newOrder.getPatientEmail(),newOrder.getStudy(),"",newOrder.getIdentificationType(),newOrder.getIdentification(),newOrder.getPatientInsurancePlan(),newOrder.getPatientInsuranceNumber());

        Assert.assertEquals(newOrder.getPatientName(), testOrder.getPatientName());
        Assert.assertArrayEquals(newOrder.getIdentification(), testOrder.getIdentification());
        Assert.assertEquals(1,JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,ORDERS_TABLE_NAME, "patient_name='" + newOrder.getPatientName() +"'"));
    }

    @Transactional
    @Rollback
    @Test (expected = PersistenceException.class)
    public void testRegisterInvalid() {
        dao.register(invalidMedic,newOrder.getDate(),invalidClinic,newOrder.getPatientName(), newOrder.getPatientEmail(),invalidStudyType,"",newOrder.getIdentificationType(),newOrder.getIdentification(),"","");
    }

    @Transactional
    @Rollback
    @Test
    public void testGetAllAsMedicValid() {

        int expectedCount = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,ORDERS_TABLE_NAME,"medic_id = "+medic.getUser().getId());

        final Collection<Order> orders = dao.getAllAsMedic(userMedic,DEFAULT_PAGE, PAGE_SIZE_WITH_ALL_ORDERS);

        Assert.assertNotNull(orders);
        Assert.assertEquals(expectedCount,orders.size());
        Order firstOrder = orders.stream().findFirst().get();
        Assert.assertTrue(descriptionListOwnedByMedic.contains(firstOrder.getDescription()));
    }

    @Transactional
    @Rollback
    @Test
    public void testGetAllSharedAsMedicValid() {

        int expectedCount = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,MEDICAL_ORDERS_USERS_TABLE_NAME,"shared_with_id = "+sharedMedic.getUser().getId());

        final Collection<Order> orders = dao.getAllSharedAsMedic(sharedUserMedic,DEFAULT_PAGE, PAGE_SIZE_WITH_ALL_ORDERS);
        Assert.assertNotNull(orders);
        Assert.assertEquals(expectedCount,orders.size());
        Order firstOrder = orders.stream().findFirst().get();
        Assert.assertTrue(descriptionListSharedToSharedMedic.contains(firstOrder.getDescription()));
    }

    @Transactional
    @Rollback
    @Test
    public void testGetAllParticipatingAsMedicValid() {

        int expectedCount = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,MEDICAL_ORDERS_USERS_TABLE_NAME,"shared_with_id = "+medic.getUser().getId())+
                JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,ORDERS_TABLE_NAME,"medic_id = "+medic.getUser().getId());

        final Collection<Order> orders = dao.getAllParticipatingAsMedic(userMedic,DEFAULT_PAGE, PAGE_SIZE_WITH_ALL_ORDERS);
        Assert.assertNotNull(orders);
        Assert.assertEquals(expectedCount,orders.size());
        Order firstOrder = orders.stream().findFirst().get();
        Assert.assertTrue(descriptionListParticipatedByMedic.contains(firstOrder.getDescription()));
    }

    @Transactional
    @Rollback
    @Test
    public void testGetAllAsMedicCountValid() {

        int expectedCount = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,ORDERS_TABLE_NAME,"medic_id = "+medic.getUser().getId());

        final long actualCount = dao.getAllAsMedicCount(userMedic);
        Assert.assertEquals(expectedCount,actualCount);
    }

    @Transactional
    @Rollback
    @Test
    public void testGetAllParticipatingAsMedicCountValid() {

        int expectedCount = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,MEDICAL_ORDERS_USERS_TABLE_NAME,"shared_with_id = "+medic.getUser().getId())+
                JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,ORDERS_TABLE_NAME,"medic_id = "+medic.getUser().getId());

        final long actualCount = dao.getAllParticipatingAsMedicCount(userMedic);
        Assert.assertEquals(expectedCount,actualCount);
    }

    @Transactional
    @Rollback
    @Test
    public void testGetFilteredNoParams(){
        // should return the same as getting all orders of the user

        long expectedCount = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,ORDERS_TABLE_NAME,"medic_id = "+medic.getUser().getId());

        final Collection<Order> orders = dao.getFiltered(userMedic,null, null, null, null, null, null,false,DEFAULT_PAGE, PAGE_SIZE_WITH_ALL_ORDERS);

        Assert.assertNotNull(orders);
        Assert.assertEquals(expectedCount,orders.size());
        Assert.assertTrue(descriptionListOwnedByMedic.contains(orders.stream().findAny().get().getDescription()));
    }

    @Transactional
    @Rollback
    @Test
    public void testGetFilteredOnlyMedic(){

        long expectedCount = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,ORDERS_TABLE_NAME,"medic_id = "+medic.getUser().getId()+" AND clinic_id = "+clinic.getUser().getId());

        Collection<User> medics = new ArrayList<>();
        medics.add(medic.getUser());
        final Collection<Order> orders = dao.getFiltered(userClinic,null, medics, null, null, null, null,false,DEFAULT_PAGE, PAGE_SIZE_WITH_ALL_ORDERS);

        Assert.assertNotNull(orders);
        Assert.assertEquals(expectedCount,orders.size());
        Assert.assertTrue(descriptionListOwnedByMedic.contains(orders.stream().findAny().get().getDescription()));
    }

    @Transactional
    @Rollback
    @Test
    public void testGetFilteredOnlyDate(){

        long expectedCount = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,ORDERS_TABLE_NAME,"date = '"+DATE_A+"' AND clinic_id = "+clinic.getUser().getId());

        final Collection<Order> orders = dao.getFiltered(userMedic,null, null, null, LocalDate.parse(DATE_A),LocalDate.parse(DATE_A), null,false,DEFAULT_PAGE, PAGE_SIZE_WITH_ALL_ORDERS);

        Assert.assertNotNull(orders);
        Assert.assertEquals(expectedCount,orders.size());
        Assert.assertTrue(descriptionListDateA.contains(orders.stream().findAny().get().getDescription()));
    }

    @Transactional
    @Rollback
    @Test
    public void testGetFilteredOnlyStudyId(){

        long expectedCount = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,ORDERS_TABLE_NAME,"study_id = "+studyType.getId().toString()+" AND medic_id = "+medic.getUser().getId());

        Collection<StudyType> studyTypes = new ArrayList<>();
        studyTypes.add(order.getStudy());

        final Collection<Order> orders = dao.getFiltered(userMedic,null,null, null, null, null, studyTypes,false,DEFAULT_PAGE, PAGE_SIZE_WITH_ALL_ORDERS);

        Assert.assertNotNull(orders);
        Assert.assertEquals(expectedCount,orders.size());
        Assert.assertTrue(descriptionListOwnedByMedic.contains(orders.stream().findAny().get().getDescription()));
    }

    @Transactional
    @Rollback
    @Test
    public void testGetFilteredFilledAll(){

        long expectedCount = 1;

        Collection<User> clinics = new ArrayList<>();
        clinics.add(order.getClinic().getUser());
        Collection<User> medics = new ArrayList<>();
        medics.add(order.getMedic().getUser());
        Collection<String> patients = new ArrayList<>();
        patients.add(order.getPatientEmail());
        Collection<StudyType> studyTypes = new ArrayList<>();
        studyTypes.add(order.getStudy());

        final Collection<Order> orders = dao.getFiltered(userClinic, clinics, medics, patients, order.getDate(), order.getDate(), studyTypes,false,DEFAULT_PAGE,PAGE_SIZE_WITH_ALL_ORDERS);

        Assert.assertNotNull(orders);
        Assert.assertEquals(expectedCount,orders.size());
        Assert.assertEquals(order.getDescription(),orders.stream().findAny().get().getDescription());
    }

    @Transactional
    @Rollback
    @Test
    public void testGetAllAsUser(){

        long expectedCount = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,ORDERS_TABLE_NAME,"medic_id = "+medic.getUser().getId());

        final Collection<Order> orders = dao.getAllAsUser(userMedic,false,DEFAULT_PAGE,PAGE_SIZE_WITH_ALL_ORDERS);

        Assert.assertNotNull(orders);
        Assert.assertEquals(expectedCount,orders.size());
        Assert.assertTrue(descriptionListOwnedByMedic.contains(orders.stream().findAny().get().getDescription()));
    }

    @Transactional
    @Rollback
    @Test
    public void testGetRelevantStudyTypesNoParams(){
        // should return the same as getting all orders of the user

        long expectedCount = clinicIdListWithOrders.size();

        final Collection<StudyType> studyTypes = dao.getRelevantStudyTypes(userMedic,null, null, null, null, null, null,false,DEFAULT_PAGE, PAGE_SIZE_WITH_ALL_ORDERS);

        Assert.assertNotNull(studyTypes);
        Assert.assertEquals(expectedCount,studyTypes.size());
        Assert.assertTrue(studyIdListWithOrders.contains(studyTypes.stream().findAny().get().getId()));
    }

    @Transactional
    @Rollback
    @Test
    public void testGetRelevantStudyTypesNoSuchPatientEmail(){

        String patientEmail = invalidUserClinic.getEmail();
        Collection<String> patientEmails = new ArrayList<>(Arrays.asList(patientEmail));

        long expectedCount = 0;

        final Collection<StudyType> studyTypes = dao.getRelevantStudyTypes(userMedic,null, null, patientEmails, null, null, null,false,DEFAULT_PAGE, PAGE_SIZE_WITH_ALL_ORDERS);

        Assert.assertNotNull(studyTypes);
        Assert.assertEquals(expectedCount,studyTypes.size());
    }

    @Transactional
    @Rollback
    @Test
    public void testGetRelevantStudyTypesOnlyPatientEmail(){

        String patientEmail = patientEmailsAssingedToMedic.get(0);
        Collection<String> patientEmails = new ArrayList<>(Arrays.asList(patientEmail));

        long expectedCount = studyIdListWithOrders.size();

        final Collection<StudyType> studyTypes = dao.getRelevantStudyTypes(userMedic,null, null, patientEmails, null, null, null,false,DEFAULT_PAGE, PAGE_SIZE_WITH_ALL_ORDERS);

        Assert.assertNotNull(studyTypes);
        Assert.assertEquals(expectedCount,studyTypes.size());
        Assert.assertTrue(studyIdListWithOrders.contains(studyTypes.stream().findAny().get().getId()));
    }

    @Transactional
    @Rollback
    @Test
    public void testGetRelevantClinicsNoParams(){
        // should return the same as getting all orders of the user

        long expectedCount = studyIdListWithOrders.size();

        final Collection<Clinic> clinics = dao.getRelevantClinics(userMedic,null, null, null, null, null, null,false,DEFAULT_PAGE, PAGE_SIZE_WITH_ALL_ORDERS);

        Assert.assertNotNull(clinics);
        Assert.assertEquals(expectedCount, clinics.size());
        Assert.assertTrue(clinicIdListWithOrders.contains(clinics.stream().findAny().get().getUser().getId()));
    }

    @Transactional
    @Rollback
    @Test
    public void testGetRelevantClinicsStudyTypeOnly(){

        List<StudyType> studyTypeList = new ArrayList<>(Arrays.asList(studyType));

        long expectedCount = clinicIdListWithOrders.size();

        final Collection<Clinic> clinics = dao.getRelevantClinics(userMedic,null, null, null, null, null, studyTypeList,false,DEFAULT_PAGE, PAGE_SIZE_WITH_ALL_ORDERS);

        Assert.assertNotNull(clinics);
        Assert.assertEquals(expectedCount, clinics.size());
        Assert.assertTrue(clinicIdListWithOrders.contains(clinics.stream().findAny().get().getUser().getId()));
    }

    @Transactional
    @Rollback
    @Test
    public void testGetRelevantClinicsNoSuchStudyType(){

        List<User> clinicList = new ArrayList<>(Arrays.asList(invalidUserClinic));

        long expectedCount = 0;

        final Collection<Clinic> clinics = dao.getRelevantClinics(userMedic, clinicList, null, null, null, null, null,false,DEFAULT_PAGE, PAGE_SIZE_WITH_ALL_ORDERS);

        Assert.assertNotNull(clinics);
        Assert.assertEquals(expectedCount, clinics.size());
    }

    @Transactional
    @Rollback
    @Test
    public void testGetRelevantMedicsNoParams(){
        // should return the same as getting all orders of the user

        long expectedCount = medicIdListWithOrders.size();

        final Collection<Medic> medics = dao.getRelevantMedics(userMedic,null, null, null, null, null, null,true,DEFAULT_PAGE, PAGE_SIZE_WITH_ALL_ORDERS);

        Assert.assertNotNull(medics);
        Assert.assertEquals(expectedCount, medics.size());
        Assert.assertTrue(medicIdListWithOrders.contains(medics.stream().findAny().get().getUser().getId()));
    }

    @Transactional
    @Rollback
    @Test
    public void testGetRelevantMedicsClinicOnly(){

        List<User> clinicList = new ArrayList<>(Arrays.asList(userClinic));

        long expectedCount = medicIdListWithOrders.size();

        final Collection<Medic> medics = dao.getRelevantMedics(userMedic,clinicList, null, null, null, null, null,true,DEFAULT_PAGE, PAGE_SIZE_WITH_ALL_ORDERS);

        Assert.assertNotNull(medics);
        Assert.assertEquals(expectedCount, medics.size());
        Assert.assertTrue(medicIdListWithOrders.contains(medics.stream().findAny().get().getUser().getId()));
    }

    @Transactional
    @Rollback
    @Test
    public void testGetRelevantMedicsNoSuchClinic(){

        List<User> clinicList = new ArrayList<>(Arrays.asList(invalidUserClinic));

        long expectedCount = 0;

        final Collection<Medic> medics = dao.getRelevantMedics(userMedic, clinicList, null, null, null, null, null,true,DEFAULT_PAGE, PAGE_SIZE_WITH_ALL_ORDERS);

        Assert.assertNotNull(medics);
        Assert.assertEquals(expectedCount, medics.size());
    }

    @Transactional
    @Rollback
    @Test
    public void testGetRelevantPatientEmailsNoParams(){
        // should return the same as getting all orders of the user

        long expectedCount = patientEmailsAssingedToMedic.size();

        final Collection<String> patientEmails = dao.getRelevantPatientEmails(userMedic,null, null, null, null, null, null,true,DEFAULT_PAGE, PAGE_SIZE_WITH_ALL_ORDERS);

        Assert.assertNotNull(patientEmails);
        Assert.assertEquals(expectedCount, patientEmails.size());
        Assert.assertTrue(patientEmailsAssingedToMedic.contains(patientEmails.stream().findAny().get()));
    }

    @Transactional
    @Rollback
    @Test
    public void testGetRelevantPatientEmailsMedicOnly(){

        List<User> medicList = new ArrayList<>(Arrays.asList(userMedic));

        long expectedCount = patientEmailsAssingedToMedic.size();

        final Collection<String> patientEmails = dao.getRelevantPatientEmails(userMedic, null, medicList, null, null, null, null,false, DEFAULT_PAGE, PAGE_SIZE_WITH_ALL_ORDERS);

        Assert.assertNotNull(patientEmails);
        Assert.assertEquals(expectedCount, patientEmails.size());
        Assert.assertTrue(patientEmailsAssingedToMedic.contains(patientEmails.stream().findAny().get()));
    }

    @Transactional
    @Rollback
    @Test
    public void testGetRelevantPatientEmailsNoSuchMedic(){

        List<User> medicList = new ArrayList<>(Arrays.asList(invalidUserMedic));

        long expectedCount = 0;

        final Collection<String> patientEmails = dao.getRelevantPatientEmails(userMedic, null, medicList, null, null, null, null,false,DEFAULT_PAGE, PAGE_SIZE_WITH_ALL_ORDERS);

        Assert.assertNotNull(patientEmails);
        Assert.assertEquals(expectedCount, patientEmails.size());
    }
}
