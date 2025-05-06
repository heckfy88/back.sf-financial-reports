package sf.financialreports.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sf.financialreports.dao.domain.Audit;
import sf.financialreports.dao.domain.MessageType;
import sf.financialreports.dao.domain.RequestType;
import sf.financialreports.dao.domain.User;
import sf.financialreports.dao.repository.AuditRepository;
import sf.financialreports.service.AuditService;
import sf.financialreports.service.AuthenticationService;
import sf.financialreports.util.HeaderUtils;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;
    private final AuthenticationService authenticationService;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public void audit(Audit audit) {
        User user = authenticationService.getUserFromToken();
        // when login - no user yet
        if (user != null) {
            audit.setUserId(user.getId());
        }

        auditRepository.save(audit);
    }

    @Override
    public Audit prepareRequestAudit(
            UUID operUid,
            HttpServletRequest request,
            RequestType requestType,
            Object payload,
            String requestParams
    ) throws JsonProcessingException {
        return Audit.builder()
                .operUid(operUid)
                .messageType(MessageType.REQUEST)
                .requestType(requestType)
                .requestPath(request.getRequestURI())
                .requestHeaders(HeaderUtils.getRequestHeaders(request).toString())
                .payload(payload == null ? "" : objectMapper.writeValueAsString(payload))
                .build();
    }

    @Override
    public Audit prepareResponseAudit(
            UUID operUid,
            HttpServletResponse response,
            RequestType requestType,
            String path,
            Object payload
    ) throws JsonProcessingException {
        return Audit.builder()
                .operUid(operUid)
                .messageType(MessageType.RESPONSE)
                .requestType(requestType)
                .requestPath(path)
                .requestHeaders(HeaderUtils.getResponseHeaders(response).toString())
                .payload(payload == null ? "" : objectMapper.writeValueAsString(payload))
                .build();
    }
}
