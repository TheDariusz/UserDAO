package pl.coderslab.workshop2;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    public static void main(String[] args) {
        String query = "SELECT * FROM users";
        ResultSet rs = null;

        try (Connection conn = DBUtil.getConnection()){
            rs = DBUtil.getRows(conn, query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DBUtil.showRows(rs);
    }
}
