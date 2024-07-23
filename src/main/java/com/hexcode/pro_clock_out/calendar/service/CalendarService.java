package com.hexcode.pro_clock_out.calendar.service;

import com.hexcode.pro_clock_out.calendar.domain.Calendar;
import com.hexcode.pro_clock_out.calendar.dto.*;
import com.hexcode.pro_clock_out.calendar.exception.*;
import com.hexcode.pro_clock_out.calendar.repository.CalendarRepository;
import com.hexcode.pro_clock_out.member.domain.Member;
import com.hexcode.pro_clock_out.calendar.dto.AddCalendarResponse;
import com.hexcode.pro_clock_out.member.repository.MemberRepository;
import com.hexcode.pro_clock_out.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CalendarService {
    private final MemberService memberService;
    private final CalendarRepository calendarRepository;
    private final MemberRepository memberRepository;

    public Calendar findCalendarById(final Long calendarId) {
        return calendarRepository.findById(calendarId)
                .orElseThrow(() -> new CalendarNotFoundException(calendarId));
    }



//    public FindWeeklyCalendarResponse findWeeklyCalendar(Long memberId, String option) {
//        long year
//    }

    public FindCalendarDetailResponse findCalendarDetail(Long calendarId) {
        Calendar calendar = findCalendarById(calendarId);
        return FindCalendarDetailResponse.createWith(calendar);
    }

    public AddCalendarResponse addCalendar(Long memberId, UpdateCalendarRequest request) {
        Member member = memberService.findMemberById(memberId);
        Calendar calendar = Calendar.builder()
                .label(request.getLabel())
                .title(request.getTitle())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .location(request.getLocation())
                .notes(request.getNotes())
                .member(member)
                .build();
        calendarRepository.save(calendar);
        return AddCalendarResponse.createWith(calendar);
    }

    public UpdateCalendarResponse updateCalendar(Long calendarId, UpdateCalendarRequest request) {
        Calendar calendar = findCalendarById(calendarId);
        UpdateCalendarData updateCalendarData = UpdateCalendarData.createWith(request);
        calendar.updateCalendar(updateCalendarData);
        calendarRepository.save(calendar);
        return UpdateCalendarResponse.createWith(calendar);

    }


    public DeleteCalendarResponse deleteCalendar(Long calendarId) {
        Calendar calendar = findCalendarById(calendarId);
        calendarRepository.deleteById(calendarId);
        return DeleteCalendarResponse.createWith(calendar);
    }

   public List<Calendar> returncat(Long memberId) {
        return calendarRepository.findAllByMemberId(memberId);
   }

//    public List<Documents> returncat(Long id, String type) {
//        return documentRepository.findDocumentsByUser_IdAndType(id, type);
//    }

    public List<CalendarEventResponse> getAllCalendarEvents(Long memberId) {
        Member member = memberService.findMemberById(memberId);

        return returncat(member.getId())
                .stream()
                .map(calendar -> new CalendarEventResponse(calendar))
                .collect(Collectors.toList());
    }
}
