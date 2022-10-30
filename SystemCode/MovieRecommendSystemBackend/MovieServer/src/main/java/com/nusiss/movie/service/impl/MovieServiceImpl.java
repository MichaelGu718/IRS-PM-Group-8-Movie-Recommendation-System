package com.nusiss.movie.service.impl;


import com.nusiss.movie.model.entity.*;
import com.nusiss.movie.model.recommend.MovieContentRecommend;
import com.nusiss.movie.model.recommend.MovieRecommend;
import com.nusiss.movie.repository.*;
import com.nusiss.movie.service.MovieService;
import com.nusiss.movie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.mapping.TextScore;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: MovieRecommendSystem
 * Package: com.nusiss.movie.service.impl
 * <p>
 * Created by tangyi on 2022-10-21 18:36
 * @author tangyi
 */
@Service
public class MovieServiceImpl implements MovieService {
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRateCountRepository movieRateCountRepository;

    @Autowired
    private MovieAvgScoreRepository movieAvgScoreRepository;

    @Autowired
    private MovieContentRecRepository movieContentRecRepository;

    @Autowired
    private GenresTopMovieRepository genresTopMovieRepository;

    @Override
    public Movie getMovieById(Long id) {
        Movie movie = movieRepository.getMovieByMovieId(id);
        if (movie == null) {
            return null;
        }
        if (movie.getPosterPath() == null || movie.getPosterPath().length() == 0) {
            movie.setPosterPath("https://upload.wikimedia.org/wikipedia/commons/b/b9/No_Cover.jpg");
        } else {
            movie.setPosterPath("https://image.tmdb.org/t/p/w500" + movie.getPosterPath());
        }
        List<String> imgUrls = movie.getBackdropPaths();
        for (int i = 0; i < imgUrls.size(); i++) {
            imgUrls.set(i, "https://image.tmdb.org/t/p/w500" + imgUrls.get(i));
        }
        movie.setBackdropPaths(imgUrls);
        List<String> videoUrls = movie.getYoutubeTrailerIds();
        for (int i = 0; i < videoUrls.size(); i++) {
            videoUrls.set(i, "https://www.youtube.com/embed/" + videoUrls.get(i));
        }
        movie.setYoutubeTrailerIds(videoUrls);
        return movie;
    }

    @Override
    public List<Movie> getMoviesByIds(List<Long> ids) {
        List<Movie> movies = new ArrayList<>();
        for (Long id : ids) {
            movies.add(getMovieById(id));
        }
        return movies;
    }

    @Override
    public List<Movie> getTopHotMovies(int num) {
        num = Math.min(num, 20);
        List<MovieRateCount> movieRateCountList = movieRateCountRepository.findAll(Sort.by(Sort.Direction.DESC, "count"));
        List<Movie> movies = new ArrayList<>();
        for (MovieRateCount movieRateCount : movieRateCountList) {
            movies.add(getMovieById(movieRateCount.getMovieId()));
            if (movies.size() >= num) {
                break;
            }
        }
        return movies;
    }

    @Override
    public List<Movie> getTopRatingMovies(Integer movieNum) {
        movieNum = Math.min(movieNum, 20);
        List<MovieAvgScore> movieAvgScoreList = movieAvgScoreRepository.findAll(Sort.by(Sort.Direction.DESC, "avgScore"));
        List<Movie> movies = new ArrayList<>();
        for (MovieAvgScore movieAvgScore : movieAvgScoreList) {
            movies.add(getMovieById(movieAvgScore.getMovieId()));
            if (movies.size() >= movieNum) {
                break;
            }
        }
        return movies;
    }

    @Override
    public List<Movie> getSimilarMovies(Long movieId, int num) {
        num = Math.min(num, 10);
        num = Math.max(num, 1);
        MovieContentRecommend movieContentRecommend = movieContentRecRepository.getMovieContentRecommendByMovieId(movieId);
        List<MovieRecommend> similarMovieIds = movieContentRecommend.getRecs();
        List<Movie> movies = new ArrayList<>();
        for (MovieRecommend movieRecommend : similarMovieIds) {
            movies.add(getMovieById(movieRecommend.getMovieId()));
            if (movies.size() >= num) {
                break;
            }
        }
        return movies;
    }

    @Override
    public List<Movie> searchMovies(String title) {
        List<Movie> movies = movieRepository.findByTitleLike(title);
        for (Movie movie : movies) {
            if (movie.getPosterPath() == null) {
                movie.setPosterPath("https://upload.wikimedia.org/wikipedia/commons/b/b9/No_Cover.jpg");
            } else {
                movie.setPosterPath("https://image.tmdb.org/t/p/w500" + movie.getPosterPath());
            }
            List<String> imgUrls = movie.getBackdropPaths();
            for (int i = 0; i < imgUrls.size(); i++) {
                imgUrls.set(i, "https://image.tmdb.org/t/p/w500" + imgUrls.get(i));
            }
            movie.setBackdropPaths(imgUrls);
            List<String> videoUrls = movie.getYoutubeTrailerIds();
            for (int i = 0; i < videoUrls.size(); i++) {
                videoUrls.set(i, "https://www.youtube.com/embed/" + videoUrls.get(i));
            }
            movie.setYoutubeTrailerIds(videoUrls);
        }
        return movies;
    }

    @Override
    public List<Movie> getTopMoviesByGenre(String genre) {
        List<MovieRecommend> movieRecommendList = genresTopMovieRepository.getGenresTopMovieByGenres(genre).getRecs();
        List<Movie> movies = new ArrayList<>();
        for (MovieRecommend movieRecommend : movieRecommendList) {
            movies.add(getMovieById(movieRecommend.getMovieId()));
        }
        return movies;
    }

    @Override
    public boolean rateMovie(String username, long movieId, double rate) {
        if (rate == 0) {
            rate = 4;
        }
        User user = userRepository.findByUsername(username);
        long userId = user.getUserId();
        Rating rating = new Rating();
        rating.setUserId(userId);
        rating.setMovieId(movieId);
        rating.setRating(rate);
        rating.setTimestamp(System.currentTimeMillis() / 1000);
        if (ratingRepository.findByUserIdAndMovieId(userId, movieId) == null) {
            ratingRepository.save(rating);
        }
        return true;
    }
}
