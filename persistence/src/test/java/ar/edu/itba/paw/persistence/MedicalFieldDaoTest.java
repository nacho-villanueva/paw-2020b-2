package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Medic;
import ar.edu.itba.paw.models.MedicalField;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.config.TestConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class MedicalFieldDaoTest {

    //Table name
    private static final String MEDICAL_FIELDS_TABLE_NAME = "medical_fields";

    //Known fields
    private static final MedicalField fieldOne = new MedicalField(1,"Neurology");

    //Test data
    private static final int ZERO_ID = 0;
    private static final MedicalField fieldTest = new MedicalField(ZERO_ID,"Test field");


    @Autowired
    private DataSource ds;

    @Autowired
    private MedicalFieldDao dao;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Test
    @Transactional
    @Rollback
    public void testFindByIdExists() {
        Optional<MedicalField> mf = dao.findById(fieldOne.getId());

        Assert.assertTrue(mf.isPresent());
        Assert.assertEquals(fieldOne.getName(),mf.get().getName());
    }

    @Test
    @Transactional
    @Rollback
    public void testFindByIdNotExists() {
        Optional<MedicalField> mf = dao.findById(fieldTest.getId());

        Assert.assertFalse(mf.isPresent());
    }

    @Test
    @Transactional
    @Rollback
    public void testFindByNameExists() {
        Optional<MedicalField> mf = dao.findByName(fieldOne.getName().toUpperCase());

        Assert.assertTrue(mf.isPresent());
        Assert.assertEquals(fieldOne.getName(),mf.get().getName());
    }

    @Test
    @Transactional
    @Rollback
    public void testFindByNameNotExists() {
        Optional<MedicalField> mf = dao.findByName(fieldTest.getName().toUpperCase());

        Assert.assertFalse(mf.isPresent());
    }

    @Test
    @Transactional
    @Rollback
    public void testGetAll() {
        Collection<MedicalField> mfs = dao.getAll();

        Assert.assertEquals(JdbcTestUtils.countRowsInTable(jdbcTemplate,MEDICAL_FIELDS_TABLE_NAME),mfs.size());
        Optional<MedicalField> maybeOne = mfs.stream().filter(mf -> mf.getName().equals(fieldOne.getName())).findFirst();
        Assert.assertTrue(maybeOne.isPresent());
    }

    @Test
    @Transactional
    @Rollback
    public void testFindOrRegisterNewField() {
        MedicalField mf = dao.findOrRegister(fieldTest.getName().toUpperCase());

        Assert.assertNotNull(mf);
        Assert.assertEquals(1,JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,MEDICAL_FIELDS_TABLE_NAME,"lower(name) = lower('" + fieldTest.getName() + "')"));
    }

    @Test
    @Transactional
    @Rollback
    public void testFindOrRegisterOldField() {
        MedicalField mf = dao.findOrRegister(fieldOne.getName().toUpperCase());

        Assert.assertNotNull(mf);
        Assert.assertEquals(1,JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,MEDICAL_FIELDS_TABLE_NAME,"lower(name) = lower('" + fieldOne.getName() + "')"));
    }
}
