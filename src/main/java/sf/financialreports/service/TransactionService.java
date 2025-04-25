package sf.financialreports.service;

import sf.financialreports.api.dto.TransactionDto;
import sf.financialreports.api.dto.TransactionStatusDto;

import java.util.List;
import java.util.UUID;

public interface TransactionService {

    TransactionDto save(TransactionDto dto);
    TransactionDto update(TransactionDto dto);
    void delete(UUID id);

    List<TransactionStatusDto> getStatuses();
}
