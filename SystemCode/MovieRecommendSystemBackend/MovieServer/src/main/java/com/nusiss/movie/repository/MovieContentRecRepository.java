package com.nusiss.movie.repository;

import com.nusiss.movie.model.recommend.MovieContentRecommend;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Project: MovieRecommendSystem
 * Package: com.nusiss.movie.repository
 * <p>
 * Created by tangyi on 2022-10-27 03:01
 * @author tangyi
 */
public interface MovieContentRecRepository extends MongoRepository<MovieContentRecommend, Long> {

    MovieContentRecommend getMovieContentRecommendByMovieId(Long movieId);
}
