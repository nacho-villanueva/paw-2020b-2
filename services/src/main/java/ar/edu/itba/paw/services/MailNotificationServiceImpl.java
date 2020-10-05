package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.OrderNotFoundForExistingResultException;

import ar.edu.itba.paw.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
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
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Primary
@Service
public class MailNotificationServiceImpl implements MailNotificationService {

    @Autowired
    private MailService ms;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private URL address;

    @Autowired
    private UrlEncoderService urlEncoderService;

    @Autowired
    private OrderService orderService;

    @Value("classpath:mail/mailTemplate.html")
    private Resource mailTemplateResource;

    private String mailTemplate;
    private boolean useHTML;

    private String textTemplate;

    @PostConstruct
    private void init(){

        this.textTemplate    = "<replace-content/>\n\n<replace-m-complementaryClose/>\n\t<replace-m-appname/> (<replace-url/>)";

        try {
            InputStream resourceInputStream = this.mailTemplateResource.getInputStream();
            this.mailTemplate = new BufferedReader(new InputStreamReader(resourceInputStream))
                    .lines().collect(Collectors.joining("\n"));
            this.useHTML = true;
        } catch (IOException e) {
            this.useHTML = false;
            //TODO log accordingly
            e.printStackTrace();
        }

    }

    public void sendOrderMail(Order order) {

        if(this.useHTML){
            sendOrderMailHtml(order);
        }else{
            sendOrderMailNoHtml(order);
        }
    }

    public void sendResultMail(Result result){

        if(this.useHTML){
            sendResultMailHtml(result);
        }else{
            sendResultMailNoHtml(result);
        }
    }

    public void sendMedicApplicationValidatingMail(Medic medic){
        if(this.useHTML){
            sendMedicApplicationValidatingMailHtml(medic);
        }else{
            sendMedicApplicationValidatingMailNoHtml(medic);
        }
    }

    public void sendClinicApplicationValidatingMail(Clinic clinic){
        if(this.useHTML){
            sendClinicApplicationValidatingMailHtml(clinic);
        }else{
            sendClinicApplicationValidatingMailNoHtml(clinic);
        }
    }

