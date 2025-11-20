package com.nawasenaproject.dawis.service;

import com.nawasenaproject.dawis.dto.CreateWorkerRequest;
import com.nawasenaproject.dawis.dto.UpdateWorkerRequest;
import com.nawasenaproject.dawis.dto.WorkerResponse;
import com.nawasenaproject.dawis.entity.User;
import com.nawasenaproject.dawis.entity.Worker;
import com.nawasenaproject.dawis.repository.WorkerRepository;
import com.nawasenaproject.dawis.util.DateUtil;
import com.nawasenaproject.dawis.util.GenerateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Optional;
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

        String recruitDate = DateUtil.formatRecruitDate(System.currentTimeMillis());
        String nip = GenerateUtil.nipGenerator(workerRepository.findLatestNip(), System.currentTimeMillis());

        Worker worker = new Worker();
        worker.setId(UUID.randomUUID().toString());
        worker.setName(request.getName());
        worker.setRecruitDate(recruitDate);
        worker.setNip(nip);
        worker.setPosition(request.getPosition());
        worker.setWage(request.getWage());
        worker.setUser(user);
        worker.setIsActive(true);

        Date timestamp = new Date(System.currentTimeMillis());
        worker.setCreatedBy(user.getUsername());
        worker.setCreatedAt(timestamp.toString());

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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Data pekerja tersebut tidak ada !"));

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
    public WorkerResponse update(User user, UpdateWorkerRequest request){
        validationService.validate(request);


        Worker worker = workerRepository.findFirstByUserAndId(user, request.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Data pekerja tersebut tidak ada !"));

        worker.setName(request.getName());
        worker.setPosition(request.getPosition());
        worker.setWage(request.getWage());
        worker.setIsActive(true);

        Date timestamp = new Date(System.currentTimeMillis());
        worker.setModifiedBy(user.getUsername());
        worker.setModifiedAt(timestamp.toString());

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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Data pekerja tersebut tidak ada !"));

        workerRepository.delete(worker);
    }

}
