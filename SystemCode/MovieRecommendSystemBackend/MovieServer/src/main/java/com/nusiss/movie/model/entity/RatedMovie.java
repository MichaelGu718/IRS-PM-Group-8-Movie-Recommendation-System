package com.nusiss.movie.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Project: MovieRecommendSystem
 * Package: com.nusiss.movie.model.entity
 * <p>
 * Created by tangyi on 2022-10-29 02:42
 * @author tangyi
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RatedMovie {
    private long movieId;

    private String title;

    private String posterPath;

    private Double yourRating;

    private String ratingTime;
}
