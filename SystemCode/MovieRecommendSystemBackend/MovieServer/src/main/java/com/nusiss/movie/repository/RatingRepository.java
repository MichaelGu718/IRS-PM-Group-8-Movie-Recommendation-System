package com.nusiss.movie.repository;

import com.nusiss.movie.model.entity.Movie;
import com.nusiss.movie.model.entity.Rating;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Project: MovieRecommendSystem
 * Package: com.nusiss.movie.repository
 * <p>
 * Created by tangyi on 2022-10-23 01:06
 * @author tangyi
 */
@Repository
public interface RatingRepository extends MongoRepository<Rating, String> {

    List<Rating> findAllByUserId(Long userId);

    Rating findByUserIdAndMovieId(Long userId, Long movieId);
}
