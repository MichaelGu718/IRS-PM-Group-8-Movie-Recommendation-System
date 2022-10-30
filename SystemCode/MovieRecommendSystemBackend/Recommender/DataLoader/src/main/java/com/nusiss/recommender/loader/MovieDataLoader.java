package com.nusiss.recommender.loader;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.nusiss.recommender.utils.GetList;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * Project: MovieRecommendSystem
 * Package: com.nusiss.recommender
 *
 * Created by tangyi on 2022-10-19 23:39
 * @author tangyi
 *
 */
public class MovieDataLoader {
    private final static String MOVIE_DETAIL_API = "https://movielens.org/api/movies/";
    private final static String MONGODB_HOST = "nus2.com";

    public void addMovieDataById(String id, MongoClient mongoClient) throws IOException {
        String url = MOVIE_DETAIL_API + id;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Cookie",
                "_ga=GA1.2.1566377270.1666168757; _gid=GA1.2.1757202678.1666168757; ml4_session=c988793a131978c2b0235b3fc970727de5b61276_8d3ee4f3-0132-4c83-b9a4-02c981259224; uvts=4ec54e64-2098-4a31-49da-15bb46a491a7");
        httpGet.addHeader("User-Agent",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/106.0.0.0 Safari/537.36");
        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity httpEntity = response.getEntity();
        BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(httpEntity.getContent()));

        try {
            String content = "", line;
            while ((line = reader.readLine()) != null) {
                content += line;
            }
            /**
             * Notice:
             *
             * FastJSON will transform double to BigDecimal
             * But Mongodb doesn't support BigDecimal
             * So we do the operation below
             */
            int disableDecimalFeature = JSON.DEFAULT_PARSER_FEATURE & ~Feature.UseBigDecimal.getMask();
            JSONObject jsonObject = JSON.parseObject(content, JSONObject.class, disableDecimalFeature);
            JSONObject movie = jsonObject.getJSONObject("data").getJSONObject("movieDetails").getJSONObject("movie");

            System.out.println(movie.get("movieId"));
            mongoClient.getDatabase("movierecommender").getCollection("movie").insertOne(new Document(movie));
        } catch (NullPointerException npe) {
            System.out.println("Movie " + id + " not found");
        }
    }

    public void addMovieDataByIdList(List<String> idList) throws IOException, InterruptedException {
        String mongoUri = "mongodb://movieAdmin:nusiss123@nus2.com:37018";
        MongoClientURI connStr = new MongoClientURI(mongoUri);
        MongoClient mongoClient = new MongoClient(connStr);

        for (String id : idList) {
            addMovieDataById(id, mongoClient);
        }
    }

    @Test
    public void movieDataLoad() throws IOException, InterruptedException {
        List<String> movieIdList = GetList.getMovieIdList();
        System.out.println(movieIdList.size());
        addMovieDataByIdList(movieIdList);
    }

    @Test
    public void updateAvgMovieList() throws IOException, InterruptedException {
        String mongoUri = "mongodb://movieAdmin:{thepassword}@nus2.com:37018";
        MongoClientURI connStr = new MongoClientURI(mongoUri);
        MongoClient mongoClient = new MongoClient(connStr);
        MongoCollection<Document> movieCollection = mongoClient.getDatabase("movierecommender").getCollection("movie");

        // sort by avgRating
        List<Document> movieList = movieCollection.find().sort(new Document("avgRating", -1)).into(new ArrayList<>());
        for (Document movie : movieList) {
            int movieId = movie.getInteger("movieId");
            double avgRating = movie.getDouble("avgRating");
            // insert into avgMovieList
            mongoClient.getDatabase("movierecommender").getCollection("avgMovieList").insertOne(new Document("movieId", movieId).append("avgRating", avgRating));
        }

    }
}




