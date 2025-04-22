package sf.financialreports.api.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record TransactionDto(
        UUID id,
        UserDto user,
        LocalDateTime datetime,
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