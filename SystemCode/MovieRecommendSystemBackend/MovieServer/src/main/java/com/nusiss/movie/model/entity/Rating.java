package com.nusiss.movie.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Project: MovieRecommendSystem
 * Package: com.nusiss.movie.model.entity
 * <p>
 * Created by tangyi on 2022-10-23 01:05
 * @author tangyi
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("rating")
public class Rating {
    private long userId;
    private long movieId;
    private double rating;
    private long timestamp;
}
