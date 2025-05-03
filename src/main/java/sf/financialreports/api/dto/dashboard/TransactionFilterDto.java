package sf.financialreports.api.dto.dashboard;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Data
public class TransactionFilterDto {
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
}