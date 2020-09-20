package ar.edu.itba.paw.services;

import ar.edu.itba.paw.model.User;


import ar.edu.itba.paw.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User findById(final long id) {
        return userDao.findById(id);
    }

    @Override
    public User findByUsername(final String username) {
        return userDao.findByUsername(username);
    }
}
