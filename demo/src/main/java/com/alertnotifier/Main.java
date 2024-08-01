
package com.alertnotifier;

import java.util.regex.*;
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

        // ResponseJsonMain responseJsonMain = new ResponseJsonMain();
        // ObjectMapper objectMapper = new ObjectMapper();

        // Pattern pattern = Pattern.compile("(BSE:\\w+)\\D+(\\d+)\\D+(IN\\d+)");
        Pattern pattern = Pattern.compile("(BSE_EQ:\\w+)\\D+(\\d+.?\\d+)\\D+(INE(\\d+|\\w+)+)");
        Matcher matcher;
        String symbol , ltp , instrument_token;

        String access_token = getAccessToken();
        System.out.println("symbol\t\t\tltp\tpriceBelow\tpriceAbove\tremarks");

        while (true) {
            response = Unirest.get("https://api.upstox.com/v2/market-quote/ltp")
                    .header("Accept", "application/json")
                    .header("Authorization",
                            "Bearer " + access_token)
                    .queryString("instrument_key", instrumentKeys).asString();

            jsonString = response.getBody();
            try {

                matcher = pattern.matcher(jsonString);
                symbol = matcher.group(1);
                ltp = matcher.group(2);
                instrument_token = matcher.group(3);

                while(matcher.find()){
                    if(DatabaseMain.checkOutOfPriceRange(ltp , symbol)){
                        instrumentKeys = instrumentKeys.replaceFirst("," + instrument_token + "|" + instrument_token + "," , "");
                    }
                }

                Thread.sleep(10000);
            } catch (InterruptedException exception) {
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
