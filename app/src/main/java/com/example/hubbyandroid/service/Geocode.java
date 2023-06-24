package com.example.hubbyandroid.service;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.ContentValues.TAG;
import static com.example.hubbyandroid.BuildConfig.MAPS_API_KEY;

public class Geocode {
    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

    public String getAddressByLatLng(LatLng latLng) {

        StrictMode.setThreadPolicy(policy);

        double latitude = latLng.latitude;
        double longitude = latLng.longitude;
        String address = "Endereço não encontrado.";

        try {

            String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + "," + longitude + "&key=" + MAPS_API_KEY;
            URL geocodeUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) geocodeUrl.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                reader.close();

                JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                JSONArray results = jsonObject.getJSONArray("results");
                if (results.length() > 0) {
                    JSONObject result = results.getJSONObject(0);
                    address = result.getString("formatted_address");
                }
            }
            else
            {
                Log.e(TAG, "HTTP error code: " + responseCode);
            }

        } catch (IOException | JSONException e) {
            Log.e(TAG, "Error: " + e.getMessage());
        }

        return address;
    }
}
