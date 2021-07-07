package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface OrderService {
    public Optional<Order> findById(long id);

    Order register(Medic medic, LocalDate date, Clinic clinic, String patientName, String patientEmail, StudyType studyType, String description, String identificationType, byte[] identification, String medicPlan, String medicPlanNumber);

    //TODO: deprecated, remove usages when possible
    Collection<Order> getAllAsClinic(User user);

    Collection<Order> getAllAsClinic(User user, int page);

    Collection<Order> getAllAsClinic(User user, int page, int pageSize);

    long getAllAsClinicCount(User user);

    long getAllAsClinicLastPage(User user);

    long getAllAsClinicLastPage(User user, int pageSize);

    //TODO: deprecated, remove usages when possible
    Collection<Order> getAllAsMedic(User user);

    Collection<Order> getAllAsMedic(User user, int page);

    Collection<Order> getAllAsMedic(User user, int page, int pageSize);

    Collection<Order> getAllAsMedic(User user, boolean includeShared, int page);

    Collection<Order> getAllAsMedic(User user, boolean includeShared, int page, int pageSize);

    long getAllAsMedicCount(User user);

    long getAllAsMedicLastPage(User user);

    long getAllAsMedicLastPage(User user, int pageSize);

    long getAllAsMedicCount(User user, boolean includeShared);

    long getAllAsMedicLastPage(User user, boolean includeShared);

    long getAllAsMedicLastPage(User user, boolean includeShared, int pageSize);

    //TODO: deprecated, remove usages when possible
    Collection<Order> getAllAsPatient(User user);

    Collection<Order> getAllAsPatient(User user, int page);

    Collection<Order> getAllAsPatient(User user, int page, int pageSize);

    long getAllAsPatientCount(User user);

    long getAllAsPatientLastPage(User user);

    long getAllAsPatientLastPage(User user, int pageSize);

    Order shareWithMedic(Order order, User user);

    Order changeOrderClinic(Order order, Clinic clinic);

    //TODO: DEPRECATED, remove after removing deprecated filterOrders
    enum Parameters{
        DATE,
        CLINIC,
        MEDIC,
        PATIENT,
        STUDYTYPE;
    }

    //TODO: SHOULD BE DEPRECATED AFTER REFACTOR
    Collection<Order> filterOrders(User user, Map<Parameters, String> parameters);

    Collection<Order> filterOrders(User user, Collection<User> clinicUsers, Collection<User> medicUsers, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<StudyType> studyTypes, boolean includeSharedIfMedic, int page);

    Collection<Order> filterOrders(User user, Collection<User> clinicUsers, Collection<User> medicUsers, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<StudyType> studyTypes, boolean includeSharedIfMedic, int page, int pageSize);

    long filterOrdersCount(User user, Collection<User> clinicUsers, Collection<User> medicUsers, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<StudyType> studyTypes, boolean includeSharedIfMedic);

    long filterOrdersLastPage(User user, Collection<User> clinicUsers, Collection<User> medicUsers, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<StudyType> studyTypes, boolean includeSharedIfMedic);

    long filterOrdersLastPage(User user, Collection<User> clinicUsers, Collection<User> medicUsers, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<StudyType> studyTypes, boolean includeSharedIfMedic, int pageSize);

    Collection<StudyType> studyTypesFromFilteredOrders(User user, Collection<User> clinicUsers, Collection<User> medicUsers, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<StudyType> studyTypes, boolean includeSharedIfMedic, int page);

    Collection<StudyType> studyTypesFromFilteredOrders(User user, Collection<User> clinicUsers, Collection<User> medicUsers, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<StudyType> studyTypes, boolean includeSharedIfMedic, int page, int pageSize);

    long studyTypesFromFilteredOrdersCount(User user, Collection<User> clinicUsers, Collection<User> medicUsers, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<StudyType> studyTypes, boolean includeSharedIfMedic);

    long studyTypesFromFilteredOrdersLastPage(User user, Collection<User> clinicUsers, Collection<User> medicUsers, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<StudyType> studyTypes, boolean includeSharedIfMedic);

    long studyTypesFromFilteredOrdersLastPage(User user, Collection<User> clinicUsers, Collection<User> medicUsers, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<StudyType> studyTypes, boolean includeSharedIfMedic, int pageSize);

    Collection<Clinic> clinicsFromFilteredOrders(User user, Collection<User> clinicUsers, Collection<User> medicUsers, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<StudyType> studyTypes, boolean includeSharedIfMedic, int page);

    Collection<Clinic> clinicsFromFilteredOrders(User user, Collection<User> clinicUsers, Collection<User> medicUsers, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<StudyType> studyTypes, boolean includeSharedIfMedic, int page, int pageSize);

    long clinicsFromFilteredOrdersCount(User user, Collection<User> clinicUsers, Collection<User> medicUsers, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<StudyType> studyTypes, boolean includeSharedIfMedic);

    long clinicsFromFilteredOrdersLastPage(User user, Collection<User> clinicUsers, Collection<User> medicUsers, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<StudyType> studyTypes, boolean includeSharedIfMedic);

    long clinicsFromFilteredOrdersLastPage(User user, Collection<User> clinicUsers, Collection<User> medicUsers, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<StudyType> studyTypes, boolean includeSharedIfMedic, int pageSize);

    Collection<Medic> medicsFromFilteredOrders(User user, Collection<User> clinicUsers, Collection<User> medicUsers, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<StudyType> studyTypes, boolean includeSharedIfMedic, int page);

    Collection<Medic> medicsFromFilteredOrders(User user, Collection<User> clinicUsers, Collection<User> medicUsers, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<StudyType> studyTypes, boolean includeSharedIfMedic, int page, int pageSize);

    long medicsFromFilteredOrdersCount(User user, Collection<User> clinicUsers, Collection<User> medicUsers, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<StudyType> studyTypes, boolean includeSharedIfMedic);

    long medicsFromFilteredOrdersLastPage(User user, Collection<User> clinicUsers, Collection<User> medicUsers, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<StudyType> studyTypes, boolean includeSharedIfMedic);

    long medicsFromFilteredOrdersLastPage(User user, Collection<User> clinicUsers, Collection<User> medicUsers, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<StudyType> studyTypes, boolean includeSharedIfMedic, int pageSize);

    Collection<String> patientEmailsFromFilteredOrders(User user, Collection<User> clinicUsers, Collection<User> medicUsers, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<StudyType> studyTypes, boolean includeSharedIfMedic, int page);

    Collection<String> patientEmailsFromFilteredOrders(User user, Collection<User> clinicUsers, Collection<User> medicUsers, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<StudyType> studyTypes, boolean includeSharedIfMedic, int page, int pageSize);

    long patientEmailsFromFilteredOrdersCount(User user, Collection<User> clinicUsers, Collection<User> medicUsers, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<StudyType> studyTypes, boolean includeSharedIfMedic);

    long patientEmailsFromFilteredOrdersLastPage(User user, Collection<User> clinicUsers, Collection<User> medicUsers, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<StudyType> studyTypes, boolean includeSharedIfMedic);

    long patientEmailsFromFilteredOrdersLastPage(User user, Collection<User> clinicUsers, Collection<User> medicUsers, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<StudyType> studyTypes, boolean includeSharedIfMedic, int pageSize);

    //TODO: deprecated, remove usages when possible
    Collection<Order> getAllAsPatientOfType(String email, StudyType type);

    Collection<Order> getAllAsPatientOfType(String email, StudyType type, int page);

    Collection<Order> getAllAsPatientOfType(String email, StudyType type, int page, int pageSize);

    long getAllAsPatientOfTypeCount(String email, StudyType type);

    long getAllAsPatientOfTypeLastPage(String email, StudyType type);

    long getAllAsPatientOfTypeLastPage(String email, StudyType type, int pageSize);

    //TODO: deprecated, remove usages when possible
    Collection<Order> getAllAsUser(User user);

    Collection<Order> getAllAsUser(User user, int page);

    Collection<Order> getAllAsUser(User user, int page, int pageSize);

    Collection<Order> getAllAsUser(User user, boolean includeShared, int page);

    Collection<Order> getAllAsUser(User user, boolean includeShared, int page, int pageSize);

    long getAllAsUserCount(User user);

    long getAllAsUserLastPage(User user);

    long getAllAsUserLastPage(User user, int pageSize);

    long getAllAsUserCount(User user, boolean includeShared);

    long getAllAsUserLastPage(User user, boolean includeShared);

    long getAllAsUserLastPage(User user, boolean includeShared, int pageSize);
}
