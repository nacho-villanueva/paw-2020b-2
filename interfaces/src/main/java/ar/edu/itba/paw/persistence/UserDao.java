package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User;

import java.util.Optional;

public interface UserDao {
    Optional<User> findById(int id);

    Optional<User> findByEmail(String email);

    User register(String email, String password, int role, String locale);

    User updateRole(User user, int role);

    User updateEmail(User user, String email);

    User updatePassword(User user, String password);
}
