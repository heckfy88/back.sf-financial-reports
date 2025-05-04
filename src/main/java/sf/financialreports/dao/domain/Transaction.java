package sf.financialreports.dao.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sf.financialreports.api.dto.TransactionDto;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    private UUID id;
    private UUID userId;
    private String categoryName;
    private String date;
    private String description;
    private BigDecimal amount;
    private Status status;
    private String senderBank;
    private String senderAccount;
    private String receiverBank;
    private String receiverAccount;
    private String receiverInn;
    private String receiverPhone;

    public static Transaction from(TransactionDto dto, UUID userId) {
        return new Transaction(
                dto.getId(),
                userId,
                dto.getCategory().getName(),
                dto.getDate(),
                dto.getDescription(),
                dto.getAmount(),
                Status.valueOf(dto.getStatus().name()),
                dto.getSenderBank(),
                dto.getSenderAccount(),
                dto.getReceiverBank(),
                dto.getReceiverAccount(),
                dto.getReceiverInn(),
                dto.getReceiverPhone()
        );
    }
}