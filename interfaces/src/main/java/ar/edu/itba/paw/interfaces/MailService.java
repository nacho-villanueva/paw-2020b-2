package ar.edu.itba.paw.interfaces;

public interface MailService {
    public void sendSimpleMessage(String to, String subject, String text);
}
