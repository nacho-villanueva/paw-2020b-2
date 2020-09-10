package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl  implements UserDao{

    @Override
    public User findById(long id) {
        return new User(id, "PAW from DAO for " + id);
    }
}
