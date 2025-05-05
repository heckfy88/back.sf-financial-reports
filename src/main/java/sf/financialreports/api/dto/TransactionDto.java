package sf.financialreports.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sf.financialreports.dao.domain.UserType;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    private UUID id;
    private String date;
    private String description;
    private BigDecimal amount;
    private TransactionStatusDto status;
    private String senderBank;
    private UserType receiverUserType;
    private String senderAccount;
    private String receiverBank;
    private String receiverAccount;
    private String receiverInn;
    private CategoryDto category;
    private String receiverPhone;
}