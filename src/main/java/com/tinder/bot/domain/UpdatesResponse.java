package com.tinder.bot.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class UpdatesResponse {

    @JsonProperty("matches")
    private List<Chick> matches;
}
