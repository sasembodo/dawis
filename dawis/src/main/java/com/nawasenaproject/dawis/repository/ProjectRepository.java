package com.nawasenaproject.dawis.repository;

import com.nawasenaproject.dawis.entity.Project;
import com.nawasenaproject.dawis.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String>, JpaSpecificationExecutor<Project> {

    @Query("""
        SELECT 
            CAST(SUBSTRING(p.code, LENGTH(p.code) - 2, 3) AS INTEGER)
        FROM Project p
        WHERE p.type = :typeCode
          AND SUBSTRING(p.code, 5, 2) = :year
          AND SUBSTRING(p.code, 8, 2) = :month
        ORDER BY p.code DESC
        LIMIT 1
    """)
    Integer findLastIndex(
            @Param("typeCode") String typeCode,
            @Param("year") String year,
            @Param("month") String month
    );

    Optional<Project> findFirstByUserAndId(User user, String id);
}
