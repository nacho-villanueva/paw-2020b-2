package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User;
import org.springframework.stereotype.Repository;

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
        em.getTransaction().begin();
        final User user = new User(email,password,role,locale);
        em.persist(user);
        em.getTransaction().commit();
        return user;
    }

    @Override
    public User updateRole(final User user, final int role) {

        em.detach(user);
        em.getTransaction().begin();
        user.setRole(role);
        em.getTransaction().commit();

        return user;
    }

    @Override
    public User updateEmail(final User user, final String email) {
        em.detach(user);
        em.getTransaction().begin();
        user.setEmail(email);
        em.getTransaction().commit();

        return user;
    }

    @Override
    public User updatePassword(final User user, final String password) {
        em.detach(user);
        em.getTransaction().begin();
        user.setPassword(password);
        em.getTransaction().commit();

        return user;
    }
}
