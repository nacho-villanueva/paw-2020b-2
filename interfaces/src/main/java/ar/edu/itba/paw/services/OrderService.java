package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface OrderService {
    public Optional<Order> findById(long id);

    Order register(Medic medic, LocalDate date, Clinic clinic, String patientName, String patientEmail, StudyType studyType, String description, String identificationType, byte[] identification, String medicPlan, String medicPlanNumber);

    Collection<Order> getAllAsClinic(User user);

    Collection<Order> getAllAsMedic(User user);

    Collection<Order> getAllAsPatient(User user);

    Order shareWithMedic(Order order, User user);

    Order changeOrderClinic(Order order, Clinic clinic);

    enum Parameters{
        DATE,
        CLINIC,
        MEDIC,
        PATIENT,
        STUDYTYPE;
    }

    Collection<Order> filterOrders(User user, Map<Parameters, String> parameters);

    Collection<Order> getAllAsPatientOfType(String email, StudyType type);

    Collection<Order> getAllAsUser(User user);
}
