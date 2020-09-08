package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.OrderFormService;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.persistence.ClinicDao;
import ar.edu.itba.paw.persistence.MedicDao;
import ar.edu.itba.paw.persistence.OrderDao;
import ar.edu.itba.paw.persistence.StudyTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Primary
@Service
public class OrderFormServiceImpl implements OrderFormService {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ClinicDao clinicDao;
    @Autowired
    private MedicDao medicDao;
    @Autowired
    private StudyTypeDao studyTypeDao;

    @Override
    public Long HandleOrderForm(OrderForm orderForm, byte[] identification, String identificationType) {
        Medic medic = medicDao.findById(orderForm.getMedicId()).get();
        Clinic clinic = clinicDao.findById(orderForm.getClinicId()).get();
        StudyType studyType = studyTypeDao.findById(orderForm.getStudyId()).get();


        Order order = orderDao.register(
                medic,
                new Date(System.currentTimeMillis()),
                clinic,
                new Patient(orderForm.getPatientEmail(), orderForm.getPatientName()),
                studyType,
                orderForm.getDescription(),
                identificationType,
                identification,
                orderForm.getPatient_insurance_plan(),
                orderForm.getPatient_insurance_number());

        return order.getOrder_id();
    }
}
