package com.zebiso.Backend.config;

import com.zebiso.Backend.model.Match;
import com.zebiso.Backend.repository.MatchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    CommandLineRunner seedMatches(MatchRepository matchRepository) {
        return args -> {
            if (matchRepository.count() > 0) {
                return;
            }

            List<Match> matches = List.of(
                    match("México", "África do Sul", LocalDateTime.of(2026, 6, 11, 16, 0), "Fase de Grupos", "A", "Cidade do México"),
                    match("Coreia do Sul", "República Tcheca", LocalDateTime.of(2026, 6, 11, 23, 0), "Fase de Grupos", "A", "Guadalajara"),
                    match("Canadá", "Bósnia-Herzegovina", LocalDateTime.of(2026, 6, 12, 16, 0), "Fase de Grupos", "B", "Toronto"),
                    match("Estados Unidos", "Paraguai", LocalDateTime.of(2026, 6, 12, 22, 0), "Fase de Grupos", "D", "Los Angeles"),
                    match("Catar", "Suíça", LocalDateTime.of(2026, 6, 13, 16, 0), "Fase de Grupos", "B", "San Francisco"),
                    match("Brasil", "Marrocos", LocalDateTime.of(2026, 6, 13, 19, 0), "Fase de Grupos", "C", "Nova York / New Jersey"),
                    match("Haiti", "Escócia", LocalDateTime.of(2026, 6, 13, 22, 0), "Fase de Grupos", "C", "Boston"),
                    match("Austrália", "Turquia", LocalDateTime.of(2026, 6, 14, 1, 0), "Fase de Grupos", "D", "Vancouver"),
                    match("Alemanha", "Curaçao", LocalDateTime.of(2026, 6, 14, 14, 0), "Fase de Grupos", "E", "Houston"),
                    match("Países Baixos", "Japão", LocalDateTime.of(2026, 6, 14, 17, 0), "Fase de Grupos", "F", "Dallas"),
                    match("Costa do Marfim", "Equador", LocalDateTime.of(2026, 6, 14, 20, 0), "Fase de Grupos", "E", "Filadélfia"),
                    match("Suécia", "Tunísia", LocalDateTime.of(2026, 6, 14, 23, 0), "Fase de Grupos", "F", "Monterrey"),
                    match("Espanha", "Cabo Verde", LocalDateTime.of(2026, 6, 15, 13, 0), "Fase de Grupos", "H", "Atlanta"),
                    match("Bélgica", "Egito", LocalDateTime.of(2026, 6, 15, 16, 0), "Fase de Grupos", "G", "Seattle"),
                    match("Arábia Saudita", "Uruguai", LocalDateTime.of(2026, 6, 15, 19, 0), "Fase de Grupos", "H", "Miami"),
                    match("Irã", "Nova Zelândia", LocalDateTime.of(2026, 6, 15, 22, 0), "Fase de Grupos", "G", "Los Angeles"),
                    match("França", "Senegal", LocalDateTime.of(2026, 6, 16, 16, 0), "Fase de Grupos", "I", "Nova York / New Jersey"),
                    match("Iraque", "Noruega", LocalDateTime.of(2026, 6, 16, 19, 0), "Fase de Grupos", "I", "Boston"),
                    match("Argentina", "Argélia", LocalDateTime.of(2026, 6, 16, 22, 0), "Fase de Grupos", "J", "Kansas City"),
                    match("Áustria", "Jordânia", LocalDateTime.of(2026, 6, 17, 1, 0), "Fase de Grupos", "J", "San Francisco"),
                    match("Portugal", "Congo (RD)", LocalDateTime.of(2026, 6, 17, 14, 0), "Fase de Grupos", "K", "Houston"),
                    match("Inglaterra", "Croácia", LocalDateTime.of(2026, 6, 17, 17, 0), "Fase de Grupos", "L", "Dallas"),
                    match("Gana", "Panamá", LocalDateTime.of(2026, 6, 17, 20, 0), "Fase de Grupos", "L", "Toronto"),
                    match("Uzbequistão", "Colômbia", LocalDateTime.of(2026, 6, 17, 23, 0), "Fase de Grupos", "K", "Cidade do México"),
                    match("República Tcheca", "África do Sul", LocalDateTime.of(2026, 6, 18, 13, 0), "Fase de Grupos", "A", "Atlanta"),
                    match("Suíça", "Bósnia-Herzegovina", LocalDateTime.of(2026, 6, 18, 16, 0), "Fase de Grupos", "B", "Los Angeles"),
                    match("Canadá", "Catar", LocalDateTime.of(2026, 6, 18, 19, 0), "Fase de Grupos", "B", "Vancouver"),
                    match("México", "Coreia do Sul", LocalDateTime.of(2026, 6, 18, 22, 0), "Fase de Grupos", "A", "Guadalajara"),
                    match("Estados Unidos", "Austrália", LocalDateTime.of(2026, 6, 19, 16, 0), "Fase de Grupos", "D", "Seattle"),
                    match("Escócia", "Marrocos", LocalDateTime.of(2026, 6, 19, 19, 0), "Fase de Grupos", "C", "Boston"),
                    match("Brasil", "Haiti", LocalDateTime.of(2026, 6, 19, 21, 30), "Fase de Grupos", "C", "Filadélfia"),
                    match("Turquia", "Paraguai", LocalDateTime.of(2026, 6, 20, 0, 0), "Fase de Grupos", "D", "San Francisco"),
                    match("Países Baixos", "Suécia", LocalDateTime.of(2026, 6, 20, 14, 0), "Fase de Grupos", "F", "Houston"),
                    match("Alemanha", "Costa do Marfim", LocalDateTime.of(2026, 6, 20, 17, 0), "Fase de Grupos", "E", "Toronto"),
                    match("Equador", "Curaçao", LocalDateTime.of(2026, 6, 20, 21, 0), "Fase de Grupos", "E", "Kansas City"),
                    match("Tunísia", "Japão", LocalDateTime.of(2026, 6, 21, 1, 0), "Fase de Grupos", "F", "Monterrey"),
                    match("Espanha", "Arábia Saudita", LocalDateTime.of(2026, 6, 21, 13, 0), "Fase de Grupos", "H", "Atlanta"),
                    match("Bélgica", "Irã", LocalDateTime.of(2026, 6, 21, 16, 0), "Fase de Grupos", "G", "Los Angeles"),
                    match("Uruguai", "Cabo Verde", LocalDateTime.of(2026, 6, 21, 19, 0), "Fase de Grupos", "H", "Miami"),
                    match("Nova Zelândia", "Egito", LocalDateTime.of(2026, 6, 21, 22, 0), "Fase de Grupos", "G", "Vancouver"),
                    match("Argentina", "Áustria", LocalDateTime.of(2026, 6, 22, 14, 0), "Fase de Grupos", "J", "Dallas"),
                    match("França", "Iraque", LocalDateTime.of(2026, 6, 22, 18, 0), "Fase de Grupos", "I", "Filadélfia"),
                    match("Noruega", "Senegal", LocalDateTime.of(2026, 6, 22, 21, 0), "Fase de Grupos", "I", "Nova York / New Jersey"),
                    match("Jordânia", "Argélia", LocalDateTime.of(2026, 6, 23, 0, 0), "Fase de Grupos", "J", "San Francisco"),
                    match("Portugal", "Uzbequistão", LocalDateTime.of(2026, 6, 23, 14, 0), "Fase de Grupos", "K", "Houston"),
                    match("Inglaterra", "Gana", LocalDateTime.of(2026, 6, 23, 17, 0), "Fase de Grupos", "L", "Boston"),
                    match("Panamá", "Croácia", LocalDateTime.of(2026, 6, 23, 20, 0), "Fase de Grupos", "L", "Toronto"),
                    match("Colômbia", "Congo (RD)", LocalDateTime.of(2026, 6, 23, 23, 0), "Fase de Grupos", "K", "Guadalajara"),
                    match("Suíça", "Canadá", LocalDateTime.of(2026, 6, 24, 16, 0), "Fase de Grupos", "B", "Vancouver"),
                    match("Bósnia-Herzegovina", "Catar", LocalDateTime.of(2026, 6, 24, 16, 0), "Fase de Grupos", "B", "Seattle"),
                    match("Escócia", "Brasil", LocalDateTime.of(2026, 6, 24, 19, 0), "Fase de Grupos", "C", "Miami"),
                    match("Marrocos", "Haiti", LocalDateTime.of(2026, 6, 24, 19, 0), "Fase de Grupos", "C", "Atlanta"),
                    match("República Tcheca", "México", LocalDateTime.of(2026, 6, 24, 22, 0), "Fase de Grupos", "A", "Cidade do México"),
                    match("África do Sul", "Coreia do Sul", LocalDateTime.of(2026, 6, 24, 22, 0), "Fase de Grupos", "A", "Monterrey"),
                    match("Curaçao", "Costa do Marfim", LocalDateTime.of(2026, 6, 25, 17, 0), "Fase de Grupos", "E", "Filadélfia"),
                    match("Equador", "Alemanha", LocalDateTime.of(2026, 6, 25, 17, 0), "Fase de Grupos", "E", "Nova York / New Jersey"),
                    match("Japão", "Suécia", LocalDateTime.of(2026, 6, 25, 20, 0), "Fase de Grupos", "F", "Dallas"),
                    match("Tunísia", "Países Baixos", LocalDateTime.of(2026, 6, 25, 20, 0), "Fase de Grupos", "F", "Kansas City"),
                    match("Turquia", "Estados Unidos", LocalDateTime.of(2026, 6, 25, 23, 0), "Fase de Grupos", "D", "Los Angeles"),
                    match("Paraguai", "Austrália", LocalDateTime.of(2026, 6, 25, 23, 0), "Fase de Grupos", "D", "San Francisco"),
                    match("Noruega", "França", LocalDateTime.of(2026, 6, 26, 16, 0), "Fase de Grupos", "I", "Boston"),
                    match("Senegal", "Iraque", LocalDateTime.of(2026, 6, 26, 16, 0), "Fase de Grupos", "I", "Toronto"),
                    match("Cabo Verde", "Arábia Saudita", LocalDateTime.of(2026, 6, 26, 21, 0), "Fase de Grupos", "H", "Houston"),
                    match("Uruguai", "Espanha", LocalDateTime.of(2026, 6, 26, 21, 0), "Fase de Grupos", "H", "Guadalajara"),
                    match("Egito", "Irã", LocalDateTime.of(2026, 6, 27, 0, 0), "Fase de Grupos", "G", "Seattle"),
                    match("Nova Zelândia", "Bélgica", LocalDateTime.of(2026, 6, 27, 0, 0), "Fase de Grupos", "G", "Vancouver"),
                    match("Panamá", "Inglaterra", LocalDateTime.of(2026, 6, 27, 18, 0), "Fase de Grupos", "L", "Nova York / New Jersey"),
                    match("Croácia", "Gana", LocalDateTime.of(2026, 6, 27, 18, 0), "Fase de Grupos", "L", "Filadélfia"),
                    match("Colômbia", "Portugal", LocalDateTime.of(2026, 6, 27, 20, 30), "Fase de Grupos", "K", "Miami"),
                    match("Congo (RD)", "Uzbequistão", LocalDateTime.of(2026, 6, 27, 20, 30), "Fase de Grupos", "K", "Atlanta"),
                    match("Argélia", "Áustria", LocalDateTime.of(2026, 6, 27, 23, 0), "Fase de Grupos", "J", "Kansas City"),
                    match("Jordânia", "Argentina", LocalDateTime.of(2026, 6, 27, 23, 0), "Fase de Grupos", "J", "Dallas")
            );

            matchRepository.saveAll(matches);
            log.info("Cadastrados {} jogos da Copa do Mundo 2026", matches.size());
        };
    }

    private static Match match(String home, String away, LocalDateTime kickoff, String stage, String group, String location) {
        return new Match(home, away, kickoff, stage, group, location);
    }
}
