package sf.financialreports.repository;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.springframework.stereotype.Repository;
import sf.financialreports.domain.User;


import java.util.List;
import java.util.UUID;

import static sf.financialreports.domain.jooq.Tables.USER;

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

    public sf.financialreports.domain.User findById(UUID userId) {
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
}
