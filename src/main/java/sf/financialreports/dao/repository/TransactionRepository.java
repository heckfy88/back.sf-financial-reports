package sf.financialreports.dao.repository;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.springframework.stereotype.Repository;
import sf.financialreports.dao.domain.Transaction;
import sf.financialreports.dao.domain.TransactionFilter;
import sf.financialreports.dao.domain.dashboard.Dashboard;
import sf.financialreports.dao.domain.dashboard.TimeGroupStat;
import sf.financialreports.dao.jooq.enums.TransactionStatus;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static sf.financialreports.dao.jooq.tables.Category.CATEGORY;
import static sf.financialreports.dao.jooq.tables.Transaction.TRANSACTION;


@Repository
public class TransactionRepository {

    private final DSLContext dslContext;

    public TransactionRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public static final List<Field<?>> TRANSACTION_FIELDS = List.of(
            TRANSACTION.ID,
            TRANSACTION.USER_ID,
            TRANSACTION.CATEGORY_NAME,
            TRANSACTION.DATE,
            TRANSACTION.DESCRIPTION,
            TRANSACTION.AMOUNT,
            TRANSACTION.STATUS,
            TRANSACTION.SENDER_BANK,
            TRANSACTION.SENDER_ACCOUNT,
            TRANSACTION.RECEIVER_BANK,
            TRANSACTION.RECEIVER_ACCOUNT,
            TRANSACTION.RECEIVER_INN,
            TRANSACTION.RECEIVER_PHONE,
            TRANSACTION.CREATED_AT
    );

    public Transaction save(Transaction transaction, String categoryName) {
        return dslContext.insertInto(TRANSACTION,
                        TRANSACTION.USER_ID,
                        TRANSACTION.CATEGORY_NAME,
                        TRANSACTION.DATE,
                        TRANSACTION.DESCRIPTION,
                        TRANSACTION.AMOUNT,
                        TRANSACTION.STATUS,
                        TRANSACTION.SENDER_BANK,
                        TRANSACTION.SENDER_ACCOUNT,
                        TRANSACTION.RECEIVER_BANK,
                        TRANSACTION.RECEIVER_ACCOUNT,
                        TRANSACTION.RECEIVER_INN,
                        TRANSACTION.RECEIVER_PHONE
                )
                .values(
                        transaction.getUserId(),
                        categoryName,
                        LocalDate.parse(transaction.getDate(), DateTimeFormatter.ofPattern("yyyy.MM.dd")),
                        transaction.getDescription(),
                        transaction.getAmount(),
                        TransactionStatus.valueOf(transaction.getStatus().name()),
                        transaction.getSenderBank(),
                        transaction.getSenderAccount(),
                        transaction.getReceiverBank(),
                        transaction.getReceiverAccount(),
                        transaction.getReceiverInn(),
                        transaction.getReceiverPhone()
                )
                .returning(TRANSACTION_FIELDS)
                .fetchSingleInto(Transaction.class);
    }

    public List<Transaction> getTransactions(UUID userId) {
        return dslContext.select(TRANSACTION_FIELDS)
                .from(TRANSACTION)
                .where(TRANSACTION.USER_ID.eq(userId))
                .fetchInto(Transaction.class);
    }

