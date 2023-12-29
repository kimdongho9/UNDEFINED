package com.lec.spring.service.naverapi;


import com.lec.spring.domain.naverapi.Book;
import com.lec.spring.domain.naverapi.News;
import com.lec.spring.domain.naverapi.YoutubeDTO;
import com.lec.spring.repository.naverapi.NewsRepository;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONArray;
import org.json.JSONException;
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

    @Value("${youtube.apikey}")
    private String youtuekey;


    private NewsRepository newsRepository;

    public NaverApiService(SqlSession sqlSession){
        newsRepository = sqlSession.getMapper(NewsRepository.class);
    }



/*    @Scheduled(fixedDelay = 3600000)
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
    }*/



/*    @Scheduled(fixedDelay = 3600000)
    public void saveYoutube1(){
        RestTemplate restTemplate =new RestTemplate();
        String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&key=" + youtuekey
                + "&maxResults=10&q=개발자채용";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String resBody = response.getBody();
        JSONObject jsonObject = new JSONObject(resBody);
        JSONArray items = jsonObject.getJSONArray("items");
        List<YoutubeDTO> YList = new ArrayList<>();

            for (int i = 0; i < items.length(); i++) {
                try {
                JSONObject videoItem = (JSONObject) items.get(i);
                JSONObject snippet = videoItem.getJSONObject("snippet");
                String videoId = videoItem.getJSONObject("id").getString("videoId");
                String title = snippet.getString("title");
                YoutubeDTO youtubeDTO = new YoutubeDTO();
                youtubeDTO.setKeyword("개발자채용");
                youtubeDTO.setTitle(title);
                youtubeDTO.setVideoId(videoId);
                YList.add(youtubeDTO);
                }catch(JSONException e){

                }
            }


        for (YoutubeDTO youtubeDTO : YList) {
            newsRepository.saveYoutue(youtubeDTO);
        }

    }*/



    /*@Scheduled(fixedDelay = 3600000)
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
    }*/

  /* @Scheduled(fixedDelay = 3600000)
    public void saveYoutube2(){
        RestTemplate restTemplate =new RestTemplate();
        String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&key=" + youtuekey
                + "&maxResults=10&q=백엔드";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String resBody = response.getBody();
        JSONObject jsonObject = new JSONObject(resBody);
        JSONArray items = jsonObject.getJSONArray("items");
        List<YoutubeDTO> YList = new ArrayList<>();

            for (int i = 0; i < items.length(); i++) {
                try {
                JSONObject videoItem = (JSONObject) items.get(i);
                JSONObject snippet = videoItem.getJSONObject("snippet");
                String videoId = videoItem.getJSONObject("id").getString("videoId");
                String title = snippet.getString("title");
                YoutubeDTO youtubeDTO = new YoutubeDTO();
                youtubeDTO.setKeyword("백엔드");
                youtubeDTO.setTitle(title);
                youtubeDTO.setVideoId(videoId);
                YList.add(youtubeDTO);
                }catch(JSONException e){

                }
            }


        for (YoutubeDTO youtubeDTO : YList) {
            newsRepository.saveYoutue(youtubeDTO);
        }

    }*/

/*    @Scheduled(fixedDelay = 3600000)
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
    }*/

