package com.hexcode.pro_clock_out.wolibal.repository;

import com.hexcode.pro_clock_out.wolibal.domain.Personal;
import com.hexcode.pro_clock_out.wolibal.domain.Wolibal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PersonalRepository extends JpaRepository<Personal, Long> {
    Optional<Personal> findByWolibal(Wolibal wolibal);

    @Query("SELECT COUNT(w) FROM Personal w WHERE w.score > :score")
    long countByPersonalHigherThan(@Param("score") int score);

    @Query("SELECT AVG(w.score) FROM Personal w")
    int getAveragePersonal();
}
