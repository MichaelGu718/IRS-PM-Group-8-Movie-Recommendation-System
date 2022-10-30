package com.nusiss.movie.service.impl;

import com.nusiss.movie.constant.UserConstant;
import com.nusiss.movie.listener.SaveMongoEventListener;
import com.nusiss.movie.model.entity.Movie;
import com.nusiss.movie.model.entity.Rating;
import com.nusiss.movie.model.entity.User;
import com.nusiss.movie.model.recommend.MovieRecommend;
import com.nusiss.movie.repository.RatingRepository;
import com.nusiss.movie.repository.UserRecsRepository;
import com.nusiss.movie.repository.UserRepository;
import com.nusiss.movie.service.MovieService;
import com.nusiss.movie.service.UserService;
import com.nusiss.movie.utils.EncryptUtil;
import com.nusiss.movie.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Project: MovieRecommendSystem
 * Package: com.nusiss.movie.service.impl
 * <p>
 * Created by tangyi on 2022-10-21 18:37
 * @author tangyi
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRecsRepository userRecsRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private MovieService movieService;

    @Override
    public boolean isUserExist(String username) {
        return userRepository.findByUsername(username) != null;
    }

    @Override
    public boolean userRegister(String username, String password) {
        if (isUserExist(username)) {
            return false;
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(EncryptUtil.getMD5Str(password));
        user.setFirst(true);
        user.setRegisterTime(System.currentTimeMillis());
        userRepository.save(user);
        return true;
    }

    @Override
    public String userLogin(String username, String password) {
        if (!isUserExist(username)) {
            return "";
        }
        User user = userRepository.findByUsername(username);
        if (user.getPassword().equals(EncryptUtil.getMD5Str(password))) {
            TokenUtil.generateToken(user.getUserId());
            return TokenUtil.generateToken(user.getUserId());
        }
        return "";
    }

    @Override
    public boolean userUpdateGenresPref(String username, List<String> genres) {
        User user = userRepository.findByUsername(username);
        user.setPrefGenres(genres);
        user.setFirst(false);
        userRepository.save(user);
        return true;
    }

    @Override
    public List<Rating> getRatingHistory(String username) {
        if  (username == null || "".equals(username)) {
            return new ArrayList<>();
        }
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return new ArrayList<>();
        }
        Long userId = user.getUserId();
        return ratingRepository.findAllByUserId(userId);
    }

    @Override
    public List<Movie> getRecommendMovies(String username) {
        User user = userRepository.findByUsername(username);
        long userId = user.getUserId();
        List<MovieRecommend> movieRecommends = new ArrayList<>();
        if (userRecsRepository.findByUserId(userId) != null) {
            movieRecommends.addAll(userRecsRepository.findByUserId(userId).getRecs());
        }
        List<Movie> movies = new ArrayList<>();
        for (MovieRecommend movieRecommend : movieRecommends) {
            Movie movie = movieService.getMovieById(movieRecommend.getMovieId());
            if (movie != null) {
                movies.add(movie);
            }
        }
        if (movies.size() == 0) {
            for (String genre : user.getPrefGenres()) {
                int i = 0;
                for (Movie movie : movieService.getTopMoviesByGenre(genre)) {
                    i++;
                    if (i > 4) {
                        break;
                    }
                    movies.add(movie);
                }
            }
        }
        if (movies.size() == 0) {
            movies.addAll(movieService.getTopHotMovies(4));
            movies.addAll(movieService.getTopRatingMovies(4));
        }
        return movies;
    }

    @Override
    public Long getUserIdByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return UserConstant.NEGATIVE_USER;
        }
        return user.getUserId();
    }

    @Override
    public Boolean isFirst(String username) {
        User user = userRepository.findByUsername(username);
        return user.isFirst();
    }

    @Override
    public String getUserPrefGenres(String username) {
        User user = userRepository.findByUsername(username);
        List<String> genreList = user.getPrefGenres();
        if (genreList.size() == 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String genre : genreList) {
            stringBuilder.append(genre).append(",");
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }

    @Override
    public List<String> getUserPrefGenreList(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return new ArrayList<>();
        }
        return user.getPrefGenres();
    }

    @Override
    public User getUserDetail(String username) {
        return userRepository.findByUsername(username);
    }
}
