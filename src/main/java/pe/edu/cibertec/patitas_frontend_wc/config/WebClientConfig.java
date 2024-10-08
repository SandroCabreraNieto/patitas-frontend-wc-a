package pe.edu.cibertec.patitas_frontend_wc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClientAutenticacion(WebClient.Builder builder) {
        return builder.build();
    }
}
