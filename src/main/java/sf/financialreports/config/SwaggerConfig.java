package sf.financialreports.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class SwaggerConfig {

    private final Map<String, String> tokens = Map.ofEntries(
            Map.entry("John Doe - [john.doe@example.com/passwordA]|(Физическое лицо)", "eyJraWQiOiJteS1oczI1Ni1rZXkiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJmaW5hbmNpYWwtcmVwb3J0cyIsInN1YiI6ImpvaG4uZG9lQGV4YW1wbGUuY29tIiwiZXhwIjoxNzQ2MjA3MzI2LCJpYXQiOjE3NDYyMDM3MjZ9.GzoJYGQksa8rVJL7Ym2HeP3JnoMPcUt3yoLmmaaoIn8"),
            Map.entry("Jane Doe - [jane.doe@example.com/passwordB]|(Физическое лицо)", "eyJraWQiOiJteS1oczI1Ni1rZXkiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJmaW5hbmNpYWwtcmVwb3J0cyIsInN1YiI6ImphbmUuZG9lQGV4YW1wbGUuY29tIiwiZXhwIjoxNzQ2MjA3NjAwLCJpYXQiOjE3NDYyMDQwMDB9.G4bM0WgO11HvGZn0LVwY-UFWIMnioQVvRDaA4yC21B0"),
            Map.entry("John Smith - [john.smith@example.com/passwordC]|(Юридическое лицо)", "eyJraWQiOiJteS1oczI1Ni1rZXkiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJmaW5hbmNpYWwtcmVwb3J0cyIsInN1YiI6ImpvaG4uc21pdGhAZXhhbXBsZS5jb20iLCJleHAiOjE3NDYyMDczODQsImlhdCI6MTc0NjIwMzc4NH0.KrfdQ-xe6jo-yjba_ZSns2BfWOVUAbb22qmj4SqE3fk"),
            Map.entry("Company Inc. - [company@example.com/passwordD]|(Юридическое лицо)", "eyJraWQiOiJteS1oczI1Ni1rZXkiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJmaW5hbmNpYWwtcmVwb3J0cyIsInN1YiI6ImNvbXBhbnlAZXhhbXBsZS5jb20iLCJleHAiOjE3NDYyMDc2OTMsImlhdCI6MTc0NjIwNDA5M30.0f3YBDDm8rcq7njcs71BP92Q1Cm5tlDPOGqhqLh4Vus")
    );

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
