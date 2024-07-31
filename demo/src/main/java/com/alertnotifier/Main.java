package com.alertnotifier;

import com.alertnotifier.databaseManagement.DatabaseMain;
import com.alertnotifier.jsonResponse.ResponseJsonMain;
import com.alertnotifier.jsonResponse.Symbol;

import kong.unirest.core.Unirest;
import kong.unirest.core.*;
import java.io.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {

    /*
     * functions : static
     *
     * PRIVATE :
     * getAccessToken
     *
     * PUBLIC :
     * run
     */

    private static String getAccessToken() {
        InputStreamReader inputStreamReader = null;
        String access_token = "";
        try {
            inputStreamReader = new InputStreamReader(System.in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            System.out.println("access_token:");
            access_token = bufferedReader.readLine().trim();

            bufferedReader.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            if (inputStreamReader != null) {
                inputStreamReader = null;
            }
        }
        return access_token;
    }

    public static void run() {


        String instrumentKeys = DatabaseMain.getInstrumentKeys();

        HttpResponse<String> response;
        String jsonString;
        ResponseJsonMain responseJsonMain = new ResponseJsonMain();
        ObjectMapper objectMapper = new ObjectMapper();

        String access_token = getAccessToken();

        System.out.println("symbol\t\t\tltp\tpriceBelow\tpriceAbove\tremarks");


        // timer.scheduleAtFixedRate(new TimerTask() {

        //     @Override
        //     public void run() {
        //         response = Unirest.get("https://api.upstox.com/v2/market-quote/ltp")
        //                 .header("Accept", "application/json")
        //                 .header("Authorization",
        //                         "Bearer " + access_token)
        //                 .queryString("instrument_key", instrumentKeys).asString();

        //         jsonString = response.getBody();
        //         try {
        //             responseJsonMain = objectMapper.readValue(jsonString, ResponseJsonMain.class);

        //             assert responseJsonMain.status == "success";

        //             for (Symbol s : responseJsonMain.data.symbols) {
        //                 // if (DatabaseHandler.isOutOfPriceRange(s.price.instrument_token,
        //                 // s.price.last_price) == true) {
        //                 // System.out.println(s.symbolName);
        //                 // }DatabaseHandler.isOutOfPriceRange(s.price.instrument_token,
        //                 // s.price.last_price) == true
        //                 if (DatabaseMain.checkOutOfPriceRange(s.price.last_price, s.symbolName) == true) {
        //                     instrumentKeys = instrumentKeys.replaceFirst(
        //                             "," + s.price.instrument_token + "|" + s.price.instrument_token + ",", "");
        //                     /*
        //                      * the instrument token is in the format
        //                      * -> ,instrument_token,instrument_token,instrument_token
        //                      * made some changes but still not sure if it is going to work or not .
        //                      */
        //                 }
        //             }

        //             responseJsonMain.data.symbols.clear();
        //         } catch (JsonProcessingException exception) {
        //             exception.printStackTrace();
        //         }

        //     }
        // }, 0, 0);

        while (true) {
            response = Unirest.get("https://api.upstox.com/v2/market-quote/ltp")
                    .header("Accept", "application/json")
                    .header("Authorization",
                            "Bearer " + access_token)
                    .queryString("instrument_key", instrumentKeys).asString();

            jsonString = response.getBody();
            try {
                responseJsonMain = objectMapper.readValue(jsonString, ResponseJsonMain.class);

                assert responseJsonMain.status == "success";

                for (Symbol s : responseJsonMain.data.symbols) {
                    // if (DatabaseHandler.isOutOfPriceRange(s.price.instrument_token,
                    // s.price.last_price) == true) {
                    // System.out.println(s.symbolName);
                    // }DatabaseHandler.isOutOfPriceRange(s.price.instrument_token,
                    // s.price.last_price) == true
                    if (DatabaseMain.checkOutOfPriceRange(s.price.last_price, s.symbolName) == true) {
                        instrumentKeys = instrumentKeys.replaceFirst(
                                "," + s.price.instrument_token + "|" + s.price.instrument_token + ",", "");
                        /*
                         * the instrument token is in the format
                         * -> ,instrument_token,instrument_token,instrument_token
                         * made some changes but still not sure if it is going to work or not .
                         */
                    }
                }

                responseJsonMain.data.symbols.clear();

                Thread.sleep(10000);
            }
            catch (JsonProcessingException | InterruptedException exception) {
                exception.printStackTrace();
            }


        }

    }

    public static void main(String[] args) {

        try {
            DatabaseMain.initiateConnection();
            run();
        } finally {
            DatabaseMain.closeConnection();
        }

    }
}
