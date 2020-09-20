package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.User;

public interface UserDao {

    public User findById(long id);

    User findByUsername(String username);
}
