package com.nawasenaproject.dawis.service;

import com.nawasenaproject.dawis.dto.CreateWorkerRequest;
import com.nawasenaproject.dawis.dto.SearchWorkerRequest;
import com.nawasenaproject.dawis.dto.UpdateWorkerRequest;
import com.nawasenaproject.dawis.dto.WorkerResponse;
import com.nawasenaproject.dawis.entity.User;
import com.nawasenaproject.dawis.entity.Worker;
import com.nawasenaproject.dawis.repository.WorkerRepository;
import com.nawasenaproject.dawis.specification.WorkerSpecification;
import com.nawasenaproject.dawis.util.DateUtil;
import com.nawasenaproject.dawis.util.GenerateUtil;
import com.nawasenaproject.dawis.util.SafeConverterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class WorkerService {

    private WorkerRepository workerRepository;
    private ValidationService validationService;

    @Autowired
    public WorkerService(WorkerRepository workerRepository, ValidationService validationService){
        this.workerRepository = workerRepository;
        this.validationService = validationService;
    }

    @Transactional
    public WorkerResponse create(User user, CreateWorkerRequest request){
        validationService.validate(request);

        String nip = GenerateUtil.nipGenerator(workerRepository.findLatestNip());

        Worker worker = new Worker();
        worker.setId(UUID.randomUUID().toString());
        worker.setName(request.getName());
        worker.setRecruitDate(LocalDate.now());
        worker.setNip(nip);
        worker.setPosition(request.getPosition());
        worker.setWage(request.getWage());
        worker.setUser(user);
        worker.setIsActive(true);

        worker.setCreatedBy(user.getUsername());
        worker.setCreatedAt(LocalDateTime.now());

        workerRepository.save(worker);

        return WorkerResponse.builder()
                .id(worker.getId())
                .name(worker.getName())
                .position(worker.getPosition())
                .nip(worker.getNip())
                .recruitDate(worker.getRecruitDate())
                .wage(worker.getWage())
                .build();
    }

    @Transactional(readOnly = true)
    public WorkerResponse get(User user, String id){
        Worker worker = workerRepository.findFirstByUserAndId(user, id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Worker is not found !"));

        return WorkerResponse.builder()
                .id(worker.getId())
                .name(worker.getName())
                .position(worker.getPosition())
                .nip(worker.getNip())
                .recruitDate(worker.getRecruitDate())
                .wage(worker.getWage())
                .build();
    }

    @Transactional(readOnly = true)
    public Page<WorkerResponse> search( User user, SearchWorkerRequest request) {

        Specification<Worker> spec = Specification.allOf(WorkerSpecification.belongsToUser(user.getUsername()));

        spec = spec
                .and(WorkerSpecification.positionEquals(request.getPosition()))
                .and(WorkerSpecification.recruitDateBetween(request.getStartDate(), request.getEndDate()))
                .and(WorkerSpecification.wageBetween(request.getMinWage(), request.getMaxWage()));

        Sort sort = Sort.unsorted();

        if (!Objects.equals(request.getSortBy(), "") && !Objects.equals(request.getSortDir(), "")) {
            sort = request.getSortDir().equalsIgnoreCase("desc") ?
                    Sort.by(request.getSortBy()).descending() :
                    Sort.by(request.getSortBy()).ascending();
        }

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);
        Page<Worker> workers = workerRepository.findAll(spec, pageable);
        List<WorkerResponse> workerResponses = workers.getContent()
                .stream()
                .map(worker -> WorkerResponse.builder()
                        .id(worker.getId())
                        .name(worker.getName())
                        .nip(worker.getNip())
                        .position(worker.getPosition())
                        .recruitDate(worker.getRecruitDate())
                        .wage(worker.getWage())
                        .build())
                .toList();

        return new PageImpl<>(workerResponses, pageable, workers.getTotalElements());
    }

    @Transactional
    public WorkerResponse update(User user, UpdateWorkerRequest request){
        validationService.validate(request);


        Worker worker = workerRepository.findFirstByUserAndId(user, request.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Worker is not found !"));

        worker.setName(request.getName());
        worker.setPosition(request.getPosition());
        worker.setWage(request.getWage());
        worker.setIsActive(true);

        worker.setModifiedBy(user.getUsername());
        worker.setModifiedAt(LocalDateTime.now());

        workerRepository.save(worker);

        return WorkerResponse.builder()
                .id(worker.getId())
                .name(worker.getName())
                .position(worker.getPosition())
                .nip(worker.getNip())
                .recruitDate(worker.getRecruitDate())
                .wage(worker.getWage())
                .build();
    }



    @Transactional
    public void delete(User user, String workerId) {
        Worker worker = workerRepository.findFirstByUserAndId(user, workerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Worker is not found !"));

        workerRepository.delete(worker);
    }

}
