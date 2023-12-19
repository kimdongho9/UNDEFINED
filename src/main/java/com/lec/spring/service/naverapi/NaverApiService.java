package com.lec.spring.service.naverapi;


import com.lec.spring.domain.naverapi.News;
import com.lec.spring.repository.naverapi.NewsRepository;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class NaverApiService {

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naver_client_id;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String naver_secret;

    private NewsRepository newsRepository;

    public NaverApiService(SqlSession sqlSession){
        newsRepository = sqlSession.getMapper(NewsRepository.class);
    }

    //@Scheduled(fixedDelay = 3600000)

    @Scheduled(cron = "0 0 1 * * *") // 매일 새벽 1시에
    public void navernews(){
        newsRepository.delete("개발자채용");
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);

        URI uri = UriComponentsBuilder.fromUriString("https://openapi.naver.com")
                .path("/v1/search/news.json")
                .queryParam("query", "개발자채용")
                .queryParam("display","100")
                .queryParam("start","1")
                .queryParam("sort","sim")
                .encode()
                .build()
                .toUri();

        RestTemplate restTemplate =new RestTemplate();

        RequestEntity<Void> requestEntity=
                RequestEntity.get(uri)
                        .header("X-Naver-Client-ID",naver_client_id)
                        .header("X-Naver-Client-Secret",naver_secret)
                        .build();

        //헤더값을 채워서 넣기 위해서 exchange이용
        ResponseEntity<String> res = restTemplate.exchange(requestEntity,String.class);
        String response = res.getBody();
        JSONObject jsonObject = new JSONObject(response);
        JSONArray items = jsonObject.getJSONArray("items");
        List<News> newsList = new ArrayList<>();
        for (int i=0; i<items.length(); i++) {
            JSONObject itemJson = (JSONObject) items.get(i);
            News news = new News();
            news.setKeyword("개발자채용");
            news.setTitle(itemJson.getString("title"));
            news.setOriginallink(itemJson.getString("originallink"));
            news.setLink(itemJson.getString("link"));
            news.setDescription(itemJson.getString("description"));
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(itemJson.getString("pubDate"), inputFormatter);
            LocalDateTime localDateTime = zonedDateTime.toLocalDateTime();
            news.setPubDate(localDateTime);
            newsList.add(news);
        }

        for (News news : newsList) {
            newsRepository.save(news);
        }
    }

    @Scheduled(cron = "0 0 1 * * *")
    public void navernews2(){
        newsRepository.delete("백엔드");
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);

        URI uri = UriComponentsBuilder.fromUriString("https://openapi.naver.com")
                .path("/v1/search/news.json")
                .queryParam("query", "백엔드")
                .queryParam("display","100")
                .queryParam("start","1")
                .queryParam("sort","sim")
                .encode()
                .build()
                .toUri();

        RestTemplate restTemplate =new RestTemplate();

        RequestEntity<Void> requestEntity=
                RequestEntity.get(uri)
                        .header("X-Naver-Client-ID",naver_client_id)
                        .header("X-Naver-Client-Secret",naver_secret)
                        .build();

        //헤더값을 채워서 넣기 위해서 exchange이용
        ResponseEntity<String> res = restTemplate.exchange(requestEntity,String.class);
        String response = res.getBody();
        JSONObject jsonObject = new JSONObject(response);
        JSONArray items = jsonObject.getJSONArray("items");
        List<News> newsList = new ArrayList<>();
        for (int i=0; i<items.length(); i++) {
            JSONObject itemJson = (JSONObject) items.get(i);
            News news = new News();
            news.setKeyword("백엔드");
            news.setTitle(itemJson.getString("title"));
            news.setOriginallink(itemJson.getString("originallink"));
            news.setLink(itemJson.getString("link"));
            news.setDescription(itemJson.getString("description"));
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(itemJson.getString("pubDate"), inputFormatter);
            LocalDateTime localDateTime = zonedDateTime.toLocalDateTime();
            news.setPubDate(localDateTime);
            newsList.add(news);
        }

        for (News news : newsList) {
            newsRepository.save(news);
        }
    }


    @Scheduled(cron = "0 0 1 * * *")
    public void navernews3(){
        newsRepository.delete("프론트엔드");
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);

        URI uri = UriComponentsBuilder.fromUriString("https://openapi.naver.com")
                .path("/v1/search/news.json")
                .queryParam("query", "프론트엔드")
                .queryParam("display","100")
                .queryParam("start","1")
                .queryParam("sort","sim")
                .encode()
                .build()
                .toUri();

        RestTemplate restTemplate =new RestTemplate();

        RequestEntity<Void> requestEntity=
                RequestEntity.get(uri)
                        .header("X-Naver-Client-ID",naver_client_id)
                        .header("X-Naver-Client-Secret",naver_secret)
                        .build();

        //헤더값을 채워서 넣기 위해서 exchange이용
        ResponseEntity<String> res = restTemplate.exchange(requestEntity,String.class);
        String response = res.getBody();
        JSONObject jsonObject = new JSONObject(response);
        JSONArray items = jsonObject.getJSONArray("items");
        List<News> newsList = new ArrayList<>();
        for (int i=0; i<items.length(); i++) {
            JSONObject itemJson = (JSONObject) items.get(i);
            News news = new News();
            news.setKeyword("프론트엔드");
            news.setTitle(itemJson.getString("title"));
            news.setOriginallink(itemJson.getString("originallink"));
            news.setLink(itemJson.getString("link"));
            news.setDescription(itemJson.getString("description"));
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(itemJson.getString("pubDate"), inputFormatter);
            LocalDateTime localDateTime = zonedDateTime.toLocalDateTime();
            news.setPubDate(localDateTime);
            newsList.add(news);
        }

        for (News news : newsList) {
            newsRepository.save(news);
        }
    }

    @Scheduled(cron = "0 0 1 * * *")
    public void navernews4(){
        newsRepository.delete("AI");
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);

        URI uri = UriComponentsBuilder.fromUriString("https://openapi.naver.com")
                .path("/v1/search/news.json")
                .queryParam("query", "AI")
                .queryParam("display","100")
                .queryParam("start","1")
                .queryParam("sort","sim")
                .encode()
                .build()
                .toUri();

        RestTemplate restTemplate =new RestTemplate();

        RequestEntity<Void> requestEntity=
                RequestEntity.get(uri)
                        .header("X-Naver-Client-ID",naver_client_id)
                        .header("X-Naver-Client-Secret",naver_secret)
                        .build();

        //헤더값을 채워서 넣기 위해서 exchange이용
        ResponseEntity<String> res = restTemplate.exchange(requestEntity,String.class);
        String response = res.getBody();
        JSONObject jsonObject = new JSONObject(response);
        JSONArray items = jsonObject.getJSONArray("items");
        List<News> newsList = new ArrayList<>();
        for (int i=0; i<items.length(); i++) {
            JSONObject itemJson = (JSONObject) items.get(i);
            News news = new News();
            news.setKeyword("AI");
            news.setTitle(itemJson.getString("title"));
            news.setOriginallink(itemJson.getString("originallink"));
            news.setLink(itemJson.getString("link"));
            news.setDescription(itemJson.getString("description"));
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(itemJson.getString("pubDate"), inputFormatter);
            LocalDateTime localDateTime = zonedDateTime.toLocalDateTime();
            news.setPubDate(localDateTime);
            newsList.add(news);
        }

        for (News news : newsList) {
            newsRepository.save(news);
        }
    }


    @Scheduled(cron = "0 0 1 * * *")
    public void navernews5(){
        newsRepository.delete("인공지능");
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);

        URI uri = UriComponentsBuilder.fromUriString("https://openapi.naver.com")
                .path("/v1/search/news.json")
                .queryParam("query", "인공지능")
                .queryParam("display","100")
                .queryParam("start","1")
                .queryParam("sort","sim")
                .encode()
                .build()
                .toUri();

        RestTemplate restTemplate =new RestTemplate();

        RequestEntity<Void> requestEntity=
                RequestEntity.get(uri)
                        .header("X-Naver-Client-ID",naver_client_id)
                        .header("X-Naver-Client-Secret",naver_secret)
                        .build();

        //헤더값을 채워서 넣기 위해서 exchange이용
        ResponseEntity<String> res = restTemplate.exchange(requestEntity,String.class);
        String response = res.getBody();
        JSONObject jsonObject = new JSONObject(response);
        JSONArray items = jsonObject.getJSONArray("items");
        List<News> newsList = new ArrayList<>();
        for (int i=0; i<items.length(); i++) {
            JSONObject itemJson = (JSONObject) items.get(i);
            News news = new News();
            news.setKeyword("인공지능");
            news.setTitle(itemJson.getString("title"));
            news.setOriginallink(itemJson.getString("originallink"));
            news.setLink(itemJson.getString("link"));
            news.setDescription(itemJson.getString("description"));
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(itemJson.getString("pubDate"), inputFormatter);
            LocalDateTime localDateTime = zonedDateTime.toLocalDateTime();
            news.setPubDate(localDateTime);
            newsList.add(news);
        }

        for (News news : newsList) {
            newsRepository.save(news);
        }
    }

    @Scheduled(cron = "0 0 1 * * *")
    public void navernews6(){
        newsRepository.delete("보안");
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);

        URI uri = UriComponentsBuilder.fromUriString("https://openapi.naver.com")
                .path("/v1/search/news.json")
                .queryParam("query", "보안")
                .queryParam("display","100")
                .queryParam("start","1")
                .queryParam("sort","sim")
                .encode()
                .build()
                .toUri();

        RestTemplate restTemplate =new RestTemplate();

        RequestEntity<Void> requestEntity=
                RequestEntity.get(uri)
                        .header("X-Naver-Client-ID",naver_client_id)
                        .header("X-Naver-Client-Secret",naver_secret)
                        .build();

        //헤더값을 채워서 넣기 위해서 exchange이용
        ResponseEntity<String> res = restTemplate.exchange(requestEntity,String.class);
        String response = res.getBody();
        JSONObject jsonObject = new JSONObject(response);
        JSONArray items = jsonObject.getJSONArray("items");
        List<News> newsList = new ArrayList<>();
        for (int i=0; i<items.length(); i++) {
            JSONObject itemJson = (JSONObject) items.get(i);
            News news = new News();
            news.setKeyword("보안");
            news.setTitle(itemJson.getString("title"));
            news.setOriginallink(itemJson.getString("originallink"));
            news.setLink(itemJson.getString("link"));
            news.setDescription(itemJson.getString("description"));
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(itemJson.getString("pubDate"), inputFormatter);
            LocalDateTime localDateTime = zonedDateTime.toLocalDateTime();
            news.setPubDate(localDateTime);
            newsList.add(news);
        }

        for (News news : newsList) {
            newsRepository.save(news);
        }
    }



}// end service
