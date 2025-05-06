package sf.financialreports.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema()
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