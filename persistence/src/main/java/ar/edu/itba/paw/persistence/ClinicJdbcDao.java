package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Clinic;
import ar.edu.itba.paw.model.StudyType;
import ar.edu.itba.paw.model.User;
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
            new Clinic(rs.getInt("user_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("telephone"),
                    rs.getBoolean("verified"));

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsertClinic;
    private SimpleJdbcInsert jdbcInsertStudies;

    @Autowired
    private StudyTypeDao studyTypeDao;

    @Autowired
    public ClinicJdbcDao(DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsertClinic = new SimpleJdbcInsert(ds)
                .withTableName("clinics");
        jdbcInsertStudies = new SimpleJdbcInsert(ds)
                .withTableName("clinic_available_studies");
    }

    @Override
    public Optional<Clinic> findByUserId(int user_id) {
        //We get the basic clinic info
        Optional<Clinic> clinic = jdbcTemplate.query("SELECT * FROM clinics INNER JOIN users u ON user_id = u.id WHERE u.id = ?", new Object[] { user_id }, CLINIC_ROW_MAPPER).stream().findFirst();
        clinic.ifPresent(value -> value.setMedical_studies(studyTypeDao.findByClinicId(user_id)));
        return clinic;
    }

    @Override
    public Collection<Clinic> getAll() {
        return getAll(true);
    }

    @Override
    public Collection<Clinic> getAllUnverified() {
        return getAll(false);
    }

    private Collection<Clinic> getAll(final boolean verified) {
        Collection<Clinic> clinics = jdbcTemplate.query("SELECT * FROM clinics INNER JOIN users u ON user_id = u.id WHERE verified = ?", new Object[]{verified}, CLINIC_ROW_MAPPER);
        clinics.forEach(clinic -> {
            clinic.setMedical_studies(studyTypeDao.findByClinicId(clinic.getUser_id()));
        });
        return clinics;
    }

    @Override
    public Collection<Clinic> getByStudyTypeId(final int studyType_id) {
        Collection<Clinic> clinics = jdbcTemplate.query("SELECT c.user_id, c.name, u.email, telephone, verified FROM clinics c" +
                " INNER JOIN users u" +
                " ON c.user_id = u.id" +
                " INNER JOIN clinic_available_studies cs " +
                " ON clinic_id = c.user_id" +
                " INNER JOIN medical_studies s" +
                " ON study_id = s.id AND s.id = ? WHERE c.verified = true", new Object[]{studyType_id}, CLINIC_ROW_MAPPER);

        clinics.forEach(clinic -> {
            clinic.setMedical_studies(studyTypeDao.findByClinicId(clinic.getUser_id()));
        });
        return clinics;
    }

    @Override
    public Clinic register(final User user, final String name, final String telephone, final Collection<StudyType> available_studies) {
        Map<String, Object> insertMap = new HashMap<>();
        insertMap.put("user_id", user.getId());
        insertMap.put("name", name);
        insertMap.put("email", user.getEmail());
        insertMap.put("telephone", telephone);
        insertMap.put("verified", false);

        jdbcInsertClinic.execute(insertMap);
        //Todo: Check success
        Collection<StudyType> available_studiesDB = new ArrayList<>();

        //Adds studies that dont exist in our database //TODO: Think of more elegant solution, prone to duplication under misspellings
        available_studies.forEach(studyType -> {
            StudyType studyTypeFromDB = this.registerStudyToClinic(user.getId(), studyType);
            available_studiesDB.add(studyTypeFromDB);
        });

        return new Clinic(user.getId(),name,user.getEmail(),telephone,available_studiesDB,false);
    }

    @Override
    public StudyType registerStudyToClinic(final int clinic_id, final StudyType studyType) {
        //We check if it exists
        StudyType studyTypeFromDB = studyTypeDao.findOrRegister(studyType.getName());

        //Now that we made sure it exist, we add the relation
        Map<String, Object> insertMap = new HashMap<>();
        insertMap.put("clinic_id", clinic_id);
        insertMap.put("study_id", studyTypeFromDB.getId());

        jdbcInsertStudies.execute(insertMap);

        //Todo: verify success

        return studyTypeFromDB;
    }
}
