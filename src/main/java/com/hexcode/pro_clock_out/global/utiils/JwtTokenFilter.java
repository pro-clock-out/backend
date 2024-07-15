package com.hexcode.pro_clock_out.global.utiils;

import com.hexcode.pro_clock_out.auth.exception.TokenException;
import com.hexcode.pro_clock_out.global.config.AuthMemberDetails;
import com.hexcode.pro_clock_out.member.domain.Member;
import com.hexcode.pro_clock_out.member.repository.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(isRequiredFiltering(authHeader)){
            String accessToken = jwtTokenProvider.getAccessToken(authHeader);
            Boolean isValidAccessToken = jwtTokenProvider.validateToken(accessToken);
            if(isValidAccessToken.equals(Boolean.FALSE)){ // 유효하지 않은 액세스 토큰
                throw new TokenException("Auth Error: Invalid Access Token");
            }
            String email = jwtTokenProvider.extractEmail(accessToken);
            Member authMember = memberRepository.findByEmail(email) // 사용자 찾기
                    .orElseThrow(() -> new TokenException("Not Found: Member"));
            AuthMemberDetails authMemberDetails = new AuthMemberDetails(authMember);
            Authentication authentication = new UsernamePasswordAuthenticationToken(authMemberDetails, null, authMemberDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        // JWT 토큰 검증이 필요한 경우에만 동작
        filterChain.doFilter(request, response);
    }

    private Boolean isRequiredFiltering(String authHeader){
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}