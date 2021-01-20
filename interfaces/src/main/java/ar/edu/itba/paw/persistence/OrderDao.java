package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

public interface OrderDao {

    Optional<Order> findById(long id);

    Order register(Medic medic, LocalDate date, Clinic clinic, String patientName, String patientEmail, StudyType studyType, String description, String identificationType, byte[] identification, String medicPlan, String medicPlanNumber);

    Collection<Order> getAllAsClinic(User user, int page, int pageSize);

    long getAllAsClinicCount(User user);

    Collection<Order> getAllAsMedic(User user, int page, int pageSize);

    long getAllAsMedicCount(User user);

    Collection<Order> getAllSharedAsMedic(User user, int page, int pageSize);

    long getAllSharedAsMedicCount(User user);

    Collection<Order> getAllParticipatingAsMedic(User user, int page, int pageSize);

    long getAllParticipatingAsMedicCount(User user);

    Order shareWithMedic(Order order, User user);

    Collection<Order> getAllAsPatient(User user, int page, int pageSize);

    long getAllAsPatientCount(User user);

    Order changeOrderClinic(Order order, Clinic clinic);

    Collection<Order> getAllAsPatientOfType(String email, StudyType type, int page, int pageSize);

    long getAllAsPatientOfTypeCount(String email, StudyType type);

    Collection<Order> getFiltered(User user, User clinicUser, User medicUser, String patientEmail, LocalDate date, StudyType type, boolean includeSharedIfMedic, int page, int pageSize);

    long getFilteredCount(User user, User clinicUser, User medicUser, String patientEmail, LocalDate date, StudyType type, boolean includeSharedIfMedic);

    Collection<Order> getAllAsUser(User user, boolean includeSharedIfMedic, int page, int pageSize);

    long getAllAsUserCount(User user, boolean includeSharedIfMedic);
}
