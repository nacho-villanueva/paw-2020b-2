package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Clinic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class ClinicJdbcDao implements ClinicDao {

    private static final RowMapper<Clinic> CLINIC_ROW_MAPPER = (rs, rowNum) ->
            new Clinic(rs.getString("name"),rs.getString("email"),rs.getString("telephone"));

    private static final RowMapper<String> CLINIC_STUDIES_ROW_MAPPER = (rs, rowNum) ->
            rs.getString("name");

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Autowired
    public ClinicJdbcDao(DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName("clinics")
                .usingGeneratedKeyColumns("id");

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS clinics (" +
                "id serial primary key," +
                "name text not null," +
                "email text not null," +
                "telephone text" +
                ")");

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS medical_studies (" +
                "id serial primary key," +
                "name text not null" +
                ")");

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS clinic_available_studies (" +
                "clinic_id int not null," +
                "study_id int not null," +
                "primary key(clinic_id,study_id)," +
                "foreign key(clinic_id) references clinics," +
                "foreign key(study_id) references medical_studies" +
                ")");
    }

    @Override
    public Optional<Clinic> findById(long id) {
        //We get the basic clinic info
        Optional<Clinic> clinic = jdbcTemplate.query("SELECT * FROM clinics WHERE id = ?", new Object[] { id }, CLINIC_ROW_MAPPER).stream().findFirst();
        if(clinic.isPresent()) {
            List<String> availableStudies = jdbcTemplate.query("SELECT name FROM clinic_available_studies INNER JOIN medical_studies ON study_id = medical_studies.id AND clinic_id = ?", new Object[] { id },CLINIC_STUDIES_ROW_MAPPER);

            if(!availableStudies.isEmpty()) {
                clinic.get().setMedical_studies(availableStudies);
            }
        }
        return clinic;
    }
}
