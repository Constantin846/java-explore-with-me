package ru.practicum;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeneratorConfiguration {
    @Bean
    public StatsClient statsClient(@Value("${stats-service.url}") String statsBaseUri) {
        return new StatsClientImpl(statsBaseUri);
    }
}
