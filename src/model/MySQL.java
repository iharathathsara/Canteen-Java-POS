/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Ihara
 */
public class MySQL {
    private static Connection connection;
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private static final String DATABASE = "canteen_db";

    private static Connection createConnection() throws Exception {
        if (connection == null) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DATABASE, USERNAME, PASSWORD);
        }
        return connection;
    }

    public static void iud(String query, Object... params) {
        try (PreparedStatement pstmt = createConnection().prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ResultSet search(String query, Object... params) throws Exception {
        PreparedStatement pstmt = createConnection().prepareStatement(query);
        for (int i = 0; i < params.length; i++) {
            pstmt.setObject(i + 1, params[i]);
        }
        return pstmt.executeQuery();
    }

    public static Connection getConnection() {
        try {
            if (connection == null) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DATABASE, USERNAME, PASSWORD);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
    
    public static boolean backupDatabase() {
        // Get the Downloads folder path
        String userHome = System.getProperty("user.home");
        String downloadFolder = userHome + File.separator + "Downloads";
        String backupFilePath = downloadFolder + File.separator + "canteen_db_backup.sql";

        // Construct the mysqldump command
        String command = String.format("mysqldump -u%s -p%s %s -r %s", USERNAME, PASSWORD, DATABASE, backupFilePath);

        try {
            // Execute the command
            Process process = Runtime.getRuntime().exec(command);

            // Wait for the process to complete
            int processComplete = process.waitFor();

            if (processComplete == 0) {
                return true;
            } else {
                return false;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
}
