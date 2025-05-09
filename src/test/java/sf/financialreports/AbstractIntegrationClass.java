package sf.financialreports;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import sf.financialreports.api.dto.login.LoginDto;
import sf.financialreports.api.dto.login.TokenDto;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
public abstract class AbstractIntegrationClass {
    @Container
    protected static PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>("postgres:latest")
                    .withExposedPorts(5432)
                    .withDatabaseName("test")
                    .withUsername("test")
                    .withPassword("test");

    protected final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    protected TokenDto tokenDto = null;

    @BeforeEach()
    void login() throws Exception {
        LoginDto loginDto = new LoginDto(
                "john.doe@example.com",
                "passwordA"
        );

        String token = mvc.perform(post("/api/auth/login")
                        .content(mapper.writeValueAsString(loginDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("operUid", UUID.randomUUID().toString())
                ).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        tokenDto = mapper.readValue(token, TokenDto.class);
    }

    @Autowired
    protected MockMvc mvc;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }
}