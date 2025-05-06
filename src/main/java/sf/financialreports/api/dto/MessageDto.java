package sf.financialreports.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(description = "Сообщение")
@Data
@AllArgsConstructor
public class MessageDto {
    @Schema(description = "Сообщение",
            example = "Transaction 'd1a78e9b-cbd9-4f6b-b614-5c9c2f5153ba' deleted successfully")
    private String message;
}
