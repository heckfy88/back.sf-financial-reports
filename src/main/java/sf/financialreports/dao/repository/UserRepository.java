package sf.financialreports.dao.repository;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.springframework.stereotype.Repository;
import sf.financialreports.dao.domain.User;
import sf.financialreports.dao.jooq.enums.UserType;

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
        return dslContext.select(
                        USER.ID,
                        USER.NAME,
                        USER.EMAIL,
                        USER.TYPE,
                        USER.CREATED_AT,
                        USER.IS_ACTIVE
                )
                .from(USER)
                .where(USER.ID.eq(userId))
                .fetchOneInto(User.class);
    }

    public User update(User user) {
        return dslContext.update(USER)
                .set(USER.TYPE, UserType.valueOf(user.getType().name()))
                .where(USER.ID.eq(user.getId()))
                .returning(
                        USER.ID,
                        USER.NAME,
                        USER.EMAIL,
                        USER.TYPE,
                        USER.CREATED_AT,
                        USER.IS_ACTIVE)
                .fetchSingleInto(User.class);
    }
}
