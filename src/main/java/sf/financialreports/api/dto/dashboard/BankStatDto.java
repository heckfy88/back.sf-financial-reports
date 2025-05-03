package sf.financialreports.api.dto.dashboard;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Builder
@Data
public class BankStatDto {
    private Map<String, Long> senderBanks;
    private Map<String, Long> receiverBanks;
}
