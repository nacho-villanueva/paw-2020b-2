package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.OrderNotFoundForExistingResultException;

import ar.edu.itba.paw.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Primary
@Service
public class MailNotificationServiceImpl implements MailNotificationService {

    @Autowired
    private MailService ms;

    @Autowired
    private URL address;

    @Autowired
    private UrlEncoderService urlEncoderService;

    @Autowired
    private OrderService orderService;

    @Value("classpath:mail/mailTemplate.html")
    private Resource mailTemplateResource;

    private String patientOrderSubject;
    private String medicOrderSubject;
    private String clinicOrderSubject;
    private String patientResultSubject;
    private String medicResultSubject;
    private String clinicResultSubject;

    private BufferedReader mailTemplate;
    private boolean useHTML;
    private Collection<String> mailInline;

    private String textTemplate;

    @PostConstruct
    private void init(){
        this.patientOrderSubject    = "[Order <replace-order-id/>] A new order awaits you!";
        this.medicOrderSubject      = "[Order <replace-order-id/>] Your order has been uploaded successfully.";
        this.clinicOrderSubject     = "[Order <replace-order-id/>] A new order has been uploaded.";
        this.patientResultSubject   = "[Order <replace-order-id/>-<replace-result-id/>] New results are up!";
        this.medicResultSubject     = "[Order <replace-order-id/>-<replace-result-id/>] New results are available.";
        this.clinicResultSubject    = "[Order <replace-order-id/>-<replace-result-id/>] Your results has been uploaded successfully.";

        this.textTemplate    = "<replace-content/>\n\nWith love,\n\tMedTracker (<replace-url/>)";


        try {
            InputStream resourceInputStream = this.mailTemplateResource.getInputStream();
            this.mailTemplate = new BufferedReader(new InputStreamReader(resourceInputStream));
            this.useHTML = true;
            this.mailInline = new ArrayList<>();
            this.mailInline.add("logo.png");
            this.mailInline.add("envelope-regular.png");
        } catch (IOException e) {
            this.useHTML = false;
            e.printStackTrace();
        }

    }

