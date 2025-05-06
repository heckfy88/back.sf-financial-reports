package sf.financialreports.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sf.financialreports.api.dto.ErrorDto;
import sf.financialreports.api.exceptions.NotFoundException;
import sf.financialreports.api.exceptions.TransactionOperationException;
import sf.financialreports.api.exceptions.TransactionValidationException;
import sf.financialreports.dao.domain.RequestType;
import sf.financialreports.service.AuditService;

import java.time.LocalDateTime;
import java.util.UUID;

@RestControllerAdvice
@RequiredArgsConstructor
public class AdviceController {

    private final AuditService auditService;

    @ExceptionHandler({TransactionValidationException.class, TransactionOperationException.class})
    public ResponseEntity<ErrorDto> handleTransactionOperationException(
            Exception ex,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws JsonProcessingException {
        ErrorDto errorDto = ErrorDto.builder()
                .message(ex.getMessage())
                .exceptionName(ex.getClass().getSimpleName())
                .httpStatusCode(HttpStatus.UNPROCESSABLE_ENTITY.name())
                .timestamp(LocalDateTime.now())
                .build();

        auditService.audit(auditService.prepareResponseAudit(
                UUID.fromString(request.getHeader("operUid")),
                response,
                RequestType.ERROR,
                request.getRequestURI(),
                errorDto
        ));

        return ResponseEntity.unprocessableEntity().body(errorDto);
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<ErrorDto> handleNotFoundException(
            Exception ex,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws JsonProcessingException {
        ErrorDto errorDto = ErrorDto.builder()
                .message(ex.getMessage())
                .exceptionName(ex.getClass().getSimpleName())
                .httpStatusCode(HttpStatus.NOT_FOUND)
                .timestamp(LocalDateTime.now())
                .build();

        auditService.audit(auditService.prepareResponseAudit(
                UUID.fromString(request.getHeader("operUid")),
                response,
                RequestType.ERROR,
                request.getRequestURI(),
                errorDto
        ));

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorDto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(
            Exception ex,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws JsonProcessingException {
        ErrorDto errorDto = ErrorDto.builder()
                .message(ex.getMessage())
                .exceptionName(ex.getClass().getSimpleName())
                .httpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .timestamp(LocalDateTime.now())
                .build();

        auditService.audit(auditService.prepareResponseAudit(
                UUID.fromString(request.getHeader("operUid")),
                response,
                RequestType.ERROR,
                request.getRequestURI(),
                errorDto
        ));

        return ResponseEntity.internalServerError().body(errorDto);
    }

}
