package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Clinic;
import ar.edu.itba.paw.models.Medic;
import ar.edu.itba.paw.models.Order;
import ar.edu.itba.paw.models.Result;

public interface MailNotificationService {
    void sendOrderMail(Order order);

    void sendResultMail(Result result);

    void sendMedicApplicationValidatingMail(Medic medic);

    void sendClinicApplicationValidatingMail(Clinic clinic);

    void sendVerificationMessage(String email, String token, String locale);

}
