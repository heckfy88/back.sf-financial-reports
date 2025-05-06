package sf.financialreports.api.dto.dashboard;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
@Schema(description = "Статистический отчет по категориям расходов и категориям поступлений")
public class CategoryStatsReportDto {
    @Schema(description = "Список статистики по категориям доходов", example = "[{\"category\":\"Зарплата\",\"count\":1000,\"totalAmount\":1000}]")
    private List<CategoryStatDto> incomeStats;
    @Schema(description = "Список статистики по категориям расходов", example = "[{\"category\":\"Покупки\",\"count\":2,\"totalAmount\":200}]")
    private List<CategoryStatDto> expenseStats;
}