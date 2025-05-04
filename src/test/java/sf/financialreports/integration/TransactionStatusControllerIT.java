package sf.financialreports.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultMatcher;
import sf.financialreports.AbstractIntegrationClass;
import sf.financialreports.api.dto.TransactionStatusDto;
import sf.financialreports.api.dto.login.LoginDto;
import sf.financialreports.api.dto.login.TokenDto;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
class TransactionStatusControllerIT extends AbstractIntegrationClass {

    static {
        postgresContainer.start();
    }

    @DisplayName("Успешное получение статуса транзакции")
    @Test
    void getTransactionStatus_success() throws Exception {
        TransactionStatusDto dto = new TransactionStatusDto("CREATED", "Создан", 1);
        getStatus(dto, status().is2xxSuccessful());
    }

    @DisplayName("Получение статуса транзакции без токена — ожидается 401 Unauthorized")
    @Test
    void getTransactionStatus_unauthorized_noToken() throws Exception {
        String token = loginAndGetToken();

        mvc.perform(delete("/api/v1/transactions/statuses")
                        .header("operUid", UUID.randomUUID().toString())
                        .header("Authorization", "Bearer ")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Создание транзакции с неверным токеном — ожидается 401 Unauthorized")
    @Test
    void getTransactionStatus_unauthorized_invalidToken() throws Exception {
        String token = loginAndGetToken();

        mvc.perform(delete("/api/v1/transactions/statuses")
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


    private String getStatus(TransactionStatusDto dto, ResultMatcher expectedStatus) throws Exception {
        String token = loginAndGetToken();
        return mvc.perform(get("/api/v1/transactions/statuses")
                        .header("operUid", UUID.randomUUID().toString())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto))
                ).andExpect(expectedStatus)
                .andDo(print())
                .andReturn()
                .getResponse().getContentAsString();
    }
}