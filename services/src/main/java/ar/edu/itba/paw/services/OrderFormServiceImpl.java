package ar.edu.itba.paw.services;

import ar.edu.itba.paw.service.*;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.service.OrderService;
import org.springframework.beans.MethodInvocationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Optional;

@Primary
@Service
public class OrderFormServiceImpl implements OrderFormService {

    @Autowired
    private OrderService orderService;
    @Autowired
    private ClinicService clinicService;
    @Autowired
    private MedicService medicService;
    @Autowired
    private StudyTypeService studyTypeService;

    @Autowired
    private MailNotificationService mailNotificationService;

    @Override
    public Long HandleOrderForm(OrderForm orderForm, byte[] identification, String identificationType) {
        Optional<Medic> medic = medicService.findById(orderForm.getMedicId());
        Optional<Clinic> clinic = clinicService.findById(orderForm.getClinicId());
        Optional<StudyType> studyType = studyTypeService.findById(orderForm.getStudyId());

        if(!medic.isPresent() || !clinic.isPresent() || !studyType.isPresent())
            return null;

        Order order = orderService.register(
                medic.get(),
                new Date(System.currentTimeMillis()),
                clinic.get(),
                new Patient(orderForm.getPatientEmail(), orderForm.getPatientName()),
                studyType.get(),
                orderForm.getDescription(),
                identificationType,
                identification,
                orderForm.getPatient_insurance_plan(),
                orderForm.getPatient_insurance_number());

        mailNotificationService.sendOrderMail(order);

        return order.getOrder_id();
    }
}
