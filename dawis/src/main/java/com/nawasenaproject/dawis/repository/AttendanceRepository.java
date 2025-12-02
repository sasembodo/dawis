package com.nawasenaproject.dawis.repository;

import com.nawasenaproject.dawis.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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

    Optional<Attendance> findFirstById(String id);

}
