package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Medic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class MedicJdbcDao implements MedicDao {

    private static final RowMapper<Medic> MEDIC_ROW_MAPPER = (rs, rowNum) ->
            new Medic(rs.getString("name"),rs.getString("email"),rs.getString("telephone"),rs.getString("licence_number"));

    private static final RowMapper<String> MEDIC_FIELDS_ROW_MAPPER = (rs, rowNum) ->
            rs.getString("name");

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
                "licence_number text not null" +
                ")");

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS medical_fields (" +
                "id serial primary key," +
                "name text not null" +
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
            List<String> knownFields = jdbcTemplate.query("SELECT name FROM medic_medical_fields INNER JOIN medical_fields ON field_id = medical_fields.id AND medic_id = ?", new Object[]{ id }, MEDIC_FIELDS_ROW_MAPPER);

            if(!knownFields.isEmpty()) {
                medic.get().setMedical_fields(knownFields);
            }
        }
        return medic;
    }
}