/*    @Scheduled(cron="0 0 20 * * ?")
    public void saveYoutube3(){
        RestTemplate restTemplate =new RestTemplate();
        String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&key=" + youtuekey
                + "&maxResults=10&q=프론트엔드";

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String resBody = response.getBody();
        JSONObject jsonObject = new JSONObject(resBody);
        JSONArray items = jsonObject.getJSONArray("items");
        List<YoutubeDTO> YList = new ArrayList<>();

            for (int i=0; i<items.length(); i++) {
                try{
                JSONObject videoItem = (JSONObject) items.get(i);
                JSONObject snippet = videoItem.getJSONObject("snippet");
                String videoId = videoItem.getJSONObject("id").getString("videoId");
                String title = snippet.getString("title");
                YoutubeDTO youtubeDTO = new YoutubeDTO();
                youtubeDTO.setKeyword("프론트엔드");
                youtubeDTO.setTitle(title);
                youtubeDTO.setVideoId(videoId);
                YList.add(youtubeDTO);
                }
                catch (JSONException e){

                }
            }

        for (YoutubeDTO youtubeDTO : YList) {
            newsRepository.saveYoutue(youtubeDTO);
        }

    }

    @Scheduled(cron="0 0 15 * * ?")
    public void navernews4(){
        System.out.println("시작");
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

    @Scheduled(cron="0 0 12 * * ?")
    public void saveYoutube4(){
        RestTemplate restTemplate =new RestTemplate();
        String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&key=" + youtuekey
                + "&maxResults=10&q=AI";
        newsRepository.deleteYoutube("AI");
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String resBody = response.getBody();
        JSONObject jsonObject = new JSONObject(resBody);
        JSONArray items = jsonObject.getJSONArray("items");
        List<YoutubeDTO> YList = new ArrayList<>();

            for (int i=0; i<items.length(); i++) {
            try{
                JSONObject videoItem = (JSONObject) items.get(i);
                JSONObject snippet = videoItem.getJSONObject("snippet");
                String videoId = videoItem.getJSONObject("id").getString("videoId");
                String title = snippet.getString("title");
                YoutubeDTO youtubeDTO = new YoutubeDTO();
                youtubeDTO.setKeyword("AI");
                youtubeDTO.setTitle(title);
                youtubeDTO.setVideoId(videoId);
                YList.add(youtubeDTO);
            }catch(JSONException e){

            }
        }

        for (YoutubeDTO youtubeDTO : YList) {
            newsRepository.saveYoutue(youtubeDTO);
        }

    }

    @Scheduled(cron="0 0 10 * * ?")
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

    @Scheduled(cron="0 0 08 * * ?")
    public void saveYoutube5(){
        RestTemplate restTemplate =new RestTemplate();
        String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&key=" + youtuekey
                + "&maxResults=10&q=인공지능";
        newsRepository.deleteYoutube("인공지능");
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String resBody = response.getBody();
        JSONObject jsonObject = new JSONObject(resBody);
        JSONArray items = jsonObject.getJSONArray("items");
        List<YoutubeDTO> YList = new ArrayList<>();
        for (int i=0; i<items.length(); i++) {
            try {
                JSONObject videoItem = (JSONObject) items.get(i);
                JSONObject snippet = videoItem.getJSONObject("snippet");
                String videoId = videoItem.getJSONObject("id").getString("videoId");
                String title = snippet.getString("title");
                YoutubeDTO youtubeDTO = new YoutubeDTO();
                youtubeDTO.setKeyword("인공지능");
                youtubeDTO.setTitle(title);
                youtubeDTO.setVideoId(videoId);
                YList.add(youtubeDTO);
            }catch(JSONException e){

            }
        }

        for (YoutubeDTO youtubeDTO : YList) {
            newsRepository.saveYoutue(youtubeDTO);
        }

    }

    @Scheduled(cron="0 0 09 * * ?")
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
    @Scheduled(cron="0 0 05 * * ?")
    public void saveYoutube6(){
        RestTemplate restTemplate =new RestTemplate();
        String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&key=" + youtuekey
                + "&maxResults=10&q=보안";
        newsRepository.deleteYoutube("보안");
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String resBody = response.getBody();
        JSONObject jsonObject = new JSONObject(resBody);
        JSONArray items = jsonObject.getJSONArray("items");
        List<YoutubeDTO> YList = new ArrayList<>();

            for (int i=0; i<items.length(); i++) {
                try{
                JSONObject videoItem = (JSONObject) items.get(i);
                JSONObject snippet = videoItem.getJSONObject("snippet");
                String videoId = videoItem.getJSONObject("id").getString("videoId");
                String title = snippet.getString("title");
                YoutubeDTO youtubeDTO = new YoutubeDTO();
                youtubeDTO.setKeyword("보안");
                youtubeDTO.setTitle(title);
                youtubeDTO.setVideoId(videoId);
                YList.add(youtubeDTO);
                }catch(JSONException e){

                }
            }


        for (YoutubeDTO youtubeDTO : YList) {
            newsRepository.saveYoutue(youtubeDTO);
        }

    }*/

    public List<News> list(String keyword){
        return newsRepository.list(keyword);
    }

    public List<YoutubeDTO> listYoutube(String keyword){
        return newsRepository.listYoutube(keyword);
    }


    public int saveBook(Book book){
        return newsRepository.saveBooks(book);
    }

    public int deleteBook(Book book){
        return newsRepository.deleteBooks(book);
    }
    public List<Book> likeBooks(Long id){
        return newsRepository.likeBooks(id);
    }
    public List<Book> getbooks(String keyword) {


        URI uri = UriComponentsBuilder.fromUriString("https://openapi.naver.com")
                .path("/v1/search/book_adv.json")
                .queryParam("d_titl", keyword.trim())
                .queryParam("display", "10")
                .queryParam("start", "1")
                .queryParam("sort", "sim")
                .encode()
                .build()
                .toUri();

        RestTemplate restTemplate = new RestTemplate();

        RequestEntity<Void> requestEntity =
                RequestEntity.get(uri)
                        .header("X-Naver-Client-ID", naver_client_id)
                        .header("X-Naver-Client-Secret", naver_secret)
                        .build();

        //헤더값을 채워서 넣기 위해서 exchange이용
        ResponseEntity<String> res = restTemplate.exchange(requestEntity, String.class);
        String response = res.getBody();
        JSONObject jsonObject = new JSONObject(response);
        JSONArray items = jsonObject.getJSONArray("items");
        List<Book> BookList = new ArrayList<>();
        for (int i = 0; i < items.length(); i++) {
            JSONObject itemJson = (JSONObject) items.get(i);
            Book book = new Book();
            book.setTitle(itemJson.getString("title"));
            book.setAuthor(itemJson.getString("author"));
            book.setLink(itemJson.getString("link"));
            book.setImage(itemJson.getString("image"));
            book.setDescription(cutDesc(itemJson.getString("description")));
            book.setPublisher(itemJson.getString("publisher"));
            book.setIsbn(itemJson.getString("isbn"));
            book.setDiscount(itemJson.getInt("discount"));
            book.setPubdate(itemJson.getString("pubdate"));

            // Add constructed Book object to the list
            BookList.add(book);
        }
        return BookList;
    }

    private String cutDesc(String desc) {
        if(desc.length() >= 100) {
            desc = desc.substring(0, 100) + "...";
        }
        return desc;
    }

    public List<Book> getBooksInCalendar(String keyword) {

        URI uri = UriComponentsBuilder.fromUriString("https://openapi.naver.com")
                .path("/v1/search/book_adv.json")
                .queryParam("d_titl", keyword.trim())
                .queryParam("display", "5")
                .queryParam("sort", "sim")
                .encode()
                .build()
                .toUri();

        RestTemplate restTemplate = new RestTemplate();

        RequestEntity<Void> requestEntity =
                RequestEntity.get(uri)
                        .header("X-Naver-Client-ID", naver_client_id)
                        .header("X-Naver-Client-Secret", naver_secret)
                        .build();

        //헤더값을 채워서 넣기 위해서 exchange이용
        ResponseEntity<String> res = restTemplate.exchange(requestEntity, String.class);
        String response = res.getBody();
        JSONObject jsonObject = new JSONObject(response);
        JSONArray items = jsonObject.getJSONArray("items");
        List<Book> BookList = new ArrayList<>();
        for (int i = 0; i < items.length(); i++) {
            JSONObject itemJson = (JSONObject) items.get(i);
            Book book = new Book();
            book.setTitle(itemJson.getString("title"));
            book.setAuthor(itemJson.getString("author"));
            book.setLink(itemJson.getString("link"));
            book.setImage(itemJson.getString("image"));
            book.setDescription(itemJson.getString("description"));
            book.setPublisher(itemJson.getString("publisher"));
            book.setIsbn(itemJson.getString("isbn"));
            book.setDiscount(itemJson.getInt("discount"));
            book.setPubdate(itemJson.getString("pubdate"));

            // Add constructed Book object to the list
            BookList.add(book);
        }
        return BookList;
    }
}// end service
