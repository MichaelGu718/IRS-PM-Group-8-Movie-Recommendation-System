package com.nusiss.movie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Project: MovieRecommendSystem
 * Package: PACKAGE_NAME
 * <p>
 * Created by tangyi on 2022-10-23 14:49
 * @author tangyi
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.nusiss")
public class MovieServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(MovieServerApplication.class, args);
    }
}
