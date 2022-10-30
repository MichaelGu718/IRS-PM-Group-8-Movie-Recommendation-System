package com.nusiss.movie.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author tangyi
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("movie")
public class Movie {
    @JsonIgnore
    private String _id;

    private long movieId;

    private String title;

    private String plotSummary;

    private int runtime;

    private String releaseYear;

    private Double avgRating;

    private List<String> languages;

    private List<String> genres;

    private List<String> actors;

    private List<String> directors;

    private List<String> backdropPaths;

    private List<String> youtubeTrailerIds;

    private String posterPath;
}
