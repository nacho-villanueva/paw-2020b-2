package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

public interface OrderService {
    public Optional<Order> findById(long id);

    Order register(Medic medic, LocalDate date, Clinic clinic, String patientName, String patientEmail, StudyType studyType, String description, String identificationType, byte[] identification, String medicPlan, String medicPlanNumber);

    Collection<Order> getAllAsClinic(User user, int page, int pageSize);

    long getAllAsClinicCount(User user);

    long getAllAsClinicLastPage(User user, int pageSize);

    Collection<Order> getAllAsMedic(User user, int page, int pageSize);

    Collection<Order> getAllAsMedic(User user, boolean includeShared, int page, int pageSize);

    long getAllAsMedicCount(User user);

    long getAllAsMedicLastPage(User user, int pageSize);

    long getAllAsMedicCount(User user, boolean includeShared);

    long getAllAsMedicLastPage(User user, boolean includeShared, int pageSize);

    Collection<Order> getAllAsPatient(User user, int page, int pageSize);

    long getAllAsPatientCount(User user);

    long getAllAsPatientLastPage(User user, int pageSize);

    Order shareWithMedic(Order order, User user);

    Order changeOrderClinic(Order order, Clinic clinic);

    Collection<Order> filterOrders(User user, Collection<Integer> clinicUsersId, Collection<Integer> medicUsersId, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<Integer> studyTypesId, boolean includeSharedIfMedic, int page, int pageSize);

    long filterOrdersCount(User user, Collection<Integer> clinicUsersId, Collection<Integer> medicUsersId, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<Integer> studyTypesId, boolean includeSharedIfMedic);

    long filterOrdersLastPage(User user, Collection<Integer> clinicUsersId, Collection<Integer> medicUsersId, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<Integer> studyTypesId, boolean includeSharedIfMedic, int pageSize);

    Collection<StudyType> studyTypesFromFilteredOrders(User user, Collection<Integer> clinicUsersId, Collection<Integer> medicUsersId, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<Integer> studyTypesId, boolean includeSharedIfMedic, int page, int pageSize);

    long studyTypesFromFilteredOrdersCount(User user, Collection<Integer> clinicUsersId, Collection<Integer> medicUsersId, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<Integer> studyTypesId, boolean includeSharedIfMedic);

    long studyTypesFromFilteredOrdersLastPage(User user, Collection<Integer> clinicUsersId, Collection<Integer> medicUsersId, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<Integer> studyTypesId, boolean includeSharedIfMedic, int pageSize);

    Collection<Clinic> clinicsFromFilteredOrders(User user, Collection<Integer> clinicUsersId, Collection<Integer> medicUsersId, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<Integer> studyTypesId, boolean includeSharedIfMedic, int page, int pageSize);

    long clinicsFromFilteredOrdersCount(User user, Collection<Integer> clinicUsersId, Collection<Integer> medicUsersId, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<Integer> studyTypesId, boolean includeSharedIfMedic);

    long clinicsFromFilteredOrdersLastPage(User user, Collection<Integer> clinicUsersId, Collection<Integer> medicUsersId, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<Integer> studyTypesId, boolean includeSharedIfMedic, int pageSize);

    Collection<Medic> medicsFromFilteredOrders(User user, Collection<Integer> clinicUsersId, Collection<Integer> medicUsersId, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<Integer> studyTypesId, boolean includeSharedIfMedic, int page, int pageSize);

    long medicsFromFilteredOrdersCount(User user, Collection<Integer> clinicUsersId, Collection<Integer> medicUsersId, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<Integer> studyTypesId, boolean includeSharedIfMedic);

    long medicsFromFilteredOrdersLastPage(User user, Collection<Integer> clinicUsersId, Collection<Integer> medicUsersId, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<Integer> studyTypesId, boolean includeSharedIfMedic, int pageSize);

    Collection<String> patientEmailsFromFilteredOrders(User user, Collection<Integer> clinicUsersId, Collection<Integer> medicUsersId, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<Integer> studyTypesId, boolean includeSharedIfMedic, int page, int pageSize);

    long patientEmailsFromFilteredOrdersCount(User user, Collection<Integer> clinicUsersId, Collection<Integer> medicUsersId, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<Integer> studyTypesId, boolean includeSharedIfMedic);

    long patientEmailsFromFilteredOrdersLastPage(User user, Collection<Integer> clinicUsersId, Collection<Integer> medicUsersId, Collection<String> patientEmails, LocalDate fromDate, LocalDate toDate, Collection<Integer> studyTypesId, boolean includeSharedIfMedic, int pageSize);

    Collection<Order> getAllAsPatientOfType(String email, StudyType type, int page, int pageSize);

    long getAllAsPatientOfTypeCount(String email, StudyType type);

    long getAllAsPatientOfTypeLastPage(String email, StudyType type, int pageSize);

    Collection<Order> getAllAsUser(User user, int page, int pageSize);

    Collection<Order> getAllAsUser(User user, boolean includeShared, int page, int pageSize);

    long getAllAsUserCount(User user);

    long getAllAsUserLastPage(User user, int pageSize);

    long getAllAsUserCount(User user, boolean includeShared);

    long getAllAsUserLastPage(User user, boolean includeShared, int pageSize);
}
