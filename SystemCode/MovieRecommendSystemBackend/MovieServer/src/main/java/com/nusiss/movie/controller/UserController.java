package com.nusiss.movie.controller;

import com.nusiss.movie.model.entity.Movie;
import com.nusiss.movie.model.entity.RatedMovie;
import com.nusiss.movie.model.entity.Rating;
import com.nusiss.movie.model.entity.User;
import com.nusiss.movie.model.request.UserLoginRequest;
import com.nusiss.movie.model.request.UserPrefGenresRequest;
import com.nusiss.movie.model.request.UserRegisterRequest;
import com.nusiss.movie.service.MovieService;
import com.nusiss.movie.service.UserService;
import com.nusiss.movie.utils.DateUtil;
import com.nusiss.movie.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.nusiss.movie.constant.Genres.GENRES_SET;

/**
 * Project: MovieRecommendSystem
 * Package: com.nusiss.movie.controller
 *
 * Created by tangyi on 2022-10-21 18:30
 * @author tangyi
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private MovieService movieService;

    @PostMapping("/register")
    public Result<?> addUser(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userService.isUserExist(userRegisterRequest.getUsername())) {
            return Result.fail("User already exists");
        }
        if (userService.userRegister(userRegisterRequest.getUsername(), userRegisterRequest.getPassword())) {
            return Result.ok("Register success");
        }
        return Result.fail("Register failed");
    }

    @PostMapping("/login")
    public Result<?> login(@RequestBody UserLoginRequest userLoginRequest) {
        if (userLoginRequest.getUsername() == null || userLoginRequest.getUsername().length() == 0) {
            return Result.fail("Username cannot be empty");
        }
        if (userLoginRequest.getPassword() == null || userLoginRequest.getPassword().length() == 0) {
            return Result.fail("Password cannot be empty");
        }
        if (!userService.isUserExist(userLoginRequest.getUsername())) {
            return Result.fail("User not found");
        }
        String token = userService.userLogin(userLoginRequest.getUsername(), userLoginRequest.getPassword());
        if (token.length() != 0) {
            Map<String, String> map = new HashMap<>();
            map.put("token", token);
            map.put("username", userLoginRequest.getUsername());
            map.put("first", userService.isFirst(userLoginRequest.getUsername()) ? "yes" : "no");
            return Result.ok(map);
        }
        return Result.fail("Login failed");
    }

    @PostMapping("/updatePrefGenres")
    public Result<?> updatePrefGenres(@RequestBody UserPrefGenresRequest userPrefGenresRequest) {
        String username = userPrefGenresRequest.getUsername();
        String genres = userPrefGenresRequest.getGenres();
        List<String> genresList = Arrays.asList(genres.split(","));
        for (String genre : genresList) {
            if (!GENRES_SET.contains(genre)) {
                return Result.fail("Invalid genres");
            }
        }
        if (userService.userUpdateGenresPref(username, genresList)) {
            return Result.ok("Update success");
        }
        return Result.fail("Update failed");
    }

    @GetMapping("/ratingHistory")
    public Result<?> ratingHistory(@RequestParam String username) {
        List<Rating> ratings = userService.getRatingHistory(username);
        List<RatedMovie> ratedMovies = new ArrayList<>();
        for (Rating rating : ratings) {
            long movieId = rating.getMovieId();
            Movie movie = movieService.getMovieById(movieId);
            if (movie != null) {
                RatedMovie ratedMovie = new RatedMovie();
                ratedMovie.setMovieId(movieId);
                ratedMovie.setTitle(movie.getTitle());
                ratedMovie.setPosterPath(movie.getPosterPath());
                ratedMovie.setYourRating(rating.getRating());
                ratedMovie.setRatingTime(DateUtil.timestampToString(rating.getTimestamp()));
                ratedMovies.add(ratedMovie);
            }
        }
        return Result.ok(ratedMovies);
    }

    @GetMapping("/recommendMovies")
    public Result<?> recommendMovies(@RequestParam String username) {
        if (username == null || username.length() == 0 || !userService.isUserExist(username)) {
            return Result.ok(new ArrayList<>());
        }
        List<Movie> movies = userService.getRecommendMovies(username);
        if (movies.size() == 0) {
            return Result.fail("No movies found");
        }
        return Result.ok(movies);
    }

    @GetMapping("/userPrefGenres")
    public Result<?> getUserPrefGenres(@RequestParam String username) {
        String genres = userService.getUserPrefGenres(username);
        Map<String, String> map = new HashMap<>();
        map.put("genres", genres);
        return Result.ok(map);
    }

    @GetMapping("/userPrefGenreList")
    public Result<?> getUserPrefGenreList(@RequestParam String username) {
        List<String> genreList = userService.getUserPrefGenreList(username);
        Map<String, List<String>> map = new HashMap<>();
        map.put("genreList", genreList);
        return Result.ok(map);
    }

    @GetMapping("/userDetail")
    public Result<?> userDetail(@RequestParam String username) {
        if (!userService.isUserExist(username)) {
            return Result.fail("User not found");
        }
        User user = userService.getUserDetail(username);
        return Result.ok(user);
    }
}
