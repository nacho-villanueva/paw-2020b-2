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

    @Autowired
    private ValidationService vs;

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
        return userDao.register(email,encoder.encode(password),User.UNDEFINED_ROLE_ID);
    }

    @Override
    public User updatePassword(User user, String password) {
        return userDao.updatePassword(user,encoder.encode(password));
    }
}
