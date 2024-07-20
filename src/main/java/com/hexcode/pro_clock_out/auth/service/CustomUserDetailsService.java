package com.hexcode.pro_clock_out.auth.service;

import com.hexcode.pro_clock_out.auth.dto.CustomUserDetails;
import com.hexcode.pro_clock_out.member.domain.Member;
import com.hexcode.pro_clock_out.member.exception.MemberNotFoundException;
import com.hexcode.pro_clock_out.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member userData = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);
        if (userData != null) {
            //UserDetails에 담아서 return하면 AutneticationManager가 검증 함
            log.info("Authentication Manager work with {}", email);
            return new CustomUserDetails(userData);
        }
        return null;
    }
}