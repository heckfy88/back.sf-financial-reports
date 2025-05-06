package sf.financialreports.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sf.financialreports.api.dto.login.LoginDto;
import sf.financialreports.api.dto.login.TokenDto;
import sf.financialreports.service.AuthenticationService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @Operation(
            summary = "Логин пользователя",
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
                    @ApiResponse(responseCode = "401", description = "Неверный логин или пароль"),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
            }
    )

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginDto loginRequest) {
        String token = authenticationService.login(loginRequest);
        return ResponseEntity.ok(new TokenDto(token));
    }
}
