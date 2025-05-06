package sf.financialreports.api.dto.dashboard;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
@Schema(description = "Сравнение количества поступивших средств и потраченных")
public class ComparisonStatDto {
    @Schema(description = "Итоговый доход", example = "123456.78")
    private BigDecimal totalIncome;
    @Schema(description = "Итоговый расход", example = "123456.78")
    private BigDecimal totalExpense;
}
