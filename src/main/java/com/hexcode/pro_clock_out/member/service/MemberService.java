package com.hexcode.pro_clock_out.member.service;

import com.hexcode.pro_clock_out.global.service.GlobalService;
import com.hexcode.pro_clock_out.member.domain.Member;
import com.hexcode.pro_clock_out.member.dto.*;
import com.hexcode.pro_clock_out.member.exception.MemberNotFoundException;
import com.hexcode.pro_clock_out.member.repository.MemberRepository;
import com.hexcode.pro_clock_out.wolibal.service.WolibalService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final RestTemplate restTemplate;
    private final GlobalService globalService;
    private final WolibalService wolibalService;
    private final MemberRepository memberRepository;

    @Value("${openai.api-key}")
    private String OPEN_AI_KEY;

    public void createMember(String email, String encodedPassword) {
        Member newMember = Member.builder()
                .email(email)
                .password(encodedPassword)
                .role("ROLE_USER") // 역할 설정
                .build();
        memberRepository.save(newMember);
        wolibalService.initializeWolibal(newMember);
    }

    public Member findMemberByEmail(final String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);
    }

    public FindCheerMessage findCheerMessage(Long memberId) {
        Member member = globalService.findMemberById(memberId);

        ChatResponse call = ChatClient.builder(new OpenAiChatModel(new OpenAiApi(OPEN_AI_KEY)))
                .defaultUser(String.format("%s의 이름을 넣어서, 그에게 힘이 될 수 있는 응원 메시지를 40자 이내로 생성하라", member.getNickname()))
                .build()
                .prompt()
                .call()
                .chatResponse();

        return FindCheerMessage.builder()
                .message(call.getResult().getOutput().getContent())
                .build();
    }

    public FindMyDDayResponse findMyDDay(Long memberId) {
        Member member = globalService.findMemberById(memberId);
        LocalDateTime createdAt = member.getCreatedAt();
        LocalDateTime now = LocalDateTime.now();
        int daysBetween = (int) ChronoUnit.DAYS.between(createdAt, now);
        return FindMyDDayResponse.builder()
                .dday(daysBetween)
                .build();
    }

    public FindProfileResponse findProfile(Long memberId) {
        Member member = globalService.findMemberById(memberId);
        return FindProfileResponse.createWith(member);
    }

    public UpdateProfileResponse updateProfileImage(Long memberId, String imageUrl) {
        Member member = globalService.findMemberById(memberId);
        member.updatePhotoUrl(imageUrl);
        memberRepository.save(member);
        return UpdateProfileResponse.createWith(member);
    }

    public UpdateProfileResponse updateNickname(Long memberId, UpdateNicknameRequest request) {
        Member member = globalService.findMemberById(memberId);
        member.updateNickname(request.getNickname());
        memberRepository.save(member);
        return UpdateProfileResponse.createWith(member);
    }

    public UpdateProfileResponse updateLifestyle(Long memberId, UpdateLifestyleRequest request) {
        Member member = globalService.findMemberById(memberId);
        member.updateLife(request.getLife());
        memberRepository.save(member);
        return UpdateProfileResponse.createWith(member);
    }

    public DuplicateEmailResponse hasEmail(DuplicateEmailRequest request) {
        Optional<Member> member = memberRepository.findByEmail(request.getEmail());
        if(member.isPresent()) {
            return DuplicateEmailResponse.createWith(true);
        } else {
            return DuplicateEmailResponse.createWith(false);
        }
    }

    public void addSuggestionMember(Member member, String suggestionMessage) {
        member.addSuggestion(suggestionMessage);
    }

    public void removeSuggestionMember(Member member, String suggestionMessage) {
        member.removeSuggestion(suggestionMessage);
    }

    public SuggestSolutionResponse findSuggestionMessage(Long memberId) {
        Member member = globalService.findMemberById(memberId);
        return SuggestSolutionResponse.builder()
                .suggestionString(member.getSuggestionString())
                .build();
    }
}
