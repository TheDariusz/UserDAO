package pl.coderslab.workshop2.user;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.coderslab.workshop2.user.exception.UserDaoException;
import pl.coderslab.workshop2.user.exception.UserNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserDaoIT {

    private static UserDao userDao;

    @BeforeEach
    public void init() {
        userDao = new UserDao();
        userDao.deleteAll();
    }

    @Test
    void shouldSaveUser() {
        User testUser = createTestUser("test@test.com");
        int status = userDao.save(testUser);

        User userFromDb = userDao.findOne(testUser.getId());

        assertThat(status).isEqualTo(1);
        assertThat(testUser.getId()).isGreaterThan(0L);
        assertThat(userFromDb.getUserName()).isEqualTo("test");
        assertThat(userFromDb.getEmail()).isEqualTo("test@test.com");
        assertThat(userFromDb.getPassword()).isEqualTo("pass");
    }

    @Test
    void shouldNotSaveUserWhenEmailExists() {
        User testUser = createTestUser("test@test.com");
        int status = userDao.save(testUser);
        assertThat(status).isEqualTo(1);

        User userWithExistingEmail = createTestUser("test@test.com");
        assertThrows(UserDaoException.class, () -> userDao.save(userWithExistingEmail));
        assertThat(userWithExistingEmail.getId()).isEqualTo(0L);
    }

    @Test
    void shouldNotSaveUserWhenIdExists() {
        User testUser = createTestUser("test@test.com");
        int status = userDao.save(testUser);
        assertThat(status).isEqualTo(1);

        assertThrows(UserDaoException.class, () -> userDao.save(testUser));
    }

    @Test
    void shouldUpdateUser() {
        User testUser = createTestUser("test@test.com");
        userDao.save(testUser);

        testUser.setUserName("dariusz");
        testUser.setEmail("dariusz@gmail.com");
        testUser.setPassword("somepass");

        int status = userDao.update(testUser);
        assertThat(status).isEqualTo(1);

        User updatedUser = userDao.findOne(testUser.getId());
        assertThat(updatedUser.getUserName()).isEqualTo("dariusz");
        assertThat(updatedUser.getEmail()).isEqualTo("dariusz@gmail.com");
        assertThat(updatedUser.getPassword()).isEqualTo("somepass");
    }

    @Test
    void shouldNotUpdateUserWhenIdNotExists() {
        User testUser = createTestUser("test@test.com");
        assertThrows(UserNotFoundException.class, () -> userDao.update(testUser));
    }

    @Test
    void shouldNotUpdateUserWhenEmailExists() {
        User testUser = createTestUser("test@test.com");
        User dariusz = createTestUser("dariusz@test.com");
        userDao.save(testUser);
        userDao.save(dariusz);

        dariusz.setEmail("test@test.com");
        dariusz.setUserName("test");

        assertThrows(UserDaoException.class, () -> userDao.update(dariusz));
    }

    @Test
    void shouldDeleteUser() {
        User testUser = createTestUser("test@test.com");
        userDao.save(testUser);

        int status = userDao.delete(testUser);
        assertThat(status).isEqualTo(1);

        Long deletedId = testUser.getId();
        assertThrows(UserNotFoundException.class, () -> userDao.findOne(deletedId));
    }

    @Test
    void shouldFindAllUsers() {
        User[] empty = userDao.findAll();
        assertThat(empty).isEmpty();

        for (int i = 1; i <= 5; i++) {
            User testUser = createTestUser("test-" + i + "@test.com");
            userDao.save(testUser);
        }
        User[] users = userDao.findAll();
        assertThat(users)
                .isNotEmpty()
                .hasSize(5);
    }

    private User createTestUser(String email) {
        return new User("test", email, "pass");
    }
}