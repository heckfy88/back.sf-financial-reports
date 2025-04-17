package sf.financialreports.service;

import sf.financialreports.api.dto.TransactionDto;
import sf.financialreports.api.dto.TransactionStatusDto;

import java.util.List;

public interface TransactionService {

    TransactionDto save(TransactionDto dto);

    List<TransactionStatusDto> getStatuses();
}