    // send order mail with html
    private void sendOrderMailHtml(Order order){

        // TODO get locale from user
        Locale patientLocale = Locale.forLanguageTag("es");
        Locale medicLocale = Locale.forLanguageTag("en");
        Locale clinicLocale = Locale.forLanguageTag("es-AR");

        String patientMail  = order.getPatient_email();
        String medicMail   = order.getMedic().getEmail();
        String clinicMail   = order.getClinic().getEmail();
        String patientName  = order.getPatient_name();
        String medicName   = order.getMedic().getName();
        String clinicName   = order.getClinic().getName();

        Object[] subjectParams = {order.getOrder_id()};

        ArrayList<String> mailInline = new ArrayList<>();
        mailInline.add("logo.png");
        mailInline.add("envelope-regular.png");

        String basicMailContent = getMailTemplate();

        String body =
                "<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                        "<tr><td><h2><replace-m-body-sendOrderMailHtml-details/></h2>\n" +
                        "<a href=\"<replace-order-url/>\" style=\"background-color:#009688;border-radius:4px;color:#ffffff;display:inline-block;;font-size:20px;font-weight:normal;line-height:50px;text-align:center;text-decoration:none;width:160px;font-weight:bold\" target=\"_blank\"><replace-m-body-sendOrderMailHtml-orderUrl/></a>\n" +
                        "</td></tr></table>\n" +
                        "<table width=\"440\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                        "   <tr>\n" +
                        "       <td>\n" +
                        "           <p>\n" +
                        "               <replace-m-contactInfo/>\n" +
                        "           </p>\n" +
                        "           <p>\n" +
                        "               <replace-contact1-role/>: <span style=\"font-weight:bold;\"><replace-contact1-name/></span>\n" +
                        "               <a href=\"mailto:<replace-contact1-email/>\" target =\"_blank\" title=\"Send Mail\" style=\"text-decoration: none;\">\n" +
                        "                   <img height=\"10\" class=\"image_fix\" src=\"cid:envelope-regular.png\" alt=\"<replace-m-altText-envelope/>\"/>\n" +
                        "               </a>\n" +
                        "           </p>\n" +
                        "           <p>\n" +
                        "               <replace-contact2-role/>: <span style=\"font-weight:bold;\"><replace-contact2-name/></span>\n" +
                        "               <a href=\"mailto:<replace-contact2-email/>\" target =\"_blank\" title=\"Send Mail\" style=\"text-decoration: none;\">\n" +
                        "                   <img height=\"10\" class=\"image_fix\" src=\"cid:envelope-regular.png\" alt=\"<replace-m-altText-envelope/>\"/>\n" +
                        "               </a>\n" +
                        "           </p>\n" +
                        "       </td>\n" +
                        "   </tr>\n" +
                        "</table>";

        basicMailContent = basicMailContent.replace("<replace-content/>",body);

        String mailContent;

        // mail to patient
        mailContent = basicMailContent;
        mailContent = replaceMessages(mailContent,patientLocale);
        mailContent = mailContent.replaceAll("<replace-contact1-role/>",messageSource.getMessage("mail.role.medic",null,patientLocale));
        mailContent = mailContent.replaceAll("<replace-contact1-name/>",medicName);
        mailContent = mailContent.replaceAll("<replace-contact1-email/>",medicMail);
        mailContent = mailContent.replaceAll("<replace-contact2-role/>",messageSource.getMessage("mail.role.clinic",null,patientLocale));
        mailContent = mailContent.replaceAll("<replace-contact2-name/>",clinicName);
        mailContent = mailContent.replaceAll("<replace-contact2-email/>",clinicMail);
        ms.sendMimeMessage(patientMail,
                messageSource.getMessage("mail.subject.order.patient",subjectParams,patientLocale),
                replaceOrderInfo(mailContent,order),
                mailInline
        );

        // mail to medic
        mailContent = basicMailContent;
        mailContent = replaceMessages(mailContent,medicLocale);
        mailContent = mailContent.replaceAll("<replace-contact1-role/>",messageSource.getMessage("mail.role.patient",null,medicLocale));
        mailContent = mailContent.replaceAll("<replace-contact1-name/>",patientName);
        mailContent = mailContent.replaceAll("<replace-contact1-email/>",patientMail);
        mailContent = mailContent.replaceAll("<replace-contact2-role/>",messageSource.getMessage("mail.role.clinic",null,medicLocale));
        mailContent = mailContent.replaceAll("<replace-contact2-name/>",clinicName);
        mailContent = mailContent.replaceAll("<replace-contact2-email/>",clinicMail);
        ms.sendMimeMessage(medicMail,
                messageSource.getMessage("mail.subject.order.medic",subjectParams,medicLocale),
                replaceOrderInfo(mailContent,order),
                mailInline
        );

        // mail to clinic
        mailContent = basicMailContent;
        mailContent = replaceMessages(mailContent,clinicLocale);
        mailContent = mailContent.replaceAll("<replace-contact1-role/>",messageSource.getMessage("mail.role.patient",null,clinicLocale));
        mailContent = mailContent.replaceAll("<replace-contact1-name/>",patientName);
        mailContent = mailContent.replaceAll("<replace-contact1-email/>",patientMail);
        mailContent = mailContent.replaceAll("<replace-contact2-role/>",messageSource.getMessage("mail.role.medic",null,clinicLocale));
        mailContent = mailContent.replaceAll("<replace-contact2-name/>",medicName);
        mailContent = mailContent.replaceAll("<replace-contact2-email/>",medicMail);
        ms.sendMimeMessage(clinicMail,
                messageSource.getMessage("mail.subject.order.clinic",subjectParams,clinicLocale),
                replaceOrderInfo(mailContent,order),
                mailInline
        );

        return;
    }


