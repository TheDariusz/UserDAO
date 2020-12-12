package pl.coderslab.workshop2;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    public static void main(String[] args) {
        String query = "SELECT * FROM users";

        try (Connection conn = DBUtil.getConnection()){
            DBUtil.printAllData(conn, query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
