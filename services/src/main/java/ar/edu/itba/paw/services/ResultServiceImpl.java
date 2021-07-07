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
    public Collection<Result> findByOrderId(long id, int page, int pageSize) {
        return resultDao.findByOrderId(id,page,pageSize);
    }

    @Override
    public long findByOrderIdCount(long id) {
        return resultDao.findByOrderIdCount(id);
    }

    @Override
    public long findByOrderIdLastPage(long id, int pageSize) {
        return getLastPage(findByOrderIdCount(id),pageSize);
    }

    @Override
    public Result register(long orderId, String resultDataType, byte[] resultData, String identificationType, byte[] identification, LocalDate date, String responsibleName, String responsibleLicenceNumber) {
        Result result = resultDao.register(orderId,resultDataType,resultData,identificationType,identification,date,responsibleName,responsibleLicenceNumber);
        mailNotificationService.sendResultMail(result);
        return result;
    }

    // auxiliar functions
    private long getLastPage(final long count, final int pageSize){
        return (long) Math.ceil((double)count / pageSize);
    }
}

