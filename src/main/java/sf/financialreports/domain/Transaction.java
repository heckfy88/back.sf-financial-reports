package sf.financialreports.domain;

import lombok.Builder;
import lombok.Setter;
import sf.financialreports.api.dto.TransactionDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record Transaction(
        UUID id,
        UUID userId,
        LocalDateTime datetime,
        String description,
        BigDecimal amount,
        Status status,
        String senderBank,
        String senderAccount,
        String receiverBank,
        String receiverAccount,
        String receiverInn,
        UUID categoryId,
        String receiverPhone
) {
   public static Transaction from(TransactionDto dto) {
       return new Transaction(
               dto.id(),
               dto.user().id(),
               dto.datetime(),
               dto.description(),
               dto.amount(),
               Status.valueOf(dto.status().name()),
               dto.senderBank(),
               dto.senderAccount(),
               dto.receiverBank(),
               dto.receiverAccount(),
               dto.receiverInn(),
               dto.category().id(),
               dto.receiverPhone()
       );
   }
}