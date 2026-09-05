package api.loja.lotus.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class CorsConfig {

    @Value("${cors.allowed.origins}")
    private List<String> corsOrigins = new ArrayList<>();

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        var configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(corsOrigins);

        configuration.setAllowedMethods(
                List.of("GET", "POST", "PUT", "PATCH", "DELETE")
        );

        configuration.setAllowedHeaders(
                List.of("*")
        );

        var source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}
