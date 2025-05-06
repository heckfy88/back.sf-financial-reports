package sf.financialreports.api.dto.dashboard;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Builder
@Data
@Schema(description = "Статистика по банкам отправителя и банкам получателей")
public class BankStatDto {
    @Schema(description = "Банки отправителя", example = "{\"bankA\": 10}")
    private Map<String, Long> senderBanks;
    @Schema(description = "Банки получателя", example = "{\"bankB\": 10}")
    private Map<String, Long> receiverBanks;
}
