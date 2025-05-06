package sf.financialreports.api.dto.dashboard;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Статистика по категории")
public class CategoryStatDto {
    @Schema(description = "Наименование категории", example = "Покупки")
    private String category;
    @Schema(description = "Количество транзакций в категории", example = "10")
    private long count;
    @Schema(description = "Сумма транзакций в категории", example = "1000")
    private BigDecimal totalAmount;
}