package sf.financialreports.api.dto.dashboard;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
@Schema(description = "Количество транзакций за период")
public class TimeGroupStatDto {
    @Schema(description = "Период: неделя/месяц/квартал/год", example = "2025-W5")
    private String period; // e.g. "2025-05"
    @Schema(description = "Количество транзакций", example = "10")
    private long count;
}
