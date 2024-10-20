package ru.practicum;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class GeneratorConfiguration {
    @Bean
    public StatsClient statsClient(@Value("${stats-service.url}") String statsBaseUri) {
        return new StatsClientImpl(statsBaseUri);
    }

    private static final int MAX_POOL_SIZE = 2;

    @Bean
    public Executor executor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        return executor;
    }
}
