package sf.financialreports.api.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record TransactionDto(
        UUID id,
        UserDto user,
        String date,
        String description,
        BigDecimal amount,
        TransactionStatusDto status,
        String senderBank,
        String senderAccount,
        String receiverBank,
        String receiverAccount,
        String receiverInn,
        CategoryDto category,
        String receiverPhone
) {
}