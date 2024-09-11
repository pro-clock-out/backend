package com.hexcode.pro_clock_out.member.service;

import com.hexcode.pro_clock_out.global.service.GlobalService;
import com.hexcode.pro_clock_out.member.domain.Member;
import com.hexcode.pro_clock_out.member.dto.*;
import com.hexcode.pro_clock_out.member.exception.MemberNotFoundException;
import com.hexcode.pro_clock_out.member.repository.MemberRepository;
import com.hexcode.pro_clock_out.wolibal.domain.Wolibal;
import com.hexcode.pro_clock_out.wolibal.service.WolibalService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private static final Logger log = LoggerFactory.getLogger(MemberService.class);
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

    public FindCheerMessage findCheerMessage() {
        ChatResponse call = ChatClient.builder(new OpenAiChatModel(new OpenAiApi(OPEN_AI_KEY)))
                .defaultSystem("너는 반말을 사용하는 사람이다.")
                .defaultUser("오늘 하루를 힘차게 보낼 수 있도록 조언 및 응원 메시지를 60자 이내로 생성하라")
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

    public SuggestSolutionResponse findSuggestionMessage(Long memberId) {
        Member member = globalService.findMemberById(memberId);
        Wolibal wolibal = wolibalService.findTodayWolibalByMember(member);
        String wolibalDataText = wolibalService.getWolibalDataText(wolibal);
        ChatResponse call = ChatClient.builder(new OpenAiChatModel(new OpenAiApi(OPEN_AI_KEY)))
                .defaultSystem("사용자의 워라밸을 개선하기 위해 피드백을 진행하라")
                .defaultUser("사용자의 워라밸 관련 정보는 다음과 같다.\n" + wolibalDataText)
                .defaultUser("답변 형식은 다음과 같다.\n문제점 분석:{}\n개선 방안:{}")
                .build()
                .prompt()
                .call()
                .chatResponse();
        log.info(wolibalDataText);

        return SuggestSolutionResponse.builder()
                .solution(call.getResult().getOutput().getContent())
                .build();
    }
}
