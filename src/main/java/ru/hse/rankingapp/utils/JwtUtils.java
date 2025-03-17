package ru.hse.rankingapp.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.hse.rankingapp.dto.UserAuthentication;
import ru.hse.rankingapp.entity.enums.Role;
import ru.hse.rankingapp.enums.SeparatorEnum;
import ru.hse.rankingapp.properties.JwtProperties;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        Claims claims;

        if (token.startsWith("Bearer ")) {
            claims = extractAllClaims(token.substring(7));
        } else {
            claims = extractAllClaims(token);
        }

        String email = claims.getSubject();
        String rolesString = claims.get("roles", String.class);
        boolean isOrganization = claims.get("isOrganization", Boolean.class);
        boolean isAdmin = claims.get("isAdmin", Boolean.class);
        boolean isCurator = claims.get("isCurator", Boolean.class);

        Set<Role> roles = Arrays.stream(rolesString.split(SeparatorEnum.SPACE.getValue()))
                .map(Role::valueOf)
                .collect(Collectors.toSet());

        return UserAuthentication.of(email, isOrganization, isAdmin, isCurator, roles);
    }

    /**
     * Получить информацию о пользователе из запроса.
     */
    public UserAuthentication getUserInfoFromRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            return null;
        }

        HttpServletRequest request = attributes.getRequest();
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return getUserAuthentication(authorizationHeader);
        }

        return null;
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
