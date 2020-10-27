package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
    public User register(final String email, final String password, final int role, final String locale) {
        final User user = new User(email,password,role,locale);
        em.persist(user);
        return user;
    }

    @Override
    public User updateRole(final User user, final int role) {
        Optional<User> userDB = findById(user.getId());
        userDB.ifPresent(user1 -> {
            user1.setRole(role);
        });
        return user;
    }

    @Override
    public User updateEmail(final User user, final String email) {
        Optional<User> userDB = findById(user.getId());
        userDB.ifPresent(user1 -> {
            user1.setEmail(email);
        });
        return user;
    }

    @Override
    public User updatePassword(final User user, final String password) {
        Optional<User> userDB = findById(user.getId());
        userDB.ifPresent(user1 -> {
            user1.setPassword(password);
        });
        return user;
    }
}
