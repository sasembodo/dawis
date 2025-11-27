package com.nawasenaproject.dawis.repository;

import com.nawasenaproject.dawis.entity.User;
import com.nawasenaproject.dawis.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, String>, JpaSpecificationExecutor<Worker> {

    @Query(
            value = "SELECT nip FROM workers ORDER BY nip DESC LIMIT 1",
            nativeQuery = true
    )
    String findLatestNip();

    Optional<Worker> findFirstByUserAndId(User user, String id);
}