    // send order mail without html
    private void sendOrderMailNoHtml(Order order){

        // TODO get locale from user
        Locale patientLocale = Locale.forLanguageTag("es-ar");
        Locale medicLocale = Locale.forLanguageTag("es-ar");
        Locale clinicLocale = Locale.forLanguageTag("es-ar");

        String patientMail  = order.getPatient_email();
        String medicMail   = order.getMedic().getEmail();
        String clinicMail   = order.getClinic().getEmail();
        String patientName  = order.getPatient_name();
        String medicName   = order.getMedic().getName();
        String clinicName   = order.getClinic().getName();

        Object[] subjectParams = {order.getOrder_id()};

        String body = "<replace-m-details/>\n\n<replace-order-url/>\n\n<replace-m-contactInfo/>\n<replace-contact1-role/>: <replace-contact1-name/> (<replace-contact1-email/>)\n<replace-contact2-role/>: <replace-contact2-name/> (<replace-contact2-email/>)";

        String basicMailContent = replaceURL(getTextTemplate());
        basicMailContent = basicMailContent.replaceAll("<replace-content/>",body);
        String mailContent;

        // mail to patient
        mailContent = basicMailContent;
        mailContent = replaceMessages(mailContent,patientLocale);
        mailContent = mailContent.replaceAll("<replace-contact1-role/>",messageSource.getMessage("mail.role.medic",null,patientLocale));
        mailContent = mailContent.replaceAll("<replace-contact1-name/>",medicName);
        mailContent = mailContent.replaceAll("<replace-contact1-email/>",medicMail);
        mailContent = mailContent.replaceAll("<replace-contact2-role/>",messageSource.getMessage("mail.role.clinic",null,patientLocale));
        mailContent = mailContent.replaceAll("<replace-contact2-name/>",clinicName);
        mailContent = mailContent.replaceAll("<replace-contact2-email/>",clinicMail);
        ms.sendSimpleMessage(patientMail,
                messageSource.getMessage("mail.subject.order.patient",subjectParams,patientLocale),
                replaceOrderInfo(mailContent,order));

        // mail to medic
        mailContent = basicMailContent;
        mailContent = replaceMessages(mailContent,medicLocale);
        mailContent = mailContent.replaceAll("<replace-contact1-role/>",messageSource.getMessage("mail.role.patient",null,medicLocale));
        mailContent = mailContent.replaceAll("<replace-contact1-name/>",patientName);
        mailContent = mailContent.replaceAll("<replace-contact1-email/>",patientMail);
        mailContent = mailContent.replaceAll("<replace-contact2-role/>",messageSource.getMessage("mail.role.clinic",null,medicLocale));
        mailContent = mailContent.replaceAll("<replace-contact2-name/>",clinicName);
        mailContent = mailContent.replaceAll("<replace-contact2-email/>",clinicMail);
        ms.sendSimpleMessage(medicMail,
                messageSource.getMessage("mail.subject.order.medic",subjectParams,medicLocale),
                replaceOrderInfo(mailContent,order));

        // mail to clinic
        mailContent = basicMailContent;
        mailContent = replaceMessages(mailContent,clinicLocale);
        mailContent = mailContent.replaceAll("<replace-contact1-role/>",messageSource.getMessage("mail.role.patient",null,clinicLocale));
        mailContent = mailContent.replaceAll("<replace-contact1-name/>",patientName);
        mailContent = mailContent.replaceAll("<replace-contact1-email/>",patientMail);
        mailContent = mailContent.replaceAll("<replace-contact2-role/>",messageSource.getMessage("mail.role.medic",null,clinicLocale));
        mailContent = mailContent.replaceAll("<replace-contact2-name/>",medicName);
        mailContent = mailContent.replaceAll("<replace-contact2-email/>",medicMail);
        ms.sendSimpleMessage(clinicMail,
                messageSource.getMessage("mail.subject.order.clinic",subjectParams,clinicLocale),
                replaceOrderInfo(mailContent,order));

        return;
    }

