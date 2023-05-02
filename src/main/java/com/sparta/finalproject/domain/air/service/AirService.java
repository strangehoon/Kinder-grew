package com.sparta.finalproject.domain.air.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.sparta.finalproject.domain.air.entity.RealtimeCityAir;
import com.sparta.finalproject.domain.air.entity.Row;
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

    public RealtimeCityAir airConditionGet() throws JsonProcessingException {
        String airCondition = callAirConditionApi();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JsonNode rootNode = objectMapper.readTree(airCondition);
        JsonNode rowNode = rootNode.path("RealtimeCityAir").path("row");
        List<Row> rowList = objectMapper.convertValue(rowNode, TypeFactory.defaultInstance().constructCollectionType(List.class, Row.class));
        return new RealtimeCityAir(rowList);
    }

    public String callAirConditionApi() {
        StringBuilder airCondition = new StringBuilder();
        try {
            String KEY = "";
            String TYPE = "json";
            String SERVICE = "RealtimeCityAir";
            int START_INDEX = 1;
            int END_INDEX = 1;
            String MSRRGN_NM = "동남권";
            String MSRSTE_NM = "강동구";

            String requestURL = "http://openapi.seoul.go.kr:8088" +
                    "/" + KEY +
                    "/" + TYPE +
                    "/" + SERVICE +
                    "/" + START_INDEX +
                    "/" + END_INDEX +
                    "/" + MSRRGN_NM +
                    "/" + MSRSTE_NM;
            URL url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader;
            bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                airCondition.append(line);
            }
            bufferedReader.close();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return airCondition.toString();
    }
}