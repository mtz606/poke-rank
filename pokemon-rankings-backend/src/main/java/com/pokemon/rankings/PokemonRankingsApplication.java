package com.pokemon.rankings;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PokemonRankingsApplication {

    public static void main(String[] args) {
        SpringApplication.run(PokemonRankingsApplication.class, args);
    }
} 