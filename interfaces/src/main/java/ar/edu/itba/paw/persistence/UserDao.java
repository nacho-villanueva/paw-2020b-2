package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.VerificationToken;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> findById(int id);

    Optional<User> findByEmail(String email);

    User register(String email, String password, int role, String locale);

    User updateRole(User user, int role);

    User updateEmail(User user, String email);

    User updatePassword(User user, String password);

    void setVerificationToken(User user, String token);

    User verify(User user);

    Optional<VerificationToken> getVerificationToken(String token);

    void freeVerificationToken(User user);

    User updateUser(User user, String email, String password, String locale);

    Collection<User> getAll(int page, int pageSize);

    long userCount();

    User updateLocale(User user, String locale);
}
