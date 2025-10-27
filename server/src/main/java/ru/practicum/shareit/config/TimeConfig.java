package ru.practicum.shareit.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TimeConfig {

    @Value("${app.booking.time-tolerance-seconds:5}")
    private int timeToleranceSeconds;

    public int getTimeToleranceSeconds() {
        return timeToleranceSeconds;
    }
}