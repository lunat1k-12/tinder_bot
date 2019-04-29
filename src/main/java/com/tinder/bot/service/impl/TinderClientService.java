package com.tinder.bot.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinder.bot.domain.*;
import com.tinder.bot.props.ChicksProperties;
import com.tinder.bot.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TinderClientService implements ClientService {

    private final ObjectMapper MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private String token;

    private boolean firstUpdate;

    private int startPeriod;

    public TinderClientService() {
        this.firstUpdate = true;
    }

    @Autowired
    private ChicksProperties chicksProperties;

    @Override
    public ChicksResponse loadChicks() {

        String requestUrl = chicksProperties.getLoadUrl();
        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod(requestUrl);
        configureHeaders(post);

        try {
            client.executeMethod(post);
            return MAPPER.readValue(post.getResponseBodyAsString(), ChicksResponse.class);
        } catch (IOException e) {
            log.info("Request Failed", e);
        }
        return null;

    }

    @Override
    public void likeChicks(List<Chick> chicks) {
        chicks.stream()
                .map(this::likeGirl)
                .filter(res -> res != null)
                .filter(ch -> !StringUtils.isEmpty(ch.getMatch().getId()))
                .map(LikeChickResponse::getMatch)
                .forEach(this::sendHiMessage);
    }

    private void sendHiMessage(Chick girl) {
        List<String> messages = Arrays.asList(
                chicksProperties.getFirstMessage(),
                chicksProperties.getSecondMessage(),
                chicksProperties.getThirdMessage());

        for (String msg : messages) {

            try {
                sendHiMessage(girl, msg);
                Thread.sleep(TimeUnit.SECONDS.toMillis(chicksProperties.getMessagePause()));
            } catch (Exception ex) {
                log.info("Send message failed", ex);
            }
        }
    }

    private void sendHiMessage(Chick girl, String message) {
        String requestUrl = chicksProperties.getMessageUrl().replace("{id}", girl.getId());
        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod(requestUrl);
        configureHeaders(post);

        try {
            MessageRequest msg = new MessageRequest(message);
            StringRequestEntity msgBody = new StringRequestEntity(MAPPER.writeValueAsString(msg), "application/json", "UTF-8");
            post.setRequestEntity(msgBody);
            client.executeMethod(post);
            String responseBody = post.getResponseBodyAsString();
            MsgResponse res = MAPPER.readValue(responseBody, MsgResponse.class);

            if (res.getStatusCode() == null) {
                log.info("Message sent to id: {}", girl.getId());
            } else {
                log.info("Send message failed for id {}, statusCode {}", girl.getId(), res.getStatusCode());
            }

        } catch (IOException e) {
            log.info("Send message Failed", e);
        }
    }

    private LikeChickResponse likeGirl(Chick chick) {
        String requestUrl = chicksProperties.getLikeUrl().replace("{id}", chick.getId());
        HttpClient client = new HttpClient();
        GetMethod get = new GetMethod(requestUrl);
        configureHeaders(get);

        try {
            client.executeMethod(get);
            String responseBody = get.getResponseBodyAsString().replace("\"match\":false", "\"match\":{}");
            LikeChickResponse res = MAPPER.readValue(responseBody, LikeChickResponse.class);
            log.info("Like girl with id {}, match - {}, likes left - {}", chick.getId(), !StringUtils.isEmpty(res.getMatch().getId()), res.getLikesRemaining());
            return res;
        } catch (IOException e) {
            log.info("Request Failed", e);
        }
        return null;
    }

    @Override
    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public void processPickup() {
        log.info("process pickup");
        ChicksResponse res = this.loadChicks();
        if (res.getStatus() != null && res.getStatus() == 200) {
            this.likeChicks(res.getResults());
        } else {
            log.info("Load failed with status {}", res.getClass());
        }
    }

    @Override
    public void processUpdates() {
        log.info("process updates");
        LocalDateTime updateTime = firstUpdate ?
                LocalDateTime.now().minus(startPeriod, ChronoUnit.DAYS) :
                LocalDateTime.now().minus(5, ChronoUnit.MINUTES);

        firstUpdate = false;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        UpdatesRequest updatesRequest = new UpdatesRequest(formatter.format(updateTime));

        String requestUrl = chicksProperties.getUpdatesUrl();
        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod(requestUrl);
        configureHeaders(post);

        try {
            StringRequestEntity updBody = new StringRequestEntity(MAPPER.writeValueAsString(updatesRequest), "application/json", "UTF-8");
            post.setRequestEntity(updBody);
            client.executeMethod(post);
            String responseBody = post.getResponseBodyAsString();
            UpdatesResponse res = MAPPER.readValue(responseBody, UpdatesResponse.class);

            log.info("Matches loaded: {}", res.getMatches().size());

            res.getMatches().stream()
                    .filter(chick -> !StringUtils.isEmpty(chick.getId()))
                    .filter(chick -> chick.getMessages().isEmpty())
                    .forEach(this::sendHiMessage);

        } catch (IOException e) {
            log.info("Request Failed", e);
        }
    }

    @Override
    public void setStartPeriod(int period) {
        this.startPeriod = period;
    }

    private void configureHeaders(PostMethod post) {
        post.setRequestHeader("X-Auth-Token", token);
        post.setRequestHeader("Content-type", "application/json");
        post.setRequestHeader("User-agent", "Tinder/7.5.3 (iPhone; iOS 10.3.2; Scale/2.00)");
    }

    private void configureHeaders(GetMethod post) {
        post.setRequestHeader("X-Auth-Token", token);
        post.setRequestHeader("Content-type", "application/json");
        post.setRequestHeader("User-agent", "Tinder/7.5.3 (iPhone; iOS 10.3.2; Scale/2.00)");
    }
}
