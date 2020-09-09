package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Medic;
import ar.edu.itba.paw.model.MedicalField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class MedicJdbcDao implements MedicDao {

    private static final RowMapper<Medic> MEDIC_ROW_MAPPER = (rs, rowNum) ->
            new Medic(rs.getInt("id"),rs.getString("name"),rs.getString("email"),rs.getString("telephone"),rs.getString("licence_number"));

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsertMedic;
    private SimpleJdbcInsert jdbcInsertField;

    @Autowired
    MedicalFieldDao medicalFieldDao;

    @Autowired
    public MedicJdbcDao(DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsertMedic = new SimpleJdbcInsert(ds)
                .withTableName("medics")
                .usingGeneratedKeyColumns("id");
        jdbcInsertField = new SimpleJdbcInsert(ds)
                .withTableName("medic_medical_fields");
    }

    @Override
    public Optional<Medic> findById(int id) {
        Optional<Medic> medic = jdbcTemplate.query("SELECT * FROM medics WHERE id = ?", new Object[] { id }, MEDIC_ROW_MAPPER).stream().findFirst();
        if(medic.isPresent()) {
            medic.get().setMedical_fields(medicalFieldDao.findByMedicId(id));
        }
        return medic;
    }

    @Override
    public Collection<Medic> getAll() {
        Collection<Medic> medics = jdbcTemplate.query("SELECT * FROM medics", MEDIC_ROW_MAPPER);
        medics.forEach(medic -> {
            medic.setMedical_fields(medicalFieldDao.findByMedicId(medic.getId()));
        });
        return medics;
    }

    @Override
    public Medic register(final String name, final String email, final String telephone, final String licence_number, final Collection<MedicalField> known_fields) {
        Map<String, Object> insertMap = new HashMap<>();
        insertMap.put("name", name);
        insertMap.put("email", email);
        insertMap.put("telephone", telephone);
        insertMap.put("licence_number", licence_number);

        Number key = jdbcInsertMedic.executeAndReturnKey(insertMap);

        //Todo: verify success and register medical fields if needed
        Collection<MedicalField> actual_known_fields = new ArrayList<>();

        known_fields.forEach(medicalField -> {
            MedicalField actualMedicField = this.registerFieldToMedic(key.intValue(),medicalField);
            actual_known_fields.add(actualMedicField);
        });

        return new Medic(key.intValue(),name, email, telephone, licence_number, actual_known_fields);
    }

    @Override
    public MedicalField registerFieldToMedic(final int medic_id, MedicalField medicalField) {
        //We check if it exists
        MedicalField medicalFieldFromDB = medicalFieldDao.findOrRegister(medicalField.getName());

        //Now that we made sure it exist, we add the relation
        Map<String, Object> insertMap = new HashMap<>();
        insertMap.put("medic_id", medic_id);
        insertMap.put("field_id", medicalFieldFromDB.getId());

        int rowsAffected = jdbcInsertField.execute(insertMap);

        return medicalFieldFromDB;
    }
}
