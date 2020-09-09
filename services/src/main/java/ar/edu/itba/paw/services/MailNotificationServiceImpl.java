package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.MailNotificationService;

import ar.edu.itba.paw.interfaces.MailService;
import ar.edu.itba.paw.interfaces.UrlEncoderService;
import ar.edu.itba.paw.model.Order;
import ar.edu.itba.paw.model.Result;
import ar.edu.itba.paw.persistence.OrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Optional;

@Primary
@Service
public class MailNotificationServiceImpl implements MailNotificationService {

    @Autowired
    private MailService ms;

    @Autowired
    private URL address;

    @Autowired
    private UrlEncoderService urlEncoderService;

    @Autowired
    private OrderDao orderDao;

    private static final String patientOrderSubject  = "[Order %s] A new order is available for you to complete!";
    private static final String doctorOrderSubject   = "[Order %s] Your order has been uploaded successfully.";
    private static final String clinicOrderSubject   = "[Order %s] A new order has been uploaded.";
    private static final String patientResultSubject = "[Order %s/%s] New results are up!";
    private static final String doctorResultSubject  = "[Order %s/%s] New results are available.";
    private static final String clinicResultSubject  = "[Order %s/%s] Your results has been uploaded successfully.";

    private static final String orderTextTemplate    = "Details for this order are available from:\n\n%s/view-order/%s\n\nContact Info\n%s: %s (%s)\n%s: %s (%s)\n\nWith love,\n\tMedTracker (%s)";
    private static final String resultTextTemplate   = orderTextTemplate;

    public void sendOrderMail(Order order) {

        String url = address.toString();

        String patientMail  = order.getPatient().getEmail();
        String doctorMail   = order.getMedic().getEmail();
        String clinicMail   = order.getClinic().getEmail();
        String patientName  = order.getPatient().getName();
        String doctorName   = order.getMedic().getName();
        String clinicName   = order.getClinic().getName();
        String orderID      = String.valueOf(order.getOrder_id());
        String encodedID    = urlEncoderService.encode(order.getOrder_id());

        // mail to patient
        ms.sendSimpleMessage(patientMail,
                String.format(patientOrderSubject, orderID),
                String.format(orderTextTemplate,url,encodedID,"Doctor",doctorName,doctorMail,"Clinic",clinicName,clinicMail,url));

        // mail to doctor
        ms.sendSimpleMessage(doctorMail,
                String.format(doctorOrderSubject, orderID),
                String.format(orderTextTemplate,url,encodedID,"Patient",patientName,patientMail,"Clinic",clinicName,clinicMail,url));

        // mail to clinic
        ms.sendSimpleMessage(order.getClinic().getEmail(),
                String.format(clinicOrderSubject, order.getOrder_id()),
                String.format(orderTextTemplate,url,encodedID,"Patient",patientName,patientMail,"Doctor",doctorName,doctorMail,url));
    }

    public void sendResultMail(Result result){

        String url = address.toString();

        Optional<Order> resultOrder = orderDao.findById(result.getOrder_id());

        if(resultOrder.isPresent()){

            Order order = resultOrder.get();

            String patientMail  = order.getPatient().getEmail();
            String doctorMail   = order.getMedic().getEmail();
            String clinicMail   = order.getClinic().getEmail();
            String patientName  = order.getPatient().getName();
            String doctorName   = order.getMedic().getName();
            String clinicName   = order.getClinic().getName();
            String orderID      = String.valueOf(order.getOrder_id());
            String encodedID    = urlEncoderService.encode(order.getOrder_id());
            String resultID     = String.valueOf(result.getId());

            // mail to patient
            ms.sendSimpleMessage(patientMail,
                    String.format(patientResultSubject,orderID,resultID),
                    String.format(resultTextTemplate,url,encodedID,"Doctor",doctorName,doctorMail,"Clinic",clinicName,clinicMail,url));

            // mail to doctor
            ms.sendSimpleMessage(doctorMail,
                    String.format(doctorResultSubject,orderID,resultID),
                    String.format(resultTextTemplate,url,encodedID,"Patient",patientName,patientMail,"Clinic",clinicName,clinicMail,url));

            // mail to clinic
            ms.sendSimpleMessage(clinicMail,
                    String.format(clinicResultSubject,orderID,resultID),
                    String.format(resultTextTemplate,url,encodedID,"Patient",patientName,patientMail,"Doctor",doctorName,doctorMail,url));

        }else{
            // log error
        }
    }
}
