package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;


import javax.sql.DataSource;
import java.util.Collection;
import java.util.Optional;

@Repository
public class ResultJdbcDao implements ResultDao {

    private static final RowMapper<Result> RESULT_ROW_MAPPER = (rs, rowNum) ->
            new Result(rs.getDate("date"),rs.getString("responsible_name"),rs.getString("responsible_licence_number"),rs.getBytes("identification"),rs.getBytes("result_data"));

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Autowired
    public ResultJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName("results")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Optional<Result> findById(long id) {
        return jdbcTemplate.query("SELECT * FROM results WHERE id = ?", new Object[]{ id }, RESULT_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public Collection<Result> findByOrderId(long id) {
        return jdbcTemplate.query("SELECT * FROM results WHERE order_id = ?", new Object[]{ id }, RESULT_ROW_MAPPER);
    }
}
