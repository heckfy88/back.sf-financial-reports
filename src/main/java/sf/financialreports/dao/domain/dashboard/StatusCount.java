package sf.financialreports.dao.domain.dashboard;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class StatusCount {
    private long completed;
    private long cancelled;
}
