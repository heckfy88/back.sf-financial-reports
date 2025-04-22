package sf.financialreports.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultMatcher;
import sf.financialreports.AbstractIntegrationClass;
import sf.financialreports.api.dto.CategoryDto;
import sf.financialreports.api.dto.TransactionDto;
import sf.financialreports.api.dto.TransactionStatusDto;
import sf.financialreports.api.dto.UserDto;
import sf.financialreports.domain.CategoryType;
import sf.financialreports.domain.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
public class TransactionControllerIT extends AbstractIntegrationClass {

    static {
        postgresContainer.start();
    }

    @DisplayName("Успешное создание транзакции")
    @Test
    public void createTransaction_success() throws Exception {
        createTransaction(trasactionDto, status().isOk());
    }


    private String createTransaction(TransactionDto dto, ResultMatcher expectedStatus) throws Exception {
        return mvc.perform(post("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto))
                ).andExpect(expectedStatus)
                .andDo(print())
                .andReturn()
                .getResponse().getContentAsString();
    }

    private static final TransactionDto trasactionDto = TransactionDto.builder()
            .id(UUID.randomUUID())
            .user(
                    UserDto.builder()
                            .id(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
                            .build()
            )
            .datetime(LocalDateTime.now())
            .description("test")
            .amount(BigDecimal.valueOf(1000))
            .status(TransactionStatusDto.from(Status.CANCELLED))
            .senderBank("test")
            .senderAccount("test")
            .receiverBank("test")
            .receiverAccount("test")
            .receiverInn("test")
            .category(CategoryDto.builder()
                    .id(UUID.randomUUID())
                    .name("test")
                    .description("test")
                    .type(CategoryType.EXPENSE)
                    .build())
            .receiverPhone("89001001111")
            .build();
}
