package com.nawasenaproject.dawis.repository;

import com.nawasenaproject.dawis.entity.Project;
import com.nawasenaproject.dawis.entity.ProjectWorker;
import com.nawasenaproject.dawis.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectWorkerRepository extends JpaRepository<ProjectWorker, String> {

    Optional<ProjectWorker> findByProjectAndWorker(Project project, Worker worker);
}
