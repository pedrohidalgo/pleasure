package com.qualixium.playnb.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Optional;

public class HttpUtil {

    public static Optional<String> executeGetRequest(String targetURL) throws IOException {
        StringBuilder sbResult = new StringBuilder();
        try {
            URL url = new URL(targetURL);
            URLConnection connection = url.openConnection();
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()))) {
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    sbResult.append(inputLine);
                }
            }

            return Optional.of(sbResult.toString());
        } catch (MalformedURLException ex) {
            ExceptionManager.logException(ex);
        } 

        return Optional.empty();
    }
    
    public static Optional<String> executePostRequest(String targetURL, String jsonString) throws IOException {
        StringBuilder sbResult = new StringBuilder();
        try {
            URL url = new URL(targetURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.getOutputStream().write(jsonString.getBytes());
            
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()))) {
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    sbResult.append(inputLine);
                }
            }

            return Optional.of(sbResult.toString());
        } catch (MalformedURLException ex) {
            ExceptionManager.logException(ex);
        } 

        return Optional.empty();
    }

}
