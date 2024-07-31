package com.alertnotifier.databaseManagement;

import java.io.InputStream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;


class DataHandler {

    /*
     * functions: static
     *
     *
     * PACKAGE ACCESS : 
     * insert
     * insertBulk
     * compare : is under range
     * getInstrumentKeys : this may be the source of duplicacy of instrument keys if
     * there happens some duplicacy in instrument keys.
     */

    static String getInstrumentKeys(Connection conn) {
        String res = "";
        String query = "SELECT instrument_key FROM alerts";
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            res += resultSet.getString("instrument_key"); // here may be some duplicacy happend when gettting all the
                                                          // instrument keys
            while (resultSet.next()) {
                res = res + "," + resultSet.getString("instrument_key");
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return res;
    }

    static boolean compare(Connection conn, int ltp, String symbol) {
        boolean isOutOfBounds = false;

        String query = "SELECT priceBelow,priceAbove,remarks FROM alerts WHERE symbol =?";
        int priceBelow, priceAbove;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, symbol);
            ResultSet resultSet = preparedStatement.executeQuery();

            priceBelow = resultSet.getInt("priceBelow");
            priceAbove = resultSet.getInt("priceAbove");

            /*
             * if (ltp < priceBelow || ltp > priceAbove) {
             * System.out.println(symbol + "\t\t" + ltp + "\t" + priceBelow + "\t" +
             * priceAbove + "\t" + resultSet.getString("remarks"));
             * }
             */
            if (ltp < priceBelow) {
                System.out.println(symbol + "\t\t" + ltp + "\t" + priceBelow + "\t" + priceAbove + "\t"
                        + resultSet.getString("remarks"));
                isOutOfBounds = true;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return isOutOfBounds;
    }

    static void insertBulk(Connection conn, InputStream inputStream) throws SQLException, IOException {
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedInputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedInputStreamReader = new BufferedReader(inputStreamReader);

            while (true) {
                insert(conn, bufferedInputStreamReader.readLine().trim());
            }

        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            if (inputStreamReader != null) {
                inputStreamReader = null;
            }
            if (bufferedInputStreamReader != null) {
                bufferedInputStreamReader.close();
            }
        }

    }

    static void insert(Connection conn, String symbol) throws SQLException {
        /*
         * for single symbol insertion into the database
         */
        String query = "INSERT INTO alerts(symbol,priceBelow,priceAbove,remarks) VALUES (?,?,?,?)";
        int priceBelow, priceAbove;
        String remarks;
        InputStreamReader inputStreamReader = null;

        try {
            inputStreamReader = new InputStreamReader(System.in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            System.out.println("price Below : ");
            priceBelow = Integer.parseInt(bufferedReader.readLine().trim());
            System.out.println("price Above : ");
            priceAbove = Integer.parseInt(bufferedReader.readLine().trim());
            System.out.println("remarks : ");
            remarks = bufferedReader.readLine().trim();

            // instrument_key = fetchInstrumentKey(conn, symbol);

            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, symbol);
            // preparedStatement.setString(2, instrument_key);
            preparedStatement.setInt(2, priceBelow);
            preparedStatement.setInt(3, priceAbove);
            preparedStatement.setString(5, remarks);
            preparedStatement.executeUpdate();

            bufferedReader.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            if (inputStreamReader != null) {
                inputStreamReader = null;
            }
        }

    }

}
