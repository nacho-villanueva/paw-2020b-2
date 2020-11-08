package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.OrderNotFoundForExistingResultException;

import ar.edu.itba.paw.models.*;
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

    @Autowired
    private UserService userService;

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

    @Override
    public void sendOrderMail(Order order) {

        if(this.useHTML){
            sendOrderMailHtml(order);
        }else{
            sendOrderMailNoHtml(order);
        }
    }

    @Override
    public void sendResultMail(Result result){

        if(this.useHTML){
            sendResultMailHtml(result);
        }else{
            sendResultMailNoHtml(result);
        }
    }

    @Override
    public void sendMedicApplicationValidatingMail(Medic medic){
        if(this.useHTML){
            sendMedicApplicationValidatingMailHtml(medic);
        }else{
            sendMedicApplicationValidatingMailNoHtml(medic);
        }
    }

    @Override
    public void sendClinicApplicationValidatingMail(Clinic clinic){
        if(this.useHTML){
            sendClinicApplicationValidatingMailHtml(clinic);
        }else{
            sendClinicApplicationValidatingMailNoHtml(clinic);
        }
    }

    // send order mail with html
    private void sendOrderMailHtml(Order order){

        String patientMail  = order.getPatient_email();
        String medicMail   = order.getMedic().getEmail();
        String clinicMail   = order.getClinic().getEmail();
        String patientName  = order.getPatient_name();
        String medicName   = order.getMedic().getName();
        String clinicName   = order.getClinic().getName();
        String studyName = order.getStudy().getName();
        String description = order.getDescription();

        Locale patientLocale = getLocale(patientMail);
        Locale medicLocale = getLocale(medicMail);
        Locale clinicLocale = getLocale(clinicMail);

        Object[] subjectParams = {order.getOrder_id()};
        Object[] patientContactParams = {patientName};
        Object[] medicContactParams = {medicName};
        Object[] clinicContactParams = {clinicName};
        Object[] studyParam = {studyName};
        Object[] descriptionParam = {description};

        ArrayList<String> mailInline = new ArrayList<>();
        mailInline.add("logo.png");
        mailInline.add("envelope-regular.png");

        String basicMailContent = getMailTemplate();

        String body =
                "<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                        "<tr><td align=\"center\"><h2><replace-m-body-sendOrderMailHtml-details/></h2>\n" +
                        "<a href=\"<replace-order-url/>\" style=\"background-color:#009688;border-radius:4px;color:#ffffff;display:inline-block;;font-size:20px;font-weight:normal;line-height:50px;text-align:center;text-decoration:none;width:160px;font-weight:bold\" target=\"_blank\"><replace-m-body-sendOrderMailHtml-orderUrl/></a>\n" +
                        "</td></tr></table>\n" +
                        "<replace-new-info/>\n"+
                        "<table width=\"440\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                        "   <tr>\n" +
                        "       <td style=\"padding: 24px 0 0 0;\">\n" +
                        "           <table width=\"100%\">\n" +
                        "               <tr>\n" +
                        "                   <td align=\"center\" width=\"100%\" style=\"font-family: Arial, sans-serif; font-size:18px;\">\n"+
                        "                      <replace-study-type/>\n" +
                        "                   </td>\n"+
                        "               </tr>\n" +
                        "               <tr>\n" +
                        "                   <td align=\"center\" width=\"100%\" style=\"font-family: Arial, sans-serif; font-size:18px;\">\n"+
                        "                      <replace-description/>\n" +
                        "                   </td>\n"+
                        "               </tr>\n" +
                        "           </table>\n"+
                        "       </td>\n" +
                        "   </tr>\n" +
                        "   <tr>\n" +
                        "   <tr>\n" +
                        "       <td style=\"padding: 32px 0 0 0;\">\n"+
                        "           <p>\n" +
                        "               <replace-m-contactInfo/>\n" +
                        "           </p>\n" +
                        "       </td>\n"+
                        "   </tr>\n" +
                        "   <tr>\n" +
                        "       <td style=\"padding: 24px 0 0 0;\">\n" +
                        "                           <a href=\"mailto:<replace-contact1-email/>\" target =\"_blank\" title=\"Send Mail\" style=\"text-decoration: none;\">\n" +
                        "           <table width=\"100%\">\n" +
                        "               <tr>\n" +
                        "                   <td align=\"left\" width=\"35%\">\n"+
                        "                           <replace-contact1-name/>\n" +
                        "                   </td>\n" +
                        "                   <td align=\"left\" width=\"65%\">\n" +
                        "                               <img height=\"10\" class=\"image_fix\" src=\"cid:envelope-regular.png\" alt=\"<replace-m-altText-envelope/>\"/>\n" +
                        "                   </td>\n"+
                        "               </tr>\n" +
                        "           </table>\n"+
                        "                           </a>\n" +
                        "       </td>\n" +
                        "   </tr>\n" +
                        "   <tr>\n" +
                        "       <td style=\"padding: 24px 0 0 0;\">\n" +
                        "                           <a href=\"mailto:<replace-contact2-email/>\" target =\"_blank\" title=\"Send Mail\" style=\"text-decoration: none;\">\n" +
                        "           <table width=\"100%\">\n" +
                        "               <tr>\n" +
                        "                   <td align=\"left\" width=\"35%\">\n"+
                        "                           <replace-contact2-name/>\n" +
                        "                   </td>\n" +
                        "                   <td align=\"left\" width=\"65%\">\n" +
                        "                               <img height=\"10\" class=\"image_fix\" src=\"cid:envelope-regular.png\" alt=\"<replace-m-altText-envelope/>\"/>\n" +
                        "                   </td>\n"+
                        "               </tr>\n" +
                        "           </table>\n"+
                        "                           </a>\n" +
                        "       </td>\n" +
                        "   </tr>\n" +
                        "</table>";

        basicMailContent = basicMailContent.replace("<replace-content/>",body);

        String mailContent;

        // mail to patient
        mailContent = basicMailContent;
        mailContent = replaceMessages(mailContent,patientLocale);
        mailContent = mailContent.replaceAll("<replace-study-type/>",messageSource.getMessage("mail.orderInfo.study", studyParam, patientLocale));
        if(description != null && !description.isEmpty()){
            if(!userService.findByEmail(order.getPatient_email()).isPresent()){
                mailContent = mailContent.replaceAll("<replace-study-type/>",messageSource.getMessage("mail.orderInfo.description", descriptionParam, medicLocale));
            }else{
                mailContent = mailContent.replaceAll("<replace-study-type/>",messageSource.getMessage("mail.orderInfo.description", descriptionParam, patientLocale));
            }
        }else{
            mailContent = mailContent.replaceAll("<replace-study-type/>",messageSource.getMessage("mail.newInfo.others", descriptionParam, patientLocale));
        }
        if(!userService.findByEmail(order.getPatient_email()).isPresent()){
            mailContent = mailContent.replaceAll("<replace-new-info/>",messageSource.getMessage("mail.newInfo.patient",patientContactParams,patientLocale));
        }else{
            mailContent = mailContent.replaceAll("<replace-new-info/>",messageSource.getMessage("mail.newInfo.others",patientContactParams,patientLocale));
        }
        mailContent = mailContent.replaceAll("<replace-contact1-name/>",messageSource.getMessage("mail.contact.medic",medicContactParams,patientLocale));
        mailContent = mailContent.replaceAll("<replace-contact1-email/>",medicMail);
        mailContent = mailContent.replaceAll("<replace-contact2-name/>",messageSource.getMessage("mail.contact.clinic",clinicContactParams,patientLocale));
        mailContent = mailContent.replaceAll("<replace-contact2-email/>",clinicMail);
        ms.sendMimeMessage(patientMail,
                messageSource.getMessage("mail.subject.order.patient",subjectParams,patientLocale),
                replaceOrderInfo(mailContent,order),
                mailInline
        );

        // mail to medic
        mailContent = basicMailContent;
        mailContent = replaceMessages(mailContent,medicLocale);
        mailContent = mailContent.replaceAll("<replace-study-type/>",messageSource.getMessage("mail.orderInfo.study", studyParam, medicLocale));
        if(description != null && !description.isEmpty()){
            mailContent = mailContent.replaceAll("<replace-study-type/>",messageSource.getMessage("mail.orderInfo.description", descriptionParam, medicLocale));
        }else{
            mailContent = mailContent.replaceAll("<replace-study-type/>",messageSource.getMessage("mail.newInfo.others", descriptionParam, medicLocale));
        }
        mailContent = mailContent.replaceAll("<replace-new-info/>",messageSource.getMessage("mail.newInfo.others",medicContactParams,medicLocale));
        mailContent = mailContent.replaceAll("<replace-contact1-name/>",messageSource.getMessage("mail.contact.patient",patientContactParams,medicLocale));
        mailContent = mailContent.replaceAll("<replace-contact1-email/>",patientMail);
        mailContent = mailContent.replaceAll("<replace-contact2-name/>",messageSource.getMessage("mail.contact.clinic",clinicContactParams,medicLocale));
        mailContent = mailContent.replaceAll("<replace-contact2-email/>",clinicMail);
        ms.sendMimeMessage(medicMail,
                messageSource.getMessage("mail.subject.order.medic",subjectParams,medicLocale),
                replaceOrderInfo(mailContent,order),
                mailInline
        );

        // mail to clinic
        mailContent = basicMailContent;
        mailContent = replaceMessages(mailContent,clinicLocale);
        mailContent = mailContent.replaceAll("<replace-study-type/>",messageSource.getMessage("mail.orderInfo.study", studyParam, clinicLocale));
        if(description != null && !description.isEmpty()){
            mailContent = mailContent.replaceAll("<replace-study-type/>",messageSource.getMessage("mail.orderInfo.description", descriptionParam, clinicLocale));
        }else{
            mailContent = mailContent.replaceAll("<replace-study-type/>",messageSource.getMessage("mail.newInfo.others", descriptionParam, clinicLocale));
        }
        mailContent = mailContent.replaceAll("<replace-new-info/>",messageSource.getMessage("mail.newInfo.others",clinicContactParams,clinicLocale));
        mailContent = mailContent.replaceAll("<replace-contact1-name/>",messageSource.getMessage("mail.contact.patient",patientContactParams,clinicLocale));
        mailContent = mailContent.replaceAll("<replace-contact1-email/>",patientMail);
        mailContent = mailContent.replaceAll("<replace-contact2-name/>",messageSource.getMessage("mail.contact.medic",medicContactParams,clinicLocale));
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

        String patientMail  = order.getPatient_email();
        String medicMail   = order.getMedic().getEmail();
        String clinicMail   = order.getClinic().getEmail();
        String patientName  = order.getPatient_name();
        String medicName   = order.getMedic().getName();
        String clinicName   = order.getClinic().getName();

        Locale patientLocale = getLocale(patientMail);
        Locale medicLocale = getLocale(medicMail);
        Locale clinicLocale = getLocale(clinicMail);

        Object[] subjectParams = {order.getOrder_id()};
        Object[] patientContactParams = {patientName};
        Object[] medicContactParams = {medicName};
        Object[] clinicContactParams = {clinicName};
        Object[] patientMailParams = {patientName};
        Object[] medicMailParams = {medicMail};
        Object[] clinicMailParams = {clinicMail};

        String body = "<replace-m-body-sendOrderMailNoHtml-details/>\n\n<replace-order-url/>\n\n<replace-m-contactInfo/>\n<replace-contact1-name/><replace-contact1-mail/>\n<replace-contact2-name/><replace-contact2-mail>";

        String basicMailContent = replaceURL(getTextTemplate());
        basicMailContent = basicMailContent.replaceAll("<replace-content/>",body);
        String mailContent;

        // mail to patient
        mailContent = basicMailContent;
        mailContent = replaceMessages(mailContent,patientLocale);
        mailContent = mailContent.replaceAll("<replace-contact1-name/>",messageSource.getMessage("mail.contact.medic",medicContactParams,patientLocale));
        mailContent = mailContent.replaceAll("<replace-contact1-email/>",messageSource.getMessage("mail.contact.email",medicMailParams,patientLocale));
        mailContent = mailContent.replaceAll("<replace-contact2-name/>",messageSource.getMessage("mail.contact.clinic",clinicContactParams,patientLocale));
        mailContent = mailContent.replaceAll("<replace-contact2-email/>",messageSource.getMessage("mail.contact.email",clinicMailParams,patientLocale));
        ms.sendSimpleMessage(patientMail,
                messageSource.getMessage("mail.subject.order.patient",subjectParams,patientLocale),
                replaceOrderInfo(mailContent,order));

        // mail to medic
        mailContent = basicMailContent;
        mailContent = replaceMessages(mailContent,medicLocale);
        mailContent = mailContent.replaceAll("<replace-contact1-name/>",messageSource.getMessage("mail.contact.patient",patientContactParams,medicLocale));
        mailContent = mailContent.replaceAll("<replace-contact1-email/>",messageSource.getMessage("mail.contact.email",patientMailParams,medicLocale));
        mailContent = mailContent.replaceAll("<replace-contact2-name/>",messageSource.getMessage("mail.contact.clinic",clinicContactParams,medicLocale));
        mailContent = mailContent.replaceAll("<replace-contact2-email/>",messageSource.getMessage("mail.contact.email",clinicMailParams,medicLocale));
        ms.sendSimpleMessage(medicMail,
                messageSource.getMessage("mail.subject.order.medic",subjectParams,medicLocale),
                replaceOrderInfo(mailContent,order));

        // mail to clinic
        mailContent = basicMailContent;
        mailContent = replaceMessages(mailContent,clinicLocale);
        mailContent = mailContent.replaceAll("<replace-contact1-name/>",messageSource.getMessage("mail.contact.patient",patientContactParams,clinicLocale));
        mailContent = mailContent.replaceAll("<replace-contact1-email/>",messageSource.getMessage("mail.contact.email",patientMailParams,clinicLocale));
        mailContent = mailContent.replaceAll("<replace-contact2-name/>",messageSource.getMessage("mail.contact.medic",medicContactParams,clinicLocale));
        mailContent = mailContent.replaceAll("<replace-contact2-email/>",messageSource.getMessage("mail.contact.email",medicMailParams,clinicLocale));
        ms.sendSimpleMessage(clinicMail,
                messageSource.getMessage("mail.subject.order.clinic",subjectParams,clinicLocale),
                replaceOrderInfo(mailContent,order));

        return;
    }

    // send result mail with html
    private void sendResultMailHtml(Result result){
        Optional<Order> resultOrder = orderService.findById(result.getOrder_id());

        if(resultOrder.isPresent()){

            Order order = resultOrder.get();
            String patientMail  = order.getPatient_email();
            String medicMail   = order.getMedic().getEmail();
            String clinicMail   = order.getClinic().getEmail();
            String patientName  = order.getPatient_name();
            String medicName   = order.getMedic().getName();
            String clinicName   = order.getClinic().getName();
            String studyName = order.getStudy().getName();
            String description = order.getDescription();

            Locale patientLocale = getLocale(patientMail);
            Locale medicLocale = getLocale(medicMail);
            Locale clinicLocale = getLocale(clinicMail);

            Object[] subjectParams = {result.getOrder_id(),result.getId()};
            Object[] patientContactParams = {patientName};
            Object[] medicContactParams = {medicName};
            Object[] clinicContactParams = {clinicName};
            Object[] studyParam = {studyName};
            Object[] descriptionParam = {description};

            String basicMailContent = getMailTemplate();

            ArrayList<String> mailInline = new ArrayList<>();
            mailInline.add("logo.png");
            mailInline.add("envelope-regular.png");

            String body =
                    "<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                            "<tr><td align=\"center\"><h2><replace-m-body-sendOrderMailHtml-details/></h2>\n" +
                            "<a href=\"<replace-order-url/>\" style=\"background-color:#009688;border-radius:4px;color:#ffffff;display:inline-block;;font-size:20px;font-weight:normal;line-height:50px;text-align:center;text-decoration:none;width:160px;font-weight:bold\" target=\"_blank\"><replace-m-body-sendResultMailHtml-orderUrl/></a>\n" +
                            "</td></tr></table>\n" +
                            "<replace-new-info/>\n"+
                            "<table width=\"440\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                            "   <tr>\n" +
                            "       <td style=\"padding: 24px 0 0 0;\">\n" +
                            "           <table width=\"100%\">\n" +
                            "               <tr>\n" +
                            "                   <td align=\"center\" width=\"100%\" style=\"font-family: Arial, sans-serif; font-size:18px;\">\n"+
                            "                      <replace-study-type/>\n" +
                            "                   </td>\n"+
                            "               </tr>\n" +
                            "               <tr>\n" +
                            "                   <td align=\"center\" width=\"100%\" style=\"font-family: Arial, sans-serif; font-size:18px;\">\n"+
                            "                      <replace-description/>\n" +
                            "                   </td>\n"+
                            "               </tr>\n" +
                            "           </table>\n"+
                            "       </td>\n" +
                            "   </tr>\n" +
                            "   <tr>\n" +
                            "   <tr>\n" +
                            "       <td style=\"padding: 32px 0 0 0;\">\n"+
                            "           <p>\n" +
                            "               <replace-m-contactInfo/>\n" +
                            "           </p>\n" +
                            "       </td>\n"+
                            "   </tr>\n" +
                            "   <tr>\n" +
                            "   <tr>\n" +
                            "       <td style=\"padding: 24px 0 0 0;\">\n" +
                            "                           <a href=\"mailto:<replace-contact1-email/>\" target =\"_blank\" title=\"Send Mail\" style=\"text-decoration: none;\">\n" +
                            "           <table width=\"100%\">\n" +
                            "               <tr>\n" +
                            "                   <td align=\"left\" width=\"35%\">\n"+
                            "                           <replace-contact1-name/>\n" +
                            "                   </td>\n" +
                            "                   <td align=\"left\" width=\"65%\">\n" +
                            "                               <img height=\"10\" class=\"image_fix\" src=\"cid:envelope-regular.png\" alt=\"<replace-m-altText-envelope/>\"/>\n" +
                            "                   </td>\n"+
                            "               </tr>\n" +
                            "           </table>\n"+
                            "                           </a>\n" +
                            "       </td>\n" +
                            "   </tr>\n" +
                            "   <tr>\n" +
                            "       <td style=\"padding: 24px 0 0 0;\">\n" +
                            "                           <a href=\"mailto:<replace-contact2-email/>\" target =\"_blank\" title=\"Send Mail\" style=\"text-decoration: none;\">\n" +
                            "           <table width=\"100%\">\n" +
                            "               <tr>\n" +
                            "                   <td align=\"left\" width=\"35%\">\n"+
                            "                           <replace-contact2-name/>\n" +
                            "                   </td>\n" +
                            "                   <td align=\"left\" width=\"65%\">\n" +
                            "                               <img height=\"10\" class=\"image_fix\" src=\"cid:envelope-regular.png\" alt=\"<replace-m-altText-envelope/>\"/>\n" +
                            "                   </td>\n"+
                            "               </tr>\n" +
                            "           </table>\n"+
                            "                           </a>\n" +
                            "       </td>\n" +
                            "   </tr>\n" +
                            "</table>";

            basicMailContent = basicMailContent.replace("<replace-content/>",body);

            String mailContent;

            // mail to patient
            mailContent = basicMailContent;
            mailContent = replaceMessages(mailContent,patientLocale);
            mailContent = mailContent.replaceAll("<replace-study-type/>",messageSource.getMessage("mail.orderInfo.study", studyParam, patientLocale));
            if(description != null && !description.isEmpty()){
                if(!userService.findByEmail(order.getPatient_email()).isPresent()){
                    mailContent = mailContent.replaceAll("<replace-study-type/>",messageSource.getMessage("mail.orderInfo.description", descriptionParam, medicLocale));
                }else{
                    mailContent = mailContent.replaceAll("<replace-study-type/>",messageSource.getMessage("mail.orderInfo.description", descriptionParam, patientLocale));
                }
            }else{
                mailContent = mailContent.replaceAll("<replace-study-type/>",messageSource.getMessage("mail.newInfo.others", descriptionParam, patientLocale));
            }
            if(!userService.findByEmail(order.getPatient_email()).isPresent()){
                mailContent = mailContent.replaceAll("<replace-new-info/>",messageSource.getMessage("mail.newInfo.patient",patientContactParams,patientLocale));
            }else{
                mailContent = mailContent.replaceAll("<replace-new-info/>",messageSource.getMessage("mail.newInfo.others",patientContactParams,patientLocale));
            }
            mailContent = mailContent.replaceAll("<replace-contact1-name/>",messageSource.getMessage("mail.contact.medic",medicContactParams,patientLocale));
            mailContent = mailContent.replaceAll("<replace-contact1-email/>",medicMail);
            mailContent = mailContent.replaceAll("<replace-contact2-name/>",messageSource.getMessage("mail.contact.clinic",clinicContactParams,patientLocale));
            mailContent = mailContent.replaceAll("<replace-contact2-email/>",clinicMail);
            ms.sendMimeMessage(patientMail,
                    messageSource.getMessage("mail.subject.result.patient",subjectParams,patientLocale),
                    replaceResultInfo(mailContent,result,order),
                    mailInline
            );

            // mail to medic
            mailContent = basicMailContent;
            mailContent = replaceMessages(mailContent,medicLocale);
            mailContent = mailContent.replaceAll("<replace-study-type/>",messageSource.getMessage("mail.orderInfo.study", studyParam, medicLocale));
            if(description != null && !description.isEmpty()){
                mailContent = mailContent.replaceAll("<replace-study-type/>",messageSource.getMessage("mail.orderInfo.description", descriptionParam, medicLocale));
            }else{
                mailContent = mailContent.replaceAll("<replace-study-type/>",messageSource.getMessage("mail.newInfo.others", descriptionParam, medicLocale));
            }
            mailContent = mailContent.replaceAll("<replace-new-info/>",messageSource.getMessage("mail.newInfo.others",medicContactParams,medicLocale));
            mailContent = mailContent.replaceAll("<replace-contact1-name/>",messageSource.getMessage("mail.contact.patient",patientContactParams,medicLocale));
            mailContent = mailContent.replaceAll("<replace-contact1-email/>",patientMail);
            mailContent = mailContent.replaceAll("<replace-contact2-name/>",messageSource.getMessage("mail.contact.clinic",clinicContactParams,medicLocale));
            mailContent = mailContent.replaceAll("<replace-contact2-email/>",clinicMail);
            ms.sendMimeMessage(medicMail,
                    messageSource.getMessage("mail.subject.result.medic",subjectParams,medicLocale),
                    replaceResultInfo(mailContent,result,order),
                    mailInline
            );

            // mail to clinic
            mailContent = basicMailContent;
            mailContent = replaceMessages(mailContent,clinicLocale);
            mailContent = mailContent.replaceAll("<replace-study-type/>",messageSource.getMessage("mail.orderInfo.study", studyParam, clinicLocale));
            if(description != null && !description.isEmpty()){
                mailContent = mailContent.replaceAll("<replace-study-type/>",messageSource.getMessage("mail.orderInfo.description", descriptionParam, clinicLocale));
            }else{
                mailContent = mailContent.replaceAll("<replace-study-type/>",messageSource.getMessage("mail.newInfo.others", descriptionParam, clinicLocale));
            }
            mailContent = mailContent.replaceAll("<replace-new-info/>",messageSource.getMessage("mail.newInfo.others",clinicContactParams,clinicLocale));
            mailContent = mailContent.replaceAll("<replace-contact1-name/>",messageSource.getMessage("mail.contact.patient",patientContactParams,clinicLocale));
            mailContent = mailContent.replaceAll("<replace-contact1-email/>",patientMail);
            mailContent = mailContent.replaceAll("<replace-contact2-name/>",messageSource.getMessage("mail.contact.medic",medicContactParams,clinicLocale));
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

            Order order = resultOrder.get();
            String patientMail  = order.getPatient_email();
            String medicMail   = order.getMedic().getEmail();
            String clinicMail   = order.getClinic().getEmail();
            String patientName  = order.getPatient_name();
            String medicName   = order.getMedic().getName();
            String clinicName   = order.getClinic().getName();

            Locale patientLocale = getLocale(patientMail);
            Locale medicLocale = getLocale(medicMail);
            Locale clinicLocale = getLocale(clinicMail);

            Object[] subjectParams = {result.getOrder_id(),result.getId()};
            Object[] patientContactParams = {patientName};
            Object[] medicContactParams = {medicName};
            Object[] clinicContactParams = {clinicName};
            Object[] patientMailParams = {patientName};
            Object[] medicMailParams = {medicMail};
            Object[] clinicMailParams = {clinicMail};

            String body = "<replace-m-body-sendResultMailNoHtml-details/>\n\n<replace-order-url/>\n\nContact Info\n<replace-contact1-name/><replace-contact1-email/>\n<replace-contact2-name/><replace-contact2-email/>";

            String basicMailContent = replaceURL(getTextTemplate());
            basicMailContent = basicMailContent.replaceAll("<replace-content/>",body);
            String mailContent;

            // mail to patient
            mailContent = basicMailContent;
            mailContent = replaceMessages(mailContent,patientLocale);
            mailContent = mailContent.replaceAll("<replace-contact1-name/>",messageSource.getMessage("mail.contact.medic",medicContactParams,patientLocale));
            mailContent = mailContent.replaceAll("<replace-contact1-email/>",messageSource.getMessage("mail.contact.email",medicMailParams,patientLocale));
            mailContent = mailContent.replaceAll("<replace-contact2-name/>",messageSource.getMessage("mail.contact.clinic",clinicContactParams,patientLocale));
            mailContent = mailContent.replaceAll("<replace-contact2-email/>",messageSource.getMessage("mail.contact.email",clinicMailParams,patientLocale));

            ms.sendSimpleMessage(patientMail,
                    messageSource.getMessage("mail.subject.result.patient",subjectParams,patientLocale),
                    replaceResultInfo(mailContent,result,order));

            // mail to medic
            mailContent = basicMailContent;
            mailContent = replaceMessages(mailContent,medicLocale);
            mailContent = mailContent.replaceAll("<replace-contact1-name/>",messageSource.getMessage("mail.contact.patient",patientContactParams,medicLocale));
            mailContent = mailContent.replaceAll("<replace-contact1-email/>",messageSource.getMessage("mail.contact.email",patientMailParams,medicLocale));
            mailContent = mailContent.replaceAll("<replace-contact2-name/>",messageSource.getMessage("mail.contact.clinic",clinicContactParams,medicLocale));
            mailContent = mailContent.replaceAll("<replace-contact2-email/>",messageSource.getMessage("mail.contact.email",clinicMailParams,medicLocale));
            ms.sendSimpleMessage(medicMail,
                    messageSource.getMessage("mail.subject.result.medic",subjectParams,medicLocale),
                    replaceResultInfo(mailContent,result,order));

            // mail to clinic
            mailContent = basicMailContent;
            mailContent = replaceMessages(mailContent,clinicLocale);
            mailContent = mailContent.replaceAll("<replace-contact1-name/>",messageSource.getMessage("mail.contact.patient",patientContactParams,clinicLocale));
            mailContent = mailContent.replaceAll("<replace-contact1-email/>",messageSource.getMessage("mail.contact.email",patientMailParams,clinicLocale));
            mailContent = mailContent.replaceAll("<replace-contact2-name/>",messageSource.getMessage("mail.contact.medic",medicContactParams,clinicLocale));
            mailContent = mailContent.replaceAll("<replace-contact2-email/>",messageSource.getMessage("mail.contact.email",medicMailParams,clinicLocale));
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

        String medicMail   = medic.getEmail();

        Locale locale = getLocale(medicMail);

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

        String medicMail   = medic.getEmail();

        Locale locale = getLocale(medicMail);

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

        String clinicMail   = clinic.getEmail();

        Locale locale = getLocale(clinicMail);

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

        String clinicMail   = clinic.getEmail();

        Locale locale = getLocale(clinicMail);

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

    // get data
    private String getMailTemplate(){
        String mailTemplateString = this.mailTemplate;
        mailTemplateString = replaceURL(mailTemplateString);
        return mailTemplateString;
    }

    private String getTextTemplate(){
        return this.textTemplate;
    }

    private Locale getLocale(String email){
        Optional<User> userOptional = userService.findByEmail(email);
        return (userOptional.isPresent())?Locale.forLanguageTag(userOptional.get().getLocale()):Locale.getDefault();
    }

    //replace functions
    private String replaceMessages(String m, Locale locale){
        String mailContent = m;

        mailContent = mailContent.replaceAll("<replace-m-appname/>",messageSource.getMessage("appname",null,locale));
        mailContent = mailContent.replaceAll("<replace-m-body-sendOrderMailHtml-details/>",messageSource.getMessage("mail.body.sendOrderMailHtml.details",null,locale));
        mailContent = mailContent.replaceAll("<replace-m-body-sendOrderMailHtml-orderUrl/>",messageSource.getMessage("mail.body.sendOrderMailHtml.orderUrl",null,locale));
        mailContent = mailContent.replaceAll("<replace-m-body-sendOrderMailNoHtml-details/>",messageSource.getMessage("mail.body.sendOrderMailNoHtml.details",null,locale));
        mailContent = mailContent.replaceAll("<replace-m-body-sendResultMailHtml-details/>",messageSource.getMessage("mail.body.sendResultMailHtml.details",null,locale));
        mailContent = mailContent.replaceAll("<replace-m-body-sendResultMailHtml-orderUrl/>",messageSource.getMessage("mail.body.sendResultMailHtml.orderUrl",null,locale));
        mailContent = mailContent.replaceAll("<replace-m-body-sendResultMailNoHtml-details/>",messageSource.getMessage("mail.body.sendResultMailNoHtml.details",null,locale));
        mailContent = mailContent.replaceAll("<replace-m-contactInfo/>",messageSource.getMessage("mail.contact.title",null,locale));
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
