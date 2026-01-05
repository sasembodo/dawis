package com.nawasenaproject.dawis.repository;

import com.nawasenaproject.dawis.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, String>, JpaSpecificationExecutor<Attendance> {

    @Query("""
        SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END
        FROM Attendance a
        JOIN a.projectWorker pw
        WHERE pw.project.id = :projectId
        AND pw.worker.id = :workerId
        AND a.date = :date
    """)
    boolean existsByProjectIdAndWorkerIdAndDate(@Param("projectId") String projectId,
                                         @Param("workerId") String workerId,
                                         @Param("date") LocalDate date);

    @Query("""
        SELECT 
            pw.worker.id,
            SUM(COALESCE(a.mandays, 0)),
            SUM(COALESCE(a.bonuses, 0)),
            SUM(COALESCE(a.advances, 0)),
            SUM(
                CASE 
                    WHEN a.paidStatus = :unpaid 
                    THEN (COALESCE(a.mandays,0) * pw.worker.wage + COALESCE(a.specialWage,0))
                    ELSE 0 
                END
            )
        FROM Attendance a
        JOIN a.projectWorker pw
        JOIN pw.project p
        JOIN p.user u
        WHERE p.id = :projectId
          AND u.username = :username
          AND (:workerNip IS NULL OR pw.worker.nip = :workerNip)
          AND (
                :start IS NULL 
                OR :end IS NULL 
                OR a.date BETWEEN :start AND :end
          )
        GROUP BY pw.worker.id
    """)
    List<Object[]> aggregateByWorker(
            @Param("projectId") String projectId,
            @Param("username") String username,
            @Param("workerNip") String workerNip,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end,
            @Param("unpaid") Integer unpaid
    );

    Optional<Attendance> findFirstById(String id);

}
