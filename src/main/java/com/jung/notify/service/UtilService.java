package com.jung.notify.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class UtilService {

    @Value("${naver.clientId}")
    String clientId; //애플리케이션 클라이언트 아이디값"

    @Value("${naver.clientSecret}")
    String clientSecret; //애플리케이션 클라이언트 시크릿값"

    public String shortUrl(String url){
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders header = new HttpHeaders();
        header.set("X-Naver-Client-Id", clientId);
        header.set("X-Naver-Client-Secret", clientSecret);

        HttpEntity<?> entity = new HttpEntity<>(header);

        UriComponents uri = UriComponentsBuilder.fromHttpUrl("https://openapi.naver.com/v1/util/shorturl?url=" + url).build();

        ResponseEntity<String> resultMap = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, String.class);
        JSONObject jsonObject = new JSONObject(resultMap.getBody());

        JSONObject object = new JSONObject(jsonObject.get("result").toString());

        return object.get("url").toString();
    }
}
