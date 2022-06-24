package com.jung.notify.service;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;

@Service
@Slf4j
public class MessageService {

    @Value("${naver.authorization}")
    String authorization; //애플리케이션 클라이언트 아이디값"

    public void sendMessage(MultiValueMap<String, Object> body) {
        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", "Bearer " + authorization);
        header.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, header);
        String serverUrl = "https://notify-api.line.me/api/notify";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(serverUrl, requestEntity, String.class);
        System.out.println("Response code: " + response.getStatusCode());

    }
}
