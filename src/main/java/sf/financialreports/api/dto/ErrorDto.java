package sf.financialreports.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
@Schema(description = "Ответ об ошибке")
public class ErrorDto {
        @Schema(description = "Сообщение об ошибке", example = "Text '2025-01-01' could not be parsed at index 4")
        private String message;
        @Schema(description = "Название исключения", example = "DateTimeParseException")
        private String exceptionName;
        @Schema(description = "HTTP статус", example = "INTERNAL_SERVER_ERROR")
        private String httpStatusCode;
        @Schema(description = "Момент ошибки", example = "2025-05-06T21:18:19.083115")
        private LocalDateTime timestamp;
}