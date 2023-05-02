package com.sparta.finalproject.domain.api.service;

import com.sparta.finalproject.domain.api.entity.AirQuality;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


@Service
public class AirService {

    public AirQuality testApi() {
        StringBuilder sb = new StringBuilder();
        try {
            String KEY = "72576146456879753132344661637646";
            String TYPE = "json";
            String SERVICE = "RealtimeCityAir";
            int START_INDEX = 1;
            int END_INDEX = 1;
            String MSRRGN_NM = "동남권";
            String MSRSTE_NM = "강동구";

            String urlBuilder = "http://openapi.seoul.go.kr:8088" +
                    "/" + KEY +
                    "/" + TYPE +
                    "/" + SERVICE +
                    "/" + START_INDEX +
                    "/" + END_INDEX +
                    "/" + MSRRGN_NM +
                    "/" + MSRSTE_NM;
            URL url = new URL(urlBuilder);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            BufferedReader rd;
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String result = sb.toString();
        double pm10 = 0;
        double pm25 = 0;
        String[] arr = result.split("\"row\":");
        String json = arr[1].substring(1, arr[1].length() - 2);
        String[] data = json.split(",");
        for (String s : data) {
            String[] d = s.split(":");
            if (d[0].equals("\"PM10\"")) {
                pm10 = Double.parseDouble(d[1].trim());
            }
            if (d[0].equals("\"PM25\"")) {
                pm25 = Double.parseDouble(d[1].trim());
            }
        }
        AirQuality airQuality = new AirQuality();
        airQuality.setPm10(pm10);
        airQuality.setPm25(pm25);
        return airQuality;
    }
}