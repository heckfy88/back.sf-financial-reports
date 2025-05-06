package sf.financialreports.api.dto.dashboard;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import sf.financialreports.dao.domain.CategoryType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Schema(description = "Фильтр для поиска транзакций")
@Builder
@Data
public class TransactionFilterDto {

    @Schema(description = "Список банков-отправителей", example = "[\"Тинькофф\", \"Сбер\"]")
    private List<String> senderBanks;

    @Schema(description = "Список банков-получателей", example = "[\"Тинькофф\", \"Сбер\"]")
    private List<String> receiverBanks;

    @Schema(description = "Список статусов транзакций", example = "[\"CONFIRMED\", \"NEW\"]")
    private List<String> statuses;

    @Schema(description = "ИНН получателя", example = "77070838993")
    private String inn;

    @Schema(description = "Дата от", example = "2024.01.01")
    private LocalDate dateFrom;

    @Schema(description = "Дата до", example = "2024.12.31")
    private LocalDate dateTo;

    @Schema(description = "Конкретная дата", example = "2024.06.10")
    private LocalDate specificDate;

    @Schema(description = "Минимальная сумма", example = "1000.00")
    private BigDecimal amountFrom;

    @Schema(description = "Максимальная сумма", example = "10000.00")
    private BigDecimal amountTo;

    @Schema(description = "Тип категории", example = "EXPENSE")
    private CategoryType categoryType;

    @Schema(description = "Список названий категорий", example = "[\"Продукты\", \"Связь\"]")
    private List<String> categoryNames;
}
