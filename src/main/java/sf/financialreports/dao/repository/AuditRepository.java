package sf.financialreports.dao.repository;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.springframework.stereotype.Repository;
import sf.financialreports.dao.domain.Audit;

import java.util.List;

import static sf.financialreports.dao.jooq.Tables.AUDIT;


@Repository
public class AuditRepository {

    public static List<Field<?>> AUDIT_FIELDS = List.of(
            AUDIT.ID,
            AUDIT.OPER_UID,
            AUDIT.USER_ID,
            AUDIT.MESSAGE_TYPE,
            AUDIT.REQUEST_PATH,
            AUDIT.REQUEST_HEADERS,
            AUDIT.REQUEST_PARAMS,
            AUDIT.PAYLOAD,
            AUDIT.CREATED_AT
    );
    private final DSLContext dslContext;

    public AuditRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public Audit save(Audit audit) {
        return dslContext.insertInto(AUDIT,
                        AUDIT.OPER_UID,
                        AUDIT.USER_ID,
                        AUDIT.MESSAGE_TYPE,
                        AUDIT.REQUEST_TYPE,
                        AUDIT.REQUEST_PATH,
                        AUDIT.REQUEST_HEADERS,
                        AUDIT.REQUEST_PARAMS,
                        AUDIT.PAYLOAD
                )
                .values(
                        audit.getOperUid(),
                        audit.getUserId(),
                        audit.getMessageType().name(),
                        audit.getRequestType().name(),
                        audit.getRequestPath(),
                        audit.getRequestHeaders(),
                        audit.getRequestParams(),
                        audit.getPayload()
                )
                .returning(AUDIT_FIELDS)
                .fetchSingleInto(Audit.class);
    }
}
