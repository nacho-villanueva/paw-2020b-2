package ar.edu.itba.paw.services;

import java.util.Collection;

public interface MailService {
    public void sendSimpleMessage(String to, String subject, String text);

    public void sendMimeMessage(String to, String subject, String htmlText, Collection<String> inline);

    void sendVerificationMessage(String email, String token);
}
