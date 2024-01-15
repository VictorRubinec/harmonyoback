package school.sptech.harmonyospringapi.service.socket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@CrossOrigin(origins = {"http://localhost:3000", "http://harmonyoapp.sytes.net", "http://harmonyo.sytes.net"})
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/ws/**")
                .allowedOrigins("http://localhost:3000", "http://harmonyoapp.sytes.net", "http://harmonyo.sytes.net")
                .allowedMethods("GET", "POST")
                .allowCredentials(true);
    }
}