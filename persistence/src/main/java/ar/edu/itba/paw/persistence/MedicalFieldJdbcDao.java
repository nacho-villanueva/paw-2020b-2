package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.MedicalField;
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


public class MedicalFieldJdbcDao implements MedicalFieldDao {

    private static final RowMapper<MedicalField> MEDICAL_FIELD_ROW_MAPPER = (rs, rowNum) ->
            new MedicalField(rs.getInt("id"),rs.getString("name"));

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Autowired
    public MedicalFieldJdbcDao(DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName("medical_fields")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Optional<MedicalField> findById(int id) {
        return jdbcTemplate.query("SELECT * FROM medical_fields WHERE id = ?", new Object[]{ id }, MEDICAL_FIELD_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public Optional<MedicalField> findByName(String name) {
        return jdbcTemplate.query("SELECT * FROM medical_fields WHERE lower(name) = lower(?)", new Object[]{name}, MEDICAL_FIELD_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public Collection<MedicalField> getAll() {
        return jdbcTemplate.query("SELECT * FROM medical_fields", MEDICAL_FIELD_ROW_MAPPER);
    }

    @Override
    public Collection<MedicalField> findByMedicId(int medic_id) {
        return jdbcTemplate.query("SELECT name, medical_fields.id as id FROM medic_medical_fields INNER JOIN medical_fields ON field_id = medical_fields.id AND medic_id = ?",
                new Object[]{medic_id},MEDICAL_FIELD_ROW_MAPPER);
    }

    @Override
    public MedicalField findOrRegister(String name) {
        Optional<MedicalField> medicalField = this.findByName(name);

        if(medicalField.isPresent()) {
            return medicalField.get();
        }

        return this.register(name.substring(0, 1).toUpperCase() + name.substring(1));
    }

    private MedicalField register(final String name) {
        Map<String,Object> insertMap = new HashMap<>();
        insertMap.put("name", name);

        Number key = jdbcInsert.executeAndReturnKey(insertMap);
        //TODO: Check success
        return new MedicalField(key.intValue(), name);
    }
}
