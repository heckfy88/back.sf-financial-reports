package sf.financialreports.api.dto.dashboard;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Builder
@Data
public class TransactionFilterDto {
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
}