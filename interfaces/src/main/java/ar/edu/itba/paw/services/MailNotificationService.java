package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.*;

public interface MailNotificationService {
    void sendOrderMail(Order order);

    void sendResultMail(Result result);

    void sendMedicApplicationValidatingMail(Medic medic);

    void sendClinicApplicationValidatingMail(Clinic clinic);

    void sendVerificationMessage(String email, String token, String locale);

    void sendShareRequestMail(ShareRequest shareRequest);

    void sendAcceptRequestMail(ShareRequest shareRequest);

    void sendDenyRequestMail(ShareRequest shareRequest);

    void sendChangeClinicMail(Order order);

}
