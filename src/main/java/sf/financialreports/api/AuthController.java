package sf.financialreports.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sf.financialreports.api.dto.ErrorDto;
import sf.financialreports.api.dto.login.LoginDto;
import sf.financialreports.api.dto.login.TokenDto;
import sf.financialreports.dao.domain.RequestType;
import sf.financialreports.service.AuditService;
import sf.financialreports.service.AuthenticationService;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Авторизация", description = "Сервис авторизации пользователей")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;
    private final AuditService auditService;

    @Operation(
            summary = "Авторизация пользователя",
            description = "Авторизация пользователя с получением токена для дальнейших запросов.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешный логин",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TokenDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Неверный логин или пароль",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Внутренняя ошибка сервера",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    )
            }
    )
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(
            @Parameter(
                    description = "Уникальный идентификатор оператора",
                    required = true,
                    example = "9f8c1d45-b4e1-4f4b-9ad8-12b3d98f726e"
            )
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
