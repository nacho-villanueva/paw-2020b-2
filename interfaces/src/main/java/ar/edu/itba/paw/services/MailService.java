package ar.edu.itba.paw.services;

import java.util.Collection;

public interface MailService {
    void sendSimpleMessage(String to, String subject, String text);

    void sendMimeMessage(String to, String subject, String htmlText, Collection<String> inline);
}
