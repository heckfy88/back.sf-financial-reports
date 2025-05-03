package sf.financialreports.api.dto.dashboard;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TimeGroupStatDto {
    private String period; // e.g. "2025-05"
    private long count;
}
