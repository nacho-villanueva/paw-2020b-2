package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.config.TestConfig;
import org.hibernate.SessionFactory;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
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
    private static final User userZero = new User(1,"zero@zero.com","passZero",User.PATIENT_ROLE_ID);
    private static final User userSix = new User(7,"six@six.com","passSix",User.UNDEFINED_ROLE_ID);

    @Autowired
    private DataSource ds;

    @Autowired
    private UserDao dao;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource() {
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

    @Test(expected = PersistenceException.class)
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
    public void testFindByEmailExists() {
        Optional<User> maybeUser = dao.findByEmail(userZero.getEmail());

        Assert.assertTrue(maybeUser.isPresent());
        Assert.assertEquals(userZero.getEmail(),maybeUser.get().getEmail());
        Assert.assertEquals(userZero.getRole(),maybeUser.get().getRole());
    }

    @Transactional
    @Rollback
    @Test
    public void testFindByEmailNotExists() {
        Optional<User> maybeUser = dao.findByEmail(userTest.getEmail());

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
    public void testUpdateRoleNonExistentUser() {
        User user = dao.updateRole(userTest,User.PATIENT_ROLE_ID);  //Does not fail, it updates nothing

        Assert.assertNull(user);
        Assert.assertEquals(0,JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,USERS_TABLE_NAME,"email = '" + userTest.getEmail() + "'"));
    }

    @Transactional
    @Rollback
    @Test
    public void testUpdatePasswordValidUser() {
        User newUser = dao.updatePassword(userSix,userTest.getPassword());

        Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,USERS_TABLE_NAME,"id = " + userSix.getId() + " AND password = '" + userTest.getPassword() + "'"));
        Assert.assertEquals(userTest.getPassword(),newUser.getPassword());
        Assert.assertEquals(userSix.getId(),newUser.getId());
    }

    @Transactional
    @Rollback
    @Test
    public void testUpdatePasswordNonExistentUser() {
        User user = dao.updatePassword(userTest,userTest.getPassword());  //Does not fail, it updates nothing

        Assert.assertNull(user);
        Assert.assertEquals(0,JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,USERS_TABLE_NAME,"email = '" + userTest.getEmail() + "'"));
    }

    @Transactional
    @Rollback
    @Test
    public void testUpdateEmailValidUser() {
        User newUser = dao.updateEmail(userSix,userTest.getEmail());

        Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,USERS_TABLE_NAME,"id = " + userSix.getId() + " AND email = '" + userTest.getEmail() + "'"));
        Assert.assertEquals(userSix.getRole(),newUser.getRole());
        Assert.assertEquals(userSix.getId(),newUser.getId());
    }

    @Transactional
    @Rollback
    @Test
    public void testUpdateEmailNonExistentUser() {
        User user = dao.updateEmail(userTest,userTest.getEmail());  //Does not fail, it updates nothing

        Assert.assertNull(user);
        Assert.assertEquals(0,JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,USERS_TABLE_NAME,"email = '" + userTest.getEmail() + "'"));
    }
}
