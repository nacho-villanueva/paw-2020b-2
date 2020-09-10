package ar.edu.itba.paw.services;

public interface MailService {
    public void sendSimpleMessage(String to, String subject, String text);
}
