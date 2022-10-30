package com.nusiss.movie.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nusiss.movie.annotation.AutoInc;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tangyi
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("user")
public class User {
    @JsonIgnore
    private String _id;

    @AutoInc
    private long userId;

    private String username;

    private String password;

    private boolean first;

    private long registerTime;

    private List<String> prefGenres = new ArrayList<>();
}