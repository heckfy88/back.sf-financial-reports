package sf.financialreports.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sf.financialreports.api.dto.CategoryDto;
import sf.financialreports.api.dto.TransactionDto;
import sf.financialreports.api.dto.TransactionStatusDto;
import sf.financialreports.api.dto.UserDto;
import sf.financialreports.api.exceptions.NotFoundException;
import sf.financialreports.api.exceptions.TransactionOperationException;
import sf.financialreports.api.exceptions.TransactionValidationException;
import sf.financialreports.dao.domain.Category;
import sf.financialreports.dao.domain.Status;
import sf.financialreports.dao.domain.Transaction;
import sf.financialreports.dao.domain.User;
import sf.financialreports.dao.repository.CategoryRepository;
import sf.financialreports.dao.repository.TransactionRepository;
import sf.financialreports.dao.repository.UserRepository;
import sf.financialreports.service.TransactionService;

import java.util.List;
import java.util.UUID;

@Service
public class TransactionServiceImpl implements TransactionService {


    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;


    public TransactionServiceImpl(
            TransactionRepository transactionRepository,
            CategoryRepository categoryRepository,
            UserRepository userRepository
    ) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public TransactionDto save(TransactionDto dto) {
        // TODO: user auth check, убрать userId, когда будет jwt

        validate(dto);

        Category category = categoryRepository.save(Category.from(dto.getCategory(), dto.getUser().getId()));

        Transaction transaction = transactionRepository.save(Transaction.from(dto), category.getName());

        return TransactionDto.builder()
                .id(transaction.getId())
                .user(UserDto.from(userRepository.findById(transaction.getUserId())))
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

    @Transactional
    @Override
    public TransactionDto update(TransactionDto dto) {
        // TODO: user auth check

        Transaction existingTransaction = transactionRepository.findById(dto.getId());
        if (existingTransaction == null) {
            throw new NotFoundException(String.format("Transaction '%s' ", dto.getId()));
        }
        if (Status.valueOf(existingTransaction.getStatus().name()) != Status.NEW) {
            throw new TransactionOperationException(String.format("Transaction '%s' is not new", dto.getId()));
        }

        if (dto.getUser() != null) {
            User existingUser = userRepository.findById(dto.getUser().getId());
            if (existingUser == null) {
                throw new NotFoundException(String.format("User '%s' ", dto.getUser().getId()));
            }
            userRepository.update(User.from(dto.getUser()));
        }

        if (dto.getCategory() != null) {
            Category existingCategory = categoryRepository.findById(dto.getCategory().getId());
            if (existingCategory == null) {
                // TODO: заменить на значение из jwt
                categoryRepository.save(Category.from(dto.getCategory(), UUID.randomUUID()));
            }
            // TODO: заменить на значение из jwt
            categoryRepository.update(Category.from(dto.getCategory(), UUID.randomUUID()));
        }

        validate(dto);
        transactionRepository.update(Transaction.from(dto));

        return new TransactionDto();
    }

    @Transactional
    @Override
    public void delete(UUID id) {
        Transaction transaction = transactionRepository.findById(id);
        // TODO: user auth check
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
}
