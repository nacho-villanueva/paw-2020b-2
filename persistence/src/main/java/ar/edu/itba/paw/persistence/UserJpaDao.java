package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

//@Repository
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

    //TODO: los updates funcionan de manera rara en hibernate, voy a investigar un poco mas antes de armarlos
    @Override
    public User updateRole(final User user, final int role) {
        return null;
    }

    @Override
    public User updateEmail(final User user, final String email) {
        return null;
    }

    @Override
    public User updatePassword(final User user, final String password) {
        return null;
    }
}
