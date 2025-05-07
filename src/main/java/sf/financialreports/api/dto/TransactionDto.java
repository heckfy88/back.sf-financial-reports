package sf.financialreports.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sf.financialreports.dao.domain.UserType;

import java.math.BigDecimal;
import java.util.UUID;


@Schema(description = "Транзакция")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    @Schema(description = "Идентификатор транзакции",
            example = "d290f1ee-6c54-4b01-90e6-d701748f0851",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private UUID id;
    @Schema(description = "Категория транзакции")
    private CategoryDto category;
    @Schema(description = "Дата транзакции", example = "2024-12-01")
    private String date;
    @Schema(description = "Описание транзакции", example = "Оплата услуг связи")
    private String description;
    @Schema(description = "Сумма", example = "1500.75")
    private BigDecimal amount;
    @Schema(description = "Статус транзакции")
    private TransactionStatusDto status;
    @Schema(description = "Банк отправителя", example = "Сбербанк")
    private String senderBank;
    @Schema(description = "Счёт отправителя", example = "40817810099910004312")
    private String senderAccount;
    @Schema(description = "Юридический статус (Физ. лицо/Юр. лицо)", example = "LEGAL")
    private UserType receiverUserType;
    @Schema(description = "Банк получателя", example = "Тинькофф")
    private String receiverBank;
    @Schema(description = "Счёт получателя", example = "40702810900000001234")
    private String receiverAccount;
    @Schema(description = "ИНН получателя", example = "77070838993")
    private String receiverInn;
    @Schema(description = "Телефон получателя", example = "+79991234567")
    private String receiverPhone;
}