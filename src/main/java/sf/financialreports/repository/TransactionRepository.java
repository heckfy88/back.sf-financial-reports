package sf.financialreports.repository;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.springframework.stereotype.Repository;
import sf.financialreports.domain.Transaction;
import sf.financialreports.domain.jooq.enums.TransactionStatus;


import java.util.List;
import java.util.UUID;

import static sf.financialreports.domain.jooq.tables.Transaction.TRANSACTION;


@Repository
public class TransactionRepository {

    private final DSLContext dslContext;

    public TransactionRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public static List<Field<?>> TRANSACTION_FIELDS = List.of(
            TRANSACTION.ID,
            TRANSACTION.USER_ID,
            TRANSACTION.CATEGORY_ID,
            TRANSACTION.DATETIME,
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

    public Transaction save(Transaction transaction, UUID categoryId) {
        return dslContext.insertInto(TRANSACTION,
                        TRANSACTION.USER_ID,
                        TRANSACTION.CATEGORY_ID,
                        TRANSACTION.DATETIME,
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
                        transaction.userId(),
                        categoryId, // разобраться, как в record transaction передать это - record class doesn't support setters
                        transaction.datetime(),
                        transaction.description(),
                        transaction.amount(),
                        TransactionStatus.valueOf(transaction.status().name()),
                        transaction.senderBank(),
                        transaction.senderAccount(),
                        transaction.receiverBank(),
                        transaction.receiverAccount(),
                        transaction.receiverInn(),
                        transaction.receiverPhone()
                )
                .returning(TRANSACTION_FIELDS)
                .fetchSingleInto(Transaction.class);
    }
}
