package com.nawasenaproject.dawis.service;

import com.nawasenaproject.dawis.dto.*;
import com.nawasenaproject.dawis.entity.Attendance;
import com.nawasenaproject.dawis.entity.User;
import com.nawasenaproject.dawis.entity.Worker;
import com.nawasenaproject.dawis.enums.PaidStatus;
import com.nawasenaproject.dawis.repository.AttendanceRepository;
import com.nawasenaproject.dawis.repository.WorkerRepository;
import com.nawasenaproject.dawis.specification.ReportSpecification;
import com.nawasenaproject.dawis.specification.WorkerSpecification;
import com.nawasenaproject.dawis.util.DateUtil;
import com.nawasenaproject.dawis.util.GenerateUtil;
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

    private final WorkerRepository workerRepository;
    private final AttendanceRepository attendanceRepository;
    private final ValidationService validationService;

    @Autowired
    public WorkerService(
        WorkerRepository workerRepository,
        AttendanceRepository attendanceRepository,
        ValidationService validationService
    ){
        this.workerRepository = workerRepository;
        this.attendanceRepository = attendanceRepository;
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
                .and(WorkerSpecification.nameEquals(request.getName()))
                .and(WorkerSpecification.nipEquals(request.getNip().toUpperCase()))
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

    @Transactional(readOnly = true)
    public WorkerReportResponse report(User user, String workerId, WorkerReportRequest request){
        Worker worker = workerRepository.findFirstByUserAndId(user, workerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Worker is not found !"));

        LocalDate startDateReq = DateUtil.stringToLocalDate(request.getStartDate());
        LocalDate endDateReq = DateUtil.stringToLocalDate(request.getEndDate());

        Specification<Attendance> spec = Specification.allOf(ReportSpecification.belongsToUser(user.getUsername()));
        spec = spec
                .and(ReportSpecification.belongsToWorker(workerId))
                .and(ReportSpecification.dateBetween(startDateReq, endDateReq))
                .and(ReportSpecification.projectCodeEquals(request.getProjectCode()))
                .and(ReportSpecification.paidStatusEqualsUnpaid());

        Sort sort = Sort.unsorted();

        if (!Objects.equals(request.getSortBy(), "") && !Objects.equals(request.getSortDir(), "")) {
            sort = request.getSortDir().equalsIgnoreCase("desc") ?
                    Sort.by("date").descending() :
                    Sort.by("date").ascending();
        }

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        Page<Attendance> workerReportAttendances = attendanceRepository.findAll(spec, pageable);
        List<WorkerReportAttendanceResponse> responses =
                workerReportAttendances.getContent().stream()
                        .map(att -> WorkerReportAttendanceResponse.builder()
                                .id(att.getId())
                                .date(att.getDate())
                                .specialWage(att.getSpecialWage())
                                .mandays(att.getMandays())
                                .bonuses(att.getBonuses())
                                .advances(att.getAdvances())
                                .paidStatus(PaidStatus.fromCode(att.getPaidStatus()).getDescription())
                                .project(
                                        WorkerReportProjectResponse.builder()
                                                .id(att.getProjectWorker().getProject().getId())
                                                .code(att.getProjectWorker().getProject().getCode())
                                                .name(att.getProjectWorker().getProject().getName())
                                                .type(att.getProjectWorker().getProject().getType())
                                                .location(att.getProjectWorker().getProject().getLocation())
                                                .build()
                                )
                                .build()
                        )
                        .toList();

        List<Attendance> all = attendanceRepository.findAll(spec);

        double totalManday = all.stream()
                .mapToDouble(a -> a.getMandays() != null ? a.getMandays() : 0)
                .sum();

        Integer totalBonus = all.stream()
                .mapToInt(a -> a.getBonuses() != null ? a.getBonuses() : 0)
                .sum();

        Integer totalAdvance = all.stream()
                .mapToInt(a -> a.getAdvances() != null ? a.getAdvances() : 0)
                .sum();

        Integer totalSpecialWage = all.stream()
                .mapToInt(a -> a.getSpecialWage() != null ? a.getSpecialWage() : 0)
                .sum();

        Integer totalWage = (int) Math.round(
                worker.getWage() * totalManday + totalSpecialWage
        );

        Integer totalUnpaid = all.stream()
                .filter(a -> a.getPaidStatus() == PaidStatus.UNPAID.getCode())
                .mapToInt(a ->
                        (int) Math.round(
                                worker.getWage() * a.getMandays() +
                                        (a.getSpecialWage() != null ? a.getSpecialWage() : 0)
                        )
                )
                .sum();

        return WorkerReportResponse.builder()
                .id(worker.getId())
                .name(worker.getName())
                .nip(worker.getNip())
                .recruitDate(worker.getRecruitDate())
                .position(worker.getPosition())
                .wage(worker.getWage())
                .totalManday(totalManday)
                .totalBonus(totalBonus)
                .totalAdvance(totalAdvance)
                .totalWage(totalWage)
                .totalUnpaid(totalUnpaid)
                .attendances(responses)
                .build();
    }

}