    public void sendOrderMail(Order order) {

        String patientMail  = order.getPatient_email();
        String medicMail   = order.getMedic().getEmail();
        String clinicMail   = order.getClinic().getEmail();
        String patientName  = order.getPatient_name();
        String medicName   = order.getMedic().getName();
        String clinicName   = order.getClinic().getName();

        if(this.useHTML){

            String basicMailContent = getMailTemplate();

            String body =
                    "<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                    "<tr><td><h2> See changes on our site </h2>\n" +
                    "<a href=\"<replace-order-url/>\" style=\"background-color:#009688;border-radius:4px;color:#ffffff;display:inline-block;;font-size:20px;font-weight:normal;line-height:50px;text-align:center;text-decoration:none;width:160px;font-weight:bold\" target=\"_blank\">View Order</a>\n" +
                    "</td></tr></table>\n" +
                    "<table width=\"440\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                    "                <tr>\n" +
                    "                    <td>\n" +
                    "                        <p>\n" +
                    "                            Contact Info\n" +
                    "                        </p>\n" +
                    "                        <p>\n" +
                    "                            <replace-contact1-role/>: <span style=\"font-weight:bold;\"><replace-contact1-name/></span>\n" +
                    "                            <a href=\"mailto:<replace-contact1-email/>\" target =\"_blank\" title=\"Send Mail\" style=\"text-decoration: none;\">\n" +
                    "                                <img height=\"10\" class=\"image_fix\" src=\"cid:envelope-regular.png\" alt=\"(Send Mail)\"/>\n" +
                    "                            </a>\n" +
                    "                        </p>\n" +
                    "                        <p>\n" +
                    "                            <replace-contact2-role/>: <span style=\"font-weight:bold;\"><replace-contact2-name/></span>\n" +
                    "                            <a href=\"mailto:<replace-contact2-email/>\" target =\"_blank\" title=\"Send Mail\" style=\"text-decoration: none;\">\n" +
                    "                                <img height=\"10\" class=\"image_fix\" src=\"cid:envelope-regular.png\" alt=\"(Send Mail)\"/>\n" +
                    "                            </a>\n" +
                    "                        </p>\n" +
                    "                    </td>\n" +
                    "                </tr>\n" +
                    "            </table>";

            basicMailContent = basicMailContent.replace("<replace-content/>",body);

            String mailContent;

            // mail to patient
            mailContent = basicMailContent;
            mailContent = mailContent.replaceAll("<replace-contact1-role/>","Medic");
            mailContent = mailContent.replaceAll("<replace-contact1-name/>",medicName);
            mailContent = mailContent.replaceAll("<replace-contact1-email/>",medicMail);
            mailContent = mailContent.replaceAll("<replace-contact2-role/>","Clinic");
            mailContent = mailContent.replaceAll("<replace-contact2-name/>",clinicName);
            mailContent = mailContent.replaceAll("<replace-contact2-email/>",clinicMail);
            ms.sendMimeMessage(patientMail,
                    replaceOrderInfo(patientOrderSubject,order),
                    replaceOrderInfo(mailContent,order),
                    this.mailInline
            );

            // mail to medic
            mailContent = basicMailContent;
            mailContent = mailContent.replaceAll("<replace-contact1-role/>","Patient");
            mailContent = mailContent.replaceAll("<replace-contact1-name/>",patientName);
            mailContent = mailContent.replaceAll("<replace-contact1-email/>",patientMail);
            mailContent = mailContent.replaceAll("<replace-contact2-role/>","Clinic");
            mailContent = mailContent.replaceAll("<replace-contact2-name/>",clinicName);
            mailContent = mailContent.replaceAll("<replace-contact2-email/>",clinicMail);
            ms.sendMimeMessage(medicMail,
                    replaceOrderInfo(medicOrderSubject,order),
                    replaceOrderInfo(mailContent,order),
                    this.mailInline
            );

            // mail to clinic
            mailContent = basicMailContent;
            mailContent = mailContent.replaceAll("<replace-contact1-role/>","Patient");
            mailContent = mailContent.replaceAll("<replace-contact1-name/>",patientName);
            mailContent = mailContent.replaceAll("<replace-contact1-email/>",patientMail);
            mailContent = mailContent.replaceAll("<replace-contact2-role/>","Medic");
            mailContent = mailContent.replaceAll("<replace-contact2-name/>",medicName);
            mailContent = mailContent.replaceAll("<replace-contact2-email/>",medicMail);
            ms.sendMimeMessage(clinicMail,
                    replaceOrderInfo(clinicOrderSubject,order),
                    replaceOrderInfo(mailContent,order),
                    this.mailInline
            );

        }else{

            String body = "Details for this order are available from:\n\n<replace-order-url/>\n\nContact Info\n<replace-contact1-role/>: <replace-contact1-name/> (<replace-contact1-email/>)\n<replace-contact2-role/>: <replace-contact2-name/> (<replace-contact2-email/>)";

            String basicMailContent = replaceURL(getTextTemplate());
            basicMailContent = basicMailContent.replaceAll("<replace-content/>",body);
            String mailContent;

            // mail to patient
            mailContent = basicMailContent;
            mailContent = mailContent.replaceAll("<replace-contact1-role/>","Medic");
            mailContent = mailContent.replaceAll("<replace-contact1-name/>",medicName);
            mailContent = mailContent.replaceAll("<replace-contact1-email/>",medicMail);
            mailContent = mailContent.replaceAll("<replace-contact2-role/>","Clinic");
            mailContent = mailContent.replaceAll("<replace-contact2-name/>",clinicName);
            mailContent = mailContent.replaceAll("<replace-contact2-email/>",clinicMail);
            ms.sendSimpleMessage(patientMail,
                    replaceOrderInfo(patientOrderSubject,order),
                    replaceOrderInfo(mailContent,order));

            // mail to medic
            mailContent = basicMailContent;
            mailContent = mailContent.replaceAll("<replace-contact1-role/>","Patient");
            mailContent = mailContent.replaceAll("<replace-contact1-name/>",patientName);
            mailContent = mailContent.replaceAll("<replace-contact1-email/>",patientMail);
            mailContent = mailContent.replaceAll("<replace-contact2-role/>","Clinic");
            mailContent = mailContent.replaceAll("<replace-contact2-name/>",clinicName);
            mailContent = mailContent.replaceAll("<replace-contact2-email/>",clinicMail);
            ms.sendSimpleMessage(medicMail,
                    replaceOrderInfo(medicOrderSubject,order),
                    replaceOrderInfo(mailContent,order));

            // mail to clinic
            mailContent = basicMailContent;
            mailContent = mailContent.replaceAll("<replace-contact1-role/>","Patient");
            mailContent = mailContent.replaceAll("<replace-contact1-name/>",patientName);
            mailContent = mailContent.replaceAll("<replace-contact1-email/>",patientMail);
            mailContent = mailContent.replaceAll("<replace-contact2-role/>","Medic");
            mailContent = mailContent.replaceAll("<replace-contact2-name/>",medicName);
            mailContent = mailContent.replaceAll("<replace-contact2-email/>",medicMail);
            ms.sendSimpleMessage(clinicMail,
                    replaceOrderInfo(clinicOrderSubject,order),
                    replaceOrderInfo(mailContent,order));
        }


    }

