package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.OrderNotFoundForExistingResultException;

import ar.edu.itba.paw.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ResourceUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Primary
@Service
public class MailNotificationServiceImpl implements MailNotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailNotificationService.class);

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

    @Value("classpath:mail/mailTemplate.txt")
    private Resource textTemplateResource;

    @Autowired
    private ResourceLoader resourceLoader;

    private String mailTemplate;
    private boolean useHTML;

    private String textTemplate;

    @PostConstruct
    private void init(){

        InputStream resourceInputStream;

        try {
            resourceInputStream = this.textTemplateResource.getInputStream();
            this.textTemplate = new BufferedReader(new InputStreamReader(resourceInputStream))
                    .lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            this.textTemplate = "<replace-content/>";
            LOGGER.error(e.getMessage());
        }


        try {
            resourceInputStream = this.mailTemplateResource.getInputStream();
            this.mailTemplate = new BufferedReader(new InputStreamReader(resourceInputStream))
                    .lines().collect(Collectors.joining("\n"));
            this.useHTML = true;
        } catch (IOException e) {
            this.useHTML = false;
            LOGGER.error(e.getMessage());
        }
    }

    @Override
    public void sendOrderMail(Order order) {
        String body = loadBodyFromFile("orderMail.html", "orderMail.txt");
        if(body != null) {
            if (this.useHTML) {
                String bodyPlain = loadBodyFromFile("orderMail.txt");
                sendOrderMailHtml(order, body,bodyPlain);
            } else {
                sendOrderMailPlainText(order, body);
            }
        }
    }

    @Override
    public void sendSharedOrderMail(Order order, User medic){
        String body = loadBodyFromFile("orderMail.html", "orderMail.txt");
        if(body != null){
            if(this.useHTML){
                String bodyPlain = loadBodyFromFile("orderMail.txt");
                sendSharedOrderMailHtml(order, medic, body, bodyPlain);
            }
            else {
                sendSharedOrderMailPlainText(order, medic, body);
            }
        }
    }

    @Override
    public void sendResultMail(Result result){
        String body = loadBodyFromFile("resultMail.html", "resultMail.txt");
        if(body != null) {
            if (this.useHTML) {
                String bodyPlain = loadBodyFromFile("resultMail.txt");
                sendResultMailHtml(result, body, bodyPlain);
            } else {
                sendResultMailPlainText(result, body);
            }
        }
    }

    @Override
    public void sendMedicApplicationValidatingMail(Medic medic){
        String body = loadBodyFromFile("medicApplicationMail.html", "medicApplicationMail.txt");
        if(body != null) {
            if (this.useHTML) {
                String bodyPlain = loadBodyFromFile("medicApplicationMail.txt");
                sendMedicApplicationValidatingMailHtml(medic, body, bodyPlain);
            } else {
                sendMedicApplicationValidatingMailPlainText(medic, body);
            }
        }
    }

    @Override
    public void sendClinicApplicationValidatingMail(Clinic clinic){
        String body = loadBodyFromFile("clinicApplicationMail.html", "clinicApplicationMail.txt");
        if(body != null) {
            if (this.useHTML) {
                String bodyPlain = loadBodyFromFile("clinicApplicationMail.txt");
                sendClinicApplicationValidatingMailHtml(clinic, body, bodyPlain);
            } else {
                sendClinicApplicationValidatingMailPlainText(clinic, body);
            }
        }
    }

    @Override
    public void sendVerificationMessage(String email, String token, String locale) {
        String verificationUrl = address.toString()+"/user-verification?token=" + token;
        Locale l = Locale.forLanguageTag(locale);
        String body = loadBodyFromFile("verificationMail.html", "verificationMail.txt");
        if(body != null) {
            if (this.useHTML) {
                String bodyPlain = loadBodyFromFile("verificationMail.txt");
                sendVerificationMessageHtml(email, body, bodyPlain, verificationUrl, l);
            } else {
                sendVerificationMessagePlainText(email, body, verificationUrl, l);
            }
        }

    }

    @Override
    public void sendShareRequestMail(ShareRequest shareRequest) {

        String body = loadBodyFromFile("shareRequestMail.html", "shareRequestMail.txt");
        if(body != null) {
            if (this.useHTML) {
                String bodyPlain = loadBodyFromFile("shareRequestMail.txt");
                sendShareRequestMailHtml(shareRequest, body, bodyPlain);
            } else {
                sendShareRequestMailPlainText(shareRequest, body);
            }
        }
    }

    @Override
    public void sendAcceptRequestMail(ShareRequest shareRequest) {

        String body = loadBodyFromFile("acceptRequestMail.html", "acceptRequestMail.txt");
        if(body != null) {
            if (this.useHTML) {
                String bodyPlain = loadBodyFromFile("acceptRequestMail.txt");
                sendAcceptRequestMailHtml(shareRequest, body, bodyPlain);
            } else {
                sendAcceptRequestMailPlainText(shareRequest, body);
            }
        }
    }

    @Override
    public void sendDenyRequestMail(ShareRequest shareRequest) {

        String body = loadBodyFromFile("denyRequestMail.html", "denyRequestMail.txt");
        if(body != null) {
            if (this.useHTML) {
                String bodyPlain = loadBodyFromFile("denyRequestMail.txt");
                sendDenyRequestMailHtml(shareRequest, body, bodyPlain);
            } else {
                sendDenyRequestMailPlainText(shareRequest, body);
            }
        }
    }

    @Override
    public void sendChangeClinicMail(Order order) {
        String body = loadBodyFromFile("orderMail.html", "orderMail.txt");
        if(body != null) {
            if (this.useHTML) {
                String bodyPlain = loadBodyFromFile("orderMail.txt");
                sendChangeClinicMailHtml(order, body,bodyPlain);
            } else {
                sendChangeClinicMailPlainText(order, body);
            }
        }
    }

    private String loadBodyFromFile(String htmlFile, String txtFile){
        URL bodyFile = null;
        if(useHTML){
            try {
                bodyFile = ResourceUtils.getURL("classpath:mail/" + htmlFile);
            } catch (FileNotFoundException e) {
                useHTML = false;
                LOGGER.error(e.getMessage());
            }
        }
        if(!useHTML){
            try {
                bodyFile = ResourceUtils.getURL("classpath:mail/" + txtFile);
            } catch (FileNotFoundException e) {
                LOGGER.error(e.getMessage());
            }
        }

        String body = null;

        try {
            if (bodyFile != null) {
                InputStream stream = bodyFile.openStream();
                body = new BufferedReader(new InputStreamReader(stream)).lines().collect(Collectors.joining("\n"));
                stream.close();
            }

        }
        catch (IOException e) {
            LOGGER.error(e.getMessage());
        }

        return body;
    }

    private String loadBodyFromFile(String mailFile){
        URL bodyFile;
        String body = null;

        try {
            bodyFile = ResourceUtils.getURL("classpath:mail/" + mailFile);
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage());
            return body;
        }

        try {
            if (bodyFile != null) {
                InputStream stream = bodyFile.openStream();
                body = new BufferedReader(new InputStreamReader(stream)).lines().collect(Collectors.joining("\n"));
                stream.close();
            }

        }
        catch (IOException e) {
            LOGGER.error(e.getMessage());
        }

        return body;

    }

    // send order mail with html
    private void sendOrderMailHtml(Order order, String bodyHtml, String bodyPlain){

        Locale patientLocale = getLocale(order.getPatientEmail());
        Locale medicLocale = getLocale(order.getMedic().getEmail());
        Locale clinicLocale = getLocale(order.getClinic().getEmail());

        Object[] subjectParams = {order.getOrderId()};

        ArrayList<String> mailInline = new ArrayList<>();
        mailInline.add("logo.png");
        mailInline.add("envelope-regular.png");

        String basicMailContent = getMailTemplate();

        basicMailContent = basicMailContent.replace("<replace-content/>",bodyHtml);

        String mailContent;

        // ------- MAIL TO PATIENT ---------
        mailContent = basicMailContent;

        Map<String, String> replacePatient = new HashMap<>();
        replacePatient.put("url", address.toString());

        if(!userService.findByEmail(order.getPatientEmail()).isPresent()){
            replacePatient.put("new-info", messageSource.getMessage("mail.newInfo.patient.html",new Object[] {order.getPatientName()}, patientLocale));
        }

        replacePatientMailContacts(order, replacePatient, patientLocale);
        replaceOrderInfo(order, replacePatient, patientLocale);

        ms.sendMimeMessage(order.getPatientEmail(),
                messageSource.getMessage("mail.subject.order.patient",subjectParams,patientLocale),
                getOrderMailPatientBodyPlainText(order, bodyPlain, patientLocale),
                replaceAllMessages(mailContent, replacePatient, patientLocale),
                mailInline
        );
        // ---------------------------------

        // ------- MAIL TO MEDIC ----------
        mailContent = basicMailContent;

        Map<String, String> replaceMedic = new HashMap<>();
        replaceMedic.put("url", address.toString());

        replaceMedicMailContacts(order, replaceMedic, medicLocale);
        replaceOrderInfo(order, replaceMedic, medicLocale);

        ms.sendMimeMessage(order.getMedic().getEmail(),
                messageSource.getMessage("mail.subject.order.medic",subjectParams,medicLocale),
                getOrderMailMedicBodyPlainText(order, bodyPlain, medicLocale),
                replaceAllMessages(mailContent, replaceMedic, medicLocale),
                mailInline
        );
        // ---------------------------------

        // ------- MAIL TO CLINIC ----------
        mailContent = basicMailContent;

        Map<String, String> replaceClinic = new HashMap<>();
        replaceClinic.put("url", address.toString());

        replaceClinic.put("order-url", address.toString()+"/view-study/"+urlEncoderService.encode(order.getOrderId()));

        replaceOrderInfo(order, replaceClinic, clinicLocale);
        replaceClinicMailContacts(order, replaceClinic, clinicLocale);

        ms.sendMimeMessage(order.getClinic().getEmail(),
                messageSource.getMessage("mail.subject.order.clinic",subjectParams,clinicLocale),
                getOrderMailClinicBodyPlainText(order, bodyPlain, clinicLocale),
                replaceAllMessages(mailContent, replaceClinic, clinicLocale),
                mailInline
        );
        // ----------------------------------


    }

    // send order mail without html
    private void sendOrderMailPlainText(Order order, String body){

        Locale patientLocale = getLocale(order.getPatientEmail());
        Locale medicLocale = getLocale(order.getMedic().getEmail());
        Locale clinicLocale = getLocale(order.getClinic().getEmail());

        Object[] subjectParams = {order.getOrderId()};



        // ------- MAIL TO PATIENT ---------

        ms.sendSimpleMessage(order.getPatientEmail(),
                messageSource.getMessage("mail.subject.order.patient",subjectParams,patientLocale),
                getOrderMailPatientBodyPlainText(order, body, patientLocale));
        // ---------------------------------

        // ------- MAIL TO MEDIC -----------

        ms.sendSimpleMessage(order.getMedic().getEmail(),
                messageSource.getMessage("mail.subject.order.medic",subjectParams,medicLocale),
                getOrderMailMedicBodyPlainText(order, body, medicLocale));

        // ---------------------------------

        // ------- MAIL TO CLINIC ----------

        ms.sendSimpleMessage(order.getClinic().getEmail(),
                messageSource.getMessage("mail.subject.order.clinic",subjectParams,clinicLocale),
                getOrderMailClinicBodyPlainText(order, body, clinicLocale));
        // ----------------------------------

    }

    private String getOrderMailPatientBodyPlainText(Order order, String body, Locale locale){

        String mailContent = getTextTemplate();
        mailContent = mailContent.replace("<replace-content/>",body);

        Map<String, String> replacePatient = new HashMap<>();
        replacePatient.put("url", address.toString());

        if(!userService.findByEmail(order.getPatientEmail()).isPresent()){
            replacePatient.put("new-info", messageSource.getMessage("mail.newInfo.patient.plain",new Object[] {order.getPatientName()}, locale));
        }

        replacePatientMailContacts(order, replacePatient, locale);
        replaceOrderInfo(order, replacePatient, locale);

        return replaceAllMessages(mailContent, replacePatient, locale);
    }

    private String getOrderMailMedicBodyPlainText(Order order, String body, Locale locale){

        String mailContent = getTextTemplate();
        mailContent = mailContent.replace("<replace-content/>",body);

        Map<String, String> replaceMedic = new HashMap<>();
        replaceMedic.put("url", address.toString());

        replaceMedicMailContacts(order, replaceMedic, locale);
        replaceOrderInfo(order, replaceMedic, locale);

        return replaceAllMessages(mailContent, replaceMedic, locale);
    }

    private String getOrderMailClinicBodyPlainText(Order order, String body, Locale locale){

        String mailContent = getTextTemplate();
        mailContent = mailContent.replace("<replace-content/>",body);

        Map<String, String> replaceClinic = new HashMap<>();
        replaceClinic.put("url", address.toString());

        replaceMedicMailContacts(order, replaceClinic, locale);
        replaceOrderInfo(order, replaceClinic, locale);

        return replaceAllMessages(mailContent, replaceClinic, locale);
    }

    private void sendSharedOrderMailHtml(Order order, User medic, String bodyHtml, String bodyPlain){
        Locale medicLocale = getLocale(order.getMedic().getEmail());

        Object[] subjectParams = {order.getPatientName()};

        ArrayList<String> mailInline = new ArrayList<>();
        mailInline.add("logo.png");
        mailInline.add("envelope-regular.png");

        String mailContent = getMailTemplate();
        mailContent = mailContent.replace("<replace-content/>",bodyHtml);

        Map<String, String> replaceMedic = new HashMap<>();
        replaceMedic.put("url", address.toString());

        replaceMedicMailContacts(order, replaceMedic, medicLocale);
        replaceOrderInfo(order, replaceMedic, medicLocale);

        ms.sendMimeMessage(medic.getEmail(),
                messageSource.getMessage("mail.subject.shared-order.medic",subjectParams,medicLocale),
                getOrderMailMedicBodyPlainText(order, bodyPlain, medicLocale),
                replaceAllMessages(mailContent, replaceMedic, medicLocale),
                mailInline
        );

    }

    private void sendSharedOrderMailPlainText(Order order, User medic, String body){
        Locale medicLocale = getLocale(order.getMedic().getEmail());
        Object[] subjectParams = {order.getPatientName()};

        ms.sendSimpleMessage(medic.getEmail(),
                messageSource.getMessage("mail.subject.order.medic",subjectParams,medicLocale),
                getOrderMailMedicBodyPlainText(order, body, medicLocale));
    }

    // send result mail with html
    private void sendResultMailHtml(Result result, String bodyHtml, String bodyPlain){
        Optional<Order> resultOrder = orderService.findById(result.getOrderId());

        if(resultOrder.isPresent()){
            Order order = resultOrder.get();

            Locale patientLocale = getLocale(order.getPatientEmail());
            Locale medicLocale = getLocale(order.getMedic().getEmail());
            Locale clinicLocale = getLocale(order.getClinic().getEmail());

            Object[] subjectParams = {result.getOrderId(),result.getId()};

            String basicMailContent = getMailTemplate();

            ArrayList<String> mailInline = new ArrayList<>();
            mailInline.add("logo.png");
            mailInline.add("envelope-regular.png");

            basicMailContent = basicMailContent.replace("<replace-content/>",bodyHtml);

            String mailContent;

            // ------- MAIL TO PATIENT ----------
            mailContent = basicMailContent;

            Map<String, String> replacePatient = new HashMap<>();
            replacePatient.put("url", address.toString());

            if(!userService.findByEmail(order.getPatientEmail()).isPresent()){
                replacePatient.put("new-info", messageSource.getMessage("mail.newInfo.patient.html",new Object[] {order.getPatientName()}, patientLocale));
            }

            replaceResultInfo(result, order, replacePatient, patientLocale);
            replacePatientMailContacts(order, replacePatient, patientLocale);

            ms.sendMimeMessage(order.getPatientEmail(),
                    messageSource.getMessage("mail.subject.result.patient",subjectParams,patientLocale),
                    getResultMailPatientBodyPlainText(result, order, bodyPlain, patientLocale),
                    replaceAllMessages(mailContent, replacePatient, patientLocale),
                    mailInline
            );
            // ----------------------------------

            // ------- MAIL TO MEDIC ----------
            mailContent = basicMailContent;

            Map<String, String> replaceMedic = new HashMap<>();
            replaceMedic.put("url", address.toString());

            replaceResultInfo(result, order, replaceMedic, medicLocale);
            replaceMedicMailContacts(order, replaceMedic, medicLocale);

            ms.sendMimeMessage(order.getMedic().getEmail(),
                    messageSource.getMessage("mail.subject.result.medic",subjectParams,medicLocale),
                    getResultMailMedicBodyPlainText(result, order, bodyPlain, medicLocale),
                    replaceAllMessages(mailContent, replaceMedic, medicLocale),
                    mailInline
            );
            // ----------------------------------

            // ------- MAIL TO CLINIC ----------
            mailContent = basicMailContent;

            Map<String, String> replaceClinic = new HashMap<>();
            replaceClinic.put("url", address.toString());

            replaceResultInfo(result, order, replaceClinic, clinicLocale);
            replaceClinicMailContacts(order, replaceClinic, clinicLocale);

            ms.sendMimeMessage(order.getClinic().getEmail(),
                    messageSource.getMessage("mail.subject.result.clinic",subjectParams,clinicLocale),
                    getResultMailClinicBodyPlainText(result, order, bodyPlain, clinicLocale),
                    replaceAllMessages(mailContent, replaceClinic, clinicLocale),
                    mailInline
            );
            // ----------------------------------

        }else{
            throw new OrderNotFoundForExistingResultException();
        }
    }

    // send result mail without html
    private void sendResultMailPlainText(Result result, String body){

        Optional<Order> resultOrder = orderService.findById(result.getOrderId());

        if(resultOrder.isPresent()){

            Order order = resultOrder.get();

            Locale patientLocale = getLocale(order.getPatientEmail());
            Locale medicLocale = getLocale(order.getMedic().getEmail());
            Locale clinicLocale = getLocale(order.getClinic().getEmail());

            Object[] subjectParams = {result.getOrderId(),result.getId()};

            // ------- MAIL TO PATIENT ----------

            ms.sendSimpleMessage(order.getPatientEmail(),
                    messageSource.getMessage("mail.subject.result.patient",subjectParams,patientLocale),
                    getResultMailPatientBodyPlainText(result, order, body, patientLocale));
            // ----------------------------------

            // ------- MAIL TO MEDIC ----------

            ms.sendSimpleMessage(order.getMedic().getEmail(),
                    messageSource.getMessage("mail.subject.result.medic",subjectParams,medicLocale),
                    getResultMailMedicBodyPlainText(result, order, body, medicLocale));
            // ----------------------------------

            // ------- MAIL TO CLINIC ----------

            ms.sendSimpleMessage(order.getClinic().getEmail(),
                    messageSource.getMessage("mail.subject.result.clinic",subjectParams,clinicLocale),
                    getResultMailClinicBodyPlainText(result, order, body, clinicLocale));
            // ----------------------------------

        }else{
            throw new OrderNotFoundForExistingResultException();
        }
    }
    
    private String getResultMailPatientBodyPlainText(Result result, Order order, String body, Locale locale){

        String mailContent = getTextTemplate();
        mailContent = mailContent.replace("<replace-content/>",body);

        Map<String, String> replacePatient = new HashMap<>();
        replacePatient.put("url", address.toString());

        if(!userService.findByEmail(order.getPatientEmail()).isPresent()){
            replacePatient.put("new-info", messageSource.getMessage("mail.newInfo.patient.plain",new Object[] {order.getPatientName()}, locale));
        }

        replaceResultInfo(result, order, replacePatient, locale);
        replacePatientMailContacts(order, replacePatient, locale);

        return replaceAllMessages(mailContent, replacePatient, locale);
    }

    private String getResultMailMedicBodyPlainText(Result result, Order order, String body, Locale locale){

        String mailContent = getTextTemplate();
        mailContent = mailContent.replace("<replace-content/>",body);

        Map<String, String> replaceMedic = new HashMap<>();
        replaceMedic.put("url", address.toString());

        replaceResultInfo(result, order, replaceMedic, locale);
        replaceMedicMailContacts(order, replaceMedic, locale);

        return replaceAllMessages(mailContent, replaceMedic, locale);
    }

    private String getResultMailClinicBodyPlainText(Result result, Order order, String body, Locale locale){

        String mailContent = getTextTemplate();
        mailContent = mailContent.replace("<replace-content/>",body);

        Map<String, String> replaceClinic = new HashMap<>();
        replaceClinic.put("url", address.toString());

        replaceResultInfo(result, order, replaceClinic, locale);
        replaceClinicMailContacts(order, replaceClinic, locale);

        return replaceAllMessages(mailContent, replaceClinic, locale);
    }

    // send medic application validating mail with html
    private void sendMedicApplicationValidatingMailHtml(Medic medic, String bodyHtml, String bodyPlain){
        Locale locale = getLocale(medic.getEmail());

        Object[] subjectParams = {medic.getName()};

        Map<String, String> replace = new HashMap<>();
        replace.put("url", address.toString());

        String basicMailContent = getMailTemplate();
        String mailContent = basicMailContent.replace("<replace-content/>",bodyHtml);

        replaceMedicInfo(medic, replace);

        ArrayList<String> mailInline = new ArrayList<>();
        mailInline.add("logo.png");

        String mailContentPlainText = getMedicApplicationmValidBodyPlainText(medic,bodyPlain);

        ms.sendMimeMessage(medic.getEmail(),
                messageSource.getMessage("mail.subject.application.medic",subjectParams,locale),
                mailContentPlainText,
                replaceAllMessages(mailContent, replace, locale),
                mailInline
        );

    }

    // send medic application validating mail without html
    private void sendMedicApplicationValidatingMailPlainText(Medic medic, String body){
        Locale locale = getLocale(medic.getEmail());
        Object[] subjectParams = {medic.getName()};


        String mailContentPlainText = getMedicApplicationmValidBodyPlainText(medic,body);

        ms.sendSimpleMessage(medic.getEmail(),
                messageSource.getMessage("mail.subject.application.medic",subjectParams,locale),
                mailContentPlainText
        );
    }

    private String getMedicApplicationmValidBodyPlainText(Medic medic, String body){
        Locale locale = getLocale(medic.getEmail());
        Map<String, String> replace = new HashMap<>();
        replace.put("url", address.toString());

        String basicMailContent = getTextTemplate();
        String mailContent = basicMailContent.replace("<replace-content/>",body);

        replaceMedicInfo(medic, replace);

        return replaceAllMessages(mailContent, replace, locale);
    }

    // send clinic application validating mail with html
    private void sendClinicApplicationValidatingMailHtml(Clinic clinic, String bodyHtml, String bodyPlain){
        Locale locale = getLocale(clinic.getEmail());
        Object[] subjectParams = {clinic.getName()};

        String basicMailContent = getMailTemplate();
        String mailContent = basicMailContent.replace("<replace-content/>",bodyHtml);

        Map<String, String> replace = new HashMap<>();
        replace.put("url", address.toString());

        replaceClinicInfo(clinic, replace);

        ArrayList<String> mailInline = new ArrayList<>();
        mailInline.add("logo.png");

        String mailContentPlainText = getClinicApplicationValidatingMailBodyPlainText(clinic, bodyPlain);

        ms.sendMimeMessage(clinic.getEmail(),
                messageSource.getMessage("mail.subject.application.clinic",subjectParams,locale),
                mailContentPlainText,
                replaceAllMessages(mailContent, replace, locale),
                mailInline
        );
    }

    // send clinic application validating mail without html
    private void sendClinicApplicationValidatingMailPlainText(Clinic clinic, String body){
        Locale locale = getLocale(clinic.getEmail());
        Object[] subjectParams = {clinic.getName()};

        String mailSubject = messageSource.getMessage("mail.subject.application.clinic",subjectParams,locale);
        String mailContentPlainText = getClinicApplicationValidatingMailBodyPlainText(clinic, body);

        ms.sendSimpleMessage(clinic.getEmail(),
                mailSubject,
                mailContentPlainText
        );

    }

    private String getClinicApplicationValidatingMailBodyPlainText(Clinic clinic, String body){
        Locale locale = getLocale(clinic.getEmail());

        String basicMailContent = getTextTemplate();
        String mailContent = basicMailContent.replace("<replace-content/>",body);

        Map<String, String> replace = new HashMap<>();
        replace.put("url", address.toString());

        replaceClinicInfo(clinic, replace);

        return replaceAllMessages(mailContent, replace, locale);
    }

    private void sendVerificationMessageHtml(String email, String bodyHtml, String bodyPlain, String verificationUrl,Locale locale){

        ArrayList<String> mailInline = new ArrayList<>();
        mailInline.add("logo.png");

        String mailSubject = messageSource.getMessage("mail.subject.verification.user",null,locale);

        String basicMailContent = getMailTemplate().replace("<replace-content/>", bodyHtml);
        Map<String, String> replace = new HashMap<>();
        replace.put("url", address.toString());
        replace.put("verificationUrl", verificationUrl);
        String mailContent = replaceAllMessages(basicMailContent, replace, locale);

        String mailContentPlainText = getVerificationBodyPlainText(bodyPlain, verificationUrl, locale);

        ms.sendMimeMessage(email,
                mailSubject,
                mailContentPlainText,
                mailContent,
                mailInline
        );
    }

    private void sendVerificationMessagePlainText(String email, String body, String verificationUrl, Locale locale){

        String mailSubject = messageSource.getMessage("mail.subject.verification.user",null,locale);
        String mailContentPlainText = getVerificationBodyPlainText(body, verificationUrl, locale);

        ms.sendSimpleMessage(email,
                mailSubject,
                mailContentPlainText
        );
    }

    private String getVerificationBodyPlainText(String body, String verificationUrl, Locale locale){
        String mailContent = getTextTemplate().replace("<replace-content/>",body);
        Map<String, String> replace = new HashMap<>();
        replace.put("verificationUrl", verificationUrl);
        replace.put("url", address.toString());

        mailContent = replaceAllMessages(mailContent, replace, locale);

        return mailContent;
    }

    private void sendShareRequestMailHtml(ShareRequest shareRequest, String bodyHtml, String bodyPlain){

        String l = Locale.getDefault().getLanguage();;

        Optional<User> userOptional = userService.findByEmail(shareRequest.getPatientEmail());
        if(userOptional.isPresent())
            l = userOptional.get().getLocale();

        Locale patientLocale = Locale.forLanguageTag(l);

        ArrayList<String> mailInline = new ArrayList<>();
        mailInline.add("logo.png");
        mailInline.add("envelope-regular.png");

        String mailSubject = messageSource.getMessage("mail.subject.shareRequest.patient",null,patientLocale);
        String mailContentPlainText = getShareRequestBodyPlainText(shareRequest,bodyPlain,patientLocale);

        String basicMailContent = getMailTemplate().replace("<replace-content/>", bodyHtml);
        Map<String, String> replace = new HashMap<>();
        replace.put("url", address.toString());
        replaceShareRequestInfo(shareRequest,replace, patientLocale);

        String mailContent = replaceAllMessages(basicMailContent,replace,patientLocale);

        ms.sendMimeMessage(shareRequest.getPatientEmail(),
                mailSubject,
                mailContentPlainText,
                mailContent,
                mailInline
        );


    }

    private void sendShareRequestMailPlainText(ShareRequest shareRequest, String body){

        String l = Locale.getDefault().getLanguage();

        Optional<User> userOptional = userService.findByEmail(shareRequest.getPatientEmail());
        if(userOptional.isPresent())
            l = userOptional.get().getLocale();

        Locale patientLocale = Locale.forLanguageTag(l);

        String mailSubject = messageSource.getMessage("mail.subject.shareRequest.patient",null,patientLocale);
        String mailContentPlainText = getShareRequestBodyPlainText(shareRequest,body,patientLocale);

        ms.sendSimpleMessage(shareRequest.getPatientEmail(),
                mailSubject,
                mailContentPlainText
        );
    }

    private String getShareRequestBodyPlainText(ShareRequest shareRequest, String body, Locale locale){
        String mailContent = getTextTemplate().replace("<replace-content/>",body);
        Map<String, String> replace = new HashMap<>();
        replace.put("url", address.toString());
        replaceShareRequestInfo(shareRequest,replace, locale);

        mailContent = replaceAllMessages(mailContent, replace, locale);

        return mailContent;
    }

    private void sendAcceptRequestMailHtml(ShareRequest shareRequest, String bodyHtml, String bodyPlain){

        Locale medicLocale = Locale.forLanguageTag(shareRequest.getMedic().getUser().getLocale());

        ArrayList<String> mailInline = new ArrayList<>();
        mailInline.add("logo.png");

        String mailSubject = messageSource.getMessage("mail.subject.acceptRequest.medic",null,medicLocale);
        String mailContentPlainText = getShareRequestBodyPlainText(shareRequest,bodyPlain,medicLocale);

        String basicMailContent = getMailTemplate().replace("<replace-content/>", bodyHtml);
        Map<String, String> replace = new HashMap<>();
        replace.put("url", address.toString());
        replaceShareRequestInfo(shareRequest,replace, medicLocale);

        String mailContent = replaceAllMessages(basicMailContent,replace,medicLocale);

        ms.sendMimeMessage(shareRequest.getMedic().getUser().getEmail(),
                mailSubject,
                mailContentPlainText,
                mailContent,
                mailInline
        );
    }

    private void sendAcceptRequestMailPlainText(ShareRequest shareRequest, String body){

        Locale medicLocale = Locale.forLanguageTag(shareRequest.getMedic().getUser().getLocale());

        String mailSubject = messageSource.getMessage("mail.subject.acceptRequest.medic",null,medicLocale);
        String mailContentPlainText = getShareRequestBodyPlainText(shareRequest,body,medicLocale);

        ms.sendSimpleMessage(shareRequest.getMedic().getUser().getEmail(),
                mailSubject,
                mailContentPlainText
        );
    }

    private void sendDenyRequestMailHtml(ShareRequest shareRequest, String bodyHtml, String bodyPlain){

        Locale medicLocale = Locale.forLanguageTag(shareRequest.getMedic().getUser().getLocale());

        ArrayList<String> mailInline = new ArrayList<>();
        mailInline.add("logo.png");

        String mailSubject = messageSource.getMessage("mail.subject.denyRequest.medic",null,medicLocale);
        String mailContentPlainText = getShareRequestBodyPlainText(shareRequest,bodyPlain,medicLocale);

        String basicMailContent = getMailTemplate().replace("<replace-content/>", bodyHtml);
        Map<String, String> replace = new HashMap<>();
        replace.put("url", address.toString());
        replaceShareRequestInfo(shareRequest,replace, medicLocale);

        String mailContent = replaceAllMessages(basicMailContent,replace,medicLocale);

        ms.sendMimeMessage(shareRequest.getMedic().getUser().getEmail(),
                mailSubject,
                mailContentPlainText,
                mailContent,
                mailInline
        );
    }

    private void sendDenyRequestMailPlainText(ShareRequest shareRequest, String body){

        Locale medicLocale = Locale.forLanguageTag(shareRequest.getMedic().getUser().getLocale());

        String mailSubject = messageSource.getMessage("mail.subject.denyRequest.medic",null,medicLocale);
        String mailContentPlainText = getShareRequestBodyPlainText(shareRequest,body, medicLocale);

        ms.sendSimpleMessage(shareRequest.getMedic().getUser().getEmail(),
                mailSubject,
                mailContentPlainText
        );
    }

    private void sendChangeClinicMailHtml(Order order, String bodyHtml, String bodyPlain){

        Locale clinicLocale = getLocale(order.getClinic().getEmail());

        Object[] subjectParams = {order.getOrderId()};

        ArrayList<String> mailInline = new ArrayList<>();
        mailInline.add("logo.png");
        mailInline.add("envelope-regular.png");

        String mailContent = getMailTemplate().replace("<replace-content/>",bodyHtml);

        Map<String, String> replaceClinic = new HashMap<>();
        replaceClinic.put("url", address.toString());

        replaceClinic.put("order-url", address.toString()+"/view-study/"+urlEncoderService.encode(order.getOrderId()));

        replaceOrderInfo(order, replaceClinic, clinicLocale);
        replaceClinicMailContacts(order, replaceClinic, clinicLocale);

        ms.sendMimeMessage(order.getClinic().getEmail(),
                messageSource.getMessage("mail.subject.changeOrder.clinic",subjectParams,clinicLocale),
                getOrderMailClinicBodyPlainText(order, bodyPlain, clinicLocale),
                replaceAllMessages(mailContent, replaceClinic, clinicLocale),
                mailInline
        );

    }

    private void sendChangeClinicMailPlainText(Order order, String body){

        Locale clinicLocale = getLocale(order.getClinic().getEmail());

        Object[] subjectParams = {order.getOrderId()};

        ms.sendSimpleMessage(order.getClinic().getEmail(),
                messageSource.getMessage("mail.subject.changeOrder.clinic",subjectParams,clinicLocale),
                getOrderMailClinicBodyPlainText(order, body, clinicLocale));

    }

    // get data
    private String getMailTemplate(){
        return this.mailTemplate;
    }

    private String getTextTemplate(){
        return this.textTemplate;
    }

    private Locale getLocale(String email){
        Optional<User> userOptional = userService.findByEmail(email);
        return (userOptional.isPresent())?Locale.forLanguageTag(userOptional.get().getLocale()):Locale.getDefault();
    }

    private String replaceAllMessages(String body, Map<String, String> replacements, Locale locale){
        Pattern r = Pattern.compile("\\$\\{replace-[^}]*}");
        Matcher m = r.matcher(body);

        StringBuffer sb = new StringBuffer();

        while (m.find()){
            String value = m.group(0).replace("${replace-","").replace("}","");
            String replaceValue = null;
            if(replacements != null && replacements.containsKey(value))
                replaceValue = replacements.get(value);
            else
                if(value.startsWith("m-"))
                    replaceValue = messageSource.getMessage(value.replace("m-",""), null, locale);
            if(replaceValue == null)
                replaceValue = "";

            m.appendReplacement(sb, replaceValue);
        }
        m.appendTail(sb);

        return sb.toString();
    }

    private void replaceOrderInfo(Order order, Map<String, String> replace, Locale locale){
        String studyName = order.getStudy().getName();
        String description = order.getDescription();

        Object[] studyParam = {studyName};
        Object[] descriptionParam = {description};

        replace.put("study-type", messageSource.getMessage("mail.orderInfo.study", studyParam, locale));
        if(description != null && !description.isEmpty()){
            replace.put("description", messageSource.getMessage("mail.orderInfo.description", descriptionParam, locale));
        }


        replace.put("order-url", address.toString()+"/view-study/"+urlEncoderService.encode(order.getOrderId()));
        replace.put("order-id", String.valueOf(order.getOrderId()));
        replace.put("patient-name", order.getPatientName());
        replace.put("patient-email",order.getPatientEmail());

        replaceMedicInfo(order.getMedic(), replace);
        replaceClinicInfo(order.getClinic(), replace);
    }

    private void replaceResultInfo(Result result, Order order, Map<String, String> replace, Locale locale){
        replace.put("result-id", String.valueOf(result.getId()));
        replaceOrderInfo(order, replace, locale);
    }

    private void replacePatientMailContacts(Order order, Map<String, String> replace, Locale locale){
        replace.put("contact1-name", messageSource.getMessage("mail.contact.medic",new Object[] {order.getMedic().getName()}, locale));
        replace.put("contact1-email", order.getMedic().getEmail());
        replace.put("contact2-name", messageSource.getMessage("mail.contact.clinic",new Object[] {order.getClinic().getName()}, locale));
        replace.put("contact2-email", order.getClinic().getEmail());
    }

    private void replaceMedicMailContacts(Order order, Map<String, String> replace, Locale locale){
        replace.put("contact1-name", messageSource.getMessage("mail.contact.patient",new Object[] {order.getPatientName()}, locale));
        replace.put("contact1-email", order.getPatientEmail());
        replace.put("contact2-name", messageSource.getMessage("mail.contact.clinic",new Object[] {order.getClinic().getName()}, locale));
        replace.put("contact2-email", order.getClinic().getEmail());
    }

    private void replaceClinicMailContacts(Order order, Map<String, String> replace, Locale locale){
        replace.put("contact1-name", messageSource.getMessage("mail.contact.medic",new Object[] {order.getMedic().getName()}, locale));
        replace.put("contact1-email", order.getMedic().getEmail());
        replace.put("contact2-name", messageSource.getMessage("mail.contact.patient",new Object[] {order.getPatientName()}, locale));
        replace.put("contact2-email",order.getPatientEmail());
    }

    private void replaceMedicInfo(Medic medic, Map<String, String> replace){
        replace.put("medic-name",medic.getName());
        replace.put("medic-email",medic.getEmail());
    }

    private void replaceClinicInfo(Clinic clinic, Map<String, String> replace){
        replace.put("clinic-name",clinic.getName());
        replace.put("clinic-email",clinic.getEmail());
    }

    private void replaceShareRequestInfo(ShareRequest shareRequest, Map<String, String> replace, Locale locale){

        UriComponents uriComponentsShare = UriComponentsBuilder.fromUriString(address.toString()+"/access-request/view/")
                .queryParam("medicId",shareRequest.getMedic().getUser().getId().toString())
                .queryParam("patientEmail",shareRequest.getPatientEmail())
                .queryParam("studyTypeId",shareRequest.getStudyType().getId().toString())
                .build().encode();
        String shareRequestUrl = uriComponentsShare.toUriString();

        UriComponents uriComponentsAccept = UriComponentsBuilder.fromUriString(address.toString()+"/my-orders/")
                .queryParam("studyId",shareRequest.getStudyType().getId().toString())
                .queryParam("patientEmail",shareRequest.getPatientEmail())
                .queryParam("submit","search")
                .build().encode();
        String acceptRequestUrl = uriComponentsAccept.toUriString();

        Object[] medicName = {shareRequest.getMedic().getName()};

        replace.put("share-request-main-title",messageSource.getMessage("mail.body.sendShareRequestMailHtml.main",medicName,locale));
        replace.put("share-request-url", shareRequestUrl);

        replace.put("accept-request-url",acceptRequestUrl);

        replace.put("contact-patient",messageSource.getMessage("mail.contact.patient",new Object[] {shareRequest.getPatientEmail()},locale));
        replace.put("medic-name", messageSource.getMessage("mail.contact.medic",medicName, locale));
        replace.put("medic-email", shareRequest.getMedic().getEmail());
        replace.put("study-type", messageSource.getMessage("mail.shareRequestMailPlainText.body.studyType",
                new Object[] {shareRequest.getStudyType().getName()},locale));
    }
}
