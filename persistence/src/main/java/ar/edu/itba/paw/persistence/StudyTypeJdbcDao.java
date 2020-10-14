package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.StudyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class StudyTypeJdbcDao implements StudyTypeDao{

    private static final RowMapper<StudyType> STUDY_TYPE_ROW_MAPPER = (rs,rowNum) ->
            new StudyType(rs.getInt("id"), rs.getString("name"));

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Autowired
    public StudyTypeJdbcDao(DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName("medical_studies")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Optional<StudyType> findById(int id) {
        return jdbcTemplate.query("SELECT * FROM medical_studies WHERE id = ?", new Object[]{id}, STUDY_TYPE_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public Optional<StudyType> findByName(String name) {
        return jdbcTemplate.query("SELECT * FROM medical_studies WHERE lower(name) = lower(?)", new Object[]{name}, STUDY_TYPE_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public Collection<StudyType> getAll() {
        return jdbcTemplate.query("SELECT * FROM medical_studies",STUDY_TYPE_ROW_MAPPER);
    }

    @Override
    public Collection<StudyType> findByClinicId(int clinic_id) {
        return jdbcTemplate.
                query("SELECT name, medical_studies.id as id FROM clinic_available_studies INNER JOIN medical_studies ON study_id = medical_studies.id AND clinic_id = ?",
                        new Object[]{clinic_id}, STUDY_TYPE_ROW_MAPPER);
    }

    @Override
    public StudyType findOrRegister(String name) {
        Optional<StudyType> studyType = this.findByName(name);

        if(studyType.isPresent()) {
            return studyType.get();
        }

        return this.register(name.substring(0, 1).toUpperCase() + name.substring(1));
    }

    private StudyType register(final String name) {
        Map<String, Object> insertMap = new HashMap<>();
        insertMap.put("name",name);

        Number key = jdbcInsert.executeAndReturnKey(insertMap);
        //TODO: Check success
        return new StudyType(key.intValue(),name);
    }
}
