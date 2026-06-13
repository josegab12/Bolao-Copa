package com.zebiso.Backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOriginPatterns("*")
                        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .exposedHeaders("X-User-Id");
            }
        };
    }

    @Bean
    public BolaoProperties bolaoProperties(
            @Value("${bolao.pontos.resultado-exato:5}") int pontosResultadoExato,
            @Value("${bolao.pontos.vencedor:2}") int pontosVencedor) {
        return new BolaoProperties(pontosResultadoExato, pontosVencedor);
    }

    public record BolaoProperties(int pontosResultadoExato, int pontosVencedor) {
    }
}
