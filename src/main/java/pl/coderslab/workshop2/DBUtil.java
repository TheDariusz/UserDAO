package pl.coderslab.workshop2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

  public static final String DB_SCHEMA = "workshop2";
  public static final String DB_URL = "jdbc:mysql://localhost:3306/" + DB_SCHEMA + "?useSSL=false&characterEncoding=utf8";
  public static final String DB_USER = "root";
  public static final String DB_PASSWORD = "coderslab";

  private DBUtil() {
    throw new IllegalStateException("Utility class!");
  }

  public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
  }

}
