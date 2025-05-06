package sf.financialreports.api.dto.dashboard;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@Schema(description = "Количество проведенных транзакций и отмененных транзакций")
public class StatusCountDto {
    @Schema(description = "Количество проведенных транзакций", example = "100")
    private long completed;
    @Schema(description = "Количество отмененных транзакций", example = "10")
    private long cancelled;
}
