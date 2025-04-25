package sf.financialreports.dao.repository;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.springframework.stereotype.Repository;
import sf.financialreports.dao.domain.Transaction;
import sf.financialreports.dao.jooq.enums.TransactionStatus;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

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
}
