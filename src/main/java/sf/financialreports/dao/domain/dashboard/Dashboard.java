package sf.financialreports.dao.domain.dashboard;

import java.util.List;
import java.util.Map;

public class Dashboard {
    private Map<String, List<TimeGroupStat>> transactionDynamics;
    private TypeDynamics typeDynamics;
    private ComparisonStat incomeVsExpense;
    private StatusCount statusCount;
    private BankStat bankStat;
    private CategoryStat categoryStat;
}
