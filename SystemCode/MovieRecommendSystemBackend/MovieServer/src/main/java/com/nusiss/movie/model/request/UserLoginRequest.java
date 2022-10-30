package com.nusiss.movie.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Project: MovieRecommendSystem
 * Package: com.nusiss.movie.model.request
 * <p>
 * Created by tangyi on 2022-10-26 22:17
 * @author tangyi
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginRequest {
    private String username;
    private String password;
}
