package com.bzdata.gestimospringbackend.Utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

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

    public void sendSms(String accessToken,String sms,String telEnvoi,String telRecepteur,String nomSociete) throws Exception {

    String json = "{\r\n" +
            "  \"outboundSMSMessageRequest\": {\r\n" +
            "  \"address\": \"tel:+225"+telRecepteur+"\",\r\n" +
            "  \"senderAddress\": \"tel:"+telEnvoi+"\",\r\n" +
            "  \"senderName\": \""+nomSociete+"\",\r\n" +
            "  \"outboundSMSTextMessage\": {\r\n" +
            "  \"message\": \""+sms+"\"\r\n" +
            "  }\r\n }\r\n" +

            "}";
    System.out.println("Le Json est :");
System.out.println(json);

        URL obj = new URL("https://api.orange.com/smsmessaging/v1/outbound/tel%3A%2B2250000/requests");
        HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization",
                "Bearer "+accessToken);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Accept", "application/json");

        conn.setDoOutput(true);
        OutputStream os = conn.getOutputStream();
        os.write(json.getBytes());
        os.flush();
        os.close();
        // For POST only - END

        int responseCode = conn.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();


           System.out.println( this.accessToken );
        } else {
            System.out.println("POST request not worked");
        }
    }
    public String getHttpCon() throws Exception{

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

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            JSONObject json = new JSONObject(response.toString());
            //Object tok=Json
            this.accessToken = json.getString("access_token");

           System.out.println( this.accessToken );
        } else {
            System.out.println("POST request not worked");
        }
        return this.accessToken;
    }

}
