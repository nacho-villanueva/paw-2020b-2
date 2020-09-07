package ar.edu.itba.paw.services;

import ar.edu.itba.paw.model.Patient;
import ar.edu.itba.paw.model.User;

import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.persistence.PatientDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    PatientDao patientDao;

    @Override
    public User findById(long id) {
        // TODO : Make 4 real!
        patientDao.register("asd","dasdas");
        return new User(id, "PAW");
    }
}
