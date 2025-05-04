package sf.financialreports.integration;
import sf.financialreports.AbstractIntegrationClass;
import sf.financialreports.api.dto.login.LoginDto;
import sf.financialreports.api.dto.login.TokenDto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;


@ExtendWith(SpringExtension.class)
class TransactionDeleteControllerIT extends AbstractIntegrationClass {

    static {
        postgresContainer.start();
    }

    @DisplayName("Ошибка удаление невалидной транзакции")
    @Test
    void deleteTransaction_fail() throws Exception {
        String token = loginAndGetToken();

        mvc.perform(delete("/api/v1/transactions/96ef0429-267e-4791-96c1-f176bf99c2d6")
                        .header("operUid", UUID.randomUUID().toString())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    @DisplayName("Удаление транзакции без токена — ожидается 401 Unauthorized")
    @Test
    void deleteTransaction_unauthorized_noToken() throws Exception {
        String token = loginAndGetToken();

        mvc.perform(delete("/api/v1/transactions/96ef0429-267e-4791-96c1-f176bf99c2d6")
                        .header("operUid", UUID.randomUUID().toString())
                        .header("Authorization", "Bearer ")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Создание транзакции с неверным токеном — ожидается 401 Unauthorized")
    @Test
    void deleteTransaction_unauthorized_invalidToken() throws Exception {
        String token = loginAndGetToken();

        mvc.perform(delete("/api/v1/transactions/96ef0429-267e-4791-96c1-f176bf99c2d6")
                        .header("operUid", UUID.randomUUID().toString())
                        .header("Authorization", "Bearer invalid_token_123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }


    private String loginAndGetToken() throws Exception {
        LoginDto loginDto = new LoginDto("john.doe@example.com", "passwordA");

        String response = mvc.perform(post("/api/auth/login")
                        .content(mapper.writeValueAsString(loginDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        TokenDto tokenDto = mapper.readValue(response, TokenDto.class);
        return tokenDto.getToken();
    }
}