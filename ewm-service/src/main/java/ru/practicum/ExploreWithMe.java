package ru.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ExploreWithMe {

    public static void main(String[] args) {
        SpringApplication.run(ExploreWithMe.class);
    }

}
