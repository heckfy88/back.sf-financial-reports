package sf.financialreports.service.impl;

import com.nimbusds.jose.JWSAlgorithm;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import sf.financialreports.api.dto.login.LoginDto;
import sf.financialreports.config.security.JwtProperties;
import sf.financialreports.dao.domain.User;
import sf.financialreports.dao.repository.UserRepository;
import sf.financialreports.service.AuthenticationService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(JwtProperties.class)
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;
    private final JwtProperties jwtProperties;
    private final UserRepository userRepository;

    Logger log = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    @Override
    public String login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()
                )
        );

        return generateToken(authentication);
    }

    @Override
    public User getUserFromToken() {
        Jwt jwt = getToken();
        if (jwt == null) {
            return null;
        }

        Map<String, Object> claims = jwt.getClaims();
        return userRepository.findById(UUID.fromString(claims.get("sub").toString()));
    }

    private String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        Instant expiry = now.plus(jwtProperties.getExpiration(), ChronoUnit.HOURS);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(jwtProperties.getIssuer())
                .issuedAt(now)
                .expiresAt(expiry)
                .subject(authentication.getName())
                .build();

        JwsHeader headers = JwsHeader.with(JWSAlgorithm.HS256::getName)
                .keyId(jwtProperties.getKeyId())
                .build();

        log.info("Generating token for user: {}", authentication.getName());

        return this.jwtEncoder.encode(JwtEncoderParameters.from(headers, claims)).getTokenValue();
    }
}
