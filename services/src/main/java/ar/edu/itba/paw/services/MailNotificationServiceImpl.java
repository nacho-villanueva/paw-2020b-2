package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.MailNotificationService;

import ar.edu.itba.paw.interfaces.MailService;
import ar.edu.itba.paw.model.Order;
import ar.edu.itba.paw.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class MailNotificationServiceImpl implements MailNotificationService {

    @Autowired
    private MailService ms;

    public void sendOrderMail(Order order) {

        // todo: update url
        // mail to patient
        ms.sendSimpleMessage(order.getPatient().getEmail(),"A new order is available!","Details available on [url]");

        // mail to doctor
        ms.sendSimpleMessage(order.getMedic().getEmail(),"You just updated a new order","Details available on [url]");

        // mail to clinic
        ms.sendSimpleMessage(order.getClinic().getEmail(),"A new order is available","Details available on [url]");
    }

    public void sendResultMail(Result result){

        // todo: update after getting order from result
        /*
        // mail to patient
        ms.sendSimpleMessage(result.getOrder().getPatient().getEmail(),"A new result has been uploaded!","Details available on [url]");

        // mail to doctor
        ms.sendSimpleMessage(order.getOrder().getMedic().getEmail(),"A new result has been uploaded","Details available on [url]");

        // mail to clinic
        ms.sendSimpleMessage(order.getOrder().getClinic().getEmail(),"You just uploaded a new result","Details available on [url]");
        */
    }
}