    public Transaction update(Transaction transaction) {
        return dslContext.update(TRANSACTION)
                .set(TRANSACTION.DATE, LocalDate.parse(transaction.getDate(), DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                .set(TRANSACTION.DESCRIPTION, transaction.getDescription())
                .set(TRANSACTION.AMOUNT, transaction.getAmount())
                .set(TRANSACTION.STATUS, TransactionStatus.valueOf(transaction.getStatus().name()))
                .set(TRANSACTION.SENDER_BANK, transaction.getSenderBank())
                .set(TRANSACTION.RECEIVER_BANK, transaction.getReceiverBank())
                .set(TRANSACTION.RECEIVER_INN, transaction.getReceiverInn())
                .set(TRANSACTION.RECEIVER_PHONE, transaction.getReceiverPhone())
                .where(TRANSACTION.ID.eq(transaction.getId()))
                .returning(TRANSACTION_FIELDS)
                .fetchSingleInto(Transaction.class);

    }

    public Transaction findById(UUID id) {
        return dslContext.select(TRANSACTION_FIELDS)
                .from(TRANSACTION)
                .where(TRANSACTION.ID.eq(id))
                .fetchOneInto(Transaction.class);
    }

    public Dashboard getDashboard(UUID userId, TransactionFilter filter) {
        var t = TRANSACTION.as("t");
        var c = CATEGORY.as("c");

        // базовый запрос с join
        var condition = t.USER_ID.eq(userId)
                .and(t.CATEGORY_NAME.eq(c.NAME))
                .and(c.USER_ID.eq(userId));

        // применим фильтры
        if (filter.getSenderBank() != null)
            condition = condition.and(t.SENDER_BANK.in(filter.getSenderBank()));

        if (filter.getReceiverBank() != null)
            condition = condition.and(t.RECEIVER_BANK.in(filter.getReceiverBank()));

/*        if (filter.getStatus() != null)
            condition = condition.and(t.STATUS.in(TransactionStatus.valueOf(filter.getStatus())));*/

        if (filter.getInn() != null)
            condition = condition.and(t.RECEIVER_INN.eq(filter.getInn()));

        if (filter.getSpecificDate() != null)
            condition = condition.and(t.DATE.eq(filter.getSpecificDate()));
        else {
            if (filter.getDateFrom() != null)
                condition = condition.and(t.DATE.ge(filter.getDateFrom()));
            if (filter.getDateTo() != null)
                condition = condition.and(t.DATE.le(filter.getDateTo()));
        }

        if (filter.getAmountFrom() != null)
            condition = condition.and(t.AMOUNT.ge(filter.getAmountFrom()));
        if (filter.getAmountTo() != null)
            condition = condition.and(t.AMOUNT.le(filter.getAmountTo()));

        if (filter.getCategoryName() != null)
            condition = condition.and(t.CATEGORY_NAME.eq(filter.getCategoryName()));
/*        if (filter.getCategoryType() != null)
            condition = condition.and(c.TYPE.eq(CategoryType.valueOf(filter.getCategoryType())));*/

        // Выполняем запрос
        List<Transaction> transactions = dslContext
                .select(t.asterisk(), c.TYPE)
                .from(t)
                .join(c).on(c.USER_ID.eq(t.USER_ID).and(c.NAME.eq(t.CATEGORY_NAME)))
                .where(condition)
                .fetchInto(Transaction.class);

        // Обработка
        return aggregateDashboard(transactions);
    }

    private Dashboard aggregateDashboard(List<Transaction> transactions) {
        Dashboard result = new Dashboard();

        return result;
    }

    private Map<String, List<TimeGroupStat>> aggregateTimeGroups(List<Transaction> transactions) {
        DateTimeFormatter weeklyFmt = DateTimeFormatter.ofPattern("YYYY-'W'ww");
        DateTimeFormatter monthlyFmt = DateTimeFormatter.ofPattern("yyyy-MM");
        DateTimeFormatter yearlyFmt = DateTimeFormatter.ofPattern("yyyy");

        Function<Transaction, LocalDate> toDate = t -> LocalDate.parse(t.getDate());

        Map<String, List<TimeGroupStat>> result = new HashMap<>();

        result.put("weekly", groupAndCount(transactions, t -> toDate.apply(t).format(weeklyFmt)));
        result.put("monthly", groupAndCount(transactions, t -> toDate.apply(t).format(monthlyFmt)));
        result.put("quarterly", groupAndCount(transactions, t -> formatQuarter(toDate.apply(t))));
        result.put("yearly", groupAndCount(transactions, t -> toDate.apply(t).format(yearlyFmt)));

        return result;
    }

    private List<TimeGroupStat> groupAndCount(List<Transaction> transactions, Function<Transaction, String> classifier) {
        return transactions.stream()
                .collect(Collectors.groupingBy(classifier, Collectors.counting()))
                .entrySet().stream()
                .map(e -> new TimeGroupStat(e.getKey(), e.getValue()))
                .sorted(Comparator.comparing(TimeGroupStat::getPeriod))
                .collect(Collectors.toList());
    }

    private String formatQuarter(LocalDate date) {
        int q = (date.getMonthValue() - 1) / 3 + 1;
        return date.getYear() + "-Q" + q;
    }
}
