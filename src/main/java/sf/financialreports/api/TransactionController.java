package sf.financialreports.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sf.financialreports.api.dto.TransactionDto;
import sf.financialreports.api.dto.TransactionStatusDto;
import sf.financialreports.api.dto.dashboard.DashboardDto;
import sf.financialreports.api.dto.dashboard.TransactionFilterDto;
import sf.financialreports.dao.domain.Audit;
import sf.financialreports.dao.domain.MessageType;
import sf.financialreports.dao.domain.RequestType;
import sf.financialreports.service.AuditService;
import sf.financialreports.service.TransactionService;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final AuditService auditService;

    @PostMapping()
    public TransactionDto save(
            @RequestHeader("operUid") UUID operUid,
            @RequestBody TransactionDto dto,
            @Parameter(hidden = true) HttpServletRequest request,
            @Parameter(hidden = true) HttpServletResponse response
    ) throws JsonProcessingException {
        auditService.audit(auditService.prepareRequestAudit(
                operUid,
                request,
                RequestType.SAVE_TRANSACTION,
                dto,
                null
        ));

        TransactionDto savedDto = transactionService.save(dto);

        auditService.audit(auditService.prepareResponseAudit(
                operUid,
                response,
                RequestType.SAVE_TRANSACTION,
                request.getRequestURI(),
                savedDto
        ));

        return savedDto;
    }

    @GetMapping()
    public List<TransactionDto> getTransactions(
            @RequestHeader("operUid") UUID operUid,
            @Parameter(hidden = true) HttpServletRequest request,
            @Parameter(hidden = true) HttpServletResponse response
    ) throws JsonProcessingException {
        auditService.audit(auditService.prepareRequestAudit(
                operUid,
                request,
                RequestType.GET_TRANSACTIONS,
                null,
                null
        ));

        List<TransactionDto> transactions = transactionService.getTransactions();

        auditService.audit(auditService.prepareResponseAudit(
                operUid,
                response,
                RequestType.GET_TRANSACTIONS,
                request.getRequestURI(),
                transactions
        ));

        return transactions;
    }

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> download(
            @RequestHeader("operUid") UUID operUid,
            @Parameter(hidden = true) HttpServletRequest request
    ) throws JsonProcessingException {

        auditService.audit(auditService.prepareRequestAudit(
                operUid,
                request,
                RequestType.DOWNLOAD_TRANSACTIONS,
                null,
                null
        ));


        byte[] csvFile = transactionService.download();
        InputStreamResource inputStreamResource = new InputStreamResource(new ByteArrayInputStream(csvFile));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=transactions.csv");

        // response
        auditService.audit(
                Audit.builder()
                        .operUid(operUid)
                        .messageType(MessageType.RESPONSE)
                        .requestType(RequestType.DOWNLOAD_TRANSACTIONS)
                        .requestPath(request.getRequestURI())
                        .requestHeaders(headers.toString())
                        .payload(null)
                        .build()
        );

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(new MediaType("text", "csv"))
                .body(inputStreamResource);
    }

    @PostMapping("/dashboard")
    public DashboardDto getDashboard(
            @RequestHeader("operUid") UUID operUid,
            @RequestBody TransactionFilterDto dto,
            @Parameter(hidden = true) HttpServletRequest request,
            @Parameter(hidden = true) HttpServletResponse response
    ) throws JsonProcessingException {
        auditService.audit(auditService.prepareRequestAudit(
                operUid,
                request,
                RequestType.GET_DASHBOARD,
                dto,
                null
        ));

        DashboardDto dashboardDto = transactionService.getDashboard(dto);

        auditService.audit(auditService.prepareResponseAudit(
                operUid,
                response,
                RequestType.GET_DASHBOARD,
                request.getRequestURI(),
                dashboardDto
        ));

        return dashboardDto;
    }

    @PatchMapping()
    public TransactionDto update(
            @RequestHeader("operUid") UUID operUid,
            @RequestBody TransactionDto dto,
            @Parameter(hidden = true) HttpServletRequest request,
            @Parameter(hidden = true) HttpServletResponse response
    ) throws JsonProcessingException {
        auditService.audit(auditService.prepareRequestAudit(
                operUid,
                request,
                RequestType.UPDATE_TRANSACTION,
                dto,
                null
        ));

        TransactionDto updatedDto = transactionService.update(dto);

        auditService.audit(auditService.prepareResponseAudit(
                operUid,
                response,
                RequestType.UPDATE_TRANSACTION,
                request.getRequestURI(),
                updatedDto
        ));

        return updatedDto;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @RequestHeader("operUid") UUID operUid,
            @PathVariable UUID id,
            @Parameter(hidden = true) HttpServletRequest request,
            @Parameter(hidden = true) HttpServletResponse response
    ) throws JsonProcessingException {
        auditService.audit(auditService.prepareRequestAudit(
                operUid,
                request,
                RequestType.DELETE_TRANSACTION,
                null,
                "{\"id\": %s}".formatted(id)
        ));

        transactionService.delete(id);

        String message = "Transaction '%s' deleted successfully".formatted(id);

        auditService.audit(auditService.prepareResponseAudit(
                operUid,
                response,
                RequestType.DELETE_TRANSACTION,
                request.getRequestURI(),
                message
        ));

        return ResponseEntity.ok().body(message);
    }

    @GetMapping("/statuses")
    public List<TransactionStatusDto> getStatuses(
            @RequestHeader("operUid") UUID operUid,
            @Parameter(hidden = true) HttpServletRequest request,
            @Parameter(hidden = true) HttpServletResponse response
    ) throws JsonProcessingException {
        auditService.audit(auditService.prepareRequestAudit(
                operUid,
                request,
                RequestType.GET_STATUSES,
                null,
                null
        ));
        List<TransactionStatusDto> statuses = transactionService.getStatuses();

        auditService.audit(auditService.prepareResponseAudit(
                operUid,
                response,
                RequestType.GET_STATUSES,
                request.getRequestURI(),
                statuses
        ));

        return statuses;
    }
}
