package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Clinic;
import ar.edu.itba.paw.models.Medic;
import ar.edu.itba.paw.models.Order;
import ar.edu.itba.paw.models.Result;

public interface MailNotificationService {
    public void sendOrderMail(Order order);

    public void sendResultMail(Result result);

    public void sendMedicApplicationValidatingMail(Medic medic);

    public void sendClinicApplicationValidatingMail(Clinic clinic);

}