    public void sendResultMail(Result result){

        Optional<Order> resultOrder = orderService.findById(result.getOrder_id());

        if(resultOrder.isPresent()){

            Order order = resultOrder.get();
            String patientMail  = order.getPatient_email();
            String medicMail   = order.getMedic().getEmail();
            String clinicMail   = order.getClinic().getEmail();
            String patientName  = order.getPatient_name();
            String medicName   = order.getMedic().getName();
            String clinicName   = order.getClinic().getName();

            if(this.useHTML){

                String basicMailContent = getMailTemplate();

                String body =
                        "<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                                "<tr><td><h2> See changes on our site </h2>\n" +
                                "<a href=\"<replace-order-url/>\" style=\"background-color:#009688;border-radius:4px;color:#ffffff;display:inline-block;;font-size:20px;font-weight:normal;line-height:50px;text-align:center;text-decoration:none;width:160px;font-weight:bold\" target=\"_blank\">View Order</a>\n" +
                                "</td></tr></table>\n" +
                                "<table width=\"440\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                                "                <tr>\n" +
                                "                    <td>\n" +
                                "                        <p>\n" +
                                "                            Contact Info\n" +
                                "                        </p>\n" +
                                "                        <p>\n" +
                                "                            <replace-contact1-role/>: <span style=\"font-weight:bold;\"><replace-contact1-name/></span>\n" +
                                "                            <a href=\"mailto:<replace-contact1-email/>\" target =\"_blank\" title=\"Send Mail\" style=\"text-decoration: none;\">\n" +
                                "                                <img height=\"10\" class=\"image_fix\" src=\"cid:envelope-regular.png\" alt=\"(Send Mail)\"/>\n" +
                                "                            </a>\n" +
                                "                        </p>\n" +
                                "                        <p>\n" +
                                "                            <replace-contact2-role/>: <span style=\"font-weight:bold;\"><replace-contact2-name/></span>\n" +
                                "                            <a href=\"mailto:<replace-contact2-email/>\" target =\"_blank\" title=\"Send Mail\" style=\"text-decoration: none;\">\n" +
                                "                                <img height=\"10\" class=\"image_fix\" src=\"cid:envelope-regular.png\" alt=\"(Send Mail)\"/>\n" +
                                "                            </a>\n" +
                                "                        </p>\n" +
                                "                    </td>\n" +
                                "                </tr>\n" +
                                "            </table>";

                basicMailContent = basicMailContent.replace("<replace-content/>",body);

                String mailContent;

                // mail to patient
                mailContent = basicMailContent;
                mailContent = mailContent.replaceAll("<replace-contact1-role/>","Medic");
                mailContent = mailContent.replaceAll("<replace-contact1-name/>",medicName);
                mailContent = mailContent.replaceAll("<replace-contact1-email/>",medicMail);
                mailContent = mailContent.replaceAll("<replace-contact2-role/>","Clinic");
                mailContent = mailContent.replaceAll("<replace-contact2-name/>",clinicName);
                mailContent = mailContent.replaceAll("<replace-contact2-email/>",clinicMail);
                ms.sendMimeMessage(patientMail,
                        replaceResultInfo(patientResultSubject,result,order),
                        replaceResultInfo(mailContent,result,order),
                        this.mailInline
                );

                // mail to medic
                mailContent = basicMailContent;
                mailContent = mailContent.replaceAll("<replace-contact1-role/>","Patient");
                mailContent = mailContent.replaceAll("<replace-contact1-name/>",patientName);
                mailContent = mailContent.replaceAll("<replace-contact1-email/>",patientMail);
                mailContent = mailContent.replaceAll("<replace-contact2-role/>","Clinic");
                mailContent = mailContent.replaceAll("<replace-contact2-name/>",clinicName);
                mailContent = mailContent.replaceAll("<replace-contact2-email/>",clinicMail);
                ms.sendMimeMessage(medicMail,
                        replaceResultInfo(medicResultSubject,result,order),
                        replaceResultInfo(mailContent,result,order),
                        this.mailInline
                );

                // mail to clinic
                mailContent = basicMailContent;
                mailContent = mailContent.replaceAll("<replace-contact1-role/>","Patient");
                mailContent = mailContent.replaceAll("<replace-contact1-name/>",patientName);
                mailContent = mailContent.replaceAll("<replace-contact1-email/>",patientMail);
                mailContent = mailContent.replaceAll("<replace-contact2-role/>","Medic");
                mailContent = mailContent.replaceAll("<replace-contact2-name/>",medicName);
                mailContent = mailContent.replaceAll("<replace-contact2-email/>",medicMail);
                ms.sendMimeMessage(clinicMail,
                        replaceResultInfo(clinicResultSubject,result,order),
                        replaceResultInfo(mailContent,result,order),
                        this.mailInline
                );

            }else{

                String body = "Details for this order are available from:\n\n<replace-order-url/>\n\nContact Info\n<replace-contact1-role/>: <replace-contact1-name/> (<replace-contact1-email/>)\n<replace-contact2-role/>: <replace-contact2-name/> (<replace-contact2-email/>)";

                String basicMailContent = replaceURL(getTextTemplate());
                basicMailContent = basicMailContent.replaceAll("<replace-content/>",body);
                String mailContent;

                // mail to patient
                mailContent = basicMailContent;
                mailContent = mailContent.replaceAll("<replace-contact1-role/>","Medic");
                mailContent = mailContent.replaceAll("<replace-contact1-name/>",medicName);
                mailContent = mailContent.replaceAll("<replace-contact1-email/>",medicMail);
                mailContent = mailContent.replaceAll("<replace-contact2-role/>","Clinic");
                mailContent = mailContent.replaceAll("<replace-contact2-name/>",clinicName);
                mailContent = mailContent.replaceAll("<replace-contact2-email/>",clinicMail);
                ms.sendSimpleMessage(patientMail,
                        replaceResultInfo(patientResultSubject,result,order),
                        replaceResultInfo(mailContent,result,order));

                // mail to medic
                mailContent = basicMailContent;
                mailContent = mailContent.replaceAll("<replace-contact1-role/>","Patient");
                mailContent = mailContent.replaceAll("<replace-contact1-name/>",patientName);
                mailContent = mailContent.replaceAll("<replace-contact1-email/>",patientMail);
                mailContent = mailContent.replaceAll("<replace-contact2-role/>","Clinic");
                mailContent = mailContent.replaceAll("<replace-contact2-name/>",clinicName);
                mailContent = mailContent.replaceAll("<replace-contact2-email/>",clinicMail);
                ms.sendSimpleMessage(medicMail,
                        replaceResultInfo(medicResultSubject,result,order),
                        replaceResultInfo(mailContent,result,order));

                // mail to clinic
                mailContent = basicMailContent;
                mailContent = mailContent.replaceAll("<replace-contact1-role/>","Patient");
                mailContent = mailContent.replaceAll("<replace-contact1-name/>",patientName);
                mailContent = mailContent.replaceAll("<replace-contact1-email/>",patientMail);
                mailContent = mailContent.replaceAll("<replace-contact2-role/>","Medic");
                mailContent = mailContent.replaceAll("<replace-contact2-name/>",medicName);
                mailContent = mailContent.replaceAll("<replace-contact2-email/>",medicMail);
                ms.sendSimpleMessage(clinicMail,
                        replaceResultInfo(clinicResultSubject,result,order),
                        replaceResultInfo(mailContent,result,order));
            }

        }else{
            throw new OrderNotFoundForExistingResultException();
        }
    }

