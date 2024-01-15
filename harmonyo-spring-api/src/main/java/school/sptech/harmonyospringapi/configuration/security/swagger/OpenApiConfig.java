package school.sptech.harmonyospringapi.configuration.security.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Api Harmonyo",
                description = "Api do sistema do grupo harmonyo",
                contact = @Contact(
                        name = "Harmonyo",
                        url = "https://github.com/Harmon-yo/java-api-spring-boot",
                        email = "xxxxx.xxxxx@sptech.school"
                ),
                license = @License(name = "UNLICENSED"),
                version = "1.0.0"
        )
)
@SecurityScheme(
        name = "Bearer", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT"
)

public class OpenApiConfig {

}
