package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.ClinicDoesNotHaveStudyTypeException;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.persistence.OrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service
public class OrderServiceImpl implements OrderService {

    // Pagination-related constants
    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_MAX_PAGE_SIZE = 10;

    @Autowired
    private MailNotificationService mailNotificationService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private MedicService medicService;

    @Autowired
    private ClinicService clinicService;

    @Autowired
    private UserService userService;

    @Autowired
    private StudyTypeService studyService;


    @Override
    public Optional<Order> findById(long id) {
        return orderDao.findById(id);
    }

    @Override
    public Order register(Medic medic, LocalDate date, Clinic clinic, String patientEmail, String patientName, StudyType studyType, String description, String identificationType, byte[] identification, String medicPlan, String medicPlanNumber) throws ClinicDoesNotHaveStudyTypeException {

        if (!clinic.getMedicalStudies().stream().map(StudyType::getId).collect(Collectors.toList()).contains(studyType.getId()))
            throw new ClinicDoesNotHaveStudyTypeException();

        Order order = orderDao.register(medic, date, clinic, patientName, patientEmail, studyType, description, identificationType, identification, medicPlan, medicPlanNumber);
        mailNotificationService.sendOrderMail(order);
        return order;
    }

    @Override
    public Collection<Order> getAllAsClinic(User user, int page, int pageSize) {
        return orderDao.getAllAsClinic(user, page, pageSize);
    }

    @Override
    public long getAllAsClinicCount(User user) {
        return orderDao.getAllAsClinicCount(user);
    }

    @Override
    public long getAllAsClinicLastPage(User user, int pageSize) {
        return getLastPage(getAllAsClinicCount(user), pageSize);
    }

    @Override
    public Collection<Order> getAllAsMedic(User user, int page, int pageSize) {
        return getAllAsMedic(user, false, page, pageSize);
    }

    @Override
    public Collection<Order> getAllAsMedic(User user, boolean includeShared, int page, int pageSize) {
        if (includeShared)
            return orderDao.getAllParticipatingAsMedic(user, page, pageSize);
        else
            return orderDao.getAllAsMedic(user, page, pageSize);
    }

    @Override
    public long getAllAsMedicCount(User user) {
        return getAllAsMedicCount(user, false);
    }

    @Override
    public long getAllAsMedicLastPage(User user, int pageSize) {
        return getAllAsMedicLastPage(user, false, pageSize);
    }

    @Override
    public long getAllAsMedicCount(User user, boolean includeShared) {
        if (includeShared)
            return orderDao.getAllParticipatingAsMedicCount(user);
        else
            return orderDao.getAllAsMedicCount(user);
    }

    @Override
    public long getAllAsMedicLastPage(User user, boolean includeShared, int pageSize) {
        return getLastPage(getAllAsMedicCount(user, includeShared), pageSize);
    }

    @Override
    public Collection<Order> getAllAsPatient(User user, int page, int pageSize) {
        return orderDao.getAllAsPatient(user, page, pageSize);
    }

    @Override
    public long getAllAsPatientCount(User user) {
        return orderDao.getAllAsPatientCount(user);
    }

    @Override
    public long getAllAsPatientLastPage(User user, int pageSize) {
        return getLastPage(getAllAsPatientCount(user), pageSize);
    }

    @Override
    public Collection<Order> getAllAsPatientOfType(String email, StudyType type, int page, int pageSize) {
        return orderDao.getAllAsPatientOfType(email, type, page, pageSize);
    }

    @Override
    public long getAllAsPatientOfTypeCount(String email, StudyType type) {
        return orderDao.getAllAsPatientOfTypeCount(email, type);
    }

    @Override
    public long getAllAsPatientOfTypeLastPage(String email, StudyType type, int pageSize) {
        return getLastPage(getAllAsPatientOfTypeCount(email, type), pageSize);
    }

    @Override
    public Collection<Order> getAllAsUser(User user, int page, int pageSize) {
        return getAllAsUser(user, false, page, pageSize);
    }

