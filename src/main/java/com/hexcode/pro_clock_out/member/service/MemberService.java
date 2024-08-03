package com.hexcode.pro_clock_out.member.service;

import com.hexcode.pro_clock_out.member.domain.Member;
import com.hexcode.pro_clock_out.member.dto.*;
import com.hexcode.pro_clock_out.member.exception.MemberNotFoundException;
import com.hexcode.pro_clock_out.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Member findMemberById(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));
    }

    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }

    public FindMyDDayResponse findMyDDay(Long memberId) {
        Member member = findMemberById(memberId);
        LocalDateTime createdAt = member.getCreatedAt();
        LocalDateTime now = LocalDateTime.now();
        int daysBetween = (int) ChronoUnit.DAYS.between(createdAt, now);
        return FindMyDDayResponse.builder()
                .dday(daysBetween)
                .build();
    }

    public FindProfileResponse findProfile(Long memberId) {
        Member member = findMemberById(memberId);
        return FindProfileResponse.createWith(member);
    }

    public UpdateProfileResponse updateProfileImage(Long memberId, UpdateProfileImageRequest request) {
        Member member = findMemberById(memberId);
        member.updatePhotoUrl(request.getPhotoUrl());
        memberRepository.save(member);
        return UpdateProfileResponse.createWith(member);
    }

    public UpdateProfileResponse updateNickname(Long memberId, UpdateNicknameRequest request) {
        Member member = findMemberById(memberId);
        member.updateNickname(request.getNickname());
        memberRepository.save(member);
        return UpdateProfileResponse.createWith(member);
    }

    public UpdateProfileResponse updateLifestyle(Long memberId, UpdateLifestyleRequest request) {
        Member member = findMemberById(memberId);
        member.updateLife(request.getLife());
        memberRepository.save(member);
        return UpdateProfileResponse.createWith(member);
    }
}
