package sf.financialreports.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sf.financialreports.api.dto.CategoryDto;
import sf.financialreports.api.dto.TransactionDto;
import sf.financialreports.api.dto.TransactionStatusDto;
import sf.financialreports.api.dto.UserDto;
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
        // TODO: validate fields

        Category category = categoryRepository.save(Category.from(dto.category()));

        Transaction transaction = transactionRepository.save(Transaction.from(dto), category.id());

        return TransactionDto.builder()
                .id(transaction.id())
                .user(UserDto.from(userRepository.findById(transaction.userId())))
                .datetime(transaction.datetime())
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
}