    @Override
    public Collection<Order> getAllAsUser(User user, boolean includeShared, int page, int pageSize) {
        return orderDao.getAllAsUser(user, includeShared, page, pageSize);
    }

    @Override
    public long getAllAsUserCount(User user) {
        return getAllAsUserCount(user, false);
    }

    @Override
    public long getAllAsUserLastPage(User user, int pageSize) {
        return getAllAsUserLastPage(user, false, pageSize);
    }

    @Override
    public long getAllAsUserCount(User user, boolean includeShared) {
        return orderDao.getAllAsUserCount(user, includeShared);
    }

    @Override
    public long getAllAsUserLastPage(User user, boolean includeShared, int pageSize) {
        return getLastPage(getAllAsUserCount(user, includeShared), pageSize);
    }

    @Override
    public Collection<Order> filterOrders(User user, Collection<Integer> clinicUsersId, Collection<Integer> medicUsersId, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<Integer> studyTypesId, boolean includeSharedIfMedic, int page, int pageSize) {

        Collection<User> clinicUsers = getClinicUsersFromId(clinicUsersId);
        Collection<User> medicUsers = getMedicUsersFromId(medicUsersId);
        Collection<StudyType> studyTypes = getStudyTypesFromId(studyTypesId);

        return orderDao.getFiltered(user, clinicUsers, medicUsers, patientEmails, fromDate, toDate, studyTypes, includeSharedIfMedic, page, pageSize);
    }

    @Override
    public long filterOrdersCount(User user, Collection<Integer> clinicUsersId, Collection<Integer> medicUsersId, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<Integer> studyTypesId, boolean includeSharedIfMedic) {

        Collection<User> clinicUsers = getClinicUsersFromId(clinicUsersId);
        Collection<User> medicUsers = getMedicUsersFromId(medicUsersId);
        Collection<StudyType> studyTypes = getStudyTypesFromId(studyTypesId);

        return orderDao.getFilteredCount(user, clinicUsers, medicUsers, patientEmails, fromDate, toDate, studyTypes, includeSharedIfMedic);
    }

    @Override
    public long filterOrdersLastPage(User user, Collection<Integer> clinicUsersId, Collection<Integer> medicUsersId, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<Integer> studyTypesId, boolean includeSharedIfMedic, int pageSize) {
        return getLastPage(filterOrdersCount(user, clinicUsersId, medicUsersId, patientEmails, fromDate, toDate, studyTypesId, includeSharedIfMedic), pageSize);
    }

    @Override
    public Collection<StudyType> studyTypesFromFilteredOrders(User user, Collection<Integer> clinicUsersId, Collection<Integer> medicUsersId, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<Integer> studyTypesId, boolean includeSharedIfMedic, int page, int pageSize) {

        Collection<User> clinicUsers = getClinicUsersFromId(clinicUsersId);
        Collection<User> medicUsers = getMedicUsersFromId(medicUsersId);
        Collection<StudyType> studyTypes = getStudyTypesFromId(studyTypesId);

        return orderDao.getRelevantStudyTypes(user, clinicUsers, medicUsers, patientEmails, fromDate, toDate, studyTypes, includeSharedIfMedic, page, pageSize);
    }

    @Override
    public long studyTypesFromFilteredOrdersCount(User user, Collection<Integer> clinicUsersId, Collection<Integer> medicUsersId, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<Integer> studyTypesId, boolean includeSharedIfMedic) {

        Collection<User> clinicUsers = getClinicUsersFromId(clinicUsersId);
        Collection<User> medicUsers = getMedicUsersFromId(medicUsersId);
        Collection<StudyType> studyTypes = getStudyTypesFromId(studyTypesId);

        return orderDao.getRelevantStudyTypesCount(user, clinicUsers, medicUsers, patientEmails, fromDate, toDate, studyTypes, includeSharedIfMedic);
    }

