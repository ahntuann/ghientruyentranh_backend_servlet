package com.mycompany.testmaven;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DatabaseConnection {
    private String dbUrl;
    private String dbUsername;
    private String dbPassword;

    public DatabaseConnection() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("db.properties")) {
            Properties properties = new Properties();
            if (input == null) {
                System.out.println("Sorry, unable to find db.properties");
                return;
            }
            properties.load(input);

            // Đọc các thuộc tính từ file
            dbUrl = properties.getProperty("db.url");
            dbUsername = properties.getProperty("db.username");
            dbPassword = properties.getProperty("db.password");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Phương thức kết nối đến cơ sở dữ liệu
    public Connection connect() {
        Connection con = null;
        try {
            // Tải driver JDBC
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            System.out.println("Connecting to database: " + dbUrl);
            con = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            System.out.println("Kết nối thành công đến cơ sở dữ liệu.");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver không tìm thấy: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Lỗi kết nối đến cơ sở dữ liệu: " + e.getMessage());
        }
        
        return con; 
    }
}
