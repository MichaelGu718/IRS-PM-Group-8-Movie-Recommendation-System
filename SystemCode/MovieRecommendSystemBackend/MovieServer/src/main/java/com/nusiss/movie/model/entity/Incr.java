package com.nusiss.movie.model.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Project: MovieRecommendSystem
 * Package: com.nusiss.movie.model.entity
 * <p>
 * Created by tangyi on 2022-10-27 02:16
 * @author zjh
 * https://blog.csdn.net/qq_36989302/article/details/98944708
 */
@Document
@Data
public class Incr {
    @Id
    private String id;
    private String collectionName;
    private Long incrId;
}