package com.alertnotifier.extras;


import com.alertnotifier.jsonResponse.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class jsonTesting {
    // private static final Logger logging = Logger.getLogger();


    public String readString(String path) {
        FileReader reader = null;
        String res = "";

        try {
            reader = new FileReader(path);
            BufferedReader bufferedReader = new BufferedReader(reader);
            res = bufferedReader.readLine();
            bufferedReader.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            if (reader != null) {
                reader = null;
            }
        }

        return res;
    }

    public static void main(String[] args) {
        // jsonTesting testing = new jsonTesting();
        String rawJsonString = "{\"status\":\"success\",\"data\":{\"BSE_EQ:AMIORG\":{\"last_price\":1313.35,\"instrument_token\":\"BSE_EQ|INE00FF01017\"},\"BSE_EQ:ALPHAGEO\":{\"last_price\":406.9,\"instrument_token\":\"BSE_EQ|INE137C01018\"},\"BSE_EQ:AZAD\":{\"last_price\":1797.6,\"instrument_token\":\"BSE_EQ|INE02IJ01035\"},\"BSE_EQ:NMDC\":{\"last_price\":267.95,\"instrument_token\":\"BSE_EQ|INE584A01023\"},\"BSE_EQ:SINCLAIR\":{\"last_price\":117.25,\"instrument_token\":\"BSE_EQ|INE985A01022\"},\"BSE_EQ:KDDL\":{\"last_price\":2678.4,\"instrument_token\":\"BSE_EQ|INE291D01011\"},\"BSE_EQ:ELECON\":{\"last_price\":1330.55,\"instrument_token\":\"BSE_EQ|INE205B01023\"},\"BSE_EQ:HFCL\":{\"last_price\":115.55,\"instrument_token\":\"BSE_EQ|INE548A01028\"},\"BSE_EQ:3IINFOLTD\":{\"last_price\":37.01,\"instrument_token\":\"BSE_EQ|INE748C01038\"},\"BSE_EQ:ASHOKA\":{\"last_price\":227.4,\"instrument_token\":\"BSE_EQ|INE442H01029\"},\"BSE_EQ:JYOTICNC\":{\"last_price\":1190,\"instrument_token\":\"BSE_EQ|INE980O01024\"},\"BSE_EQ:KFINTECH\":{\"last_price\":727.95,\"instrument_token\":\"BSE_EQ|INE138Y01010\"},\"BSE_EQ:ETHOSLTD\":{\"last_price\":2558.7,\"instrument_token\":\"BSE_EQ|INE04TZ01018\"},\"BSE_EQ:JWL\":{\"last_price\":703,\"instrument_token\":\"BSE_EQ|INE209L01016\"},\"BSE_EQ:JKTYRE\":{\"last_price\":401,\"instrument_token\":\"BSE_EQ|INE573A01042\"},\"BSE_EQ:SHANTIGEAR\":{\"last_price\":546.95,\"instrument_token\":\"BSE_EQ|INE631A01022\"},\"BSE_EQ:RECLTD\":{\"last_price\":525.2,\"instrument_token\":\"BSE_EQ|INE020B01018\"},\"BSE_EQ:GILLETTE\":{\"last_price\":7866.45,\"instrument_token\":\"BSE_EQ|INE322A01010\"},\"BSE_EQ:RAJESHEXPO\":{\"last_price\":293.4,\"instrument_token\":\"BSE_EQ|INE343B01030\"},\"BSE_EQ:VOLTAMP\":{\"last_price\":10532.05,\"instrument_token\":\"BSE_EQ|INE540H01012\"},\"BSE_EQ:BEPL\":{\"last_price\":103.39,\"instrument_token\":\"BSE_EQ|INE922A01025\"},\"BSE_EQ:ADVANIHOTR\":{\"last_price\":70.55,\"instrument_token\":\"BSE_EQ|INE199C01026\"},\"BSE_EQ:OFSS\":{\"last_price\":9701.3,\"instrument_token\":\"BSE_EQ|INE881D01027\"},\"BSE_EQ:HAPPYFORGE\":{\"last_price\":1202.75,\"instrument_token\":\"BSE_EQ|INE330T01021\"}}}";
        // String jsonString = testing.readString(System.getProperty("user.dir") + "/projects/demo/src/main/java/"
                // + testing.getClass().getPackageName().replace(".", "/") + "/" + "jsonformatter.txt");


        // System.out.println(jsonString);

        ResponseJsonMain responseJsonMain = new ResponseJsonMain();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            responseJsonMain = objectMapper.readValue(rawJsonString, ResponseJsonMain.class);
            assert responseJsonMain.status == "success";
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        System.out.println(responseJsonMain.status);
        for ( Symbol s : responseJsonMain.data.symbols){
            System.out.println(s.symbolName);
            System.out.println(s.price.last_price);
            System.out.println(s.price.instrument_token);
        }
    }
}
