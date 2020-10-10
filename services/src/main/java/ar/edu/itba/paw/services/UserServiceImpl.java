package ar.edu.itba.paw.services;

import ar.edu.itba.paw.model.User;


import ar.edu.itba.paw.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.jws.soap.SOAPBinding;
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
    public User register(String email, String password, String locale) {
        return userDao.register(email,encoder.encode(password), User.UNDEFINED_ROLE_ID, locale);
    }

    @Override
    public User updateRole(User user, int role) {
        if(vs.isValidRole(role)) {
            return userDao.updateRole(user,role);
        }

        return null;
    }

    @Override
    public User updateEmail(User user, String email) {
        //TODO: Verification of email
        return userDao.updateEmail(user,email);
    }

    @Override
    public User updatePassword(User user, String password) {
        return userDao.updatePassword(user,encoder.encode(password));
    }

    @Override
    public boolean checkPassword(int user_id, String password) {
        return userDao.checkPassword(user_id,encoder.encode(password));
    }
}
