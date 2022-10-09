package com.bzdata.gestimospringbackend.Utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class SmsOrangeConfig {

    public String SmsOrangeConfig() {
        Map<String,String> mp = new HashMap<>();
        mp.put("grant_type", "client_credentials");
        try {
            // Construct data
            String apiKey = "apikey=" + "yourapiKey";
            String message = "&message=" + "This is your message";
            String sender = "&sender=" + "Jims Autos";
            String numbers = "&numbers=" + "447123456789";

            // Send data
            HttpURLConnection conn = (HttpURLConnection) new URL("https://api.orange.com/smsmessaging/v1/outbound/tel%3A%2B{{dev_phone_number}}/requests").openConnection();
            String data = apiKey + numbers + message + sender;
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer {{access_token}}");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded}");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Length", Integer.toString(data.length()));

            conn.getOutputStream().write(data.getBytes("UTF-8"));
            final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                stringBuffer.append(line);
            }
            rd.close();

            return stringBuffer.toString();
        } catch (Exception e) {
            System.out.println("Error SMS " + e);
            return "Error " + e;
        }
    }
}
