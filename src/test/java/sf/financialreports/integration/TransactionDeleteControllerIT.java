package sf.financialreports.integration;
import sf.financialreports.AbstractIntegrationClass;

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

        mvc.perform(delete("/api/v1/transactions/96ef0429-267e-4791-96c1-f176bf99c2d6")
                        .header("operUid", UUID.randomUUID().toString())
                        .header("Authorization", "Bearer " + tokenDto.getToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Удаление транзакции неавторизованным пользователем")
    @Test
    void deleteTransaction_unauthorized_noToken() throws Exception {

        mvc.perform(delete("/api/v1/transactions/96ef0429-267e-4791-96c1-f176bf99c2d6")
                        .header("operUid", UUID.randomUUID().toString())
                        .header("Authorization", "Bearer ")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Ошибка удаления транзакции")
    @Test
    void deleteTransaction_unauthorized_invalidToken() throws Exception {

        mvc.perform(get("/api/v1/transactions/96ef0429-267e-4791-96c1-f176bf99c2d6")
                        .header("operUid", UUID.randomUUID().toString())
                        .header("Authorization", "Bearer invalid_token_123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

}