package com.hexcode.pro_clock_out.wolibal.repository;

import com.hexcode.pro_clock_out.member.domain.Member;
import com.hexcode.pro_clock_out.wolibal.domain.Wolibal;
import com.hexcode.pro_clock_out.wolibal.domain.Work;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WorkRepository extends JpaRepository<Work, Long> {
    Optional<Work> findByWolibal(Wolibal wolibal);

    @Query("SELECT COUNT(w) FROM Work w WHERE w.score > :score")
    long countByWorkHigherThan(@Param("score") int score);

    @Query("SELECT AVG(w.score) FROM Work w")
    int getAverageWork();

    @Query("SELECT w FROM Work w WHERE w.wolibal.member = :member ORDER BY w.wolibal.date DESC")
    List<Work> findRecent10(@Param("member") Member member, Pageable pageable);
}
