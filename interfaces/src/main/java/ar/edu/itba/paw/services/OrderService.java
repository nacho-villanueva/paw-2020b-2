package ar.edu.itba.paw.services;

import ar.edu.itba.paw.model.*;

import java.sql.Date;
import java.util.Optional;

public interface OrderService {
    public Optional<Order> findById(long id);

    public Order register(final Medic medic,
                          final Date date,
                          final Clinic clinic,
                          final Patient patient,
                          final StudyType studyType,
                          final String description,
                          final String identification_type,
                          final byte[] identification,
                          final String medic_plan,
                          final String medic_plan_number);
}
