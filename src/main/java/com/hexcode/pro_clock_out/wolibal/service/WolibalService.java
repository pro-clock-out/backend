package com.hexcode.pro_clock_out.wolibal.service;

import com.hexcode.pro_clock_out.member.domain.Member;
import com.hexcode.pro_clock_out.member.exception.WolibalNotFoundException;
import com.hexcode.pro_clock_out.member.service.MemberService;
import com.hexcode.pro_clock_out.wolibal.domain.Wolibal;
import com.hexcode.pro_clock_out.wolibal.dto.FindLabelsWolibalResponse;
import com.hexcode.pro_clock_out.wolibal.dto.FindTotalWolibalResponse;
import com.hexcode.pro_clock_out.wolibal.dto.WolibalScoreRankAvgDto;
import com.hexcode.pro_clock_out.wolibal.repository.WolibalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class WolibalService {
//    private final WolibalRepository wolibalRepository;
//    private final MemberService memberService;
//
//    public Wolibal findWolibalByMember(final Member member) {
//        return wolibalRepository.findByMember(member)
//                .orElseThrow(() -> new WolibalNotFoundException(member.getId()));
//    }
//
//    public FindTotalWolibalResponse findTotalWolibal(Long memberId, String option) {
//        Member member = memberService.findMemberById(memberId);
//        Wolibal wolibal = findWolibalByMember(member);
//        WolibalScoreRankAvgDto totalDto = createScoreRankAvgDto(wolibal.getTotalScore(), "total");
//        return FindTotalWolibalResponse.createWith(wolibal, totalDto);
//    }
//
//    public FindLabelsWolibalResponse findLabelsWolibal(Long memberId, String option) {
//        Member member = memberService.findMemberById(memberId);
//        Wolibal wolibal = findWolibalByMember(member);
//        WolibalScoreRankAvgDto workDto = createScoreRankAvgDto(wolibal.getWorkScore(), "work");
//        WolibalScoreRankAvgDto restDto = createScoreRankAvgDto(wolibal.getRestScore(), "rest");
//        WolibalScoreRankAvgDto sleepDto = createScoreRankAvgDto(wolibal.getSleepScore(), "sleep");
//        WolibalScoreRankAvgDto personalDto = createScoreRankAvgDto(wolibal.getPersonalScore(), "personal");
//        WolibalScoreRankAvgDto healthDto = createScoreRankAvgDto(wolibal.getHealthScore(), "health");
//        return FindLabelsWolibalResponse.createWith(member.getId(), workDto, restDto, sleepDto, personalDto, healthDto);
//    }
//
//    private WolibalScoreRankAvgDto createScoreRankAvgDto(int score, String label) {
//        long higherCount = calculateHigherCount(label, score);
//        int rank = calculateRank(higherCount);
//        int avg = getAverage(label);
//        return WolibalScoreRankAvgDto.builder()
//                .score(score)
//                .rank(rank)
//                .avg(avg)
//                .build();
//    }
//
//    private int calculateRank(long higherCount) {
//        long allCount = wolibalRepository.count();
//        return (int) ((higherCount * 100) / allCount);
//    }
//
//    private long calculateHigherCount(String label, int score) {
//        return switch (label) {
//            case "total" -> wolibalRepository.countByTotalHigherThan(score);
//            case "work" -> wolibalRepository.countByWorkHigherThan(score);
//            case "rest" -> wolibalRepository.countByRestHigherThan(score);
//            case "sleep" -> wolibalRepository.countBySleepHigherThan(score);
//            case "personal" -> wolibalRepository.countByPersonalHigherThan(score);
//            case "health" -> wolibalRepository.countByHealthHigherThan(score);
//            default -> throw new IllegalArgumentException("Invalid label name: " + label);
//        };
//    }
//
//    private int getAverage(String label) {
//        return switch (label) {
//            case "total" -> wolibalRepository.getAverageTotal();
//            case "work" -> wolibalRepository.getAverageWork();
//            case "rest" -> wolibalRepository.getAverageRest();
//            case "sleep" -> wolibalRepository.getAverageSleep();
//            case "personal" -> wolibalRepository.getAveragePersonal();
//            case "health" -> wolibalRepository.getAverageHealth();
//            default -> throw new IllegalArgumentException("Invalid label name: " + label);
//        };
//    }
}
