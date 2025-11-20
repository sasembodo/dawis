package com.nawasenaproject.dawis.service;

import com.nawasenaproject.dawis.dto.CreateWorkerRequest;
import com.nawasenaproject.dawis.dto.WorkerResponse;
import com.nawasenaproject.dawis.entity.User;
import com.nawasenaproject.dawis.entity.Worker;
import com.nawasenaproject.dawis.repository.WorkerRepository;
import com.nawasenaproject.dawis.util.DateUtil;
import com.nawasenaproject.dawis.util.GenerateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
        worker.setCreatedBy(user.getCreatedBy());
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
}
