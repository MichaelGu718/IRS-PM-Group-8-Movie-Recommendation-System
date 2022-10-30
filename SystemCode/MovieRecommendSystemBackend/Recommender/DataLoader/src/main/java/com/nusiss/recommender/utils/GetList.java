package com.nusiss.recommender.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Project: MovieRecommendSystem
 * Package: com.nusiss.recommender.utils
 *
 * Created by tangyi on 2022-10-19 23:45
 * @author tangyi
 */
public class GetList {
    public static List<String> getMovieIdList() throws IOException {
        String filePath = "src/main/resources/MovieId.txt";
        File file = new File(filePath);
        InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
        BufferedReader br = new BufferedReader(reader);
        List<String> movieIdList = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            movieIdList.add(line);
        }
        return movieIdList;
    }
}
