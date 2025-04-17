package sf.financialreports.domain;

import sf.financialreports.api.dto.UserDto;

import java.util.UUID;

public record User(
        UUID id,
        String username,
        String email,
        String passwordHash,
        UserType type
) {
    public static User from(UserDto dto) {
        return new User(
                dto.id(),
                dto.username(),
                dto.email(),
                dto.passwordHash(),
                dto.type()
        );
    }
}