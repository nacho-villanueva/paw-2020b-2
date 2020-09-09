package ar.edu.itba.paw.service;

public interface MailService {
    public void sendSimpleMessage(String to, String subject, String text);
}
