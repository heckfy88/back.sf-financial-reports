package sf.financialreports.dao.repository;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.springframework.stereotype.Repository;
import sf.financialreports.dao.domain.*;
import sf.financialreports.dao.jooq.enums.TransactionStatus;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static org.jooq.impl.DSL.*;
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
            TRANSACTION.RECEIVER_USER_TYPE,
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
                        TRANSACTION.RECEIVER_USER_TYPE,
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
                        UserType.fromDomain(transaction.getReceiverUserType()),
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
                .set(TRANSACTION.RECEIVER_USER_TYPE, UserType.fromDomain(transaction.getReceiverUserType()))
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

    public String download(UUID userId) {
        var result = dslContext.select(
                        TRANSACTION.ID.as("Идентификатор транзакции"),
                        TRANSACTION.CATEGORY_NAME.as("Название категории"),
                        TRANSACTION.DATE.as("Дата проведения транзакции"),
                        TRANSACTION.DESCRIPTION.as("Описание"),
                        TRANSACTION.AMOUNT.as("Сумма"),
                        getTransactionStatusValue().as("Статус"),
                        TRANSACTION.SENDER_BANK.as("Банк-отправитель"),
                        TRANSACTION.SENDER_ACCOUNT.as("Счет отправителя"),
                        getReceiverUserTypeValue().as("Тип получателя"),
                        TRANSACTION.RECEIVER_BANK.as("Банк-получатель"),
                        TRANSACTION.RECEIVER_ACCOUNT.as("Счет получателя"),
                        TRANSACTION.RECEIVER_INN.as("ИНН получателя"),
                        TRANSACTION.RECEIVER_PHONE.as("Телефон получателя"),
                        TRANSACTION.CREATED_AT.as("Дата добавления транзакции")
                )
                .from(TRANSACTION)
                .where(TRANSACTION.USER_ID.eq(userId))
                .fetch();

        return result.formatCSV();
    }

    public List<TransactionByCategoryType> getTransactionsByFilter(UUID userId, TransactionFilter filter) {
        var condition = TRANSACTION.USER_ID.eq(userId)
                .and(TRANSACTION.CATEGORY_NAME.eq(CATEGORY.NAME))
                .and(CATEGORY.USER_ID.eq(userId));

        if (filter.getSenderBanks() != null && !filter.getSenderBanks().isEmpty()) {
            condition = condition.and(TRANSACTION.SENDER_BANK.in(filter.getSenderBanks()));
        }

        if (filter.getReceiverBanks() != null && !filter.getReceiverBanks().isEmpty()) {
            condition = condition.and(TRANSACTION.RECEIVER_BANK.in(filter.getReceiverBanks()));
        }

        if (filter.getStatuses() != null && !filter.getStatuses().isEmpty()) {
            condition = condition.and(
                    TRANSACTION.STATUS.in(filter.getStatuses().stream().map(TransactionStatus::valueOf).toList())
            );
        }

        if (filter.getInn() != null) {
            condition = condition.and(TRANSACTION.RECEIVER_INN.eq(filter.getInn()));
        }

        if (filter.getSpecificDate() != null) {
            condition = condition.and(TRANSACTION.DATE.eq(filter.getSpecificDate()));
        } else {
            if (filter.getDateFrom() != null)
                condition = condition.and(TRANSACTION.DATE.ge(filter.getDateFrom()));
            if (filter.getDateTo() != null)
                condition = condition.and(TRANSACTION.DATE.le(filter.getDateTo()));
        }

        if (filter.getAmountFrom() != null) {
            condition = condition.and(TRANSACTION.AMOUNT.ge(filter.getAmountFrom()));
        }
        if (filter.getAmountTo() != null) {
            condition = condition.and(TRANSACTION.AMOUNT.le(filter.getAmountTo()));
        }

        if (filter.getCategoryType() != null) {
            condition = condition.and(CATEGORY.TYPE.eq(
                    CategoryType.fromDomain(filter.getCategoryType())
            ));
        }

        if (filter.getCategoryNames() != null && !filter.getCategoryNames().isEmpty()) {
            condition = condition.and(TRANSACTION.CATEGORY_NAME.in(filter.getCategoryNames()));
        }

        return dslContext
                .select(
                        row(
                                TRANSACTION.ID,
                                TRANSACTION.USER_ID,
                                TRANSACTION.CATEGORY_NAME,
                                TRANSACTION.DATE,
                                TRANSACTION.DESCRIPTION,
                                TRANSACTION.AMOUNT,
                                TRANSACTION.STATUS,
                                TRANSACTION.SENDER_BANK,
                                TRANSACTION.SENDER_ACCOUNT,
                                TRANSACTION.RECEIVER_USER_TYPE,
                                TRANSACTION.RECEIVER_BANK,
                                TRANSACTION.RECEIVER_ACCOUNT,
                                TRANSACTION.RECEIVER_INN,
                                TRANSACTION.RECEIVER_PHONE,
                                TRANSACTION.CREATED_AT
                        ).as("transaction"),
                        getCategoryTypeValue().as("type")
                )
                .from(TRANSACTION)
                .join(CATEGORY).on(
                        CATEGORY.USER_ID.eq(TRANSACTION.USER_ID)
                                .and(CATEGORY.NAME.eq(TRANSACTION.CATEGORY_NAME))
                )
                .where(condition)
                .fetchInto(TransactionByCategoryType.class);
    }

    //Если будет время - разобраться через конвертер типа, но там тоже была проблема. Намного удобнее конвертация в csv
    private Field<String> getTransactionStatusValue() {
        return when(TRANSACTION.STATUS.eq(TransactionStatus.NEW), inline(Status.NEW.getTitle()))
                .when(TRANSACTION.STATUS.eq(TransactionStatus.CONFIRMED), inline(Status.CONFIRMED.getTitle()))
                .when(TRANSACTION.STATUS.eq(TransactionStatus.PROCESSING), inline(Status.PROCESSING.getTitle()))
                .when(TRANSACTION.STATUS.eq(TransactionStatus.COMPLETED), inline(Status.COMPLETED.getTitle()))
                .when(TRANSACTION.STATUS.eq(TransactionStatus.RETURNED), inline(Status.RETURNED.getTitle()))
                .when(TRANSACTION.STATUS.eq(TransactionStatus.CANCELLED), inline(Status.CANCELLED.getTitle()))
                .when(TRANSACTION.STATUS.eq(TransactionStatus.DELETED), inline(Status.DELETED.getTitle()));
    }

    private Field<String> getReceiverUserTypeValue() {
        return when(TRANSACTION.RECEIVER_USER_TYPE.eq(sf.financialreports.dao.jooq.enums.UserType.PHYSICAL),
                inline(UserType.PHYSICAL.getTitle()))
                .when(TRANSACTION.RECEIVER_USER_TYPE.eq(sf.financialreports.dao.jooq.enums.UserType.LEGAL),
                        inline(UserType.LEGAL.getTitle()));
    }

    private Field<String> getCategoryTypeValue() {
        return when(
                CATEGORY.TYPE.eq(sf.financialreports.dao.jooq.enums.CategoryType.EXPENSE),
                inline(CategoryType.EXPENSE.name()))
                .when(
                        CATEGORY.TYPE.eq(sf.financialreports.dao.jooq.enums.CategoryType.INCOME),
                        inline(CategoryType.INCOME.name()));
    }

    public List<Status> getStatuses(UUID userId) {
        return dslContext
                .select(TRANSACTION.STATUS)
                .from(TRANSACTION)
                .where(TRANSACTION.USER_ID.eq(userId))
                .fetch()
                .getValues(TRANSACTION.STATUS, Status.class);
    }
}
