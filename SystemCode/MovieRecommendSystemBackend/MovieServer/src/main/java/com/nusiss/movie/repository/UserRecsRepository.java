package com.nusiss.movie.repository;

import com.nusiss.movie.model.recommend.MovieRecommend;
import com.nusiss.movie.model.recommend.OfflineUserRecommend;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Project: MovieRecommendSystem
 * Package: com.nusiss.movie.repository
 * <p>
 * Created by tangyi on 2022-10-28 14:28
 */
public interface UserRecsRepository extends MongoRepository<OfflineUserRecommend, String> {
    OfflineUserRecommend findByUserId(long userId);
}
