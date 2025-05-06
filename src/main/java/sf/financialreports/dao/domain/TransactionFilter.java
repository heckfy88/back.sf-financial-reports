package sf.financialreports.dao.domain;

import lombok.Builder;
import lombok.Data;
import sf.financialreports.api.dto.dashboard.TransactionFilterDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Builder
@Data
public class TransactionFilter {
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

    public static TransactionFilter from(TransactionFilterDto dto) {
        return new TransactionFilter(
                dto.getSenderBanks(),
                dto.getReceiverBanks(),
                dto.getStatuses(),
                dto.getInn(),
                dto.getDateFrom(),
                dto.getDateTo(),
                dto.getSpecificDate(),
                dto.getAmountFrom(),
                dto.getAmountTo(),
                dto.getCategoryType(),
                dto.getCategoryNames()
        );
    }
}