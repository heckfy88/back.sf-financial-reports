package sf.financialreports.api.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;

@Builder
@Data
public class ErrorDto {
        private String message;
        private String exceptionName;
        private HttpStatusCode httpStatusCode;
        private LocalDateTime timestamp;
}