package com.hexcode.pro_clock_out.wolibal.repository;

import com.hexcode.pro_clock_out.member.domain.Member;
import com.hexcode.pro_clock_out.wolibal.domain.Wolibal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Date;
import java.util.Optional;

public interface WolibalRepository extends JpaRepository<Wolibal, Long> {
    Optional<Wolibal> findByMember(Member member);

    Optional<Wolibal> findByDateAndMember(Date date, Member member);

//
//    @Query("SELECT COUNT(w) FROM Wolibal w WHERE w.total > :total")
//    long countByTotalHigherThan(@Param("total") int total);
//
//    @Query("SELECT AVG(w.total) FROM Wolibal w")
//    int getAverageTotal();
//
//    @Query("SELECT COUNT(w) FROM Wolibal w WHERE w.work > :work")
//    long countByWorkHigherThan(@Param("work") int work);
//
//    @Query("SELECT AVG(w.work) FROM Wolibal w")
//    int getAverageWork();
//
//    @Query("SELECT COUNT(w) FROM Wolibal w WHERE w.rest > :rest")
//    long countByRestHigherThan(@Param("rest") int rest);
//
//    @Query("SELECT AVG(w.rest) FROM Wolibal w")
//    int getAverageRest();
//
//    @Query("SELECT COUNT(w) FROM Wolibal w WHERE w.sleep > :sleep")
//    long countBySleepHigherThan(@Param("sleep") int sleep);
//
//    @Query("SELECT AVG(w.sleep) FROM Wolibal w")
//    int getAverageSleep();
//
//    @Query("SELECT COUNT(w) FROM Wolibal w WHERE w.personal > :personal")
//    long countByPersonalHigherThan(@Param("personal") int personal);
//
//    @Query("SELECT AVG(w.personal) FROM Wolibal w")
//    int getAveragePersonal();
//
//    @Query("SELECT COUNT(w) FROM Wolibal w WHERE w.health > :health")
//    long countByHealthHigherThan(@Param("health") int health);
//
//    @Query("SELECT AVG(w.health) FROM Wolibal w")
//    int getAverageHealth();
}
