package sf.financialreports.api.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "JWT токен авторизации")
public class TokenDto {
    @Schema(description = "Токен авторизации",
            example = "eyJraWQiOiJteS1oczI1Ni1rZXkiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJmaW5hbmNpYWwtcmVwb3J0cyIsInN1YiI6IjEyM2U0NTY3LWU4OWItMTJkMy1hNDU2LTQyNjYxNDE3NDAwMCIsImV4cCI6MTc0NjU3NjQxOSwiaWF0IjoxNzQ2NTcyODE5fQ.3pwCJz6DC1aqv1PHinLvpFBeM85wrbj_bGvMHbw0ACw\n"
    )
    private String token;
}