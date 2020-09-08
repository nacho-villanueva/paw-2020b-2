package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Patient;
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

    private static final RowMapper<Patient> PATIENT_ROW_MAPPER = (rs, rowNum) ->
            new Patient(rs.getInt("id"),rs.getString("email"),rs.getString("name"));

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Autowired
    public PatientJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName("patients")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Optional<Patient> findById(int id) {
        return jdbcTemplate.query("SELECT * FROM patients WHERE id = ?", new Object[] { id }, PATIENT_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public Optional<Patient> findByEmail(String email) {
        return jdbcTemplate.query("SELECT * FROM patients WHERE email = ?", new Object[]{email}, PATIENT_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public Patient findOrRegister(String email, String name) {
        Optional<Patient> patient = this.findByEmail(email);

        if(patient.isPresent()) {
            return patient.get();
        }

        return this.register(email, name);
    }

    @Override
    public Patient register(String email, String name) {
        Map<String, Object> insertMap = new HashMap<>();
        insertMap.put("email",email);
        insertMap.put("name",name);
        Number key = jdbcInsert.executeAndReturnKey(insertMap);
        //TODO: Check success
        return new Patient(key.intValue(), email, name);
    }
}
