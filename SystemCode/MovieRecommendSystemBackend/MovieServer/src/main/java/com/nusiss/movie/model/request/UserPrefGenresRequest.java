package com.nusiss.movie.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Project: MovieRecommendSystem
 * Package: com.nusiss.movie.model.request
 * <p>
 * Created by tangyi on 2022-10-27 03:38
 * @author tangyi
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPrefGenresRequest {
    String username;
    String genres;
}
