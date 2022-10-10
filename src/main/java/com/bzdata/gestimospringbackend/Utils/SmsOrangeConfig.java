package com.bzdata.gestimospringbackend.Utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.json.Json;
import javax.json.JsonObject;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class SmsOrangeConfig {
    String telephoneDuDetinataire;
    String telephoneQuiEnvoiLesSms;
    String messageEnvyer;
    String accessToken;

    public boolean sendSms(String accessToken, String sms, String telEnvoi, String telRecepteur, String nomSociete)
            throws Exception {
        System.out.println("Les données à prendre en compte");
        System.out.println(accessToken + " " + sms + " " + telEnvoi + " " + " " + telRecepteur + " " + nomSociete);
        System.out.println("Le json est le suivant : ");
        JsonObject dataJson = Json.createObjectBuilder().add("outboundSMSMessageRequest", Json.createObjectBuilder()
                .add("address", "tel:+225" + telRecepteur)
                .add("senderAddress", "tel:+2250000")
                .add("senderName", nomSociete)
                .add("outboundSMSTextMessage", Json.createObjectBuilder().add("message", sms)))
                .build();
        String postParams = dataJson.toString();

        System.out.println("Le json est le suivant II : ");
        System.out.println(postParams);
        URL obj = new URL("https://api.orange.com/smsmessaging/v1/outbound/tel%3A%2B2250000/requests");
        HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization",
                "Bearer " + accessToken);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        OutputStream os = conn.getOutputStream();
        os.write(postParams.getBytes());
        os.flush();
        os.close();
        // For POST only - END

        int responseCode = conn.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_CREATED) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println("POST request worked");

            System.out.println(this.accessToken);
            return true;
        } else {
            System.out.println("POST request not worked");
            return false;
        }
        
    }

    public String getTokenSmsOrange() throws Exception {

        String POST_PARAMS = "grant_type=client_credentials";
        URL obj = new URL("https://api.orange.com/oauth/v3/token");
        HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization",
                "Basic STBuQkhpR3pLYldTS2F3TDQzWUg5WW41SlNKcWxnRFQ6MXBYU056c1p1Z1FlRTJJUA==");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Accept", "application/json;odata=verbose");

        conn.setDoOutput(true);
        OutputStream os = conn.getOutputStream();
        os.write(POST_PARAMS.getBytes());
        os.flush();
        os.close();
        // For POST only - END

        int responseCode = conn.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            JSONObject json = new JSONObject(response.toString());
            // Object tok=Json
            this.accessToken = json.getString("access_token");

            System.out.println(this.accessToken);
        } else {
            System.out.println("POST request not worked");
        }
        return this.accessToken;
    }

}
