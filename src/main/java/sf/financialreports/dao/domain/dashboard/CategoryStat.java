package sf.financialreports.dao.domain.dashboard;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Builder
@Data
public class CategoryStat {
    private Map<String, BigDecimal> incomeCategories;
    private Map<String, BigDecimal> expenseCategories;
}
