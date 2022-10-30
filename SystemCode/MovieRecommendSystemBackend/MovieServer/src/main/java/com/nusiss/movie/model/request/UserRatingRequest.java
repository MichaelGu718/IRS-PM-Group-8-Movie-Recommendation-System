package com.nusiss.movie.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Project: MovieRecommendSystem
 * Package: com.nusiss.movie.model.request
 * <p>
 * Created by tangyi on 2022-10-28 22:19
 * @author tangyi
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRatingRequest {
    private String username;
    private long movieId;
    private double rating;
}
