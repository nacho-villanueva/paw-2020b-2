package ar.edu.itba.paw.services;

import ar.edu.itba.paw.model.User;

import java.util.Optional;

public interface UserService {

    Optional<User> findById(int id);

    Optional<User> findByEmail(String email);

    User register(String email, String password, int role, String locale);

    User register(String email, String password, String locale);

    User updateRole(User user, int role);

    User updatePassword(User user, String password);

}
