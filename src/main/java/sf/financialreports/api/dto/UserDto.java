package sf.financialreports.api.dto;

import lombok.Builder;
import sf.financialreports.domain.User;
import sf.financialreports.domain.UserType;

import java.util.UUID;

@Builder
public record UserDto(
        UUID id,
        String username,
        String email,
        String passwordHash,
        UserType type
) {
    public static UserDto from(User user) {
        return new UserDto(
                user.id(),
                user.username(),
                user.email(),
                user.passwordHash(),
                user.type()
        );
    }
}
