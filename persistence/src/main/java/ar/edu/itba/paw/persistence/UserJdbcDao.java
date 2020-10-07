package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Clinic;
import ar.edu.itba.paw.model.Medic;
import ar.edu.itba.paw.model.Patient;
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
    private MedicDao mdao;

    @Autowired
    private ClinicDao cdao;

    @Autowired
    private PatientDao pdao;

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

        maybeUser.ifPresent(this::setFlags);

        return maybeUser;
    }

    private void setFlags(User user) {

        switch (user.getRole()) {
            case User.CLINIC_ROLE_ID:
                Optional<Clinic> maybeClinic = cdao.findByUserId(user.getId());
                user.setRegistered(maybeClinic.isPresent());
                maybeClinic.ifPresent(clinic -> user.setVerifying(!clinic.isVerified()));
                break;
            case User.MEDIC_ROLE_ID:
                Optional<Medic> maybeMedic = mdao.findByUserId(user.getId());
                user.setRegistered(maybeMedic.isPresent());
                maybeMedic.ifPresent(medic -> user.setVerifying(!medic.isVerified()));
                break;
            case User.PATIENT_ROLE_ID:
                Optional<Patient> maybePatient = pdao.findByUserId(user.getId());
                user.setRegistered(maybePatient.isPresent());
                break;
            case User.UNDEFINED_ROLE_ID:
                user.setRegistered(false);
                user.setVerifying(false);
                break;
            case User.ADMIN_ROLE_ID:
            default:
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<User> maybeUser = jdbcTemplate.query("SELECT * FROM users WHERE email = ?", new Object[]{ email }, USER_ROW_MAPPER).stream().findFirst();
        maybeUser.ifPresent(this::setFlags);
        return maybeUser;
    }

    @Override
    public User register(String email, String password, int role) {
        Map<String, Object> insertMap = new HashMap<>();
        insertMap.put("email",email);
        insertMap.put("password",password);
        insertMap.put("role",role);
        Number key = jdbcInsert.executeAndReturnKey(insertMap);
        //TODO: verify success
        return new User(key.intValue(),email,password,role,false,false);        //It was just created, its has yet to apply and therefore not verifying
    }

    @Override
    public User updateRole(User user, int role){
        jdbcTemplate.update("UPDATE users Set role = ? WHERE id = ?", role, user.getId());
        return new User(user.getId(), user.getEmail(), user.getPassword(), role);
    }

    @Override
    public User updatePassword(User user, String password) {
        jdbcTemplate.update("UPDATE users Set password = ? WHERE id = ?", password, user.getId());

        return new User(user.getId(), user.getEmail(), password, user.getRole());
    }
}
