package sf.financialreports.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultMatcher;
import sf.financialreports.AbstractIntegrationClass;
import sf.financialreports.api.dto.CategoryDto;
import sf.financialreports.api.dto.TransactionDto;
import sf.financialreports.api.dto.TransactionStatusDto;
import sf.financialreports.api.dto.login.LoginDto;
import sf.financialreports.api.dto.login.TokenDto;
import sf.financialreports.dao.domain.CategoryType;
import sf.financialreports.dao.domain.Status;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
class TransactionControllerIT extends AbstractIntegrationClass {

    static {
        postgresContainer.start();
    }

    @DisplayName("Успешное создание транзакции")
    @Test
    void createTransaction_success() throws Exception {
        createTransaction(transactionDto, status().isOk());
    }

    @DisplayName("Успешное получение транзакций")
    @Sql("/sql/get_transactions.sql")
    @Test
    void getTransactions_success() throws Exception {
        getTransactions(status().isOk());
    }


    private String createTransaction(TransactionDto dto, ResultMatcher expectedStatus) throws Exception {
        LoginDto loginDto = new LoginDto(
                "john.doe@example.com",
                "passwordA"
        );

        String token = mvc.perform(post("/api/auth/login")
                        .content(mapper.writeValueAsString(loginDto))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        TokenDto tokenDto = mapper.readValue(token, TokenDto.class);

        return mvc.perform(post("/api/v1/transactions")
                        .header("operUid", UUID.randomUUID().toString())
                        .header("Authorization", "Bearer " + tokenDto.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto))
                ).andExpect(expectedStatus)
                .andDo(print())
                .andReturn()
                .getResponse().getContentAsString();
    }

    private String getTransactions(ResultMatcher expectedStatus) throws Exception {
        LoginDto loginDto = new LoginDto(
                "john.doe@example.com",
                "passwordA"
        );

        String token = mvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .content(mapper.writeValueAsString(loginDto))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        TokenDto tokenDto = mapper.readValue(token, TokenDto.class);

        return mvc.perform(get("/api/v1/transactions")
                        .header("operUid", UUID.randomUUID().toString())
                        .header("Authorization", "Bearer " + tokenDto.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(expectedStatus)
                .andDo(print())
                .andReturn()
                .getResponse().getContentAsString();
    }

    private static final TransactionDto transactionDto = TransactionDto.builder()
            .date("2021.01.01")
            .description("test")
            .amount(BigDecimal.valueOf(1000))
            .status(TransactionStatusDto.from(Status.CANCELLED))
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
