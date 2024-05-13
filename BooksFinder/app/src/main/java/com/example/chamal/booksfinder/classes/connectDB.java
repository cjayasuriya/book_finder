package com.example.chamal.booksfinder.classes;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by chamal on 8/6/17.
 */

public class connectDB {

    public static String getJsonFromServer(String url) throws Exception{

        BufferedReader br = null;
        URL jsonUrl = new URL(url);
        URLConnection con = jsonUrl.openConnection();

        con.setConnectTimeout(30000);
        con.setReadTimeout(30000);

        br = new BufferedReader(new InputStreamReader(con.getInputStream()));

        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = br.readLine()) != null) {
            response.append(inputLine);
        }

        String jsonResult = response.toString();

        br.close();

        //String jsonResult = br.readLine();

        return jsonResult;
    }
}
