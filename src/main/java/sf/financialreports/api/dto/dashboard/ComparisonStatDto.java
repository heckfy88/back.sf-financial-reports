package sf.financialreports.api.dto.dashboard;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class ComparisonStatDto {
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
}
