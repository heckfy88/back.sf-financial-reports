package sf.financialreports.api.dto;

import lombok.Builder;
import lombok.Data;
import sf.financialreports.dao.domain.User;
import sf.financialreports.dao.domain.UserType;

import java.util.UUID;

@Builder
@Data
public class UserDto {
    private UUID id;
    private String username;
    private String email;
    private String passwordHash;
    private UserType type;

    public static UserDto from(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getType()
        );
    }
}
