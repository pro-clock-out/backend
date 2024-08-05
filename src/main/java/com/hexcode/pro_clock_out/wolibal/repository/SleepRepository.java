package com.hexcode.pro_clock_out.wolibal.repository;

import com.hexcode.pro_clock_out.wolibal.domain.Sleep;
import com.hexcode.pro_clock_out.wolibal.domain.Wolibal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SleepRepository extends JpaRepository<Sleep, Long> {
    Optional<Sleep> findByWolibal(Wolibal wolibal);

    @Query("SELECT COUNT(w) FROM Sleep w WHERE w.score > :score")
    long countBySleepHigherThan(@Param("score") int score);

    @Query("SELECT AVG(w.score) FROM Sleep w")
    int getAverageSleep();
}
