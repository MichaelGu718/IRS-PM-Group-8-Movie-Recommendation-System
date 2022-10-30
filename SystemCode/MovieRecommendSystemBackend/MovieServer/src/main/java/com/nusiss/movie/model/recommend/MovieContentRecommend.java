package com.nusiss.movie.model.recommend;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Project: MovieRecommendSystem
 * Package: com.nusiss.movie.model.recommend
 * <p>
 * Created by tangyi on 2022-10-27 03:00
 * @author tangyi
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("ContentMovieRecs")
public class MovieContentRecommend {
    private long movieId;
    private List<MovieRecommend> recs;
}
