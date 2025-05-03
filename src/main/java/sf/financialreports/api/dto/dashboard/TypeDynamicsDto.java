package sf.financialreports.api.dto.dashboard;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class TypeDynamicsDto {
    private List<TimeGroupStatDto> income;
    private List<TimeGroupStatDto> expense;
}
