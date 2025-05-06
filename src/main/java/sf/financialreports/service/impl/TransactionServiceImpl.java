package sf.financialreports.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sf.financialreports.api.dto.CategoryDto;
import sf.financialreports.api.dto.TransactionDto;
import sf.financialreports.api.dto.TransactionStatusDto;
import sf.financialreports.api.dto.dashboard.*;
import sf.financialreports.api.exceptions.NotFoundException;
import sf.financialreports.api.exceptions.TransactionOperationException;
import sf.financialreports.api.exceptions.TransactionValidationException;
import sf.financialreports.dao.domain.*;
import sf.financialreports.dao.repository.CategoryRepository;
import sf.financialreports.dao.repository.TransactionRepository;
import sf.financialreports.service.AuthenticationService;
import sf.financialreports.service.TransactionService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final AuthenticationService authenticationService;


    @Transactional
    @Override
    public TransactionDto save(TransactionDto dto) {
        User user = authenticationService.getUserFromToken();

        validate(dto);

        Category category = categoryRepository.save(Category.from(dto.getCategory(), user.getId()));
        Transaction transaction = transactionRepository.save(Transaction.from(dto, user.getId()), category.getName());

        return buildTransactionDto(category, transaction);
    }

    @Transactional
    @Override
    public TransactionDto update(TransactionDto dto) {
        User user = authenticationService.getUserFromToken();

        Transaction existingTransaction = transactionRepository.findById(dto.getId());
        if (existingTransaction == null) {
            throw new NotFoundException(String.format("Transaction '%s'", dto.getId()));
        }
        if (Status.valueOf(existingTransaction.getStatus().name()) != Status.NEW) {
            throw new TransactionOperationException(String.format("Transaction '%s' is not new", dto.getId()));
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
        return buildTransactionDto(category, transaction);

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

        transactionRepository.delete(id);
    }

    @Override
    public DashboardDto getDashboard(TransactionFilterDto dto) {
        User user = authenticationService.getUserFromToken();
        List<TransactionByCategoryType> transactionsByType = transactionRepository.getTransactionsByFilter(user.getId(), TransactionFilter.from(dto));

        return aggregateDashboard(transactionsByType);
    }

    @Override
    public List<TransactionDto> getTransactions() {
        User user = authenticationService.getUserFromToken();
        return transactionRepository.getTransactions(user.getId()).stream().map(transaction ->
                TransactionDto.builder()
                        .id(transaction.getId())
                        .date(transaction.getDate().replace("-", "."))
                        .description(transaction.getDescription())
                        .amount(transaction.getAmount())
                        .status(TransactionStatusDto.from(transaction.getStatus()))
                        .senderBank(transaction.getSenderBank())
                        .senderAccount(transaction.getSenderAccount())
                        .receiverUserType(transaction.getReceiverUserType())
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
    public byte[] download() {
        User user = authenticationService.getUserFromToken();

        String csvString = transactionRepository.download(user.getId());

        return csvString.getBytes();
    }

    @Override
    public List<TransactionStatusDto> getStatuses() {
        return Arrays.stream(Status.values()).map(status ->
                TransactionStatusDto.builder()
                        .name(status.name())
                        .title(status.getTitle())
                        .weight(status.getWeight())
                        .build()
        ).toList();
    }

    private void validate(TransactionDto dto) {
        checkInn(dto.getReceiverInn());
        checkPhone(dto.getReceiverPhone());
        checkDate(dto.getDate());
    }

    private void checkInn(String receiverInn) {
        if (receiverInn == null) {
            return;
        }
        if (!(receiverInn.matches("\\d{11}"))) {
            throw new TransactionValidationException(
                    String.format("Invalid inn number: '%s' should be 11 digits", receiverInn)
            );
        }
    }

    private void checkPhone(String receiverPhone) {
        if (receiverPhone == null) {
            return;
        }
        if (!receiverPhone.matches("^(?:\\+7|8)?[\\s-]?\\(?\\d{3}\\)?[\\s-]?\\d{3}[\\s-]?\\d{2}[\\s-]?\\d{2}$")) {
            throw new TransactionValidationException(String.format("Invalid phone number: '%s'", receiverPhone));
        }
    }

    private void checkDate(String date) {
        if (date == null) {
            return;
        }
        if (!date.matches("\\d{4}\\.\\d{2}\\.\\d{2}")) {
            throw new TransactionValidationException(String.format("Invalid date: %s", date));
        }
    }

    private TransactionDto buildTransactionDto(Category category, Transaction transaction) {
        return TransactionDto.builder()
                .id(transaction.getId())
                .date(transaction.getDate().replace("-", "."))
                .description(transaction.getDescription())
                .amount(transaction.getAmount())
                .status(TransactionStatusDto.from(transaction.getStatus()))
                .senderBank(transaction.getSenderBank())
                .senderAccount(transaction.getSenderAccount())
                .receiverUserType(transaction.getReceiverUserType())
                .receiverBank(transaction.getReceiverBank())
                .receiverAccount(transaction.getReceiverAccount())
                .receiverInn(transaction.getReceiverInn())
                .category(CategoryDto.from(category))
                .receiverPhone(transaction.getReceiverPhone())
                .build();
    }

    private DashboardDto aggregateDashboard(List<TransactionByCategoryType> transactionsByType) {
        DashboardDto result = new DashboardDto();
        List<Transaction> transactions = transactionsByType.stream()
                .map(TransactionByCategoryType::getTransaction).toList();

        result.setTransactionDynamics(aggregateTimeGroups(transactions));
        result.setTypeDynamics(aggregateTypeDynamics(transactionsByType));
        result.setIncomeVsExpense(aggregateIncomeVsExpense(transactionsByType));
        result.setStatusCount(aggregateStatusCount(transactions));
        result.setBankStat(aggregateBankStats(transactions));
        result.setCategoryStat(aggregateCategoryStats(transactionsByType));

        return result;
    }

    private Map<String, List<TimeGroupStatDto>> aggregateTimeGroups(List<Transaction> transactions) {
        DateTimeFormatter weeklyFmt = DateTimeFormatter.ofPattern("YYYY-'W'ww");
        DateTimeFormatter yearlyFmt = DateTimeFormatter.ofPattern("yyyy");

        Function<Transaction, LocalDate> toDate = t -> LocalDate.parse(t.getDate());

        Map<String, List<TimeGroupStatDto>> result = new HashMap<>();

        result.put("weekly", groupAndCount(transactions, t -> toDate.apply(t).format(weeklyFmt)));
        result.put("monthly", groupAndCount(transactions, t -> formatToMonth(toDate.apply(t))));
        result.put("quarterly", groupAndCount(transactions, t -> formatToQuarter(toDate.apply(t))));
        result.put("yearly", groupAndCount(transactions, t -> toDate.apply(t).format(yearlyFmt)));
        return result;
    }

    private TypeDynamicsDto aggregateTypeDynamics(List<TransactionByCategoryType> transactionsByType) {
        List<Transaction> incomeTransactions = extractByType(transactionsByType, CategoryType.INCOME);
        List<Transaction> expenseTransactions = extractByType(transactionsByType, CategoryType.EXPENSE);

        List<TimeGroupStatDto> income = groupAndCount(incomeTransactions, t -> formatToMonth(LocalDate.parse(t.getDate())));
        List<TimeGroupStatDto> expense = groupAndCount(expenseTransactions, t -> formatToMonth(LocalDate.parse(t.getDate())));

        return TypeDynamicsDto.builder()
                .income(income)
                .expense(expense)
                .build();
    }

    private ComparisonStatDto aggregateIncomeVsExpense(List<TransactionByCategoryType> transactionsByType) {
        Map<CategoryType, BigDecimal> totals = transactionsByType.stream()
                .collect(Collectors.groupingBy(
                        TransactionByCategoryType::getType,
                        Collectors.mapping(
                                entry -> entry.getTransaction().getAmount(),
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                ));

        BigDecimal totalIncome = totals.getOrDefault(CategoryType.INCOME, BigDecimal.ZERO);
        BigDecimal totalExpense = totals.getOrDefault(CategoryType.EXPENSE, BigDecimal.ZERO);

        return ComparisonStatDto.builder()
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .build();
    }

    private StatusCountDto aggregateStatusCount(List<Transaction> transactions) {
        Map<Status, Long> statusCounts = transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getStatus, Collectors.counting()));

        long completed = statusCounts.getOrDefault(Status.COMPLETED, 0L);
        long cancelled = statusCounts.getOrDefault(Status.CANCELLED, 0L);

        return StatusCountDto.builder()
                .completed(completed)
                .cancelled(cancelled)
                .build();
    }

    public BankStatDto aggregateBankStats(List<Transaction> transactions) {
        Map<String, Long> senderStats = transactions.stream()
                .filter(t -> t.getSenderBank() != null && !t.getSenderBank().isBlank())
                .collect(Collectors.groupingBy(Transaction::getSenderBank, Collectors.counting()));

        Map<String, Long> receiverStats = transactions.stream()
                .filter(t -> t.getReceiverBank() != null && !t.getReceiverBank().isBlank())
                .collect(Collectors.groupingBy(Transaction::getReceiverBank, Collectors.counting()));

        return BankStatDto.builder()
                .senderBanks(senderStats)
                .receiverBanks(receiverStats)
                .build();
    }

    private CategoryStatsReportDto aggregateCategoryStats(List<TransactionByCategoryType> transactionsByType) {
        List<CategoryStatDto> incomeStats = computeStatsByCategory(transactionsByType, CategoryType.INCOME);
        List<CategoryStatDto> expenseStats = computeStatsByCategory(transactionsByType, CategoryType.EXPENSE);

        return CategoryStatsReportDto.builder()
                .incomeStats(incomeStats)
                .expenseStats(expenseStats)
                .build();
    }

    private List<CategoryStatDto> computeStatsByCategory(List<TransactionByCategoryType> transactions, CategoryType type) {
        return transactions.stream()
                .filter(t -> t.getType() == type)
                .map(TransactionByCategoryType::getTransaction)
                .filter(t -> t.getCategoryName() != null && !t.getCategoryName().isBlank())
                .collect(Collectors.groupingBy(Transaction::getCategoryName, Collectors.collectingAndThen(
                        Collectors.toList(),
                        list -> {
                            long count = list.size();
                            BigDecimal total = list.stream()
                                    .map(Transaction::getAmount)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                            return new CategoryStatDto(list.get(0).getCategoryName(), count, total);
                        }
                )))
                .values().stream()
                .sorted(Comparator.comparing(CategoryStatDto::getTotalAmount).reversed())
                .toList();
    }

    private List<TimeGroupStatDto> groupAndCount(List<Transaction> transactions, Function<Transaction, String> classifier) {
        return transactions.stream()
                .collect(Collectors.groupingBy(classifier, Collectors.counting()))
                .entrySet().stream()
                .map(e -> new TimeGroupStatDto(e.getKey(), e.getValue()))
                .sorted(Comparator.comparing(TimeGroupStatDto::getPeriod))
                .toList();
    }


    private List<Transaction> extractByType(List<TransactionByCategoryType> data, CategoryType type) {
        return data.stream()
                .filter(entry -> entry.getType() == type)
                .map(TransactionByCategoryType::getTransaction)
                .toList();
    }

    private String formatToMonth(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM"));
    }

    private String formatToQuarter(LocalDate date) {
        int q = (date.getMonthValue() - 1) / 3 + 1;
        return date.getYear() + "-Q" + q;
    }

}
