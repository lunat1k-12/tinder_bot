package com.tinder.bot.service;

import com.tinder.bot.domain.Chick;
import com.tinder.bot.domain.ChicksResponse;

import java.util.List;

public interface ClientService {

    ChicksResponse loadChicks();

    void likeChicks(List<Chick> chicks);

    void setToken(String token);

    void processPickup();

    void processUpdates();

    void setStartPeriod(int period);
}
