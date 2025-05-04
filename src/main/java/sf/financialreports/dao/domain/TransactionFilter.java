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
    private List<String> senderBank;
    private List<String> receiverBank;
    private List<String> status;
    private String inn;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private LocalDate specificDate;
    private BigDecimal amountFrom;
    private BigDecimal amountTo;
    private List<String> categoryType; // INCOME or EXPENSE
    private String categoryName;

    public static TransactionFilter from(TransactionFilterDto dto) {
        return new TransactionFilter(
                dto.getSenderBank(),
                dto.getReceiverBank(),
                dto.getStatus(),
                dto.getInn(),
                dto.getDateFrom(),
                dto.getDateTo(),
                dto.getSpecificDate(),
                dto.getAmountFrom(),
                dto.getAmountTo(),
                dto.getCategoryType(),
                dto.getCategoryName()
        );
    }
}