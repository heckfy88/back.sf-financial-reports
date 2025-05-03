package sf.financialreports.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sf.financialreports.api.dto.TransactionDto;
import sf.financialreports.api.dto.TransactionStatusDto;
import sf.financialreports.api.dto.dashboard.DashboardDto;
import sf.financialreports.api.dto.dashboard.TransactionFilterDto;
import sf.financialreports.service.TransactionService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping()
    public TransactionDto save(
            @RequestHeader("operUid") UUID operUid,
            @RequestBody TransactionDto dto
    ) {
        return transactionService.save(dto);
    }

    @PostMapping("/dashboard")
    public DashboardDto getDashboard(
            @RequestHeader("operUid") UUID operUid,
            @RequestBody TransactionFilterDto dto
    ) {
        return transactionService.getDashboard(dto);
    }

    @PatchMapping("")
    public TransactionDto update(
            @RequestHeader("operUid") UUID operUid,
            @RequestBody TransactionDto dto
    ) {
        return transactionService.update(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @RequestHeader("operUid") UUID operUid,
            @PathVariable UUID id
    ) {
        transactionService.delete(id);
        return ResponseEntity.ok().body("Transaction '%s' deleted successfully".formatted(id));
    }

    @GetMapping("/statuses")
    public List<TransactionStatusDto> getStatuses(
            @RequestHeader("operUid") UUID operUid
    ) {
        return transactionService.getStatuses();
    }
}
