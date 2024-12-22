package ru.hse.commonmodule.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.hse.commonmodule.enums.Role;
import ru.hse.commonmodule.model.UserAuthentication;
import ru.hse.commonmodule.properties.JwtProperties;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

/**
 * Класс для работы с jwt токеном.
 */
@Component
@RequiredArgsConstructor
public class JwtUtils {

    private final JwtProperties jwtProperties;

    /**
     * Проверить валидность jwt токена.
     *
     * @param token jwt токен
     */
    public void validateToken(String token) {
        Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parse(token);
    }

    /**
     * Получить claims из токена.
     *
     * @param token jwt токен
     * @return claims
     */
    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Получить электронную почту из jwt токена.
     *
     * @param token jwt токен
     * @return электронная почта
     */
    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Получить модель с пользовательскими данными из jwt токена.
     *
     * @param token jwt токен
     * @return модель с пользовательскими данными
     */
    public UserAuthentication getUserAuthentication(String token) {
        Claims claims = extractAllClaims(token.substring(7));

        Long id = Long.valueOf(claims.get("id", String.class));
        String email = claims.getSubject();
        String phone = claims.get("phone", String.class);
        String fio = claims.get("fio", String.class);
        String role = claims.get("role", String.class);

        return UserAuthentication.of(id, email, phone, fio, Role.valueOf(role));
    }

    /**
     * Получить claim из jwt токена.
     *
     * @param token          jwt токен
     * @param claimsResolver функция, позволяющая получить claim
     * @param <T>            generic для claim
     * @return claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Получить секретный ключ.
     *
     * @return секретный ключ
     */
    public SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Проверка срока истечения токена.
     *
     * @param token jwt токен
     * @return true, если срок действия токена истек, false, если токен действителен.
     */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Получение времени истечения срока действия jwt токена.
     *
     * @return срок действия jwt токена
     */
    public Long getExpiresIn() {
        return jwtProperties.getExpiresIn();
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
