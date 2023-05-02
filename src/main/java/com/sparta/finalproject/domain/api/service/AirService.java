package com.sparta.finalproject.domain.api.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.sparta.finalproject.domain.api.entity.RealtimeCityAir;
import com.sparta.finalproject.domain.api.entity.Row;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AirService {

    public RealtimeCityAir testAir() throws JsonProcessingException {
        String str = testApi();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JsonNode rootNode = objectMapper.readTree(str);
        JsonNode rowNode = rootNode.path("RealtimeCityAir").path("row");
        List<Row> rowList = objectMapper.convertValue(rowNode, TypeFactory.defaultInstance().constructCollectionType(List.class, Row.class));
        return new RealtimeCityAir(rowList);
    }

    public String testApi() {
        StringBuilder sb = new StringBuilder();
        try {
            String KEY = "";
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
        return sb.toString();
    }
}