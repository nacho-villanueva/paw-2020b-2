package ar.edu.itba.paw.services;

import ar.edu.itba.paw.model.User;

import ar.edu.itba.paw.service.UserService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class UserServiceImpl implements UserService {

    @Override
    public User findById(long id) {
        // TODO : Make 4 real!
        return new User(id, "PAW");
    }
}
