package sf.financialreports.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sf.financialreports.api.dto.CategoryDto;
import sf.financialreports.api.dto.TransactionDto;
import sf.financialreports.api.dto.TransactionStatusDto;
import sf.financialreports.api.dto.UserDto;
import sf.financialreports.api.exceptions.TransactionValidationException;
import sf.financialreports.domain.Category;
import sf.financialreports.domain.Transaction;
import sf.financialreports.repository.CategoryRepository;
import sf.financialreports.repository.TransactionRepository;
import sf.financialreports.repository.UserRepository;
import sf.financialreports.service.TransactionService;

import java.util.List;

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
        // TODO: user auth check

        validate(dto);

        Category category = categoryRepository.save(Category.from(dto.category()));

        Transaction transaction = transactionRepository.save(Transaction.from(dto), category.id());

        return TransactionDto.builder()
                .id(transaction.id())
                .user(UserDto.from(userRepository.findById(transaction.userId())))
                .date(transaction.date())
                .description(transaction.description())
                .amount(transaction.amount())
                .status(TransactionStatusDto.from(transaction.status()))
                .senderBank(transaction.senderBank())
                .senderAccount(transaction.senderAccount())
                .receiverBank(transaction.receiverBank())
                .receiverAccount(transaction.receiverAccount())
                .receiverInn(transaction.receiverInn())
                .category(CategoryDto.from(category))
                .receiverPhone(transaction.receiverPhone())
                .build();
    }

    @Override
    public List<TransactionStatusDto> getStatuses() {
        // TODO: Valyok
        return List.of();
    }

    private void validate(TransactionDto dto) {
        checkInn(dto.receiverInn());
        checkPhone(dto.receiverPhone());
        checkDate(dto.date());
    }

    private void checkInn(String receiverInn) {
        if (!(receiverInn.matches("\\d{11}"))) {
            throw new TransactionValidationException("Invalid inn number: '" + receiverInn + "' should be 11 digits");
        }
    }

    private void checkPhone(String receiverPhone) {
        if (!receiverPhone.matches("^(?:\\+7|8)?[\\s-]?\\(?\\d{3}\\)?[\\s-]?\\d{3}[\\s-]?\\d{2}[\\s-]?\\d{2}$")) {
            throw new TransactionValidationException("Invalid phone number: " + receiverPhone);
        }
    }

    private void checkDate(String date) {
        if (!date.matches("\\d{4}\\.\\d{2}\\.\\d{2}")) {
            throw new TransactionValidationException("Invalid date: " + date);
        }
    }
}
