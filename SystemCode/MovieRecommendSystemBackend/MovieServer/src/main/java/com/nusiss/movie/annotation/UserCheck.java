package com.nusiss.movie.annotation;

import java.lang.annotation.*;

/**
 * Project: MovieRecommendSystem
 * Package: com.nusiss.movie.annotation
 * <p>
 * Created by tangyi on 2022-10-23 01:08
 * @author tangyi
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface UserCheck {
    String userType();
}
