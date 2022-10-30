package com.nusiss.movie.repository;

import com.nusiss.movie.model.entity.MovieRateCount;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Project: MovieRecommendSystem
 * Package: com.nusiss.movie.repository
 * <p>
 * Created by tangyi on 2022-10-23 00:35
 * @author tangyi
 */
@Repository
public interface MovieRateCountRepository extends MongoRepository<MovieRateCount, String> {
}
