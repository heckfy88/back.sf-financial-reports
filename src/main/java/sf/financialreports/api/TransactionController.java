package sf.financialreports.api;

import org.springframework.web.bind.annotation.*;
import sf.financialreports.api.dto.TransactionDto;
import sf.financialreports.api.dto.TransactionStatusDto;
import sf.financialreports.service.TransactionService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }


    @PostMapping()
    public TransactionDto save(@RequestBody TransactionDto dto) {
        return transactionService.save(dto);
    }

    @GetMapping("/statuses")
    public List<TransactionStatusDto> getStatuses() {
        return transactionService.getStatuses();
    }
}
