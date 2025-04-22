package sf.financialreports.api.dto;

import lombok.Builder;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;

@Builder
public record ErrorDto(
        String message,
        String exceptionName,
        HttpStatusCode httpStatusCode,
        LocalDateTime timestamp
) {
}
