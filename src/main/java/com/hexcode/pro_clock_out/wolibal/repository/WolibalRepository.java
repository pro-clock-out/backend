package com.hexcode.pro_clock_out.wolibal.repository;

import com.hexcode.pro_clock_out.member.domain.Member;
import com.hexcode.pro_clock_out.wolibal.domain.Wolibal;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WolibalRepository extends JpaRepository<Wolibal, Long> {
    Optional<Wolibal> findByMember(Member member);

    Optional<Wolibal> findByDateAndMember(LocalDate date, Member member);

    @Query("SELECT COUNT(w) + 1 FROM Wolibal w WHERE w.score > :score")
    long countByTotalHigherThan(@Param("score") int score);

    @Query("SELECT AVG(w.score) FROM Wolibal w")
    int getAverageTotal();

    @Query("SELECT w FROM Wolibal w WHERE w.member = :member ORDER BY w.date DESC")
    List<Wolibal> findRecent10(@Param("member") Member member, Pageable pageable);
}
