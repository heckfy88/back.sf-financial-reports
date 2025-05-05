package sf.financialreports.dao.repository;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.springframework.stereotype.Repository;
import sf.financialreports.dao.domain.User;
import sf.financialreports.dao.domain.UserType;

import java.util.List;
import java.util.UUID;

import static sf.financialreports.dao.jooq.Tables.USER;

@Repository
public class UserRepository {

    private final DSLContext dslContext;

    public UserRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public static List<Field<?>> USER_FIELDS = List.of(
            USER.ID,
            USER.NAME,
            USER.EMAIL,
            USER.PASSWORD_HASH,
            USER.TYPE,
            USER.CREATED_AT,
            USER.IS_ACTIVE
    );

    public User findById(UUID userId) {
        var userRecord = dslContext.select(USER_FIELDS)
                .from(USER)
                .where(USER.ID.eq(userId))
                .fetchOne();

        if (userRecord == null) return null;

        return new User(
                userRecord.get(USER.ID),
                userRecord.get(USER.NAME),
                userRecord.get(USER.EMAIL),
                userRecord.get(USER.PASSWORD_HASH),
                UserType.fromDb(userRecord.get(USER.TYPE))
        );
    }

    public User findByEmail(String userEmail) {
        var userRecord = dslContext.select(USER_FIELDS)
                .from(USER)
                .where(USER.EMAIL.eq(userEmail))
                .fetchOne();

        if (userRecord == null) return null;

        return new User(
                userRecord.get(USER.ID),
                userRecord.get(USER.NAME),
                userRecord.get(USER.EMAIL),
                userRecord.get(USER.PASSWORD_HASH),
                UserType.fromDb(userRecord.get(USER.TYPE))
        );
    }
}
