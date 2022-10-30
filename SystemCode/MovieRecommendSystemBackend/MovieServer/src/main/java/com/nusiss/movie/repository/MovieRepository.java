package com.nusiss.movie.repository;

import com.nusiss.movie.model.entity.Movie;
import com.nusiss.movie.model.recommend.MovieContentRecommend;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Project: MovieRecommendSystem
 * Package: com.nusiss.movie.repository
 * <p>
 * Created by tangyi on 2022-10-21 18:38
 * @author tangyi
 */
@Repository
public interface MovieRepository extends MongoRepository<Movie, String> {
    Movie getMovieByMovieId(Long id);

    List<Movie> findByTitleLike(String title);
}
