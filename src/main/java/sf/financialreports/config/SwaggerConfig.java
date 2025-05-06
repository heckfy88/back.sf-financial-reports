package sf.financialreports.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sf.financialreports.service.AuthenticationService;

import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    private final Map<String, String> tokens = Map.ofEntries(
            Map.entry("John Doe", """
                            {
                              "email": "john.doe@example.com",
                              "password": "passwordA"
                            }
                            """)
            );
    private final AuthenticationService authenticationService;
    @Bean
    OpenAPI openApiConfiguration() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(
                        new Components()
                                .addSecuritySchemes("bearerAuth",
                                        new SecurityScheme()
                                                .name("bearerAuth")
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                                .description(
                                                        tokens.entrySet().stream()
                                                                .map(entry -> "<b>" + entry.getKey() + ":</b><br>" + entry.getValue())
                                                                .collect(Collectors.joining("<br><br>"))
                                                )
                                )
                ).info(
                        new Info()
                                .title("Financial Reports API")
                                .version("1.0.0")
                );
    }
}