    public void sendMedicApplicationValidatingMail(Medic medic){
        String medicMail   = medic.getEmail();

        String mailSubject = "Processing Medic Application form for <replace-medic-name/>";

        if(this.useHTML){
            String basicMailContent = getMailTemplate();

            String body = "<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                    "<tr><td><h2> We are now processing your Medic Application Form\n" +
                    "<p>We will notify you when the validation process finishes</p>\n"+
                    "</td></tr></table>";

            String mailContent = basicMailContent.replace("<replace-content/>",body);

            ms.sendMimeMessage(medicMail,
                    replaceMedicInfo(mailSubject, medic),
                    replaceMedicInfo(mailContent, medic),
                    this.mailInline
                    );
        }else{
            String basicMailContent = getTextTemplate();

            String body = "We are now processing your Medic Application Form.\nWe will notify you after the validation process ends.";

            String mailContent = basicMailContent.replace("<replace-content/>",body);

            ms.sendSimpleMessage(medicMail,
                    replaceMedicInfo(mailSubject, medic),
                    replaceMedicInfo(mailContent, medic)
            );
        }

    }

    public void sendClinicApplicationValidatingMail(Clinic clinic){
        String clinicMail   = clinic.getEmail();

        String mailSubject = "Processing Medic Application form for <replace-clinic-name/>";

        if(this.useHTML){
            String basicMailContent = getMailTemplate();

            String body = "<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                    "<tr><td><h2> We are now processing your Clinic Application Form\n" +
                    "<p>We will notify you when the validation process finishes</p>\n"+
                    "</td></tr></table>";

            String mailContent = basicMailContent.replace("<replace-content/>",body);

            ms.sendMimeMessage(clinicMail,
                    replaceClinicInfo(mailSubject, clinic),
                    replaceClinicInfo(mailContent, clinic),
                    this.mailInline
            );
        }else{
            String basicMailContent = getTextTemplate();

            String body = "We are now processing your Clinic Application Form.\nWe will notify you after the validation process ends.";

            String mailContent = basicMailContent.replace("<replace-content/>",body);

            ms.sendSimpleMessage(clinicMail,
                    replaceClinicInfo(mailSubject, clinic),
                    replaceClinicInfo(mailContent, clinic)
            );
        }

    }
    // replace functions
    private String getMailTemplate(){
        String mailTemplateString = this.mailTemplate.lines().collect(Collectors.joining("\n"));
        mailTemplateString = replaceURL(mailTemplateString);
        return mailTemplateString;
    }

