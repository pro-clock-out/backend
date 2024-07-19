package com.hexcode.pro_clock_out.global.config;

import com.hexcode.pro_clock_out.auth.jwt.JwtUtil;
import com.hexcode.pro_clock_out.auth.jwt.LoginFilter;
import com.hexcode.pro_clock_out.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final MemberRepository memberRepository;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

//    @Bean
//    public JwtTokenFilter jwtTokenFilter() {
//        return new JwtTokenFilter(jwtTokenProvider, memberRepository);
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // security 기본 설정
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .cors(httpSecurityCorsConfigurer ->
                        httpSecurityCorsConfigurer.configurationSource(corsFilter()));
                //.headers(c -> c.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable).disable()) // h2
        // authentication 관련 설정
        http.authorizeHttpRequests((request) -> request
                        .requestMatchers("/", "/api/v1/join", "/login").permitAll()
//                        .requestMatchers("/admin").hasRole("ADMIN")
//                        .requestMatchers("/my/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/api/v1/members/me/dday").authenticated()
                        .anyRequest().authenticated()
        );
        http
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class);

        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//        http.addFilterBefore(jwtAuthenticationFilter(), LogoutFilter.class);
//        http.addFilterBefore(jwtAuthenticationExceptionHandlerFilter(), JwtTokenFilter.class);
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

//    @Bean
//    public JwtTokenFilter jwtAuthenticationFilter(){
//        return new JwtTokenFilter(jwtTokenProvider, memberRepository);
//    }
//
//    @Bean
//    public JwtAuthenticationExceptionHandler jwtAuthenticationExceptionHandlerFilter(){
//        return new JwtAuthenticationExceptionHandler(objectMapper);
//    }
//
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() throws Exception{
//        return (web) -> web.ignoring()
//                .requestMatchers("/error");
//    }
}
