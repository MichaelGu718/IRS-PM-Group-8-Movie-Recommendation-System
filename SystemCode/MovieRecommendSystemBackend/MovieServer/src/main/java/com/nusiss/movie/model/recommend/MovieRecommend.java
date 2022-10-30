package com.nusiss.movie.model.recommend;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Project: MovieRecommendSystem
 * Package: com.nusiss.movie.model.recommend
 * <p>
 * Created by tangyi on 2022-10-27 03:05
 * @author tangyi
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieRecommend {
    private long movieId;
    private double rating;
}
