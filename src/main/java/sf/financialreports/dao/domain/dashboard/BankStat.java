package sf.financialreports.dao.domain.dashboard;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Builder
@Data
public class BankStat {
    private Map<String, Long> senderBanks;
    private Map<String, Long> receiverBanks;
}
