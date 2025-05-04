package sf.financialreports.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultMatcher;
import sf.financialreports.AbstractIntegrationClass;
import sf.financialreports.api.dto.CategoryDto;
import sf.financialreports.api.dto.TransactionDto;
import sf.financialreports.api.dto.TransactionStatusDto;
import sf.financialreports.api.dto.UserDto;
import sf.financialreports.api.dto.login.LoginDto;
import sf.financialreports.api.dto.login.TokenDto;
import sf.financialreports.dao.domain.CategoryType;
import sf.financialreports.dao.domain.Status;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TransactionUpdateControllerIT extends AbstractIntegrationClass {

    @DisplayName("Попытка обновления несуществующей транзакици")
    @Test
    void updateTransaction_fail() throws Exception {
        updateTransaction(transactionDto, status().is5xxServerError());
    }

    @DisplayName("Апдейт транзакции без токена — ожидается 401 Unauthorized")
    @Test
    void updateTransaction_unauthorized_noToken() throws Exception {
        String token = loginAndGetToken();

        mvc.perform(delete("/api/v1/transactions")
                        .header("operUid", UUID.randomUUID().toString())
                        .header("Authorization", "Bearer ")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Создание транзакции с неверным токеном — ожидается 401 Unauthorized")
    @Test
    void updateTransaction_unauthorized_invalidToken() throws Exception {
        String token = loginAndGetToken();

        mvc.perform(delete("/api/v1/transactions")
                        .header("operUid", UUID.randomUUID().toString())
                        .header("Authorization", "Bearer invalid_token_123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Обновление транзакции с отсутствующими обязательными полями")
    @Test
    void updateTransaction_missingRequiredFields() throws Exception {
        createTransaction(transactionDto, status().isOk());

    }


    private String createTransaction(TransactionDto dto, ResultMatcher expectedStatus) throws Exception {
        String token = loginAndGetToken();

        return mvc.perform(post("/api/v1/transactions")
                        .header("operUid", UUID.randomUUID().toString())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto))
                ).andExpect(expectedStatus)
                .andDo(print())
                .andReturn()
                .getResponse().getContentAsString();
    }


    private String updateTransaction(TransactionDto dto, ResultMatcher expectedStatus) throws Exception {
        String token = loginAndGetToken();

        return mvc.perform(patch("/api/v1/transactions")
                        .header("operUid", UUID.randomUUID().toString())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto))
                ).andExpect(expectedStatus)
                .andDo(print())
                .andReturn()
                .getResponse().getContentAsString();
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


    private static final TransactionDto transactionDto = TransactionDto.builder()
            .id(UUID.randomUUID())
            .user(
                    UserDto.builder()
                            .id(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
                            .build()
            )
            .date("2021.01.01")
            .description("test")
            .amount(BigDecimal.valueOf(1000))
            .status(TransactionStatusDto.from(Status.NEW))
            .senderBank("test")
            .senderAccount("test")
            .receiverBank("test")
            .receiverAccount("test")
            .receiverInn("12345678901")
            .category(CategoryDto.builder()
                    .id(UUID.randomUUID())
                    .name("test")
                    .description("test")
                    .type(CategoryType.EXPENSE)
                    .build())
            .receiverPhone("89001001111")
            .build();
}
