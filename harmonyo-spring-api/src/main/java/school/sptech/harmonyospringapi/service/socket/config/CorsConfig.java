package school.sptech.harmonyospringapi.service.socket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@CrossOrigin(origins = "http://localhost:3000") // 54.209.110.127:80 -> IP do front na AWS
public class CorsConfig implements WebMvcConfigurer {

    private String url = "http://localhost:3000"; //  54.209.110.127:80 -> IP do front na AWS

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/ws/**")
                .allowedOrigins(url)
                .allowedMethods("GET", "POST")
                .allowCredentials(true);
    }
}