package sf.financialreports.api.dto.dashboard;

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
public class DashboardDto {
    private Map<String, List<TimeGroupStatDto>> transactionDynamics;
    private TypeDynamicsDto typeDynamics;
    private ComparisonStatDto incomeVsExpense;
    private StatusCountDto statusCount;
    private BankStatDto bankStat;
    private CategoryStatDto categoryStat;
}

