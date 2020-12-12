package pl.coderslab.workshop2;

import pl.coderslab.bcrypt.BCrypt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDAO {
    private static final String CREATE_USER_QUERY =
            "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";
    private static final String SELECT_USER_QUERY =
            "SELECT * FROM users WHERE id=?";
    private static final String UPDATE_USER_QUERY =
            "UPDATE users SET username=?, email=?, password=? WHERE id=?";
    private static final String CHECK_EMAIL_QUERY =
            "SELECT users.id FROM users WHERE email=?";
    private static final String CHECK_EMAIL_WITH_DIFF_ID_QUERY =
            "SELECT users.id FROM users WHERE email=? and id<>?";

    public User create(User user) {
        if (user==null){
            return null;
        }
        if (emailAlreadyExists(user.getEmail())) {
            System.out.println("User with email " + user.getEmail() + " already exists!");
            return null;
        }

        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement stmt =
                    conn.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, user.getUserName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, hashPassword(user.getPassword()));
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                user.setId(rs.getInt(1));
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean emailAlreadyExists(String email) {
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement stmnt = conn.prepareStatement(CHECK_EMAIL_QUERY);
            stmnt.setString(1, email);
            ResultSet rs = stmnt.executeQuery();
            if (rs.next()){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    private boolean emailAlreadyExists(int id, String email) {
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement stmnt = conn.prepareStatement(CHECK_EMAIL_WITH_DIFF_ID_QUERY);
            stmnt.setString(1, email);
            stmnt.setInt(2, id);
            ResultSet rs = stmnt.executeQuery();
            if (rs.next()){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void update(User user) {
        if (emailAlreadyExists(user.getId(), user.getEmail())) {
            System.out.println("User with email " + user.getEmail() + " already exists!");
            System.out.println("record was not updated!");
            return;
        }

        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(UPDATE_USER_QUERY);
            stmt.setString(1, user.getUserName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, hashPassword(user.getPassword()));
            stmt.setInt(4, user.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

}



