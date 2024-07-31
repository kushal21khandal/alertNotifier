package com.alertnotifier.databaseManagement;

import java.sql.DriverManager;
import java.io.File;
import java.sql.*;

public class DatabaseMain {

    /*
     * functions : all static
     *
     * PRIVATE :
     * getDatabasePath
     * getConnection
     *
     *
     * PUBLIC :
     * closeConnection
     * initiateConnection
     * getInstrumentKeys
     * checkOutofPriceRange
     */

    private static Connection conn = null;

    private static String getDatabasePath() {
        File database = new File(DatabaseMain.class.getClassLoader().getResource("scrips.db").getFile());
        System.out.println(database.getAbsolutePath());
        return database.getAbsolutePath();
    }

    private static Connection getConnection(String databasePath) throws ClassNotFoundException, SQLException {
        // Class.forName("org.slf4j.LoggerFactory");
        Class.forName("org.sqlite.JDBC");
        return DriverManager.getConnection("jdbc:sqlite:" + databasePath);
    }

    public static void closeConnection() {
        try {
            if (conn != null) {
                conn.close();
                System.out.println("database connection closed.");
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static void initiateConnection() {
        try {
            if (conn == null) {
                conn = getConnection(getDatabasePath());
                System.out.println("database connection established.");
            }
        } catch (SQLException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    public static String getInstrumentKeys() {
        return DataHandler.getInstrumentKeys(conn);
    }

    public static boolean checkOutOfPriceRange(String ltp, String symbol) {
        // symbol.replace("BSE_EQ:" , "");
        // Double double_ltp = Double.parseDouble(ltp);
        // DataHandler.compare(conn, Integer.parseInt(ltp), symbol);
        return DataHandler.compare(conn, (int) Double.parseDouble(ltp), symbol.substring(7));
    }

    // public static void main(String[] args) {

    // initiateConnection();
    // System.out.println(getInstrumentKeys());
    // checkOutOfPriceRange("200", "NMDC");
    // closeConnection();
    // }
}