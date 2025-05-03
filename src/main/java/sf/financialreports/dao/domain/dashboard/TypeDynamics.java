package sf.financialreports.dao.domain.dashboard;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class TypeDynamics {
    private List<TimeGroupStat> income;
    private List<TimeGroupStat> expense;
}
