package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.MailNotificationService;
import ar.edu.itba.paw.interfaces.ResultFormService;
import ar.edu.itba.paw.model.OrderForm;
import ar.edu.itba.paw.model.Result;
import ar.edu.itba.paw.model.ResultForm;
import ar.edu.itba.paw.persistence.ResultDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Primary
@Service
public class ResultFormServiceImpl implements ResultFormService {

    @Autowired
    private MailNotificationService mailNotificationService;

    @Autowired
    private ResultDao resultDao;

    @Override
    public Long HandleOrderForm(ResultForm resultForm, byte[] identification, String identificationType, byte[] resultData, String resultDataType, long orderId) {
        Result result = resultDao.register(orderId,
                                    resultDataType,
                                    resultData,
                                    identificationType,
                                    identification,
                                    new Date(System.currentTimeMillis()),
                                    resultForm.getResponsible_name(),
                                    resultForm.getResponsible_licence_number());
        mailNotificationService.sendResultMail(result);

        return result.getId();
    }
}