    // send result mail with html
    private void sendResultMailHtml(Result result){
        Optional<Order> resultOrder = orderService.findById(result.getOrder_id());

        if(resultOrder.isPresent()){

            // TODO get locale from user
            Locale patientLocale = Locale.forLanguageTag("es");
            Locale medicLocale = Locale.forLanguageTag("en");
            Locale clinicLocale = Locale.forLanguageTag("es-AR");

            Order order = resultOrder.get();
            String patientMail  = order.getPatient_email();
            String medicMail   = order.getMedic().getEmail();
            String clinicMail   = order.getClinic().getEmail();
            String patientName  = order.getPatient_name();
            String medicName   = order.getMedic().getName();
            String clinicName   = order.getClinic().getName();

            Object[] subjectParams = {result.getOrder_id(),result.getId()};

            String basicMailContent = getMailTemplate();

            ArrayList<String> mailInline = new ArrayList<>();
            mailInline.add("logo.png");
            mailInline.add("envelope-regular.png");

            String body =
                    "<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                            "<tr><td><h2><replace-m-body-sendOrderMailHtml-details/></h2>\n" +
                            "<a href=\"<replace-order-url/>\" style=\"background-color:#009688;border-radius:4px;color:#ffffff;display:inline-block;;font-size:20px;font-weight:normal;line-height:50px;text-align:center;text-decoration:none;width:160px;font-weight:bold\" target=\"_blank\"><replace-m-body-sendOrderMailHtml-orderUrl/></a>\n" +
                            "</td></tr></table>\n" +
                            "<table width=\"440\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                            "   <tr>\n" +
                            "       <td>\n" +
                            "           <p>\n" +
                            "               <replace-m-contactInfo/>\n" +
                            "           </p>\n" +
                            "           <p>\n" +
                            "               <replace-contact1-role/>: <span style=\"font-weight:bold;\"><replace-contact1-name/></span>\n" +
                            "               <a href=\"mailto:<replace-contact1-email/>\" target =\"_blank\" title=\"Send Mail\" style=\"text-decoration: none;\">\n" +
                            "                   <img height=\"10\" class=\"image_fix\" src=\"cid:envelope-regular.png\" alt=\"<replace-m-altText-envelope/>\"/>\n" +
                            "               </a>\n" +
                            "           </p>\n" +
                            "           <p>\n" +
                            "               <replace-contact2-role/>: <span style=\"font-weight:bold;\"><replace-contact2-name/></span>\n" +
                            "               <a href=\"mailto:<replace-contact2-email/>\" target =\"_blank\" title=\"Send Mail\" style=\"text-decoration: none;\">\n" +
                            "                   <img height=\"10\" class=\"image_fix\" src=\"cid:envelope-regular.png\" alt=\"<replace-m-altText-envelope/>\"/>\n" +
                            "               </a>\n" +
                            "           </p>\n" +
                            "       </td>\n" +
                            "   </tr>\n" +
                            "</table>";

            basicMailContent = basicMailContent.replace("<replace-content/>",body);

            String mailContent;

            // mail to patient
            mailContent = basicMailContent;
            mailContent = replaceMessages(mailContent,patientLocale);
            mailContent = mailContent.replaceAll("<replace-contact1-role/>",messageSource.getMessage("mail.role.medic",null,patientLocale));
            mailContent = mailContent.replaceAll("<replace-contact1-name/>",medicName);
            mailContent = mailContent.replaceAll("<replace-contact1-email/>",medicMail);
            mailContent = mailContent.replaceAll("<replace-contact2-role/>",messageSource.getMessage("mail.role.clinic",null,patientLocale));
            mailContent = mailContent.replaceAll("<replace-contact2-name/>",clinicName);
            mailContent = mailContent.replaceAll("<replace-contact2-email/>",clinicMail);
            ms.sendMimeMessage(patientMail,
                    messageSource.getMessage("mail.subject.result.patient",subjectParams,patientLocale),
                    replaceResultInfo(mailContent,result,order),
                    mailInline
            );

            // mail to medic
            mailContent = basicMailContent;
            mailContent = replaceMessages(mailContent,medicLocale);
            mailContent = mailContent.replaceAll("<replace-contact1-role/>",messageSource.getMessage("mail.role.patient",null,medicLocale));
            mailContent = mailContent.replaceAll("<replace-contact1-name/>",patientName);
            mailContent = mailContent.replaceAll("<replace-contact1-email/>",patientMail);
            mailContent = mailContent.replaceAll("<replace-contact2-role/>",messageSource.getMessage("mail.role.clinic",null,medicLocale));
            mailContent = mailContent.replaceAll("<replace-contact2-name/>",clinicName);
            mailContent = mailContent.replaceAll("<replace-contact2-email/>",clinicMail);
            ms.sendMimeMessage(medicMail,
                    messageSource.getMessage("mail.subject.result.medic",subjectParams,medicLocale),
                    replaceResultInfo(mailContent,result,order),
                    mailInline
            );

            // mail to clinic
            mailContent = basicMailContent;
            mailContent = replaceMessages(mailContent,clinicLocale);
            mailContent = mailContent.replaceAll("<replace-contact1-role/>",messageSource.getMessage("mail.role.patient",null,clinicLocale));
            mailContent = mailContent.replaceAll("<replace-contact1-name/>",patientName);
            mailContent = mailContent.replaceAll("<replace-contact1-email/>",patientMail);
            mailContent = mailContent.replaceAll("<replace-contact2-role/>",messageSource.getMessage("mail.role.medic",null,clinicLocale));
            mailContent = mailContent.replaceAll("<replace-contact2-name/>",medicName);
            mailContent = mailContent.replaceAll("<replace-contact2-email/>",medicMail);
            ms.sendMimeMessage(clinicMail,
                    messageSource.getMessage("mail.subject.result.clinic",subjectParams,clinicLocale),
                    replaceResultInfo(mailContent,result,order),
                    mailInline
            );

        }else{
            throw new OrderNotFoundForExistingResultException();
        }

        return;
    }

