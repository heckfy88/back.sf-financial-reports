package sf.financialreports.service;

import sf.financialreports.api.dto.LoginRqDto;

public interface AuthenticationService {
    String login(LoginRqDto loginDto);
}
