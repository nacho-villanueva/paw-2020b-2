package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Result;
import ar.edu.itba.paw.persistence.ResultDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

@Transactional
@Service
public class ResultServiceImpl implements ResultService {

    @Autowired
    private MailNotificationService mailNotificationService;

    @Autowired
    private ResultDao resultDao;

    @Override
    public Optional<Result> findById(long id) {
        return resultDao.findById(id);
    }

    @Override
    public Collection<Result> findByOrderId(long id) {
        return resultDao.findByOrderId(id);
    }

    @Override
    public Result register(long orderId, String resultDataType, byte[] resultData, String identificationType, byte[] identification, LocalDate date, String responsibleName, String responsibleLicenceNumber) {
        Result result = resultDao.register(orderId,resultDataType,resultData,identificationType,identification,date,responsibleName,responsibleLicenceNumber);
        mailNotificationService.sendResultMail(result);
        return result;
    }
}

