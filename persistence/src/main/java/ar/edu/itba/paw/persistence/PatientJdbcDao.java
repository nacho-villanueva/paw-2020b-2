package ar.edu.itba.paw.persistence;

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
public class PatientJdbcDao implements PatientDao {

    @Autowired
    private UserDao userDao;

    private static final RowMapper<Patient> PATIENT_ROW_MAPPER = (rs, rowNum) ->
            new Patient(rs.getInt("user_id"),
                    rs.getString("email"),
                    rs.getString("name"),
                    rs.getString("medic_plan"),
                    rs.getString("medic_plan_number"));

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Autowired
    public PatientJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName("patients");
    }

    @Override
    public Optional<Patient> findByUserId(final int user_id) {
        return jdbcTemplate.query("SELECT * FROM patients p INNER JOIN users u ON p.user_id = u.id WHERE u.id = ?", new Object[] { user_id }, PATIENT_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public Optional<Patient> findByEmail(final String email) {
        return jdbcTemplate.query("SELECT * FROM patients p INNER JOIN users u ON p.user_id = u.id WHERE u.email = ?", new Object[]{ email }, PATIENT_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public Patient register(final User user, final String name) {
        Map<String,Object> insertMap = new HashMap<>();
        insertMap.put("user_id", user.getId());
        insertMap.put("name", name);

        jdbcInsert.execute(insertMap);

        return new Patient(user.getId(),user.getEmail(),name);
    }

    @Override
    public Patient register(final User user, final String name, final String medic_plan, final String medic_plan_number) {
        Map<String,Object> insertMap = new HashMap<>();
        insertMap.put("user_id", user.getId());
        insertMap.put("name", name);
        insertMap.put("medic_plan", medic_plan);
        insertMap.put("medic_plan_number", medic_plan_number);

        jdbcInsert.execute(insertMap);

        userDao.updateRole(user, User.PATIENT_ROLE_ID);

        return new Patient(user.getId(),user.getEmail(),name,medic_plan,medic_plan_number);
    }

    @Override
    public Patient updateMedicPlan(final Patient patient, final String medic_plan, final String medic_plan_number) {
        jdbcTemplate.update("UPDATE patients Set medic_plan = ?, medic_plan_number = ? WHERE user_id = ?", medic_plan, medic_plan_number, patient.getUser_id());

        return new Patient(patient.getUser_id(),patient.getEmail(),patient.getName(),medic_plan, medic_plan_number);
    }
}