    @Override
    public long studyTypesFromFilteredOrdersLastPage(User user, Collection<Integer> clinicUsersId, Collection<Integer> medicUsersId, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<Integer> studyTypesId, boolean includeSharedIfMedic, int pageSize) {
        return getLastPage(studyTypesFromFilteredOrdersCount(user, clinicUsersId, medicUsersId, patientEmails, fromDate, toDate, studyTypesId, includeSharedIfMedic), pageSize);
    }

    @Override
    public Collection<Clinic> clinicsFromFilteredOrders(User user, Collection<Integer> clinicUsersId, Collection<Integer> medicUsersId, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<Integer> studyTypesId, boolean includeSharedIfMedic, int page, int pageSize) {

        Collection<User> clinicUsers = getClinicUsersFromId(clinicUsersId);
        Collection<User> medicUsers = getMedicUsersFromId(medicUsersId);
        Collection<StudyType> studyTypes = getStudyTypesFromId(studyTypesId);

        return orderDao.getRelevantClinics(user, clinicUsers, medicUsers, patientEmails, fromDate, toDate, studyTypes, includeSharedIfMedic, page, pageSize);
    }

    @Override
    public long clinicsFromFilteredOrdersCount(User user, Collection<Integer> clinicUsersId, Collection<Integer> medicUsersId, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<Integer> studyTypesId, boolean includeSharedIfMedic) {

        Collection<User> clinicUsers = getClinicUsersFromId(clinicUsersId);
        Collection<User> medicUsers = getMedicUsersFromId(medicUsersId);
        Collection<StudyType> studyTypes = getStudyTypesFromId(studyTypesId);

        return orderDao.getRelevantClinicsCount(user, clinicUsers, medicUsers, patientEmails, fromDate, toDate, studyTypes, includeSharedIfMedic);
    }

    @Override
    public long clinicsFromFilteredOrdersLastPage(User user, Collection<Integer> clinicUsersId, Collection<Integer> medicUsersId, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<Integer> studyTypesId, boolean includeSharedIfMedic, int pageSize) {
        return getLastPage(clinicsFromFilteredOrdersCount(user, clinicUsersId, medicUsersId, patientEmails, fromDate, toDate, studyTypesId, includeSharedIfMedic), pageSize);
    }

    @Override
    public Collection<Medic> medicsFromFilteredOrders(User user, Collection<Integer> clinicUsersId, Collection<Integer> medicUsersId, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<Integer> studyTypesId, boolean includeSharedIfMedic, int page, int pageSize) {

        Collection<User> clinicUsers = getClinicUsersFromId(clinicUsersId);
        Collection<User> medicUsers = getMedicUsersFromId(medicUsersId);
        Collection<StudyType> studyTypes = getStudyTypesFromId(studyTypesId);

        return orderDao.getRelevantMedics(user, clinicUsers, medicUsers, patientEmails, fromDate, toDate, studyTypes, includeSharedIfMedic, page, pageSize);
    }

    @Override
    public long medicsFromFilteredOrdersCount(User user, Collection<Integer> clinicUsersId, Collection<Integer> medicUsersId, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<Integer> studyTypesId, boolean includeSharedIfMedic) {

        Collection<User> clinicUsers = getClinicUsersFromId(clinicUsersId);
        Collection<User> medicUsers = getMedicUsersFromId(medicUsersId);
        Collection<StudyType> studyTypes = getStudyTypesFromId(studyTypesId);

        return orderDao.getRelevantMedicsCount(user, clinicUsers, medicUsers, patientEmails, fromDate, toDate, studyTypes, includeSharedIfMedic);
    }

    @Override
    public long medicsFromFilteredOrdersLastPage(User user, Collection<Integer> clinicUsersId, Collection<Integer> medicUsersId, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<Integer> studyTypesId, boolean includeSharedIfMedic, int pageSize) {
        return getLastPage(medicsFromFilteredOrdersCount(user, clinicUsersId, medicUsersId, patientEmails, fromDate, toDate, studyTypesId, includeSharedIfMedic), pageSize);
    }

