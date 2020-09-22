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

    private String orderTextTemplate;
    private String resultTextTemplate;

    @PostConstruct
    private void init(){
        this.patientOrderSubject    = "[Order <replace-order-id/>] A new order awaits you!";
        this.medicOrderSubject      = "[Order <replace-order-id/>] Your order has been uploaded successfully.";
        this.clinicOrderSubject     = "[Order <replace-order-id/>] A new order has been uploaded.";
        this.patientResultSubject   = "[Order <replace-order-id/>-<replace-result-id/>] New results are up!";
        this.medicResultSubject     = "[Order <replace-order-id/>-<replace-result-id/>] New results are available.";
        this.clinicResultSubject    = "[Order <replace-order-id/>-<replace-result-id/>] Your results has been uploaded successfully.";

        this.orderTextTemplate    = "Details for this order are available from:\n\n<replace-order-url/>\n\nContact Info\n<replace-contact1-role/>: <replace-contact1-name/> (<replace-contact1-email/>)\n<replace-contact2-role/>: <replace-contact2-name/> (<replace-contact2-email/>)\n\nWith love,\n\tMedTracker (<replace-url/>)";
        this.resultTextTemplate   = orderTextTemplate;


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

        String patientMail  = order.getPatient().getEmail();
        String medicMail    = order.getMedic().getEmail();
        String clinicMail   = order.getClinic().getEmail();
        String patientName  = order.getPatient().getName();
        String medicName    = order.getMedic().getName();
        String clinicName   = order.getClinic().getName();

        if(this.useHTML){

            String basicMailContent = getMailTemplate();

            String body =
                    "<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                    "<tr><td><h2> See changes on our site </h2>\n" +
                    "<a href=\"<replace-order-url/>\" style=\"background-color:#009688;border-radius:4px;color:#ffffff;display:inline-block;;font-size:20px;font-weight:normal;line-height:50px;text-align:center;text-decoration:none;width:160px;font-weight:bold\" target=\"_blank\">View Order</a>\n" +
                    "</td></tr></table>";

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
            String basicMailContent = replaceURL(this.orderTextTemplate);
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

            String patientMail  = order.getPatient().getEmail();
            String medicMail   = order.getMedic().getEmail();
            String clinicMail   = order.getClinic().getEmail();
            String patientName  = order.getPatient().getName();
            String medicName   = order.getMedic().getName();
            String clinicName   = order.getClinic().getName();

            if(this.useHTML){

                String basicMailContent = getMailTemplate();

                String body =
                        "<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                                "<h2>\n" +
                                "See changes on our site\n" +
                                "</h2>\n" +
                                "<a href=\"<replace-order-url/>\" style=\"background-color:#009688;border-radius:4px;color:#ffffff;display:inline-block;;font-size:20px;font-weight:normal;line-height:50px;text-align:center;text-decoration:none;width:160px;font-weight:bold\" target=\"_blank\">View Order</a>\n" +
                                "</table>";

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
                String basicMailContent = replaceURL(this.resultTextTemplate);
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

    // replace functions
    private String getMailTemplate(){
        String mailTemplateString = this.mailTemplate.lines().collect(Collectors.joining("\n"));
        mailTemplateString = replaceURL(mailTemplateString);
        return mailTemplateString;
    }

    private String replaceURL(String mail){
        return mail.replaceAll("<replace-url/>",address.toString());
    }

    private String replaceOrderInfo(String mail, Order order){
        String ret = mail;

        ret = ret.replaceAll("<replace-order-url/>",address.toString()+"/view/study/"+urlEncoderService.encode(order.getOrder_id()));
        ret = ret.replaceAll("<replace-order-id/>",String.valueOf(order.getOrder_id()));
        ret = replacePatientInfo(ret, order.getPatient());
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

    private String replacePatientInfo(String mail, Patient patient){
        String ret = mail;

        ret = ret.replaceAll("<replace-patient-name/>",patient.getName());
        ret = ret.replaceAll("<replace-patient-email/>",patient.getEmail());

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
