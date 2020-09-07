package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.OrderFormService;
import ar.edu.itba.paw.model.Order;
import ar.edu.itba.paw.model.OrderForm;
import ar.edu.itba.paw.persistence.ClinicDao;
import ar.edu.itba.paw.persistence.MedicDao;
import ar.edu.itba.paw.persistence.OrderDao;
import ar.edu.itba.paw.persistence.PatientDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;

@Primary
@Service
public class OrderFormServiceImpl implements OrderFormService {

    @Autowired
    private MedicDao medicDao;
    @Autowired
    private PatientDao patientDao;
    @Autowired
    private ClinicDao clinicDao;
    @Autowired
    private OrderDao orderDao;

    @Override
    public String HandleOrderForm(OrderForm orderForm, byte[] identification) {
        Order order = GenerateOrder(orderForm, identification);

        //TODO: orderDao.storeOrder(order);
        String id = "getID";

        return id; //TODO: RETURN ID TO VIEW ORDER
    }

    private Order GenerateOrder(OrderForm orderForm, byte[] identification) {
        Order order = new Order(
                medicDao.findById(orderForm.getMedicId()),
                new Date(System.currentTimeMillis()),
                clinicDao.findById(orderForm.getClinicId()),
                orderForm.getStudy(),
                orderForm.getDescription(),
                identification,
                orderForm.getPatient_insurance_plan(),
                orderForm.getPatient_insurance_number(),
                patientDao.findById(12L), //TODO: CREATE findByEmail(email);
                null //TODO: REMOVE THIS FROM CONSTRUCTOR
                );

        return order;
    }
}
