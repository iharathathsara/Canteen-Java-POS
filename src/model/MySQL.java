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
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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

    public static boolean backupDatabase() {
        String userHome = System.getProperty("user.home");
        String downloadFolder = userHome + File.separator + "Downloads";
        // Generate timestamp-based filename
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String backupFilePath = downloadFolder + File.separator + "canteen_db_backup_" + timestamp + ".sql";

        String mysqlBinPath = findMySQLDumpPath();
        if (mysqlBinPath == null) {
            System.err.println("mysqldump not found. Ensure MySQL is installed and added to PATH.");
            return false;
        }

        List<String> command = Arrays.asList(
            mysqlBinPath, "-u" + USERNAME, "-p" + PASSWORD, DATABASE, "-r", backupFilePath
        );

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            int processComplete = process.waitFor();
            return processComplete == 0;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String findMySQLDumpPath() {
        String[] possiblePaths = {
            "C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysqldump.exe",
            "C:\\Program Files (x86)\\MySQL\\MySQL Server 8.0\\bin\\mysqldump.exe",
            "C:\\xampp\\mysql\\bin\\mysqldump.exe", // For XAMPP installations
            "/usr/bin/mysqldump",  // Linux/macOS
            "/usr/local/mysql/bin/mysqldump"
        };

        for (String path : possiblePaths) {
            if (new File(path).exists()) {
                return path;
            }
        }

        return null;
    }
}
