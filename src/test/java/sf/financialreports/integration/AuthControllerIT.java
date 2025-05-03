package sf.financialreports.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import sf.financialreports.AbstractIntegrationClass;
import sf.financialreports.api.dto.login.LoginDto;
import sf.financialreports.api.dto.login.TokenDto;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerIT extends AbstractIntegrationClass {

    @DisplayName("Успешный вход — возвращается валидный токен")
    @Test
    void login_success() throws Exception {
        LoginDto loginDto = new LoginDto("john.doe@example.com", "passwordA");

        String response = mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        TokenDto tokenDto = mapper.readValue(response, TokenDto.class);
        assertNotNull(tokenDto.getToken(), "Токен должен быть возвращен");

        // Если используется JWT, можно декодировать его и проверить claims
        String[] parts = tokenDto.getToken().split("\\.");
        assertEquals(3, parts.length, "JWT должен состоять из 3 частей");
    }

    @DisplayName("Неверный email — 500 Bad Credentials")
    @Test
    void login_invalidEmail() throws Exception {
        LoginDto loginDto = new LoginDto("wrong.email@example.com", "passwordA");

        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(loginDto)))
                .andExpect(status().is5xxServerError())
                .andDo(print());
    }

}
