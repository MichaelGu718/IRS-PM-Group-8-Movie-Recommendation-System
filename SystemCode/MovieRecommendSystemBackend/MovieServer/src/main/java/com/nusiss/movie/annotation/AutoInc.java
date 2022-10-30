package com.nusiss.movie.annotation;

import java.lang.annotation.*;

/**
 * Project: MovieRecommendSystem
 * Package: com.nusiss.movie.annotation
 * <p>
 * Created by tangyi on 2022-10-27 01:53
 * @author zjh
 * https://blog.csdn.net/qq_36989302/article/details/98944708
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface AutoInc {
}