    // send result mail without html
    private void sendResultMailNoHtml(Result result){

        Optional<Order> resultOrder = orderService.findById(result.getOrder_id());

        if(resultOrder.isPresent()){

            // TODO get locale from user
            Locale patientLocale = Locale.forLanguageTag("es");
            Locale medicLocale = Locale.forLanguageTag("en");
            Locale clinicLocale = Locale.forLanguageTag("es-AR");

            Order order = resultOrder.get();
            String patientMail  = order.getPatient_email();
            String medicMail   = order.getMedic().getEmail();
            String clinicMail   = order.getClinic().getEmail();
            String patientName  = order.getPatient_name();
            String medicName   = order.getMedic().getName();
            String clinicName   = order.getClinic().getName();

            Object[] subjectParams = {result.getOrder_id(),result.getId()};

            String body = "Details for this order are available from:\n\n<replace-order-url/>\n\nContact Info\n<replace-contact1-role/>: <replace-contact1-name/> (<replace-contact1-email/>)\n<replace-contact2-role/>: <replace-contact2-name/> (<replace-contact2-email/>)";

            String basicMailContent = replaceURL(getTextTemplate());
            basicMailContent = basicMailContent.replaceAll("<replace-content/>",body);
            String mailContent;

            // mail to patient
            mailContent = basicMailContent;
            mailContent = replaceMessages(mailContent,patientLocale);
            mailContent = mailContent.replaceAll("<replace-contact1-role/>",messageSource.getMessage("mail.role.medic",null,patientLocale));
            mailContent = mailContent.replaceAll("<replace-contact1-name/>",medicName);
            mailContent = mailContent.replaceAll("<replace-contact1-email/>",medicMail);
            mailContent = mailContent.replaceAll("<replace-contact2-role/>",messageSource.getMessage("mail.role.clinic",null,patientLocale));
            mailContent = mailContent.replaceAll("<replace-contact2-name/>",clinicName);
            mailContent = mailContent.replaceAll("<replace-contact2-email/>",clinicMail);
            ms.sendSimpleMessage(patientMail,
                    messageSource.getMessage("mail.subject.result.patient",subjectParams,patientLocale),
                    replaceResultInfo(mailContent,result,order));

            // mail to medic
            mailContent = basicMailContent;
            mailContent = replaceMessages(mailContent,medicLocale);
            mailContent = mailContent.replaceAll("<replace-contact1-role/>",messageSource.getMessage("mail.role.patient",null,medicLocale));
            mailContent = mailContent.replaceAll("<replace-contact1-name/>",patientName);
            mailContent = mailContent.replaceAll("<replace-contact1-email/>",patientMail);
            mailContent = mailContent.replaceAll("<replace-contact2-role/>",messageSource.getMessage("mail.role.clinic",null,medicLocale));
            mailContent = mailContent.replaceAll("<replace-contact2-name/>",clinicName);
            mailContent = mailContent.replaceAll("<replace-contact2-email/>",clinicMail);
            ms.sendSimpleMessage(medicMail,
                    messageSource.getMessage("mail.subject.result.medic",subjectParams,medicLocale),
                    replaceResultInfo(mailContent,result,order));

            // mail to clinic
            mailContent = basicMailContent;
            mailContent = replaceMessages(mailContent,clinicLocale);
            mailContent = mailContent.replaceAll("<replace-contact1-role/>",messageSource.getMessage("mail.role.patient",null,clinicLocale));
            mailContent = mailContent.replaceAll("<replace-contact1-name/>",patientName);
            mailContent = mailContent.replaceAll("<replace-contact1-email/>",patientMail);
            mailContent = mailContent.replaceAll("<replace-contact2-role/>",messageSource.getMessage("mail.role.medic",null,clinicLocale));
            mailContent = mailContent.replaceAll("<replace-contact2-name/>",medicName);
            mailContent = mailContent.replaceAll("<replace-contact2-email/>",medicMail);
            ms.sendSimpleMessage(clinicMail,
                    messageSource.getMessage("mail.subject.result.clinic",subjectParams,clinicLocale),
                    replaceResultInfo(mailContent,result,order));

        }else{
            throw new OrderNotFoundForExistingResultException();
        }

        return;
    }

