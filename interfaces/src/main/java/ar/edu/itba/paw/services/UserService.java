package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.VerificationToken;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> findById(int id);

    Optional<User> findByEmail(String email);

    User register(String email, String password, String locale);

    User updateRole(User user, int role);

    User updateEmail(User user, String email);

    User updatePassword(User user, String password);

    boolean checkPassword(int userId, String password);

    User verify(User user);

    Optional<VerificationToken> getVerificationToken(String token);

    User updateUser(User user, String email, String password, String locale);

    Collection<User> getAll(int page, int pageSize);

    int getPageCount(int perPage);

    User updateLocale(User user, String locale);
}
