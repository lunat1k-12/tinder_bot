package com.tinder.bot.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatesRequest {

    @JsonProperty("last_activity_date")
    private String lastActivityDate;
}
