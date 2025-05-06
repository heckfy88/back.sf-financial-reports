package sf.financialreports.api.dto.dashboard;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
@Schema(description = "Количество транзакций по их типу")
public class TypeDynamicsDto {
    @Schema(description = "Количество транзакций поступления за период", example = "[{\"period\": 2021, \"count\": 1000}]")
    private List<TimeGroupStatDto> income;
    @Schema(description = "Количество транзакций расхода за период", example = "[{\"period\": 2021, \"count\": 1000}]")
    private List<TimeGroupStatDto> expense;
}