    private String getTextTemplate(){
        return this.textTemplate;
    }

    private String replaceURL(String mail){
        return mail.replaceAll("<replace-url/>",address.toString());
    }

    private String replaceOrderInfo(String mail, Order order){
        String ret = mail;

        ret = ret.replaceAll("<replace-order-url/>",address.toString()+"/view/study/"+urlEncoderService.encode(order.getOrder_id()));
        ret = ret.replaceAll("<replace-order-id/>",String.valueOf(order.getOrder_id()));

        ret = ret.replaceAll("<replace-patient-name/>",order.getPatient_name());
        ret = ret.replaceAll("<replace-patient-email/>",order.getPatient_email());

        ret = replaceMedicInfo(ret, order.getMedic());
        ret = replaceClinicInfo(ret, order.getClinic());

        return ret;
    }

    private String replaceResultInfo(String mail, Result result, Order order){
        String ret = mail;

        ret = ret.replaceAll("<replace-result-id/>",String.valueOf(result.getId()));
        ret = replaceOrderInfo(ret,order);

        return ret;
    }


    private String replaceMedicInfo(String mail, Medic medic){
        String ret = mail;

        ret = ret.replaceAll("<replace-medic-name/>",medic.getName());
        ret = ret.replaceAll("<replace-medic-email/>",medic.getEmail());

        return ret;
    }

    private String replaceClinicInfo(String mail, Clinic clinic){
        String ret = mail;

        ret = ret.replaceAll("<replace-clinic-name/>",clinic.getName());
        ret = ret.replaceAll("<replace-clinic-email/>",clinic.getEmail());

        return ret;
    }
}
