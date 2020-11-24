package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Result;
import ar.edu.itba.paw.persistence.ResultDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
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
    public Result register(long order_id, String result_data_type, byte[] result_data, String identificationType, byte[] identification, Date date, String responsible_name, String responsible_licenceNumber) {
        Result result = resultDao.register(order_id,result_data_type,result_data,identificationType,identification,date,responsible_name,responsible_licenceNumber);
        mailNotificationService.sendResultMail(result);
        return result;
    }
}

