package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.*;

import java.sql.Date;
import java.util.Collection;
import java.util.Optional;

public interface OrderDao {

    public Optional<Order> findById(long id);

    Order register(Medic medic, Date date, Clinic clinic, String patient_name, String patient_email, StudyType studyType, String description, String identification_type, byte[] identification, String medic_plan, String medic_plan_number);

    Collection<Order> getAllAsClinic(User user);

    Collection<Order> getAllAsMedic(User user);

    Collection<Order> getAllSharedAsMedic(User user);

    Order shareWithMedic(Order order, User user);

    Collection<Order> getAllAsPatient(User user);

    Collection<Order> getAllAsPatientOfType(String email, StudyType type);
}
