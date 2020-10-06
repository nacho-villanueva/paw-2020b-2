package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Clinic;
import ar.edu.itba.paw.model.Medic;
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
            new User(rs.getInt("id"),rs.getString("email"),rs.getString("password"),rs.getInt("role"),rs.getString("locale"));

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Autowired
    private MedicDao mdao;

    @Autowired
    private ClinicDao cdao;

    @Autowired
    public UserJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
    }


    @Override
    public Optional<User> findById(final int id) {
        Optional<User> maybeUser = jdbcTemplate.query("SELECT * FROM users WHERE id = ?", new Object[]{ id }, USER_ROW_MAPPER).stream().findFirst();
        return setVerificationFlags(maybeUser);
    }

    private Optional<User> setVerificationFlags(Optional<User> maybeUser) {
        if (maybeUser.isPresent()) {
            Optional<Medic> maybeMedic = mdao.findByUserId(maybeUser.get().getId());
            Optional<Clinic> maybeClinic = cdao.findByUserId(maybeUser.get().getId());

            maybeMedic.ifPresent(medic -> maybeUser.get().setVerifyingMedic(!medic.isVerified()));

            maybeClinic.ifPresent(clinic -> maybeUser.get().setVerifyingClinic(!clinic.isVerified()));
        }
        return maybeUser;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<User> maybeUser = jdbcTemplate.query("SELECT * FROM users WHERE email = ?", new Object[]{ email }, USER_ROW_MAPPER).stream().findFirst();
        return setVerificationFlags(maybeUser);
    }

    @Override
    public User register(String email, String password, int role, String locale) {
        Map<String, Object> insertMap = new HashMap<>();
        insertMap.put("email",email);
        insertMap.put("password",password);
        insertMap.put("role",role);
        insertMap.put("locale",locale);
        Number key = jdbcInsert.executeAndReturnKey(insertMap);
        //TODO: verify success
        return new User(key.intValue(),email,password,role,locale);
    }

    @Override
    public User updateRole(User user, int role) {
        jdbcTemplate.update("UPDATE users Set role = ? WHERE id = ?", role, user.getId());

        return new User(user.getId(), user.getEmail(), user.getPassword(), role, user.getLocale());
    }

    @Override
    public User updatePassword(User user, String password) {
        jdbcTemplate.update("UPDATE users Set password = ? WHERE id = ?", password, user.getId());

        return new User(user.getId(), user.getEmail(), password, user.getRole(), user.getLocale());
    }
}
