package sf.financialreports.integration;

import org.junit.jupiter.api.*;
import org.springframework.http.MediaType;
import sf.financialreports.AbstractIntegrationClass;
import sf.financialreports.api.dto.login.LoginDto;
import sf.financialreports.api.dto.login.TokenDto;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerIT extends AbstractIntegrationClass {
   @DisplayName("Успешная авторизация пользователя")
   @Test
    void login_success() throws Exception {
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

    @DisplayName("Неуспешная авторизация пользователя")
    @Test
    void login_fail() throws Exception {
        LoginDto loginDto = new LoginDto(
                "john.doe@example.com",
                "password"
        );

        mvc.perform(post("/api/auth/login")
                        .content(mapper.writeValueAsString(loginDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("operUid", UUID.randomUUID().toString())
                ).andExpect(status().is5xxServerError())
                .andReturn().getResponse().getContentAsString();
    }

}
