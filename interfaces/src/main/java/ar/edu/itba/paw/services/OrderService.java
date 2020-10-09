package ar.edu.itba.paw.services;

import ar.edu.itba.paw.model.*;

import java.sql.Date;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

public interface OrderService {
    public Optional<Order> findById(long id);

    Order register(Medic medic, Date date, Clinic clinic, String patient_name, String patient_email, StudyType studyType, String description, String identification_type, byte[] identification, String medic_plan, String medic_plan_number);

    Collection<Order> getAllAsClinic(User user);

    Collection<Order> getAllAsMedic(User user);

    Collection<Order> getAllAsPatient(User user);

    enum Parameters{
        DATE,
        CLINIC,
        MEDIC,
        PATIENT,
        STUDYTYPE;
    }

    Collection<Order> filterOrders(User user, HashMap<Parameters, String> parameters);

    Collection<Order> getAllAsUser(User user);
}