    @Override
    public Collection<String> patientEmailsFromFilteredOrders(User user, Collection<Integer> clinicUsersId, Collection<Integer> medicUsersId, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<Integer> studyTypesId, boolean includeSharedIfMedic, int page, int pageSize) {

        Collection<User> clinicUsers = getClinicUsersFromId(clinicUsersId);
        Collection<User> medicUsers = getMedicUsersFromId(medicUsersId);
        Collection<StudyType> studyTypes = getStudyTypesFromId(studyTypesId);

        return orderDao.getRelevantPatientEmails(user, clinicUsers, medicUsers, patientEmails, fromDate, toDate, studyTypes, includeSharedIfMedic, page, pageSize);
    }

    @Override
    public long patientEmailsFromFilteredOrdersCount(User user, Collection<Integer> clinicUsersId, Collection<Integer> medicUsersId, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<Integer> studyTypesId, boolean includeSharedIfMedic) {

        Collection<User> clinicUsers = getClinicUsersFromId(clinicUsersId);
        Collection<User> medicUsers = getMedicUsersFromId(medicUsersId);
        Collection<StudyType> studyTypes = getStudyTypesFromId(studyTypesId);

        return orderDao.getRelevantPatientEmailsCount(user, clinicUsers, medicUsers, patientEmails, fromDate, toDate, studyTypes, includeSharedIfMedic);
    }

    @Override
    public long patientEmailsFromFilteredOrdersLastPage(User user, Collection<Integer> clinicUsersId, Collection<Integer> medicUsersId, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<Integer> studyTypesId, boolean includeSharedIfMedic, int pageSize) {
        return getLastPage(patientEmailsFromFilteredOrdersCount(user, clinicUsersId, medicUsersId, patientEmails, fromDate, toDate, studyTypesId, includeSharedIfMedic), pageSize);
    }

    @Override
    public Order shareWithMedic(Order order, User user) {
        Order o = orderDao.shareWithMedic(order, user);
        if (o != null) {
            mailNotificationService.sendSharedOrderMail(order, user);
        }
        return o;
    }

    @Override
    public Order changeOrderClinic(Order order, Clinic clinic) throws ClinicDoesNotHaveStudyTypeException {

        if (!clinic.getMedicalStudies().stream().map(StudyType::getId).collect(Collectors.toList()).contains(order.getStudy().getId()))
            throw new ClinicDoesNotHaveStudyTypeException();

        Order newOrder = orderDao.changeOrderClinic(order, clinic);
        mailNotificationService.sendChangeClinicMail(order);
        return newOrder;
    }

    // auxiliary functions
    private long getLastPage(final long count, final int pageSize) {
        return (long) Math.ceil((double) count / pageSize);
    }

    // auxiliary functions
    private boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    private Collection<User> getClinicUsersFromId(Collection<Integer> clinicUsersId) {
        Collection<User> clinicUsers = new ArrayList<>();

        if (!isEmpty(clinicUsersId)) {
            for (Integer id : clinicUsersId) {
                if (id != null)
                    clinicService.findByUserId(id).ifPresent(clinic -> clinicUsers.add(clinic.getUser()));
            }
        }

        return clinicUsers;
    }

    private Collection<User> getMedicUsersFromId(Collection<Integer> medicUsersId) {
        Collection<User> medicUsers = new ArrayList<>();

        if (!isEmpty(medicUsersId)) {
            for (Integer id : medicUsersId) {
                if (id != null)
                    medicService.findByUserId(id).ifPresent(medic -> medicUsers.add(medic.getUser()));
            }
        }

        return medicUsers;
    }

    private Collection<StudyType> getStudyTypesFromId(Collection<Integer> studyTypesId) {
        Collection<StudyType> studyTypes = new ArrayList<>();

        if (!isEmpty(studyTypesId)) {
            for (Integer id : studyTypesId) {
                if (id != null) studyService.findById(id).ifPresent(studyTypes::add);
            }
        }

        return studyTypes;
    }
}