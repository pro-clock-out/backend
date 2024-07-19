package com.hexcode.pro_clock_out.auth.service;

import com.hexcode.pro_clock_out.auth.dto.JoinDto;
import com.hexcode.pro_clock_out.member.domain.Member;
import com.hexcode.pro_clock_out.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class JoinService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public void joinProcess(JoinDto joinDto) {

        //db에 이미 동일한 email 가진 회원이 존재하는지?
        boolean isMember = memberRepository.existByEmail(joinDto.getEmail());
        if (isMember) {
            return;
        }

        Member data = Member.builder()
                .email(joinDto.getEmail())
                .password(bCryptPasswordEncoder.encode(joinDto.getPassword()))
                .role("ROLE_USER")
                .build();

        memberRepository.save(data);
    }
}