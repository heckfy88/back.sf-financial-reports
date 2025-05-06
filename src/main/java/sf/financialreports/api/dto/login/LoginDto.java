package sf.financialreports.api.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Объект авторизации пользователя")
public class LoginDto {
    @Schema(description = "Электронная почта пользователя", requiredMode = Schema.RequiredMode.REQUIRED, example = "user@gmail.com")
    private String email;
    @Schema(description = "Пароль пользователя", requiredMode = Schema.RequiredMode.REQUIRED, example = "qwerty123")
    private String password;
}
