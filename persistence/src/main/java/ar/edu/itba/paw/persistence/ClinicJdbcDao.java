package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Clinic;
import ar.edu.itba.paw.model.StudyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class ClinicJdbcDao implements ClinicDao {

    private static final RowMapper<Clinic> CLINIC_ROW_MAPPER = (rs, rowNum) ->
            new Clinic(rs.getInt("id"),rs.getString("name"),rs.getString("email"),rs.getString("telephone"));

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsertClinic;
    private SimpleJdbcInsert jdbcInsertStudies;

    @Autowired
    StudyTypeDao studyTypeDao;

    @Autowired
    public ClinicJdbcDao(DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsertClinic = new SimpleJdbcInsert(ds)
                .withTableName("clinics")
                .usingGeneratedKeyColumns("id");
        jdbcInsertStudies = new SimpleJdbcInsert(ds)
                .withTableName("clinic_available_studies");

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS clinics (" +
                "id serial primary key," +
                "name text not null," +
                "email text not null," +
                "telephone text," +
                "unique(email)" +
                ")");

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS medical_studies (" +
                "id serial primary key," +
                "name text not null," +
                "unique(name)" +
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
    public Optional<Clinic> findById(int id) {
        //We get the basic clinic info
        Optional<Clinic> clinic = jdbcTemplate.query("SELECT * FROM clinics WHERE id = ?", new Object[] { id }, CLINIC_ROW_MAPPER).stream().findFirst();
        if(clinic.isPresent()) {
            clinic.get().setMedical_studies(studyTypeDao.findByClinicId(id));
        }
        return clinic;
    }

    @Override
    public Collection<Clinic> getAll() {
        Collection<Clinic> clinics = jdbcTemplate.query("SELECT * FROM clinics", CLINIC_ROW_MAPPER);
        clinics.forEach(clinic -> {
            clinic.setMedical_studies(studyTypeDao.findByClinicId(clinic.getId()));
        });
        return clinics;
    }

    @Override
    public Clinic register(final String name, final String email, final String telephone, final Collection<StudyType> available_studies) {
        Map<String,Object> insertMap = new HashMap<>();
        insertMap.put("name", name);
        insertMap.put("email", email);
        insertMap.put("telephone", telephone);

        Number key = jdbcInsertClinic.executeAndReturnKey(insertMap);

        //Todo: check success and register new studies types to clinic
        Collection<StudyType> actual_available_studies = new ArrayList<>();

        //Goes through the list of available studies, adds them to the clinic_available_studies and if there are new study types it adds them to db
        available_studies.forEach(studyType -> {
            StudyType actualStudyType = this.registerStudyToClinic(key.intValue(), studyType);
            actual_available_studies.add(actualStudyType);
        });

        return new Clinic(key.intValue(),name,email,telephone,actual_available_studies);
    }

    @Override
    public StudyType registerStudyToClinic(final int clinic_id, StudyType studyType) {
        //We check if it exists
        StudyType studyTypeFromDB = studyTypeDao.findOrRegister(studyType.getName());

        //Now that we made sure it exist, we add the relation
        Map<String, Object> insertMap = new HashMap<>();
        insertMap.put("clinic_id", clinic_id);
        insertMap.put("study_id", studyTypeFromDB.getId());

        int rowsAffected = jdbcInsertStudies.execute(insertMap);

        return studyTypeFromDB;
    }
}
