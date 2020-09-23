package ar.edu.itba.paw.services;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.persistence.OrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Collection;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;


    @Override
    public Optional<Order> findById(long id) {
        return orderDao.findById(id);
    }

    @Override
    public Order register(Medic medic, Date date, Clinic clinic, String patient_name, String patient_email, StudyType studyType, String description, String identification_type, byte[] identification, String medic_plan, String medic_plan_number) {
        return orderDao.register(medic,date,clinic,patient_name,patient_email,studyType,description,identification_type,identification,medic_plan,medic_plan_number);
    }

    @Override
    public Collection<Order> getAllAsClinic(User user) {
        return orderDao.getAllAsClinic(user);
    }

    @Override
    public Collection<Order> getAllAsMedic(User user) {
        return orderDao.getAllAsMedic(user);
    }

    @Override
    public Collection<Order> getAllAsPatient(User user) {
        return orderDao.getAllAsPatient(user);
    }
}