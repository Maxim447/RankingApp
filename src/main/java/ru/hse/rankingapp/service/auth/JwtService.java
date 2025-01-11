package ru.hse.rankingapp.service.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.hse.rankingapp.entity.OrganizationEntity;
import ru.hse.rankingapp.entity.UserEntity;
import ru.hse.rankingapp.utils.JwtUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
    public String generateToken(UserEntity user) {
        return generateToken(new HashMap<>(), user);
    }

    /**
     * Сгенерировать jwt токен.
     */
    public String generateToken(OrganizationEntity organization) {
        return generateToken(new HashMap<>(), organization);
    }

    private String generateToken(Map<String, Object> extraClaims, UserEntity user) {
        return buildToken(extraClaims, user);
    }

    private String generateToken(Map<String, Object> extraClaims, OrganizationEntity organization) {
        return buildTokenByOrganization(extraClaims, organization);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return jwtUtils.extractClaim(token, claimsResolver);
    }

    private String buildTokenByOrganization(
            Map<String, Object> extraClaims,
            OrganizationEntity organization
    ) {
        extraClaims.put("id", organization.getId());
        extraClaims.put("email", organization.getEmail());
        extraClaims.put("name", organization.getName());
        extraClaims.put("role", organization.getRole().name());
        extraClaims.put("isOrganization", true);

        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(organization.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtUtils.getExpiresIn()))
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserEntity user
    ) {
        extraClaims.put("id", user.getId());
        extraClaims.put("user_phone", user.getPhone());
        extraClaims.put("emergency_phone", user.getEmergencyPhone());
        extraClaims.put("email", user.getEmail());
        extraClaims.put("fio", user.getLastName() + " " + user.getFirstName() + " " + user.getMiddleName());
        extraClaims.put("role", user.getRole().name());
        extraClaims.put("isOrganization", false);

        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(user.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtUtils.getExpiresIn()))
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
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

