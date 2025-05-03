package sf.financialreports.dao.domain.dashboard;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class ComparisonStat {
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
}
