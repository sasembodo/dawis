package com.nawasenaproject.dawis.repository;

import com.nawasenaproject.dawis.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.awt.print.Pageable;
import java.util.List;

public interface WorkerRepository extends JpaRepository<Worker, String> {

    @Query(
            value = "SELECT nip FROM workers ORDER BY nip DESC LIMIT 1",
            nativeQuery = true
    )
    String findLatestNip();
}
