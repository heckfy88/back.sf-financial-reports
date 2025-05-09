package sf.financialreports.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultMatcher;
import sf.financialreports.AbstractIntegrationClass;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
public class TransactionDashboardIT extends AbstractIntegrationClass {

    @DisplayName("Успешная загрузка CSV-файла с транзакциями")
    @Test
    void downloadTransactionsCSV_success() throws Exception {
        downloadTransactionsCSVSuccess(status().isOk());
    }

    @DisplayName("Неуспешная загрузка CSV-файла с транзакциями")
    @Test
    void downloadTransactionsCSV_fail() throws Exception {
        downloadTransactionsCSVFail(status().is5xxServerError());
    }

    @DisplayName("Попытка загрузки CSV-файла с транзакциями неавторизованным пользователем")
    @Test
    void downloadTransactionsCSV_unauthorized() throws Exception {
        downloadTransactionsCSVUnauthorized(status().isUnauthorized());
    }

    @DisplayName("Успешное получение данных с дашборда")
    @Test
    void getDashboard_success() throws Exception {
        getDashboardSuccess(status().isOk());
    }

    @DisplayName("Неуспешное получение данных с дашборда")
    @Test
    void getDashboard_fail() throws Exception {
        getDashboardFail(status().is5xxServerError());
    }


    private String downloadTransactionsCSVSuccess(ResultMatcher expectedStatus) throws Exception {
        return mvc.perform(get("/api/v1/transactions/download")
                        .header("operUid", "9f8c1d45-b4e1-4f4b-9ad8-12b3d98f726e")
                        .header("Authorization", "Bearer " + tokenDto.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(expectedStatus)
                .andDo(print())
                .andReturn()
                .getResponse().getContentAsString();
    }

    private String downloadTransactionsCSVFail(ResultMatcher expectedStatus) throws Exception {
        return mvc.perform(post("/api/v1/transactions/download")
                        .header("operUid", "9f8c1d45-b4e1-4f4b-9ad8-12b3d98f726e")
                        .header("Authorization", "Bearer " + tokenDto.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(expectedStatus)
                .andDo(print())
                .andReturn()
                .getResponse().getContentAsString();
    }

    private String downloadTransactionsCSVUnauthorized(ResultMatcher expectedStatus) throws Exception {
        return mvc.perform(get("/api/v1/transactions/download")
                        .header("operUid", "9f8c1d45-b4e1-4f4b-9ad8-12b3d98f726e")
                        .header("Authorization", "Bearer ")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(expectedStatus)
                .andDo(print())
                .andReturn()
                .getResponse().getContentAsString();
    }

    private String getDashboardSuccess(ResultMatcher expectedStatus) throws Exception {
        return mvc.perform(post("/api/v1/transactions/dashboard")
                        .header("operUid", "9f8c1d45-b4e1-4f4b-9ad8-12b3d98f726e")
                        .header("Authorization", "Bearer " + tokenDto.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"senderBanks\": [\n" +
                                "    \"Тинькофф\",\n" +
                                "    \"Сбер\"\n" +
                                "  ],\n" +
                                "  \"receiverBanks\": [\n" +
                                "    \"Тинькофф\",\n" +
                                "    \"Сбер\"\n" +
                                "  ],\n" +
                                "  \"statuses\": [\n" +
                                "    \"CONFIRMED\",\n" +
                                "    \"NEW\"\n" +
                                "  ],\n" +
                                "  \"inn\": \"77070838993\",\n" +
                                "  \"dateFrom\": \"2024-01-01\",\n" +
                                "  \"dateTo\": \"2024-12-31\",\n" +
                                "  \"specificDate\": \"2024-06-10\",\n" +
                                "  \"amountFrom\": 1000,\n" +
                                "  \"amountTo\": 10000,\n" +
                                "  \"categoryType\": \"EXPENSE\",\n" +
                                "  \"categoryNames\": [\n" +
                                "    \"Продукты\",\n" +
                                "    \"Связь\"\n" +
                                "  ]\n" +
                                "}")
                ).andExpect(expectedStatus)
                .andDo(print())
                .andReturn()
                .getResponse().getContentAsString();
    }

    private String getDashboardFail(ResultMatcher expectedStatus) throws Exception {
        return mvc.perform(get("/api/v1/transactions/dashboard")
                        .header("operUid", "9f8c1d45-b4e1-4f4b-9ad8-12b3d98f726e")
                        .header("Authorization", "Bearer " + tokenDto.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(expectedStatus)
                .andDo(print())
                .andReturn()
                .getResponse().getContentAsString();
    }
}
