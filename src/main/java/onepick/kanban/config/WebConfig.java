package onepick.kanban.config;

import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import onepick.kanban.config.filter.JwtAuthFilter;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final AuthenticationEntryPoint authEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;
    private static final String[] WHITE_LIST = {"/users/signup", "/users/login", "/users/refresh", "/error"};

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(WHITE_LIST).permitAll()
                                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.INCLUDE,
                                        DispatcherType.ERROR).permitAll()
                                .requestMatchers("/admins/**").hasRole("ADMIN")
                                .requestMatchers("/staffs/**").hasRole("STAFF")
                                .requestMatchers(HttpMethod.POST, "/workspaces/**", "/boards/**", "/cards/**").hasRole("USER")
                                .requestMatchers(HttpMethod.PATCH, "/workspaces/**", "/boards/**", "/cards/**").hasRole("USER")
                                .requestMatchers(HttpMethod.PUT, "/workspaces/**", "/boards/**", "/cards/**").hasRole("USER")
                                .requestMatchers(HttpMethod.DELETE, "/workspaces/**", "/boards/**", "/cards/**").hasRole("USER")
                                .anyRequest().authenticated()
                )
                // Spring Security 예외에 대한 처리를 핸들러에 위임
                .exceptionHandling(handler -> handler
                        .authenticationEntryPoint(authEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))
                // JWT 기반 테스트를 위해 SecurityContext를 가져올 때 HttpSession을 사용하지 않도록 설정
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 사용자 권한의 계층을 설정
    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.fromHierarchy(
                    """
                    ROLE_ADMIN > ROLE_STAFF
                    ROLE_STAFF > ROLE_USER
                    ROLE_USER > ROLE_READONLY
                    """);
    }
}