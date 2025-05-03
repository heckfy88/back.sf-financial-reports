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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    private String getStatus(TransactionStatusDto dto, ResultMatcher expectedStatus) throws Exception {
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
        return mvc.perform(get("/api/v1/transactions/statuses")
                        .header("operUid", UUID.randomUUID().toString())
                        .header("Authorization", "Bearer " + tokenDto.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto))
                ).andExpect(expectedStatus)
                .andDo(print())
                .andReturn()
                .getResponse().getContentAsString();
    }
}