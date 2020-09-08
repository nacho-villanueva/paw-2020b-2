package ar.edu.itba.paw.services;

import ar.edu.itba.paw.model.Patient;
import ar.edu.itba.paw.model.User;


import ar.edu.itba.paw.persistence.UserDao;
import ar.edu.itba.paw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserDao userDao;


    @Override
    public User findById(long id) {
        return userDao.findById(id);
        // TODO : Make 4 realz
    }
}
