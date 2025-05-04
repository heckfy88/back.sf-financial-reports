package sf.financialreports.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sf.financialreports.api.dto.CategoryDto;
import sf.financialreports.api.dto.TransactionDto;
import sf.financialreports.api.dto.TransactionStatusDto;
import sf.financialreports.api.dto.dashboard.DashboardDto;
import sf.financialreports.api.dto.dashboard.TransactionFilterDto;
import sf.financialreports.api.exceptions.NotFoundException;
import sf.financialreports.api.exceptions.TransactionOperationException;
import sf.financialreports.api.exceptions.TransactionValidationException;
import sf.financialreports.dao.domain.*;
import sf.financialreports.dao.repository.CategoryRepository;
import sf.financialreports.dao.repository.TransactionRepository;
import sf.financialreports.dao.repository.UserRepository;
import sf.financialreports.service.TransactionService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final AuthenticationServiceImpl authenticationService;

    @Transactional
    @Override
    public TransactionDto save(TransactionDto dto) {
        User user = getUserFromToken();

        validate(dto);

        Category category = categoryRepository.save(Category.from(dto.getCategory(), user.getId()));
        Transaction transaction = transactionRepository.save(Transaction.from(dto, user.getId()), category.getName());

        return buildTransactionDto(user, category, transaction);
    }

    @Transactional
    @Override
    public TransactionDto update(TransactionDto dto) {
        User user = getUserFromToken();

        Transaction existingTransaction = transactionRepository.findById(dto.getId());
        if (existingTransaction == null) {
            throw new NotFoundException(String.format("Transaction '%s' ", dto.getId()));
        }
        if (Status.valueOf(existingTransaction.getStatus().name()) != Status.NEW) {
            throw new TransactionOperationException(String.format("Transaction '%s' is not new", dto.getId()));
        }

        if (dto.getUserType() != null) {
            user.setType(dto.getUserType());
            userRepository.update(user);
        }

        Category category = null;
        if (dto.getCategory() != null) {
            Category existingCategory =
                    categoryRepository.findByNameAndUserId(dto.getCategory().getName(), user.getId());
            if (existingCategory == null) {
                category = categoryRepository.save(Category.from(dto.getCategory(), user.getId()));
            } else {
                category = categoryRepository.update(Category.from(dto.getCategory(), user.getId()));
            }
        }

        validate(dto);
        Transaction transaction = transactionRepository.update(Transaction.from(dto, user.getId()));

        return buildTransactionDto(user, category, transaction);

    }

    @Transactional
    @Override
    public void delete(UUID id) {
        Transaction transaction = transactionRepository.findById(id);

        if (transaction == null) {
            throw new NotFoundException(String.format("Transaction '%s' ", id));
        }
        if (transactionRepository.findById(id).getStatus() != Status.NEW) {
            throw new TransactionOperationException(String.format("Transaction '%s' is not new", id));
        }
        transaction.setStatus(Status.DELETED);
        transactionRepository.update(transaction);
    }

    @Override
    public DashboardDto getDashboard(TransactionFilterDto dto) {
        transactionRepository.getDashboard(UUID.randomUUID(), TransactionFilter.from(dto));
        return new DashboardDto();
    }

    @Override
    public List<TransactionDto> getTransactions() {
        User user = getUserFromToken();
        return transactionRepository.getTransactions(user.getId()).stream().map(transaction ->
                TransactionDto.builder()
                        .id(transaction.getId())
                        .userType(userRepository.findById(transaction.getUserId()).getType())
                        .date(transaction.getDate())
                        .description(transaction.getDescription())
                        .amount(transaction.getAmount())
                        .status(TransactionStatusDto.from(transaction.getStatus()))
                        .senderBank(transaction.getSenderBank())
                        .senderAccount(transaction.getSenderAccount())
                        .receiverBank(transaction.getReceiverBank())
                        .receiverAccount(transaction.getReceiverAccount())
                        .receiverInn(transaction.getReceiverInn())
                        .category(CategoryDto.from(
                                categoryRepository.findByNameAndUserId(transaction.getCategoryName(), user.getId())))
                        .receiverPhone(transaction.getReceiverPhone())
                        .build()
        ).toList();
    }

    @Override
    public List<TransactionStatusDto> getStatuses() {
        // TODO: Valyok
        return List.of();
    }

    private void validate(TransactionDto dto) {
        checkInn(dto.getReceiverInn());
        checkPhone(dto.getReceiverPhone());
        checkDate(dto.getDate());
    }

    private void checkInn(String receiverInn) {
        if (!(receiverInn.matches("\\d{11}"))) {
            throw new TransactionValidationException(
                    String.format("Invalid inn number: '%s' should be 11 digits", receiverInn)
            );
        }
    }

    private void checkPhone(String receiverPhone) {
        if (!receiverPhone.matches("^(?:\\+7|8)?[\\s-]?\\(?\\d{3}\\)?[\\s-]?\\d{3}[\\s-]?\\d{2}[\\s-]?\\d{2}$")) {
            throw new TransactionValidationException(String.format("Invalid phone number: '%s'", receiverPhone));
        }
    }

    private void checkDate(String date) {
        if (!date.matches("\\d{4}\\.\\d{2}\\.\\d{2}")) {
            throw new TransactionValidationException(String.format("Invalid date: %s", date));
        }
    }

    private TransactionDto buildTransactionDto(User user, Category category, Transaction transaction) {
        return TransactionDto.builder()
                .id(transaction.getId())
                .userType(user.getType())
                .date(transaction.getDate())
                .description(transaction.getDescription())
                .amount(transaction.getAmount())
                .status(TransactionStatusDto.from(transaction.getStatus()))
                .senderBank(transaction.getSenderBank())
                .senderAccount(transaction.getSenderAccount())
                .receiverBank(transaction.getReceiverBank())
                .receiverAccount(transaction.getReceiverAccount())
                .receiverInn(transaction.getReceiverInn())
                .category(CategoryDto.from(category))
                .receiverPhone(transaction.getReceiverPhone())
                .build();
    }

    private User getUserFromToken() {
        Map<String, Object> claims = authenticationService.getToken().getClaims();
        return userRepository.findById(UUID.fromString(claims.get("sub").toString()));
    }
}
