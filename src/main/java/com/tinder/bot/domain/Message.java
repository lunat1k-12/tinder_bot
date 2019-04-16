package com.tinder.bot.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Message {

    @JsonProperty("_id")
    private String id;
}
