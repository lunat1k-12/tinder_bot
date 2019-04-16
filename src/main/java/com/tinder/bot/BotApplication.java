package com.tinder.bot;

import com.tinder.bot.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootApplication
public class BotApplication implements CommandLineRunner {

    @Autowired
    private ClientService service;

    public static void main(String[] args) {
        SpringApplication.run(BotApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String token = args[0];
        service.setToken(token);

        ScheduledExecutorService pickupService = Executors.newScheduledThreadPool(2);
        pickupService.scheduleAtFixedRate(service::processPickup,0, 30, TimeUnit.SECONDS);
        pickupService.scheduleAtFixedRate(service::processUpdates,0, 5, TimeUnit.MINUTES);
    }
}
