package com.hexcode.pro_clock_out.daily.repository;

import com.hexcode.pro_clock_out.daily.domain.Daily;
import com.hexcode.pro_clock_out.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DailyRepository extends JpaRepository<Daily, Long> {
    List<Daily> findDailyByMember(Member member);

}
