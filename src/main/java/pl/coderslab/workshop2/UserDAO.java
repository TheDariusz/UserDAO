package pl.coderslab.workshop2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.coderslab.bcrypt.BCrypt;

public class UserDAO {
  private static final String CREATE_USER_QUERY =
      "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";
  private static final String UPDATE_USER_QUERY =
      "UPDATE users SET username=?, email=?, password=? WHERE id=?";
  private static final String CHECK_EMAIL_QUERY =
      "SELECT users.id FROM users WHERE email=? and id<>?";
  private static final String SELECT_USER_ID_QUERY = "SELECT * FROM users WHERE id=?";
  private static final String SELECT_USER_EMAIL_QUERY = "SELECT * FROM users WHERE email=?";
  private static final String DELETE_USER_ID_QUERY = "DELETE FROM users WHERE id=?;";
  private static Logger logger = LogManager.getLogger(UserDAO.class);

  public User create(User user) {
    if (user == null) {
      return null;
    }
    if (emailAlreadyExists(user.getId(), user.getEmail())) {
      logger.error("User with email {} already exists!", user.getEmail());
      return null;
    }

    try (Connection conn = DBUtil.getConnection();
        PreparedStatement stmt =
            conn.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS)) {
      stmt.setString(1, user.getUserName());
      stmt.setString(2, user.getEmail());
      stmt.setString(3, hashPassword(user.getPassword()));
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
    if (emailAlreadyExists(user.getId(), user.getEmail())) {
      logger.warn("User with email {} already exists!", user.getEmail());
      return;
    }
    try (Connection conn = DBUtil.getConnection();
        PreparedStatement stmt = conn.prepareStatement(UPDATE_USER_QUERY)) {
      stmt.setString(1, user.getUserName());
      stmt.setString(2, user.getEmail());
      stmt.setString(3, hashPassword(user.getPassword()));
      stmt.setInt(4, user.getId());
      logger.info("User {id: {}, email: {}} updated in DB!", user.getId(), user.getEmail());
      stmt.executeUpdate();
    } catch (SQLException e) {
      logger.error("Update query failed!", e);
    }
  }

  public User read(int userId) {
    try (Connection conn = DBUtil.getConnection();
        PreparedStatement stmt = conn.prepareStatement(SELECT_USER_ID_QUERY)) {
      stmt.setInt(1, userId);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        User user =
            new User(rs.getString("username"), rs.getString("email"), rs.getString("password"));
        user.setId(rs.getInt("id"));
        return user;
      }
    } catch (SQLException e) {
      logger.error("User select query failed!", e);
    }
    return null;
  }

  public User read(String userEmail) {
    try (Connection conn = DBUtil.getConnection();
        PreparedStatement stmt = conn.prepareStatement(SELECT_USER_EMAIL_QUERY)) {
      stmt.setString(1, userEmail);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        User user =
            new User(rs.getString("username"), rs.getString("email"), rs.getString("password"));
        user.setId(rs.getInt("id"));
        return user;
      }
    } catch (SQLException e) {
      logger.error("User select query failed!", e);
    }
    return null;
  }

  public void delete(int userId) {
    try (Connection conn = DBUtil.getConnection();
        PreparedStatement stmt = conn.prepareStatement(DELETE_USER_ID_QUERY)) {
      stmt.setInt(1, userId);
      int result = stmt.executeUpdate();
      if (result == 1) {
        logger.info("User with id:{} was deleted in DB!", userId);
      } else {
        logger.info("User with id:{} was not found in DB!", userId);
      }
    } catch (SQLException e) {
      logger.error("Delete user query failed!", e);
    }
  }

  private boolean emailAlreadyExists(int id, String email) {
    try (Connection conn = DBUtil.getConnection();
        PreparedStatement stmnt = conn.prepareStatement(CHECK_EMAIL_QUERY)) {
      stmnt.setString(1, email);
      stmnt.setInt(2, id);
      ResultSet rs = stmnt.executeQuery();
      if (rs.next()) {
        return true;
      }
    } catch (SQLException e) {
      logger.error("Check email query failed!", e);
    }
    return false;
  }

  private String hashPassword(String password) {
    return BCrypt.hashpw(password, BCrypt.gensalt());
  }
}
