package sf.financialreports.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import sf.financialreports.api.dto.ErrorDto;
import sf.financialreports.api.exceptions.TransactionValidationException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class AdviceController {

    @ExceptionHandler(TransactionValidationException.class)
    public ResponseEntity<ErrorDto> handleException(Exception ex, WebRequest request) {
        return ResponseEntity.unprocessableEntity().body(
                ErrorDto.builder()
                        .message(ex.getMessage())
                        .exceptionName(ex.getClass().getSimpleName())
                        .httpStatusCode(HttpStatus.UNPROCESSABLE_ENTITY)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

}
