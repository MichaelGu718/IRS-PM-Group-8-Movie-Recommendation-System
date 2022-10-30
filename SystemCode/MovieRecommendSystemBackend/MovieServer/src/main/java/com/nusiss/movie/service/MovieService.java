package com.nusiss.movie.service;

import com.nusiss.movie.model.entity.Movie;
import com.nusiss.movie.utils.Result;

import java.util.Collection;
import java.util.List;

/**
 * Project: MovieRecommendSystem
 * Package: com.nusiss.movie.service
 * <p>
 * Created by tangyi on 2022-10-21 18:32
 * @author tangyi
 */
public interface MovieService {
    /**
     * get movie by id
     * @param id movie id
     * @return movie
     */
    Movie getMovieById(Long id);

    /**
     * get movie list
     * @param ids movie id list
     * @return movie list
     */
    List<Movie> getMoviesByIds(List<Long> ids);

    /**
     * get movies with top-rated times
     * @param num number of movie list
     * @return movie list
     */
    List<Movie> getTopHotMovies(int num);

    List<Movie> getTopRatingMovies(Integer movieNum);

    List<Movie> getSimilarMovies(Long movieId, int num);

    List<Movie> searchMovies(String title);

    List<Movie> getTopMoviesByGenre(String genre);

    boolean rateMovie(String username, long movieId, double rate);
}
