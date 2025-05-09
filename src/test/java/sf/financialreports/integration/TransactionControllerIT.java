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
import sf.financialreports.dao.domain.CategoryType;
import sf.financialreports.dao.domain.Status;
import sf.financialreports.dao.domain.UserType;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
class TransactionControllerIT extends AbstractIntegrationClass {

    static {
        postgresContainer.start();
    }

    @DisplayName("Успешное получение транзакций")
    @Sql("/sql/get_transactions.sql")
    @Test
    void getTransactions_success() throws Exception {
        getTransactions(status().isOk());
    }

    @DisplayName("Неуспешное получение транзакций")
    @Test
    void getTransactions_fail() throws Exception {
        getTransactionsFail(status().is5xxServerError());
    }

    @DisplayName("Успешное создание транзакции")
    @Test
    void createTransaction_success() throws Exception {
        createTransaction(transactionDto, status().isOk());
    }

    @DisplayName("Неуспешное создание транзакции с ошибкой валидации")
    @Test
    void createTransaction_noValidation() throws Exception {
        createTransaction(transactionDtoNoValidation, status().isUnprocessableEntity());
    }

    @DisplayName("Неуспешное создание транзакции")
    @Test
    void createTransaction_fail() throws Exception {
        createTransactionFail(status().is5xxServerError());
    }

    @DisplayName("Успешное обновление транзакции - транзакция не найдена")
    @Test
    void updateTransction_notFound() throws Exception {
        updateTransactionNotFound(status().isNotFound());
    }
    @DisplayName("Неуспешное обновление транзакции")
    @Test
    void updateTransction_fail() throws Exception {
        updateTransactionFail(status().is5xxServerError());
    }

    @DisplayName("Успешное получение статусов транзакций")
    @Test
    void getTransactionStatuses_success() throws Exception {
        getTransactionStatusesSuccess(status().isOk());
    }

    @DisplayName("Неуспешное получение статусов транзакций")
    @Test
    void getTransactionStatuses_fail() throws Exception {
        getTransactionStatusesFail(status().is5xxServerError());
    }

    private String createTransaction(TransactionDto dto, ResultMatcher expectedStatus) throws Exception {
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

    private String createTransactionFail(ResultMatcher expectedStatus) throws Exception {
        return mvc.perform(post("/api/v1/transactions")
                        .header("operUid", UUID.randomUUID().toString())
                        .header("Authorization", "Bearer " + tokenDto.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(expectedStatus)
                .andDo(print())
                .andReturn()
                .getResponse().getContentAsString();
    }

    private String updateTransactionNotFound(ResultMatcher expectedStatus) throws Exception {
        return mvc.perform(patch("/api/v1/transactions")
                        .header("operUid", UUID.randomUUID().toString())
                        .header("Authorization", "Bearer " + tokenDto.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"id\": \"44444444-4444-4444-4444-444444444444\",\n" +
                                "  \"category\": {\n" +
                                "    \"id\": \"123e4567-e89b-12d3-a456-426614174000\",\n" +
                                "    \"name\": \"Зарплата\",\n" +
                                "    \"description\": \"Регулярный доход от работодателя\",\n" +
                                "    \"type\": \"INCOME\"\n" +
                                "  },\n" +
                                "  \"date\": \"2024.12.01\",\n" +
                                "  \"description\": \"Оплата услуг связи\",\n" +
                                "  \"amount\": 1500.75,\n" +
                                "  \"status\": {\n" +
                                "    \"name\": \"CONFIRMED\",\n" +
                                "    \"title\": \"Подтвержденная\",\n" +
                                "    \"weight\": 100\n" +
                                "  },\n" +
                                "  \"senderBank\": \"Сбербанк\",\n" +
                                "  \"senderAccount\": \"40817810099910000000\",\n" +
                                "  \"receiverUserType\": \"LEGAL\",\n" +
                                "  \"receiverBank\": \"Тинькофф\",\n" +
                                "  \"receiverAccount\": \"40702810900000000000\",\n" +
                                "  \"receiverInn\": \"77070838993\",\n" +
                                "  \"receiverPhone\": \"+79991234567\"\n" +
                                "}")
                ).andExpect(expectedStatus)
                .andDo(print())
                .andReturn()
                .getResponse().getContentAsString();
    }

    private String updateTransactionFail(ResultMatcher expectedStatus) throws Exception {
        return mvc.perform(patch("/api/v1/transactions")
                        .header("operUid", UUID.randomUUID().toString())
                        .header("Authorization", "Bearer " + tokenDto.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(expectedStatus)
                .andDo(print())
                .andReturn()
                .getResponse().getContentAsString();
    }

    private String getTransactions(ResultMatcher expectedStatus) throws Exception {
        return mvc.perform(get("/api/v1/transactions")
                        .header("operUid", UUID.randomUUID().toString())
                        .header("Authorization", "Bearer " + tokenDto.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(expectedStatus)
                .andDo(print())
                .andReturn()
                .getResponse().getContentAsString();
    }

    private String getTransactionsFail(ResultMatcher expectedStatus) throws Exception {
        return mvc.perform(post("/api/v1/transactions")
                        .header("operUid", UUID.randomUUID().toString())
                        .header("Authorization", "Bearer " + tokenDto.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(expectedStatus)
                .andDo(print())
                .andReturn()
                .getResponse().getContentAsString();
    }

    private String getTransactionStatusesSuccess(ResultMatcher expectedStatus) throws Exception {
        return mvc.perform(get("/api/v1/transactions/statuses")
                        .header("operUid", UUID.randomUUID().toString())
                        .header("Authorization", "Bearer " + tokenDto.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(expectedStatus)
                .andDo(print())
                .andReturn()
                .getResponse().getContentAsString();
    }

    private String getTransactionStatusesFail(ResultMatcher expectedStatus) throws Exception {
        return mvc.perform(post("/api/v1/transactions/statuses")
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
            .receiverUserType(UserType.LEGAL)
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

    private static final TransactionDto transactionDtoNoValidation = TransactionDto.builder()
            .date("2021.01.01")
            .description("test")
            .amount(BigDecimal.valueOf(1000))
            .status(TransactionStatusDto.from(Status.NEW))
            .senderBank("test")
            .senderAccount("test")
            .receiverUserType(UserType.LEGAL)
            .receiverBank("test")
            .receiverAccount("test")
            .receiverInn("123456789")
            .category(CategoryDto.builder()
                    .id(UUID.randomUUID())
                    .name("test")
                    .description("test")
                    .type(CategoryType.EXPENSE)
                    .build())
            .receiverPhone("89001001111")
            .build();
}
