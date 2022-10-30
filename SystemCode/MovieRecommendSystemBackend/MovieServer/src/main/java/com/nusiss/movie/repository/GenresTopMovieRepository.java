package com.nusiss.movie.repository;

import com.nusiss.movie.model.recommend.GenresTopMovie;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Project: MovieRecommendSystem
 * Package: com.nusiss.movie.repository
 * <p>
 * Created by tangyi on 2022-10-28 16:35
 * @author tangyi
 */
public interface GenresTopMovieRepository extends MongoRepository<GenresTopMovie, String> {

    GenresTopMovie getGenresTopMovieByGenres(String genre);
}