    // send medic application validating mail with html
    private void sendMedicApplicationValidatingMailHtml(Medic medic){

        //TODO Replace with locale
        Locale locale = Locale.ENGLISH;

        String medicMail   = medic.getEmail();

        Object[] subjectParams = {medic.getName()};
        String mailSubject = messageSource.getMessage("mail.subject.application.medic",subjectParams,locale);

        String basicMailContent = getMailTemplate();

        ArrayList<String> mailInline = new ArrayList<>();
        mailInline.add("logo.png");

        String body =
                "<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n<tr><td><h2>"+
                        messageSource.getMessage("mail.body.MedicApplicationValidatingMailHtml.title",null,locale)+
                        "</h2>\n<p>"+
                        messageSource.getMessage("mail.body.MedicApplicationValidatingMailHtml.body",null,locale)+
                        "</p>\n</td></tr></table>";

        String mailContent = basicMailContent.replace("<replace-content/>",body);
        mailContent = replaceMessages(mailContent, locale);

        ms.sendMimeMessage(medicMail,
                mailSubject,
                replaceMedicInfo(mailContent, medic),
                mailInline
        );

    }

    // send medic application validating mail without html
    private void sendMedicApplicationValidatingMailNoHtml(Medic medic){

        //TODO Replace with locale
        Locale locale = Locale.ENGLISH;

        String medicMail   = medic.getEmail();

        Object[] subjectParams = {medic.getName()};
        String mailSubject = messageSource.getMessage("mail.subject.application.medic",subjectParams,locale);

        String basicMailContent = getTextTemplate();

        String body =
                messageSource.getMessage("mail.body.MedicApplicationValidatingMailHtml.title",null,locale)+
                "\n"+
                messageSource.getMessage("mail.body.MedicApplicationValidatingMailHtml.body",null,locale);

        String mailContent = basicMailContent.replace("<replace-content/>",body);
        mailContent = replaceMessages(mailContent, locale);

        ms.sendSimpleMessage(medicMail,
                mailSubject,
                replaceMedicInfo(mailContent, medic)
        );

    }

