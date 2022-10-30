package com.nusiss.movie.service;

import com.nusiss.movie.model.entity.Movie;
import com.nusiss.movie.model.entity.Rating;
import com.nusiss.movie.model.entity.User;

import java.util.List;

/**
 * Project: MovieRecommendSystem
 * Package: com.nusiss.movie.service
 * <p>
 * Created by tangyi on 2022-10-21 18:32
 * @author tangyi
 */
public interface UserService {
    public boolean isUserExist(String username);

    public boolean userRegister(String username, String password);

    public String userLogin(String username, String password);

    public boolean userUpdateGenresPref(String username, List<String> genres);

    public List<Rating> getRatingHistory(String username);

    List<Movie> getRecommendMovies(String username);

    Long getUserIdByUsername(String username);

    Boolean isFirst(String username);

    String getUserPrefGenres(String username);

    List<String> getUserPrefGenreList(String username);

    User getUserDetail(String username);
}
