package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.User;

import java.util.Optional;

public interface UserDao {
    Optional<User> findById(int id);

    Optional<User> findByEmail(String email);

    User register(String email, String password, int role);

    User updateRole(User user, int role);

    User updatePassword(User user, String password);
}
