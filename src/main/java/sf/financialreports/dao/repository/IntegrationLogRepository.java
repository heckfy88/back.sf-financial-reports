package sf.financialreports.dao.repository;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.springframework.stereotype.Repository;

import java.util.List;

import static sf.financialreports.dao.jooq.tables.IntegrationLog.INTEGRATION_LOG;

@Repository
public class IntegrationLogRepository {

    private final DSLContext dslContext;

    public IntegrationLogRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public static List<Field<?>> INTEGRATION_LOG_FIELDS = List.of(
            INTEGRATION_LOG.ID,
            INTEGRATION_LOG.OPER_UID,
            INTEGRATION_LOG.REQUEST_TYPE,
            INTEGRATION_LOG.CREATED_AT,
            INTEGRATION_LOG.PAYLOAD,
            INTEGRATION_LOG.REQUEST_PARAMS
    );
}
