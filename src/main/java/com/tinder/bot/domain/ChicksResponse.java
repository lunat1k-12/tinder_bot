package com.tinder.bot.domain;

import lombok.Data;

import java.util.List;

@Data
public class ChicksResponse {

    private Integer status;

    private List<Chick> results;
}
