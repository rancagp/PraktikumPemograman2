package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/pp2_membership";
    private static final String DB_USER = "root"; 
    private static final String DB_PASS = "";

    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                System.out.println("Koneksi ke database berhasil!");
            } catch (SQLException e) {
                System.out.println("Koneksi ke database gagal!");
                e.printStackTrace();
            }
        }
        return connection;
    }
}