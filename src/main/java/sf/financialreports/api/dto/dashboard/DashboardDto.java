package sf.financialreports.api.dto.dashboard;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Дашборд")
public class DashboardDto {
    @Schema(description = "Динамика по количеству транзакций в разрезе неделя/месяц/квартал/год")
    private Map<String, List<TimeGroupStatDto>> transactionDynamics;
    @Schema(description = "Динамика по типу транзакции")
    private TypeDynamicsDto typeDynamics;
    @Schema(description = "Сравнение количества поступивших средств и потраченных")
    private ComparisonStatDto incomeVsExpense;
    @Schema(description = "Количество проведенных транзакций и отмененных транзакций")
    private StatusCountDto statusCount;
    @Schema(description = "Статистика по банкам отправителя и банкам получателей")
    private BankStatDto bankStat;
    @Schema(description = "Статистический отчет по категориям расходов и категориям поступлений")
    private CategoryStatsReportDto categoryStat;
}