package sf.financialreports.service;

import sf.financialreports.api.dto.TransactionDto;
import sf.financialreports.api.dto.TransactionStatusDto;
import sf.financialreports.api.dto.dashboard.DashboardDto;
import sf.financialreports.api.dto.dashboard.TransactionFilterDto;

import java.util.List;
import java.util.UUID;

public interface TransactionService {

    TransactionDto save(TransactionDto dto);
    TransactionDto update(TransactionDto dto);
    void delete(UUID id);

    DashboardDto getDashboard(TransactionFilterDto dto);

    List<TransactionDto> getTransactions();

    byte[] download();

    List<TransactionStatusDto> getStatuses();
}
