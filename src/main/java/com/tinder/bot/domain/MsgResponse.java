package com.tinder.bot.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MsgResponse {

    @JsonProperty("statusCode")
    private Integer statusCode;
}
