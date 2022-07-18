package com.jung.notify.service;

import com.jung.notify.domain.News;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@Slf4j
@RequiredArgsConstructor
public class NewsService {

    @Value("${naver.clinetId}")
    String clientId; //애플리케이션 클라이언트 아이디값"

    @Value("${naver.clientSecret}")
    String clientSecret; //애플리케이션 클라이언트 시크릿값"

    private final UtilService utilService;

    public List<News> news(String keyword) {
        String apiURL = "https://openapi.naver.com/v1/search/news.json?query=" + keyword + "&sort=sim";    // json 결과
        //String apiURL = "https://openapi.naver.com/v1/search/blog.xml?query="+ text; // xml 결과

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders header = new HttpHeaders();
        header.set("X-Naver-Client-Id", clientId);
        header.set("X-Naver-Client-Secret", clientSecret);

        HttpEntity<?> entity = new HttpEntity<>(header);

        UriComponents uri = UriComponentsBuilder.fromHttpUrl(apiURL).build();

        ResponseEntity<String> resultMap = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, String.class);

        JSONObject jsonObject = new JSONObject(resultMap.getBody());

        JSONArray jsonArray = new JSONArray(jsonObject.getJSONArray("items").toString());

//            log.info(jsonObject.getJSONArray("items").toString());

        List<News> newsList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss x").withLocale(Locale.ENGLISH);

            LocalDateTime localDateTime = LocalDateTime.parse(object.get("pubDate").toString(), formatter);

            formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일(E) HH:mm");

            News news = News.builder()
                    .title(News.replace(object.get("title").toString()))
                    .link(utilService.shortUrl(object.get("link").toString()))
                    .description(News.replace(object.get("description").toString()))
                    .pubDate(localDateTime.format(formatter))
                    .build();

            newsList.add(news);
        }

        return newsList;
    }

    public List<News> dateNews(String keyword, int start) {
        String apiURL = "https://openapi.naver.com/v1/search/news.json?query=" + keyword + "&sort=date&start=" + start;    // json 결과
        //String apiURL = "https://openapi.naver.com/v1/search/blog.xml?query="+ text; // xml 결과

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders header = new HttpHeaders();
        header.set("X-Naver-Client-Id", clientId);
        header.set("X-Naver-Client-Secret", clientSecret);

        HttpEntity<?> entity = new HttpEntity<>(header);

        UriComponents uri = UriComponentsBuilder.fromHttpUrl(apiURL).build();

        ResponseEntity<String> resultMap = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, String.class);

        JSONObject jsonObject = new JSONObject(resultMap.getBody());

        JSONArray jsonArray = new JSONArray(jsonObject.getJSONArray("items").toString());

//            log.info(jsonObject.getJSONArray("items").toString());

        List<News> newsList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss x").withLocale(Locale.ENGLISH);

            LocalDateTime localDateTime = LocalDateTime.parse(object.get("pubDate").toString(), formatter);

            formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일(E) HH:mm");

            News news = News.builder()
                    .title(News.replace(object.get("title").toString()))
                    .link(utilService.shortUrl(object.get("link").toString()))
                    .description(News.replace(object.get("description").toString()))
                    .pubDate(localDateTime.format(formatter))
                    .build();

            newsList.add(news);
        }

        return newsList;
    }


}
