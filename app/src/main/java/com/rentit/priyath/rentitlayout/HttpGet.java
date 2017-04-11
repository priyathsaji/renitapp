package com.rentit.priyath.rentitlayout;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by priyath on 07-04-2017.
 */

public class HttpGet {
    String getData(String link) throws IOException {

        URL url = new URL(link);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");

        InputStream in = new BufferedInputStream(connection.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder response = new StringBuilder();
        String line;
        while((line = reader.readLine())!=null){
            response.append(line);

        }
        return response.toString();
    }
}
