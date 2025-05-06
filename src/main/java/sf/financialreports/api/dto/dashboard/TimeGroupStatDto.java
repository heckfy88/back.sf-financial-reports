package sf.financialreports.api.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class TimeGroupStatDto {
    private String period; // e.g. "2025-05"
    private long count;
}
