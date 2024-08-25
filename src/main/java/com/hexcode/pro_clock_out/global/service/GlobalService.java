package com.hexcode.pro_clock_out.global.service;

import com.hexcode.pro_clock_out.global.domain.Label;
import com.hexcode.pro_clock_out.member.domain.Member;
import com.hexcode.pro_clock_out.member.exception.MemberNotFoundException;
import com.hexcode.pro_clock_out.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class GlobalService {
    private final MemberRepository memberRepository;

    public Member findMemberById(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));
    }

    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }

    /**
     * 만족도 점수 적용 ///////////////////////////////////////////////////
     */
    public Integer applySatisfaction(double basicScore, int satisfaction, Label label) {
        int score = (int) (basicScore * (1 + ((satisfaction - 5) / 200.0)));
        String suggestionMessage = label + "에 대한 만족도가 낮습니다.";

        return Math.max(0, Math.min(100, score));
    }
}
