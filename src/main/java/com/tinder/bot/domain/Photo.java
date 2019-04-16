package com.tinder.bot.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Photo {

    @JsonProperty("group_matched")
    private Boolean groupMatched;

    @JsonProperty("distance_mi")
    private Integer distanceMi;

    @JsonProperty("content_hash")
    private String contentHash;

    @JsonProperty("common_friends")
    private List<Object> commonFriends;

    @JsonProperty("common_likes")
    private List<Object> commonLikes;

    @JsonProperty("common_friend_count")
    private Integer commonFriendCount;

    @JsonProperty("common_like_count")
    private Integer commonLikeCount;

    @JsonProperty("connection_count")
    private Integer connectionCount;

    @JsonProperty("id")
    private String id;
}
