package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.*;
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
            new Medic(rs.getInt("user_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("telephone"),
                    rs.getString("identification_type"),
                    rs.getBytes("identification"),
                    rs.getString("licence_number"),
                    rs.getBoolean("verified"));

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsertMedic;
    private SimpleJdbcInsert jdbcInsertField;

    @Autowired
    private MedicalFieldDao medicalFieldDao;

    @Autowired
    public MedicJdbcDao(DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsertMedic = new SimpleJdbcInsert(ds)
                .withTableName("medics");
        jdbcInsertField = new SimpleJdbcInsert(ds)
                .withTableName("medic_medical_fields");
    }

    @Override
    public Optional<Medic> findByUserId(int user_id) {
        Optional<Medic> medic = jdbcTemplate.query("SELECT * FROM medics WHERE user_id = ?", new Object[] { user_id }, MEDIC_ROW_MAPPER).stream().findFirst();
        medic.ifPresent(value -> value.setMedical_fields(medicalFieldDao.findByMedicId(user_id)));
        return medic;
    }

    @Override
    public Collection<Medic> getAll() {
        return getAll(true);
    }

    @Override
    public Collection<Medic> getAllUnverified() {
        return getAll(false);
    }

    private Collection<Medic> getAll(final boolean verified) {
        Collection<Medic> medics = jdbcTemplate.query("SELECT * FROM medics WHERE verified = ?", new Object[]{verified}, MEDIC_ROW_MAPPER);
        medics.forEach(medic -> {
            medic.setMedical_fields(medicalFieldDao.findByMedicId(medic.getUser_id()));
        });
        return medics;
    }

    @Override
    public Medic register(final User user, final String name, final String email, final String telephone, final String identification_type, final byte[] identification, final String licence_number, final Collection<MedicalField> known_fields) {
        Map<String, Object> insertMap = new HashMap<>();
        insertMap.put("user_id", user.getId());
        insertMap.put("name", name);
        insertMap.put("email", email);
        insertMap.put("telephone", telephone);
        insertMap.put("identification_type", identification_type);
        insertMap.put("identification", identification);
        insertMap.put("licence_number", licence_number);
        insertMap.put("verified", false);

        jdbcInsertMedic.execute(insertMap);
        //TODO: verify success
        Collection<MedicalField> known_fieldsDB = registerFieldsToMedic(known_fields,user.getId());

        return new Medic(user.getId(),name,email,telephone,identification_type,identification,licence_number,false,known_fieldsDB);
    }

    @Override
    public Medic updateMedicInfo(final int medic_id, final String name, final String email, final String telephone, final String identification_type, final byte[] identification, final String licence_number, final Collection<MedicalField> known_fields, final boolean verified) {
        jdbcTemplate.update("UPDATE medics Set name = ?, email = ?, telephone = ?, identification_type = ?, identification = ?, licence_number = ? WHERE user_id = ?", name, email, telephone, identification_type,  identification, licence_number, medic_id);

        Collection<MedicalField> known_fieldsDB = registerFieldsToMedic(known_fields,medic_id);

        return new Medic(medic_id,name,email,telephone,identification_type,identification,licence_number,verified,known_fieldsDB);
    }

    @Override
    public boolean knowsField(int medic_id, int field_id) {
        Integer cnt = jdbcTemplate.queryForObject("SELECT count(*) FROM medic_medical_fields WHERE medic_id = ? AND field_id = ?", Integer.class, medic_id, field_id);

        return cnt != null && cnt > 0;
    }

    @Override
    public MedicalField registerFieldToMedic(final int medic_id, MedicalField medicalField) {
        //We check if it exists
        MedicalField medicalFieldFromDB = medicalFieldDao.findOrRegister(medicalField.getName());

        if(!knowsField(medic_id,medicalFieldFromDB.getId())) {
            //Now that we made sure it exist and it is not registered already, we add the relation
            Map<String, Object> insertMap = new HashMap<>();
            insertMap.put("medic_id", medic_id);
            insertMap.put("field_id", medicalFieldFromDB.getId());

            jdbcInsertField.execute(insertMap);
        }

        //Todo: Verify success

        return medicalFieldFromDB;
    }

    private Collection<MedicalField> registerFieldsToMedic(final Collection<MedicalField> known_fields, final int medic_id) {
        Collection<MedicalField> known_fieldsDB = new ArrayList<>();

        known_fields.forEach(medicalField -> {
            MedicalField medicalFieldDB = this.registerFieldToMedic(medic_id, medicalField);
            known_fieldsDB.add(medicalFieldDB);
        });

        return known_fieldsDB;
    }
}
