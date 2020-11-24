package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.*;

import java.sql.Date;
import java.util.Collection;
import java.util.Optional;

public interface OrderDao {

    public Optional<Order> findById(long id);

    Order register(Medic medic, Date date, Clinic clinic, String patientName, String patientEmail, StudyType studyType, String description, String identificationType, byte[] identification, String medicPlan, String medicPlanNumber);

    Collection<Order> getAllAsClinic(User user);

    Collection<Order> getAllAsMedic(User user);

    Collection<Order> getAllSharedAsMedic(User user);

    Order shareWithMedic(Order order, User user);

    Collection<Order> getAllAsPatient(User user);
}
