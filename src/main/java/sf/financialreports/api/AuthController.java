package sf.financialreports.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sf.financialreports.api.dto.login.LoginDto;
import sf.financialreports.api.dto.login.TokenDto;
import sf.financialreports.dao.domain.RequestType;
import sf.financialreports.service.AuditService;
import sf.financialreports.service.AuthenticationService;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;
    private final AuditService auditService;

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(
            @RequestHeader("operUid") UUID operUid,
            @RequestBody LoginDto loginRequest,
            @Parameter(hidden = true) HttpServletRequest request,
            @Parameter(hidden = true) HttpServletResponse response
    ) throws JsonProcessingException {

        auditService.audit(
                auditService.prepareRequestAudit(
                        operUid,
                        request,
                        RequestType.LOGIN,
                        loginRequest,
                        null
                )
        );

        String token = authenticationService.login(loginRequest);
        TokenDto dto = new TokenDto(token);

        auditService.audit(auditService.prepareResponseAudit(
                operUid,
                response,
                RequestType.LOGIN,
                request.getRequestURI(),
                new TokenDto("Bearer ***tokenValue***")
        ));

        return ResponseEntity.ok(dto);
    }
}
