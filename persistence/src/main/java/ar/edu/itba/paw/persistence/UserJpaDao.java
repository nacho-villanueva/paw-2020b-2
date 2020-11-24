package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.VerificationToken;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
public class UserJpaDao implements UserDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<User> findById(final int id) {
        return Optional.ofNullable(em.find(User.class,id));
    }

    @Override
    public Optional<User> findByEmail(final String email) {
        final TypedQuery<User> query = em.createQuery("SELECT u FROM User as u WHERE u.email = :email", User.class);
        query.setParameter("email",email);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Optional<VerificationToken> getVerificationToken(String token) {
        final TypedQuery<VerificationToken> query = em.createQuery("SELECT t FROM VerificationToken as t WHERE t.token = :token", VerificationToken.class);
        query.setParameter("token", token);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public void freeVerificationToken(User user) {
        Query query = em.createQuery("DELETE FROM VerificationToken as t WHERE t.user.id = :userid");
        query.setParameter("user_id", user.getId());
        query.executeUpdate();
    }

    @Override
    public User register(final String email, final String password, final int role, final String locale) {
        final User user = new User(email,password,role,locale);
        em.persist(user);
        em.flush();
        return user;
    }

    @Override
    public User updateRole(final User user, final int role) {
        Optional<User> userDB = findById(user.getId());
        userDB.ifPresent(user1 -> {
            user1.setRole(role);
        });
        em.flush();
        return userDB.orElse(null);
    }

    @Override
    public User updateEmail(final User user, final String email) {
        Optional<User> userDB = findById(user.getId());
        userDB.ifPresent(user1 -> {
            user1.setEmail(email);
        });
        em.flush();
        return userDB.orElse(null);
    }

    @Override
    public User updatePassword(final User user, final String password) {
        Optional<User> userDB = findById(user.getId());
        userDB.ifPresent(user1 -> {
            user1.setPassword(password);
        });
        em.flush();
        return userDB.orElse(null);
    }

    @Override
    public void setVerificationToken(User user, String token) {
        Optional<User> userDB = findById(user.getId());
        userDB.ifPresent(user1 -> {
            final VerificationToken verificationToken = new VerificationToken();
            verificationToken.setToken(token);
            verificationToken.setUser(user1);
            em.persist(verificationToken);
        });
        em.flush();
    }

    @Override
    public User verify(User user) {
        Optional<User> userDB = findById(user.getId());
        userDB.ifPresent(user1 -> {
            user1.setEnabled(true);
        });
        em.flush();
        return userDB.orElse(null);
    }
}
