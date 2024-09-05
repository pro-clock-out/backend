package com.hexcode.pro_clock_out.wolibal.repository;

import com.hexcode.pro_clock_out.member.domain.Member;
import com.hexcode.pro_clock_out.wolibal.domain.Health;
import com.hexcode.pro_clock_out.wolibal.domain.Wolibal;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HealthRepository extends JpaRepository<Health, Long> {
    Optional<Health> findByWolibal(Wolibal wolibal);

    @Query("SELECT COUNT(w) + 1 FROM Health w WHERE w.score > :score")
    long countByHealthHigherThan(@Param("score") int score);

    @Query("SELECT AVG(w.score) FROM Health w")
    int getAverageHealth();

    @Query("SELECT w FROM Health w WHERE w.wolibal.member = :member ORDER BY w.wolibal.date DESC")
    List<Health> findRecent10(@Param("member") Member member, Pageable pageable);
}
