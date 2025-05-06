package sf.financialreports.api.dto.dashboard;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class CategoryStatsReportDto {
    private List<CategoryStatDto> incomeStats;
    private List<CategoryStatDto> expenseStats;
}