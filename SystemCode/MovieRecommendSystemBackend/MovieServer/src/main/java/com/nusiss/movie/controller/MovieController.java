package com.nusiss.movie.controller;

import com.nusiss.movie.model.entity.Movie;
import com.nusiss.movie.model.entity.Rating;
import com.nusiss.movie.model.request.UserRatingRequest;
import com.nusiss.movie.service.MovieService;
import com.nusiss.movie.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project: MovieRecommendSystem
 * Package: com.nusiss.movie.controller
 *
 * Created by tangyi on 2022-10-21 18:29
 * @author tangyi
 */
@RestController
@RequestMapping("/movie")
public class MovieController {
    @Autowired
    private MovieService movieService;

    @GetMapping("/movieDetail/{movieId}")
    public Result<?> getMovieDetail(@PathVariable Long movieId) {
        Movie movie = movieService.getMovieById(movieId);
        if (movie == null) {
            return Result.fail("Movie not found");
        }
        return Result.ok(movie);
    }

    @GetMapping("/topHotMovies/{movieNum}")
    public Result<?> getTopHotMovies(@PathVariable Integer movieNum) {
        if (movieNum <= 0) {
            return Result.fail("num must be greater than 0");
        }
        if (movieNum > 100) {
            return Result.fail("num must be less than 100");
        }
        return Result.ok(movieService.getTopHotMovies(movieNum));
    }

    @GetMapping("/topRatingMovies/{movieNum}")
    public Result<?> getTopRatingMovies(@PathVariable Integer movieNum) {
        if (movieNum <= 0) {
            return Result.fail("num must be greater than 0");
        }
        if (movieNum > 100) {
            return Result.fail("num must be less than 100");
        }
        return Result.ok(movieService.getTopRatingMovies(movieNum));
    }

    @GetMapping("/similarMovies/{movieId}")
    public Result<?> getSimilarMovies(@PathVariable Long movieId) {
        if (movieId <= 0) {
            return Result.fail("movieId must be greater than 0");
        }
        return Result.ok(movieService.getSimilarMovies(movieId, 8));
    }

    @GetMapping("/searchMovies")
    public Result<?> searchMovies(@RequestParam String keyword) {
        if (keyword == null || keyword.length() == 0) {
            return Result.ok(new ArrayList<>());
        }
        Map<String, List<Movie>> movieMap = new HashMap<>();
        movieMap.put("movieList", movieService.searchMovies(keyword));
        return Result.ok(movieMap);
    }

    @PostMapping("/rateMovie")
    public Result<?> rateMovie(@RequestBody UserRatingRequest userRatingRequest) {
        if (movieService.rateMovie(userRatingRequest.getUsername(), userRatingRequest.getMovieId(), userRatingRequest.getRating())) {
            return Result.ok("Rate movie successfully");
        }
        return Result.fail("Rate movie failed");
    }

    @GetMapping("/genreTopMovies")
    public Result<?> getGenreTopMovies(@RequestParam String genre) {
        if (genre == null || genre.length() == 0) {
            return Result.ok(new ArrayList<>());
        }
        return Result.ok(movieService.getTopMoviesByGenre(genre).subList(0, 8));
    }
}
