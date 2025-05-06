package sf.financialreports.api.dto.dashboard;

import lombok.Builder;
import lombok.Data;
import sf.financialreports.dao.domain.CategoryType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Builder
@Data
public class TransactionFilterDto {
    private List<String> senderBanks;
    private List<String> receiverBanks;
    private List<String> statuses;
    private String inn;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private LocalDate specificDate;
    private BigDecimal amountFrom;
    private BigDecimal amountTo;
    private CategoryType categoryType;
    private List<String> categoryNames;
}