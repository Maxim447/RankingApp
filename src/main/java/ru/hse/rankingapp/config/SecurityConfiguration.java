package ru.hse.rankingapp.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.hse.rankingapp.entity.enums.Role;
import ru.hse.rankingapp.service.auth.CustomUserDetailsService;

/**
 * Конфигурация безопасности.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final CustomUserDetailsService customUserDetailsService;

    /**
     * Бин SecurityFilterChain.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request ->
                        request
                                .requestMatchers(
                                        "/api/v1/auth/**",
                                        "/api/v1/user/confirm-invite",
                                        "/api/v1/password/change",
                                        "/api/v1/password/validate-token",
                                        "/api/v1/password/recovery",
                                        "/ranking-app/swagger-ui",
                                        "/ranking-app/swagger-ui/*",
                                        "/ranking-app/v3/api-docs/*",
                                        "/ranking-app/v3/api-docs",
                                        "/actuator/*",
                                        "/api/v1/event/find/**",
                                        "/api/v1/event/search/**",
                                        "/api/v1/competition/find/**",
                                        "/api/v1/competition/search/**",
                                        "/api/v1/coordinates",
                                        "/api/v1/user/search",
                                        "/api/v1/user/rating-search",
                                        "/api/v1/organization/search",
                                        "/api/v1/file/**",
                                        "/api/v1/news",
                                        "/api/v1/about-us/**",
                                        "/api/v1/organization/curator/{token}",
                                        "/api/v1/trainers/**")
                                .permitAll()
                                .requestMatchers("/api/v1/admin/**").hasAuthority(Role.ADMIN.name())
                                .requestMatchers("/api/v1/organization/curator/**").hasAuthority(Role.CURATOR.name())
                                .anyRequest()
                                .authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Бин AuthenticationProvider.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(customUserDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    /**
     * Бин PasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Бин AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
