package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserJdbcDao implements UserDao{

    private static final RowMapper<User> USER_ROW_MAPPER = (rs, rowNum) ->
            new User(rs.getInt("id"),rs.getString("email"),rs.getString("password"),rs.getInt("role"));

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Autowired
    public UserJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
    }


    @Override
    public Optional<User> findById(final int id) {
        return jdbcTemplate.query("SELECT * FROM users WHERE id = ?", new Object[]{ id }, USER_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jdbcTemplate.query("SELECT * FROM users WHERE email = ?", new Object[]{ email }, USER_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public User register(String email, String password, int role) {
        Map<String, Object> insertMap = new HashMap<>();
        insertMap.put("email",email);
        insertMap.put("password",password);
        insertMap.put("role",role);
        Number key = jdbcInsert.executeAndReturnKey(insertMap);
        //TODO: verify success
        return new User(key.intValue(),email,password,role);
    }

    @Override
    public User updateRole(User user, int role) {
        jdbcTemplate.update("UPDATE users Set role = ? WHERE id = ?", role, user.getId());

        return new User(user.getId(), user.getEmail(), user.getPassword(), role);
    }
}
