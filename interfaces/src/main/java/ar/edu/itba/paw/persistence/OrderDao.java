package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.*;

import java.sql.Date;
import java.util.Optional;

public interface OrderDao {

    public Optional<Order> findById(long id);

    Order register(Medic medic, Date date, Clinic clinic, Patient patient, Study study, String description, byte[] identification, String medic_plan, String medic_plan_number);
}
