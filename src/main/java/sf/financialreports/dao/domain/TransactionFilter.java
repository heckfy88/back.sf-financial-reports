package sf.financialreports.dao.domain;

import lombok.Builder;
import lombok.Data;
import sf.financialreports.api.dto.dashboard.TransactionFilterDto;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Data
public class TransactionFilter {
    private String senderBank;
    private String receiverBank;
    private String status;
    private String inn;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private LocalDate specificDate;
    private BigDecimal amountFrom;
    private BigDecimal amountTo;
    private String categoryType; // INCOME or EXPENSE
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