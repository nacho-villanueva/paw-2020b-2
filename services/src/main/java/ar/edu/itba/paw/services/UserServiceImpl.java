package ar.edu.itba.paw.services;

import ar.edu.itba.paw.model.User;


import ar.edu.itba.paw.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Primary
@Service
public class UserServiceImpl implements UserService {

    private final static int BASIC_ROLE = 1;        //1 is for regular users

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public Optional<User> findById(int id) {
        return userDao.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public User register(String email, String password) {
        return userDao.register(email,encoder.encode(password),BASIC_ROLE);
    }

    @Override
    public User register(String email, String password, int role) {
        return userDao.register(email,encoder.encode(password),role);
    }

    @Override
    public User updateRole(User user, int role) {
        return userDao.updateRole(user,role);
    }

    @Override
    public User updatePassword(User user, String password) {
        return userDao.updatePassword(user,encoder.encode(password));
    }
}
