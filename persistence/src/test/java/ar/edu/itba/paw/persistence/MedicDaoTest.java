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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class MedicDaoTest {

    //TABLE NAMES
    private static final String MEDICS_TABLE_NAME = "medics";
    private static final String MEDIC_FIELDS_TABLE_NAME = "medic_medical_fields";
    private static final String FIELDS_TABLE_NAME = "medical_fields";

    //Test data
    private static final User userSix = new User(7,"six@six.com","passSix",User.UNDEFINED_ROLE_ID);
    private static final User userOne = new User(2,"one@one.com","passOne",User.MEDIC_ROLE_ID);
    private static final MedicalField fieldOne = new MedicalField(1,"Neurology");
    private static final MedicalField fieldTwo = new MedicalField(2,"Cardiology");
    private static final MedicalField fieldThree = new MedicalField(3,"Endocrinology");
    private static final MedicalField fieldFour = new MedicalField(4,"Pulmonology");
    private static final MedicalField fieldTest = new MedicalField("Field test");
    private static final Medic medicTest = new Medic(userSix,"Test name","12345","image/png",new byte[1],"idkidc",false);

    //Known medics
    private static final Medic medicOne = new Medic(userOne,"Medic one","","image/png",new byte[1],"1234567",true);

    @Autowired
    private DataSource ds;

    @Autowired
    private MedicDao dao;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Test
    @Transactional
    @Rollback
    public void testFindById() {
        Optional<Medic> maybeMedic = dao.findByUserId(medicOne.getUser().getId());

        Assert.assertNotNull(maybeMedic);
        Assert.assertTrue(maybeMedic.isPresent());
        Assert.assertEquals(medicOne.getName(),maybeMedic.get().getName());
    }

    @Test
    @Transactional
    @Rollback
    public void testGetAll() {
        Collection<Medic> medics = dao.getAll();

        Assert.assertNotNull(medics);
        Assert.assertEquals(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,MEDICS_TABLE_NAME,"verified = true"),medics.size());
    }

    @Test
    @Transactional
    @Rollback
    public void testGetAllUnverified() {
        Collection<Medic> medics = dao.getAllUnverified();

        Assert.assertNotNull(medics);
        Assert.assertEquals(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,MEDICS_TABLE_NAME,"verified = false"),medics.size());
    }

    @Test
    @Transactional
    @Rollback
    public void testRegisterValid() {
        Collection<MedicalField> known_fields = new HashSet<>();
        known_fields.add(fieldOne);
        known_fields.add(fieldTwo);
        known_fields.add(fieldThree);

        Medic medic = dao.register(medicTest.getUser(),medicTest.getName(),medicTest.getTelephone(),medicTest.getIdentification_type(),medicTest.getIdentification(),medicTest.getLicence_number(),known_fields,medicTest.isVerified());

        Assert.assertNotNull(medic);
        Assert.assertEquals(known_fields.size(),medic.getMedical_fields().size());
        Assert.assertEquals(known_fields.size(),JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,MEDIC_FIELDS_TABLE_NAME,"medic_id = " + medicTest.getUser().getId()));
        Assert.assertEquals(1,JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,MEDICS_TABLE_NAME,"user_id = " + medicTest.getUser().getId()));
    }

    @Test
    @Transactional
    @Rollback
    public void testRegisterNonExistentField() {
        Collection<MedicalField> known_fields = new HashSet<>();
        known_fields.add(fieldOne);
        known_fields.add(fieldTwo);
        known_fields.add(fieldTest);

        Medic medic = dao.register(medicTest.getUser(),medicTest.getName(),medicTest.getTelephone(),medicTest.getIdentification_type(),medicTest.getIdentification(),medicTest.getLicence_number(),known_fields,medicTest.isVerified());

        Assert.assertNotNull(medic);
        Assert.assertEquals(known_fields.size(),medic.getMedical_fields().size());
        Assert.assertEquals(known_fields.size(),JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,MEDIC_FIELDS_TABLE_NAME,"medic_id = " + medicTest.getUser().getId()));
        Assert.assertEquals(1,JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,MEDICS_TABLE_NAME,"user_id = " + medicTest.getUser().getId()));
        Assert.assertEquals(1,JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,FIELDS_TABLE_NAME,"lower(name) = lower('" + fieldTest.getName() + "')"));
    }

    @Test
    @Transactional
    @Rollback
    public void testUpdateMedicInfoMedicExists() {
        Collection<MedicalField> known_fields = new HashSet<>();
        known_fields.add(fieldOne);
        known_fields.add(fieldTwo);
        known_fields.add(fieldThree);

        Medic medic = dao.updateMedicInfo(medicOne.getUser(),medicTest.getName(),medicTest.getTelephone(),medicTest.getIdentification_type(),medicTest.getIdentification(),medicTest.getLicence_number(),known_fields,medicTest.isVerified());

        Assert.assertNotNull(medic);
        Assert.assertEquals(1,JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,MEDICS_TABLE_NAME,"user_id = " + medicOne.getUser().getId() + " AND name = '" + medicTest.getName() + "'"));
        Assert.assertEquals(1,JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,MEDIC_FIELDS_TABLE_NAME,"medic_id = " + medicOne.getUser().getId() + " AND field_id = " + fieldThree.getId()));
        Assert.assertEquals(0,JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,MEDIC_FIELDS_TABLE_NAME,"medic_id = " + medicOne.getUser().getId() + " AND field_id = " + fieldFour.getId()));
        Assert.assertEquals(1,JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,FIELDS_TABLE_NAME,"id = " + fieldFour.getId()));
    }

    @Test
    @Transactional
    @Rollback
    public void testUpdateMedicInfoMedicNotExists() {
        Collection<MedicalField> known_fields = new HashSet<>();
        known_fields.add(fieldOne);
        known_fields.add(fieldTwo);
        known_fields.add(fieldThree);

        Medic medic = dao.updateMedicInfo(medicTest.getUser(),medicTest.getName(),medicTest.getTelephone(),medicTest.getIdentification_type(),medicTest.getIdentification(),medicTest.getLicence_number(),known_fields,medicTest.isVerified());

        Assert.assertNull(medic);
        Assert.assertEquals(0,JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,MEDICS_TABLE_NAME,"user_id = " + medicTest.getUser().getId()));
    }

    @Test
    @Transactional
    @Rollback
    public void testKnowsFieldValid() {
        boolean ret = dao.knowsField(medicOne.getUser().getId(),fieldFour.getId());

        Assert.assertTrue(ret);
    }

    @Test
    @Transactional
    @Rollback
    public void testKnowsFieldInvalid() {
        boolean ret = dao.knowsField(medicTest.getUser().getId(),fieldThree.getId());

        Assert.assertFalse(ret);
    }

    @Test
    @Transactional
    @Rollback
    public void testRegisterFieldToMedicValid() {
        MedicalField mf = dao.registerFieldToMedic(medicOne.getUser().getId(),fieldThree);

        Assert.assertNotNull(mf);
        Assert.assertEquals(1,JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,MEDIC_FIELDS_TABLE_NAME,"medic_id = " + medicOne.getUser().getId() + " AND field_id = " + fieldThree.getId()));
    }

    @Test
    @Transactional
    @Rollback
    public void testRegisterFieldToMedicValidNewField() {
        MedicalField mf = dao.registerFieldToMedic(medicOne.getUser().getId(),fieldTest);

        Assert.assertNotNull(mf);
        Assert.assertEquals(1,JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,MEDIC_FIELDS_TABLE_NAME,"medic_id = " + medicOne.getUser().getId() + " AND field_id = " + mf.getId()));
    }

    @Test
    @Transactional
    @Rollback
    public void testRegisterFieldToMedicNoMedic() {
        MedicalField mf = dao.registerFieldToMedic(medicTest.getUser().getId(),fieldThree);

        Assert.assertNull(mf);
        Assert.assertEquals(0,JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,MEDIC_FIELDS_TABLE_NAME,"medic_id = " + medicTest.getUser().getId() + " AND field_id = " + fieldThree.getId()));
    }
}
