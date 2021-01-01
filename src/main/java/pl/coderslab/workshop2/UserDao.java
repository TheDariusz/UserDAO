package pl.coderslab.workshop2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.coderslab.bcrypt.BCrypt;

public class UserDao {
  private static final String CREATE_USER_QUERY =
      "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";
  private static final String UPDATE_USER_QUERY =
      "UPDATE users SET username=?, email=?, password=? WHERE id=?";
  private static final String CHECK_EMAIL_QUERY =
      "SELECT users.id FROM users WHERE email=? and id<>?";
  private static final String SELECT_USER_ID_QUERY = "SELECT * FROM users WHERE id=?";
  private static final String SELECT_USER_EMAIL_QUERY = "SELECT * FROM users WHERE email=?";
  private static final String DELETE_USER_ID_QUERY = "DELETE FROM users WHERE id=?;";
  private static final String SELECT_ALL_QUERY = "SELECT * FROM users";
  private static final Logger logger = LogManager.getLogger(UserDao.class);

  public User create(User user) {
    if (user == null) {
      return null;
    }

    try {
      if (emailAlreadyExists(user)) {
        throw new EmailNotFoundException("User with email " + user.getEmail() + " already exists!");
      }
    } catch (EmailNotFoundException e) {
      logger.error(e);
      return null;
    }

    try (Connection conn = DbUtil.getConnection();
         PreparedStatement stmt =
            conn.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS)) {
      setCreateStatement(user, stmt);
      stmt.executeUpdate();
      ResultSet rs = stmt.getGeneratedKeys();
      if (rs.next()) {
        user.setId(rs.getInt(1));
      }
      logger.info("User {id: {}, email: {}} created in DB!", user.getId(), user.getEmail());
      return user;
    } catch (SQLException e) {
      logger.error("Create user query failed!", e);
      return null;
    }
  }

  public void update(User user) {
    try {
      if (emailAlreadyExists(user)) {
        throw new EmailNotFoundException("User with email " + user.getEmail() + " already exists!");
      }
    } catch (EmailNotFoundException e) {
      logger.error(e);
      return;
    }

    try (Connection conn = DbUtil.getConnection();
         PreparedStatement stmt = conn.prepareStatement(UPDATE_USER_QUERY)) {
      setUpdateStatement(user, stmt);
      stmt.executeUpdate();
      logger.info("User {id: {}, email: {}} updated in DB!", user.getId(), user.getEmail());
    } catch (SQLException e) {
      logger.error("Update query failed!", e);
    }
  }

  public User read(int userId) {
    if (userId <= 0) {
      logger.info("User id should be greater than 0!");
      return null;
    }

    try (Connection conn = DbUtil.getConnection();
         PreparedStatement stmt = conn.prepareStatement(SELECT_USER_ID_QUERY)) {
      stmt.setInt(1, userId);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        return getUser(rs);
      } else {
        logger.info("User with id:{} does not exists in DB!", userId);
      }
    } catch (SQLException e) {
      logger.error("User select query failed!", e);
    }
    return null;
  }

  public User read(String userEmail) {
    try (Connection conn = DbUtil.getConnection();
         PreparedStatement stmt = conn.prepareStatement(SELECT_USER_EMAIL_QUERY)) {
      stmt.setString(1, userEmail);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        return getUser(rs);
      } else {
        logger.info("User with email:{} does not exists in DB!", userEmail);
      }
    } catch (SQLException e) {
      logger.error("User select query failed!", e);
    }
    return null;
  }

  public void delete(int userId) {
    if (userId <= 0) {
      logger.info("User id should be greater than 0!");
      return;
    }
    try (Connection conn = DbUtil.getConnection();
         PreparedStatement stmt = conn.prepareStatement(DELETE_USER_ID_QUERY)) {

      stmt.setInt(1, userId);
      boolean isOneRow = stmt.executeUpdate() == 1;
      if (isOneRow) {
        logger.info("User with id:{} was deleted in DB!", userId);
      } else {
        logger.info("User with id:{} was not found in DB!", userId);
      }
    } catch (SQLException e) {
      logger.error("Delete user query failed!", e);
    }
  }

  public User[] findALl() {
    User[] users = new User[0];
    try (Connection conn = DbUtil.getConnection();
         PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_QUERY)) {

      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        User user = getUser(rs);
        users = addToArray(user, users);
      }
    } catch (SQLException e) {
      logger.error("Select all users query failed!", e);
    }
    return users;
  }

  private boolean emailAlreadyExists(User user) throws EmailNotFoundException {
    try (Connection conn = DbUtil.getConnection();
         PreparedStatement stmt = conn.prepareStatement(CHECK_EMAIL_QUERY)) {
      setCheckEmailStatement(user.getId(), user.getEmail(), stmt);
      ResultSet rs = stmt.executeQuery();
      return rs.next();
    } catch (SQLException e) {
      throw new EmailNotFoundException("Check email query failed!");
    }
  }

  private String hashPassword(String password) {
    return BCrypt.hashpw(password, BCrypt.gensalt());
  }

  private User[] addToArray(User u, User[] users) {
    User[] tmpUsers = Arrays.copyOf(users, users.length + 1);
    tmpUsers[users.length] = u;
    return tmpUsers;
  }

  private User getUser(ResultSet rs) throws SQLException {
    User user =
        new User(
            rs.getString(UserTable.USERNAME_COL),
            rs.getString(UserTable.EMAIL_COL),
            rs.getString(UserTable.PASSWORD_COL));
    user.setId(rs.getInt("id"));
    return user;
  }

  private void setCheckEmailStatement(int id, String email, PreparedStatement stmt)
          throws SQLException {
    stmt.setString(1, email);
    stmt.setInt(2, id);
  }

  private void setUpdateStatement(User user, PreparedStatement stmt) throws SQLException {
    stmt.setString(1, user.getUserName());
    stmt.setString(2, user.getEmail());
    stmt.setString(3, hashPassword(user.getPassword()));
    stmt.setInt(4, user.getId());
  }

  private void setCreateStatement(User user, PreparedStatement stmt) throws SQLException {
    stmt.setString(1, user.getUserName());
    stmt.setString(2, user.getEmail());
    stmt.setString(3, hashPassword(user.getPassword()));
  }

  static class UserTable {
    private UserTable() {
      throw new IllegalStateException("Inner class!");
    }

    public static final String ID_COL = "id";
    public static final String USERNAME_COL = "username";
    public static final String EMAIL_COL = "email";
    public static final String PASSWORD_COL = "password";
  }

  public class EmailNotFoundException extends Exception {
    public EmailNotFoundException(String message) {
      super(message);
    }
  }
}
