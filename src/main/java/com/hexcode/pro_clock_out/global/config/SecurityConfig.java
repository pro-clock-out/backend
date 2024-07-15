package com.hexcode.pro_clock_out.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexcode.pro_clock_out.global.utiils.JwtAuthenticationExceptionHandler;
import com.hexcode.pro_clock_out.global.utiils.JwtTokenFilter;
import com.hexcode.pro_clock_out.global.utiils.JwtTokenProvider;
import com.hexcode.pro_clock_out.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final ObjectMapper objectMapper;

    @Bean
    public JwtTokenFilter jwtTokenFilter() {
        return new JwtTokenFilter(jwtTokenProvider, memberRepository);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // security 기본 설정
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .cors(httpSecurityCorsConfigurer ->
                        httpSecurityCorsConfigurer.configurationSource(corsFilter()))
                .csrf(AbstractHttpConfigurer::disable)
                //.headers(c -> c.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable).disable()) // h2
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // authentication 관련 설정
        http.authorizeHttpRequests((request) -> {
            // ALL 인증
            request.requestMatchers("/api/v1/auth/**").authenticated();
            request.requestMatchers("/api/v1/members/**").authenticated();
            // GET 인증
            request.requestMatchers(HttpMethod.GET,
                    "/api/v1/**"
            ).authenticated();
            // POST 인증
            request.requestMatchers(HttpMethod.POST,
                    "/api/v1/**"
            ).authenticated();
            // PUT 인증
            request.requestMatchers(HttpMethod.PUT,
                    "/api/v1/**"
            ).authenticated();
            // PATCH 인증
            request.requestMatchers(HttpMethod.PATCH,
                    "/api/v1/**"
            ).authenticated();
            // DELETE 인증
            request.requestMatchers(HttpMethod.DELETE,
                    "/api/v1/**"
            ).authenticated();
            request.anyRequest().permitAll();
        });
        http.addFilterBefore(jwtAuthenticationFilter(), LogoutFilter.class);
        http.addFilterBefore(jwtAuthenticationExceptionHandlerFilter(), JwtTokenFilter.class);
        return http.build();
    }

    // CORS 설정
    @Bean
    public CorsConfigurationSource corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        // cors 설정
        config.setAllowedOriginPatterns(List.of("http://127.0.0.1:3000", "http://localhost:3000"));
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.addExposedHeader("Set-Cookie");
        config.setAllowCredentials(true);
        // source -> config 적용
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public JwtTokenFilter jwtAuthenticationFilter(){
        return new JwtTokenFilter(jwtTokenProvider, memberRepository);
    }

    @Bean
    public JwtAuthenticationExceptionHandler jwtAuthenticationExceptionHandlerFilter(){
        return new JwtAuthenticationExceptionHandler(objectMapper);
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() throws Exception{
        return (web) -> web.ignoring()
                .requestMatchers("/error");
    }
}
