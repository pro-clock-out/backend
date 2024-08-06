package com.hexcode.pro_clock_out.wolibal.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.hexcode.pro_clock_out.global.dto.ResponseDto;
import com.hexcode.pro_clock_out.wolibal.domain.*;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FindWolibalTransitionsResponse implements ResponseDto {
    private List<TransitionData> total;
    private List<TransitionData> work;
    private List<TransitionData> rest;
    private List<TransitionData> sleep;
    private List<TransitionData> personal;
    private List<TransitionData> health;

    public static FindWolibalTransitionsResponse createWith(List<Wolibal> totals10, List<Work> works10, List<Rest> rests10, List<Sleep> sleeps10, List<Personal> personals10, List<Health> healths10) {
        List<TransitionData> totals = totals10.stream()
                .map(total -> TransitionData.createWith(total.getDate(), total.getScore()))
                .toList();
        List<TransitionData> works = works10.stream()
                .map(work -> TransitionData.createWith(work.getWolibal().getDate(), work.getScore()))
                .toList();
        List<TransitionData> rests = rests10.stream()
                .map(rest -> TransitionData.createWith(rest.getWolibal().getDate(), rest.getScore()))
                .toList();
        List<TransitionData> sleeps = sleeps10.stream()
                .map(sleep -> TransitionData.createWith(sleep.getWolibal().getDate(), sleep.getScore()))
                .toList();
        List<TransitionData> personals = personals10.stream()
                .map(personal -> TransitionData.createWith(personal.getWolibal().getDate(), personal.getScore()))
                .toList();
        List<TransitionData> healths = healths10.stream()
                .map(health -> TransitionData.createWith(health.getWolibal().getDate(), health.getScore()))
                .toList();

        return FindWolibalTransitionsResponse.builder()
                .total(totals)
                .work(works)
                .rest(rests)
                .sleep(sleeps)
                .personal(personals)
                .health(healths)
                .build();
    }
}