    // send clinic application validating mail with html
    private void sendClinicApplicationValidatingMailHtml(Clinic clinic){

        //TODO Replace with locale
        Locale locale = Locale.ENGLISH;

        String clinicMail   = clinic.getEmail();

        Object[] subjectParams = {clinic.getName()};
        String mailSubject = messageSource.getMessage("mail.subject.application.clinic",subjectParams,locale);

        String basicMailContent = getMailTemplate();

        ArrayList<String> mailInline = new ArrayList<>();
        mailInline.add("logo.png");

        String body =
                "<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n<tr><td><h2>"+
                messageSource.getMessage("mail.body.ClinicApplicationValidatingMailHtml.title",null,locale)+
                "</h2>\n<p>"+
                messageSource.getMessage("mail.body.ClinicApplicationValidatingMailHtml.body",null,locale)+
                "</p>\n</td></tr></table>";

        String mailContent = basicMailContent.replace("<replace-content/>",body);
        mailContent = replaceMessages(mailContent, locale);

        ms.sendMimeMessage(clinicMail,
                mailSubject,
                replaceClinicInfo(mailContent, clinic),
                mailInline
        );

    }

    // send clinic application validating mail without html
    private void sendClinicApplicationValidatingMailNoHtml(Clinic clinic){

        //TODO Replace with locale
        Locale locale = Locale.ENGLISH;

        String clinicMail   = clinic.getEmail();

        Object[] subjectParams = {clinic.getName()};
        String mailSubject = messageSource.getMessage("mail.subject.application.clinic",subjectParams,locale);

        String basicMailContent = getTextTemplate();

        String body =
                messageSource.getMessage("mail.body.ClinicApplicationValidatingMailHtml.title",null,locale)+
                "\n"+
                messageSource.getMessage("mail.body.ClinicApplicationValidatingMailHtml.body",null,locale);

        String mailContent = basicMailContent.replace("<replace-content/>",body);
        mailContent = replaceMessages(mailContent, locale);

        ms.sendSimpleMessage(clinicMail,
                mailSubject,
                replaceClinicInfo(mailContent, clinic)
        );

    }

    // replace functions
    private String getMailTemplate(){
        String mailTemplateString = this.mailTemplate;
        mailTemplateString = replaceURL(mailTemplateString);
        return mailTemplateString;
    }

    private String getTextTemplate(){
        return this.textTemplate;
    }

    private String replaceMessages(String m, Locale locale){
        String mailContent = m;

        mailContent = mailContent.replaceAll("<replace-m-appname/>",messageSource.getMessage("appname",null,locale));
        mailContent = mailContent.replaceAll("<replace-m-body-sendOrderMailHtml-details/>",messageSource.getMessage("mail.body.sendOrderMailHtml.details",null,locale));
        mailContent = mailContent.replaceAll("<replace-m-body-sendOrderMailHtml-orderUrl/>",messageSource.getMessage("mail.body.sendOrderMailHtml.orderUrl",null,locale));
        mailContent = mailContent.replaceAll("<replace-m-contactInfo/>",messageSource.getMessage("mail.contactInfo",null,locale));
        mailContent = mailContent.replaceAll("<replace-m-altText-logo/>",messageSource.getMessage("mail.altText.logo",null,locale));
        mailContent = mailContent.replaceAll("<replace-m-altText-envelope/>",messageSource.getMessage("mail.altText.envelope",null,locale));
        mailContent = mailContent.replaceAll("<replace-m-complementaryClose/>",messageSource.getMessage("mail.complementaryClose",null,locale));

        return mailContent;
    }

    private String replaceURL(String mail){
        return mail.replaceAll("<replace-url/>",address.toString());
    }

    private String replaceOrderInfo(String mail, Order order){
        String ret = mail;

        ret = ret.replaceAll("<replace-order-url/>",address.toString()+"/view-study/"+urlEncoderService.encode(order.getOrder_id()));
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
