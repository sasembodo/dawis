package com.nawasenaproject.dawis.service;

import com.nawasenaproject.dawis.dto.AttendanceResponse;
import com.nawasenaproject.dawis.dto.CreateAttendanceRequest;
import com.nawasenaproject.dawis.dto.ProjectResponse;
import com.nawasenaproject.dawis.dto.WorkerResponse;
import com.nawasenaproject.dawis.entity.*;
import com.nawasenaproject.dawis.enums.PaidStatus;
import com.nawasenaproject.dawis.enums.ProjectStatus;
import com.nawasenaproject.dawis.enums.ProjectType;
import com.nawasenaproject.dawis.repository.AttendanceRepository;
import com.nawasenaproject.dawis.repository.ProjectRepository;
import com.nawasenaproject.dawis.repository.ProjectWorkerRepository;
import com.nawasenaproject.dawis.repository.WorkerRepository;
import com.nawasenaproject.dawis.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AttendanceService {

    private ProjectRepository projectRepository;
    private WorkerRepository workerRepository;
    private ProjectWorkerRepository projectWorkerRepository;
    private AttendanceRepository attendanceRepository;
    private ValidationService validationService;

    @Autowired
    public AttendanceService(
            ProjectRepository projectRepository,
            WorkerRepository workerRepository,
            ProjectWorkerRepository projectWorkerRepository,
            AttendanceRepository attendanceRepository,
            ValidationService validationService
    ){
        this.projectRepository = projectRepository;
        this.workerRepository = workerRepository;
        this.projectWorkerRepository = projectWorkerRepository;
        this.attendanceRepository = attendanceRepository;
        this.validationService = validationService;
    }

    public AttendanceResponse create(User user, CreateAttendanceRequest request){

        validationService.validate(request);

        Project project = projectRepository.findFirstByUserAndId(user, request.getProjectId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project is not found!"));

        Worker worker = workerRepository.findFirstByUserAndId(user, request.getWorkerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Worker is not found!"));

        ProjectWorker projectWorker = projectWorkerRepository.findByProjectAndWorker(project, worker)
                .orElseGet(() -> {
                    ProjectWorker pw = new ProjectWorker();
                    pw.setId(UUID.randomUUID().toString());
                    pw.setProject(project);
                    pw.setWorker(worker);
                    return projectWorkerRepository.save(pw);
                });

        Attendance attendance = new Attendance();
        attendance.setId(UUID.randomUUID().toString());
        attendance.setDate(
                Optional.ofNullable(request.getDate())
                        .map(DateUtil::stringToLocalDate)
                        .orElse(LocalDate.now())
        );
        attendance.setSpecialWage(request.getSpecialWage());
        attendance.setMandays(request.getMandays());
        attendance.setBonuses(request.getBonuses());
        attendance.setAdvances(request.getAdvances());
        attendance.setPaidStatus(PaidStatus.UNPAID.getCode());
        attendance.setProjectWorker(projectWorker);

        attendance.setCreatedBy(user.getCreatedBy());
        attendance.setCreatedAt(LocalDateTime.now());
        attendanceRepository.save(attendance);

        String paidStatusDesc = PaidStatus.fromCode(attendance.getPaidStatus()).getDescription();
        String typeDesc = ProjectType.fromCode(project.getType()).getDescription();
        String statusDesc = ProjectStatus.fromCode(project.getStatus()).getDescription();

        return AttendanceResponse.builder()
                .id(attendance.getId())
                .project(ProjectResponse.builder()
                        .id(project.getId())
                        .code(project.getCode())
                        .name(project.getName())
                        .type(typeDesc)
                        .location(project.getLocation())
                        .coordinates(project.getCoordinates())
                        .status(statusDesc)
                        .startDate(project.getStartDate())
                        .finishDate(project.getFinishDate())
                        .build()
                )
                .worker(WorkerResponse.builder()
                        .id(worker.getId())
                        .name(worker.getName())
                        .nip(worker.getNip())
                        .recruitDate(worker.getRecruitDate())
                        .position(worker.getPosition())
                        .wage(worker.getWage())
                        .build()
                )
                .date(attendance.getDate())
                .specialWage(attendance.getSpecialWage())
                .mandays(attendance.getMandays())
                .bonuses(attendance.getBonuses())
                .advances(attendance.getAdvances())
                .paidStatus(paidStatusDesc)
                .build();


    }

}
