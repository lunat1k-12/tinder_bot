package com.tinder.bot.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Chick {

    @JsonProperty("group_matched")
    private Boolean groupMatched;

    @JsonProperty("distance_mi")
    private Long distanceMi;

    @JsonProperty("content_hash")
    private String contentHash;

    @JsonProperty("common_friend_count")
    private Integer commonFriendCount;

    @JsonProperty("common_like_count")
    private Integer commonLikeCount;

    @JsonProperty("connection_count")
    private Integer connectionCount;

    @JsonProperty("_id")
    private String id;

    @JsonProperty("bio")
    private String bio;

    @JsonProperty("birth_date")
    private String birthDate;

    @JsonProperty("name")
    private String name;

    @JsonProperty("ping_time")
    private String pingTime;

    @JsonProperty("photos")
    private List<Photo> photos;

    @JsonProperty("gender")
    private Integer gender;

    @JsonProperty("birth_date_info")
    private String birthDateInfo;

    @JsonProperty("s_number")
    private String sNumber;

    @JsonProperty("messages")
    private List<Message> messages;
}
