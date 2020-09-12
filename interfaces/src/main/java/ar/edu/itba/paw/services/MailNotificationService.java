package ar.edu.itba.paw.services;

import ar.edu.itba.paw.model.Order;
import ar.edu.itba.paw.model.Result;

public interface MailNotificationService {
    public void sendOrderMail(Order order);

    public void sendResultMail(Result result);

}