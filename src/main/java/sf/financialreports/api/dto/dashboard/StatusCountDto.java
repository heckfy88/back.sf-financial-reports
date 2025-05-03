package sf.financialreports.api.dto.dashboard;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class StatusCountDto {
    private long completed;
    private long cancelled;
}
