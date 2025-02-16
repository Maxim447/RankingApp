package ru.hse.rankingapp.service.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.hse.rankingapp.entity.AccountEntity;
import ru.hse.rankingapp.entity.enums.Role;
import ru.hse.rankingapp.utils.JwtUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * Сервис для работы с jwt.
 */
@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtUtils jwtUtils;

    /**
     * Получить email из jwt токена.
     *
     * @param token jwt токен
     * @return email
     */
    public String extractEmail(String token) {
        return jwtUtils.extractEmail(token);
    }

    /**
     * Проверить валидность jwt токена.
     *
     * @param token       jwt токен
     * @param userDetails информация о пользователе
     * @return валидный/невалидный (true/false)
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String email = extractEmail(token);
        return (email.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Сгенерировать jwt токен.
     */
    public String generateToken(AccountEntity accountEntity) {
        Map<String, Object> extraClaims = new HashMap<>();

        Set<Role> roles = accountEntity.getRoles();

        extraClaims.put("email", accountEntity.getEmail());
        extraClaims.put("roles", accountEntity.getRoleString());
        extraClaims.put("isOrganization", roles.contains(Role.ORGANIZATION));
        extraClaims.put("isAdmin", roles.contains(Role.ADMIN));

        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(accountEntity.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtUtils.getExpiresIn()))
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return jwtUtils.extractClaim(token, claimsResolver);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private SecretKey getSignInKey() {
        return jwtUtils.getSignInKey();
    }
}

