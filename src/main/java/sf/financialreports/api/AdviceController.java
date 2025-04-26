package sf.financialreports.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import sf.financialreports.api.dto.ErrorDto;
import sf.financialreports.api.exceptions.TransactionOperationException;
import sf.financialreports.api.exceptions.TransactionValidationException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class AdviceController {

    @ExceptionHandler({TransactionValidationException.class, TransactionOperationException.class})
    public ResponseEntity<ErrorDto> handleTransactionOperationException(Exception ex, WebRequest request) {
        // TODO: add integration logging
        return ResponseEntity.unprocessableEntity().body(
                ErrorDto.builder()
                        .message(ex.getMessage())
                        .exceptionName(ex.getClass().getSimpleName())
                        .httpStatusCode(HttpStatus.UNPROCESSABLE_ENTITY)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception ex, WebRequest request) {
        // TODO: add integration loggging
        return ResponseEntity.internalServerError().body(
                ErrorDto.builder()
                        .message(ex.getMessage())
                        .exceptionName(ex.getClass().getSimpleName())
                        .httpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

}
