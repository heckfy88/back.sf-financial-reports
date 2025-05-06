package sf.financialreports.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sf.financialreports.dao.domain.Audit;
import sf.financialreports.dao.domain.RequestType;

import java.util.UUID;

public interface AuditService {
    void audit(Audit audit);

    Audit prepareRequestAudit(
            UUID operUid,
            HttpServletRequest request,
            RequestType requestType,
            Object payload,
            String requestParams
    ) throws JsonProcessingException;

    Audit prepareResponseAudit(
            UUID operUid,
            HttpServletResponse response,
            RequestType requestType,
            String path,
            Object payload
    ) throws JsonProcessingException;
}
