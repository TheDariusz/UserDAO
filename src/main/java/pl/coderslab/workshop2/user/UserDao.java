package pl.coderslab.workshop2.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.coderslab.workshop2.DBUtil;
import pl.coderslab.workshop2.user.exception.UserDaoException;
import pl.coderslab.workshop2.user.exception.UserNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static pl.coderslab.workshop2.user.UserDao.UserTable.EMAIL_COL;
import static pl.coderslab.workshop2.user.UserDao.UserTable.ID_COL;
import static pl.coderslab.workshop2.user.UserDao.UserTable.PASSWORD_COL;
import static pl.coderslab.workshop2.user.UserDao.UserTable.USERNAME_COL;

public class UserDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

    class UserTable {
        public static final String ID_COL = "id";
        public static final String USERNAME_COL = "username";
        public static final String EMAIL_COL = "email";
        public static final String PASSWORD_COL = "password";
    }

    public int save(User user) {
        if (isExisting(user)) {
            throw new UserDaoException("User already exists");
        }
        String insertUser = "INSERT INTO user(username, email, password) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertUser, RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getUserName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, hashPassword(user.getPassword()));
            int status = stmt.executeUpdate();
            setUserId(user, stmt);
            logger.info("User {} created in DB!", user);
            return status;
        } catch (SQLException e) {
            throw new UserDaoException("Create query failed", e);
        }
    }

    private void setUserId(User user, PreparedStatement stmt) {
        try (ResultSet rs = stmt.getGeneratedKeys()) {
            if (rs.next()) {
                user.setId(rs.getLong(1));
            }
        } catch (Exception e) {
            throw new UserDaoException("Failed to set userId for user " + user.getUserName(), e);
        }
    }

    public int update(User user) {
        if (!isExisting(user)) {
            throw new UserNotFoundException(user.getId());
        }
        String updateSql = "UPDATE user SET username=?, email=?, password=? WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateSql)) {
            stmt.setString(1, user.getUserName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, hashPassword(user.getPassword()));
            stmt.setLong(4, user.getId());
            int status = stmt.executeUpdate();
            logger.info("User updated successfully {}", user);
            return status;
        } catch (SQLException e) {
            throw new UserDaoException("Update query failed", e);
        }
    }

    private boolean isExisting(User user) {
        String sql = "SELECT * from user where id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, user.getId());
            try(ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new UserDaoException("Select query failed", e);
        }
    }

    public User findOne(Long userId) {
        String sql = "SELECT * FROM user WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            try(ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return userFromResultSet(rs);
                } else {
                    throw new UserNotFoundException(userId);
                }
            }
        } catch (SQLException e) {
            throw new UserDaoException("Select query failed", e);
        }
    }

    public User[] findAll() {
        User[] users = new User[0];
        String sql = "SELECT * FROM user";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                User user = userFromResultSet(rs);
                users = addToArray(user, users);
            }
            return users;
        } catch (SQLException e) {
            throw new UserDaoException("Select query failed", e);
        }
    }

    private User userFromResultSet(ResultSet rs) throws SQLException {
        long id = rs.getLong(ID_COL);
        String username = rs.getString(USERNAME_COL);
        String email = rs.getString(EMAIL_COL);
        String password = rs.getString(PASSWORD_COL);
        return new User(id, username, email, password);
    }

    public void deleteAll() {
        String deleteUsers = "DELETE FROM user where 1=1";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(deleteUsers)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new UserDaoException("Delete query failed", e);
        }
    }

    public int delete(User user) {
        if (!isExisting(user)) {
            throw new UserNotFoundException(user.getId());
        }
        String deleteUser = "DELETE FROM user where id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(deleteUser)) {
            stmt.setLong(1, user.getId());
            int status = stmt.executeUpdate();
            logger.info("User deleted successfully {}", user);
            return status;
        } catch (SQLException e) {
            throw new UserDaoException("Delete query failed", e);
        }
    }

    private String hashPassword(String password) {
        //todo: hash password properly
        return password;
    }

    private User[] addToArray(User user, User[] users) {
        User[] tmpUsers = Arrays.copyOf(users, users.length + 1);
        tmpUsers[users.length] = user;
        return tmpUsers;
    }

}
