package sf.financialreports.api.dto.dashboard;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Builder
@Data
public class CategoryStatDto {
    private Map<String, BigDecimal> incomeCategories;
    private Map<String, BigDecimal> expenseCategories;
}
