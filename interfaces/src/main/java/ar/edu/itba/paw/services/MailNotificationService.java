package ar.edu.itba.paw.services;

import ar.edu.itba.paw.model.Clinic;
import ar.edu.itba.paw.model.Medic;
import ar.edu.itba.paw.model.Order;
import ar.edu.itba.paw.model.Result;

public interface MailNotificationService {
    public void sendOrderMail(Order order);

    public void sendResultMail(Result result);

    public void sendMedicApplicationValidatingMail(Medic medic);

    public void sendClinicApplicationValidatingMail(Clinic clinic);

}
