package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;


import javax.sql.DataSource;
import java.sql.Date;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class ResultJdbcDao implements ResultDao {

    private static final RowMapper<Result> RESULT_ROW_MAPPER = (rs, rowNum) ->
            new Result(rs.getLong("id"),
                    rs.getLong("order_id"),
                    rs.getDate("date"),
                    rs.getString("responsible_name"),
                    rs.getString("responsible_licence_number"),
                    rs.getString("identification_type"),
                    rs.getBytes("identification"),
                    rs.getString("result_data_type"),
                    rs.getBytes("result_data"));

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

    @Override
    public Result register(final long order_id, final String result_data_type, final byte[] result_data, final String identification_type, final byte[] identification, final Date date, final String responsible_name, final String responsible_licence_number) {
        Map<String, Object> insertMap = new HashMap<>();
        insertMap.put("order_id", order_id);
        insertMap.put("result_data_type", result_data_type);
        insertMap.put("result_data", result_data);
        insertMap.put("identification_type", identification_type);
        insertMap.put("identification", identification);
        insertMap.put("date", date);
        insertMap.put("responsible_name", responsible_name);
        insertMap.put("responsible_licence_number", responsible_licence_number);

        Number key = jdbcInsert.executeAndReturnKey(insertMap);

        //Todo: Verify success

        return new Result(key.longValue(),order_id,date,responsible_name,responsible_licence_number,identification_type,identification,result_data_type,result_data);
    }
}
