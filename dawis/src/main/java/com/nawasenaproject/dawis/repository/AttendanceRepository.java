package com.nawasenaproject.dawis.repository;

import com.nawasenaproject.dawis.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, String> {

    @Query("""
        SELECT COUNT(a) > 0
        FROM attendances a
        JOIN project_worker pw
        WHERE pw.project_id = :projectId
        AND pw.worker_id = :workerId
        AND a.date = :date
    """)
    boolean existsByProjectIdAndWorkerIdAndDate(@Param("projectId") String projectId,
                                         @Param("workerId") String workerId,
                                         @Param("date") LocalDate date);

}
