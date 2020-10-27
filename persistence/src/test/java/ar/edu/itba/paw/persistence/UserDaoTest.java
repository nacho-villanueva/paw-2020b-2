package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.config.TestConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class UserDaoTest {

    //TABLE NAMES
    private static final String USERS_TABLE_NAME = "users";

    //Test data
    private static final User userTest = new User(0,"test@test.com","testPass",User.UNDEFINED_ROLE_ID,"es-AR");

    //Known users
    private static final User userZero = new User(1,"zero@zero.com","zeroPass",User.PATIENT_ROLE_ID);
    private static final User userOne = new User(2,"one@one.com","onePass",User.MEDIC_ROLE_ID);
    private static final User userSix = new User(7,"six@six.com","sixPass",User.UNDEFINED_ROLE_ID);


    @Autowired
    private DataSource ds;

    @Autowired
    private UserDao dao;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Test
    @Transactional
    @Rollback
    public void testRegisterValid() {
        final User user = dao.register(userTest.getEmail(),userTest.getPassword(),userTest.getRole(),userTest.getLocale());

        Assert.assertNotNull(user);
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,USERS_TABLE_NAME,"email = '" + userTest.getEmail() + "'"));
    }

    @Test(expected = DuplicateKeyException.class)
    @Transactional
    @Rollback
    public void testRegisterDuplicateEmail() {
        dao.register(userZero.getEmail(),userTest.getPassword(),userTest.getRole(),userTest.getLocale());
    }

    @Transactional
    @Rollback
    @Test
    public void testFindByIdExists() {
        Optional<User> maybeUser = dao.findById(userZero.getId());

        Assert.assertTrue(maybeUser.isPresent());
        Assert.assertEquals(userZero.getEmail(),maybeUser.get().getEmail());
        Assert.assertEquals(userZero.getRole(),maybeUser.get().getRole());
    }

    @Transactional
    @Rollback
    @Test
    public void testFindByIdNotExists() {
        Optional<User> maybeUser = dao.findById(userTest.getId());

        Assert.assertFalse(maybeUser.isPresent());
    }

    @Transactional
    @Rollback
    @Test
    public void testUpdateRoleValidUser() {
        User newUser = dao.updateRole(userSix,User.PATIENT_ROLE_ID);

        Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,USERS_TABLE_NAME,"id = " + userSix.getId() + " AND role = " + User.PATIENT_ROLE_ID));
        Assert.assertEquals(User.PATIENT_ROLE_ID,newUser.getRole());
        Assert.assertEquals(userSix.getId(),newUser.getId());
    }

    @Transactional
    @Rollback
    @Test
    public void testUpdateRoleNonExistantUser() {
        User user = dao.updateRole(userTest,User.PATIENT_ROLE_ID);  //Does not fail, it updates nothing

        Assert.assertNull(user);
        Assert.assertEquals(0,JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,USERS_TABLE_NAME,"email = '" + userTest.getEmail() + "'"));
    }
}
