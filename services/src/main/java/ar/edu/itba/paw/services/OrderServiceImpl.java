package ar.edu.itba.paw.services;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.persistence.OrderDao;
import ar.edu.itba.paw.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
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
    public Order register(Medic medic, Date date, Clinic clinic, Patient patient, StudyType studyType, String description, String identification_type, byte[] identification, String medic_plan, String medic_plan_number) {
        return orderDao.register(medic, date, clinic, patient, studyType, description, identification_type, identification, medic_plan, medic_plan_number);
    }
}