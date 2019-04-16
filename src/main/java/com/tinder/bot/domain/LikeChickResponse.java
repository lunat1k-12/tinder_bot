package com.tinder.bot.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeChickResponse {

    @JsonProperty("match")
    private Chick match;

    @JsonProperty("likes_remaining")
    private int likesRemaining;
}
