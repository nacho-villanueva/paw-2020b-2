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

    private static final RowMapper<MedicalField> MEDIC_FIELDS_ROW_MAPPER = (rs, rowNum) ->
            new MedicalField(rs.getInt("id"),rs.getString("name"));

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Autowired
    public MedicJdbcDao(DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName("medics")
                .usingGeneratedKeyColumns("id");

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS medics (" +
                "id serial primary key," +
                "name text not null," +
                "email text not null," +
                "telephone text," +
                "licence_number text not null," +
                "unique(email)" +
                ")");

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS medical_fields (" +
                "id serial primary key," +
                "name text not null," +
                "unique(name)" +
                ")");

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS medic_medical_fields (" +
                "medic_id int not null," +
                "field_id int not null," +
                "primary key(medic_id, field_id)," +
                "foreign key(medic_id) references medics," +
                "foreign key(field_id) references medical_fields" +
                ")");
    }

    @Override
    public Optional<Medic> findById(long id) {
        Optional<Medic> medic = jdbcTemplate.query("SELECT * FROM medics WHERE id = ?", new Object[] { id }, MEDIC_ROW_MAPPER).stream().findFirst();
        if(medic.isPresent()) {
            List<MedicalField> knownFields = jdbcTemplate.query("SELECT name, medical_fields.id as id FROM medic_medical_fields INNER JOIN medical_fields ON field_id = medical_fields.id AND medic_id = ?", new Object[]{ id }, MEDIC_FIELDS_ROW_MAPPER);

            if(!knownFields.isEmpty()) {
                medic.get().setMedical_fields(knownFields);
            }
        }
        return medic;
    }

    @Override
    public Medic register(final String name, final String email, final String telephone, final String licence_number, final Collection<MedicalField> known_fields) {
        Map<String, Object> insertMap = new HashMap<>();
        insertMap.put("name", name);
        insertMap.put("email", email);
        insertMap.put("telephone", telephone);
        insertMap.put("licence_number", licence_number);

        Number key = jdbcInsert.executeAndReturnKey(insertMap);

        //Todo: verify success and register medical fields if needed
        return new Medic(key.intValue(),name, email, telephone, licence_number, known_fields);
    }
}
