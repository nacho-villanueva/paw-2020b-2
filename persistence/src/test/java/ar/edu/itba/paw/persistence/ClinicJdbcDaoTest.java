package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Clinic;
import ar.edu.itba.paw.persistence.config.TestConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class ClinicJdbcDaoTest {

    private static final String TABLE_NAME = "clinics";
    private static final String NAME = "Zero's Clinic";
    private static final String EMAIL = "clinic@zero.com";
    private static final String TELEPHONE = "+011-00000000";
    private static final int ZERO_ID = 0;

    @Autowired
    private DataSource ds;

    @Autowired
    private ClinicJdbcDao clinicJdbcDao;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns("id");

        JdbcTestUtils.deleteFromTables(jdbcTemplate,TABLE_NAME);
    }

    @Test
    public void testFindByIdExists() {
        int dbkey = insertTestClinic();

        final Optional<Clinic> maybeClinic = clinicJdbcDao.findById(dbkey);

        Assert.assertNotNull(maybeClinic);
        Assert.assertTrue(maybeClinic.isPresent());
        Assert.assertEquals(NAME, maybeClinic.get().getName());
    }

    @Test
    public void testFindByIdNotExists() {
        final Optional<Clinic> maybeClinic = clinicJdbcDao.findById(ZERO_ID);

        Assert.assertNotNull(maybeClinic);
        Assert.assertFalse(maybeClinic.isPresent());
    }

    private int insertTestClinic() {
        Map<String,Object> insertMap = new HashMap<>();
        insertMap.put("name", NAME);
        insertMap.put("email", EMAIL);
        insertMap.put("telephone", TELEPHONE);
        return jdbcInsert.executeAndReturnKey(insertMap).intValue();
    }
}
