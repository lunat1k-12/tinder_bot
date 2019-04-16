package com.tinder.bot.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "chicks")
@Data
public class ChicksProperties {

    private String LoadUrl;
    private String likeUrl;
    private String messageUrl;
    private String updatesUrl;
    private String firstMessage;
    private String secondMessage;
    private String thirdMessage;
    private Long messagePause;
}
