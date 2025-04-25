package sf.financialreports.dao.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sf.financialreports.api.dto.UserDto;

import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private UUID id;
    private String username;
    private String email;
    private String passwordHash;
    private UserType type;

    public static User from(UserDto dto) {
        return new User(
                dto.getId(),
                dto.getUsername(),
                dto.getEmail(),
                dto.getPasswordHash(),
                dto.getType()
        );
    }
}