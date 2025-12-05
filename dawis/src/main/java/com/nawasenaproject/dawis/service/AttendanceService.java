package com.nawasenaproject.dawis.service;

import com.nawasenaproject.dawis.dto.*;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AttendanceService {

    private final ProjectRepository projectRepository;
    private final WorkerRepository workerRepository;
    private final ProjectWorkerRepository projectWorkerRepository;
    private final AttendanceRepository attendanceRepository;
    private final ValidationService validationService;

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

    @Transactional
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

        LocalDate attendanceDate = Optional.ofNullable(request.getDate())
                        .map(DateUtil::stringToLocalDate)
                        .orElse(LocalDate.now());

        boolean exists = attendanceRepository.existsByProjectIdAndWorkerIdAndDate(
                request.getProjectId(),
                request.getWorkerId(),
                attendanceDate
        );

        if (exists) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Attendance for this worker on this date already exists!"
            );
        }

        Attendance attendance = new Attendance();
        attendance.setId(UUID.randomUUID().toString());
        attendance.setDate(attendanceDate);
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

    @Transactional(readOnly = true)
    public AttendanceResponse get(String attendanceId){

        Attendance attendance = attendanceRepository
                .findFirstById(attendanceId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Attendance is not found!")
                );

        Project project = attendance.getProjectWorker().getProject();
        Worker worker = attendance.getProjectWorker().getWorker();

        String paidStatusDesc = Optional.ofNullable(PaidStatus.fromCode(attendance.getPaidStatus()))
                .map(PaidStatus::getDescription)
                .orElse("Unknown");

        String typeDesc = Optional.ofNullable(ProjectType.fromCode(project.getType()))
                .map(ProjectType::getDescription)
                .orElse("Unknown");

        String statusDesc = Optional.ofNullable(ProjectStatus.fromCode(project.getStatus()))
                .map(ProjectStatus::getDescription)
                .orElse("Unknown");

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

    @Transactional
    public AttendanceResponse update(User user, UpdateAttendanceRequest request, String attendanceId) {

        validationService.validate(request);

        Attendance attendance = attendanceRepository
                .findFirstById(attendanceId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Attendance is not found!")
                );

        ProjectWorker projectWorker = attendance.getProjectWorker();
        Project project = projectWorker.getProject();
        Worker worker = projectWorker.getWorker();

        if (request.getDate() != null) {
            attendance.setDate(DateUtil.stringToLocalDate(request.getDate()));
        }

        if (request.getSpecialWage() > 0) {
            attendance.setSpecialWage(request.getSpecialWage());
        }

        if (request.getMandays() != null) {
            attendance.setMandays(request.getMandays());
        }

        if (request.getBonuses() != null) {
            attendance.setBonuses(request.getBonuses());
        }

        if (request.getAdvances() != null) {
            attendance.setAdvances(request.getAdvances());
        }

        if (request.getPaidStatus() != null) {
            attendance.setPaidStatus(request.getPaidStatus());
        }

        attendance.setModifiedBy(user.getUsername());
        attendance.setModifiedAt(LocalDateTime.now());

        attendanceRepository.save(attendance);

        return AttendanceResponse.builder()
                .id(attendance.getId())
                .date(attendance.getDate())
                .specialWage(attendance.getSpecialWage())
                .mandays(attendance.getMandays())
                .bonuses(attendance.getBonuses())
                .advances(attendance.getAdvances())
                .paidStatus(PaidStatus.fromCode(attendance.getPaidStatus()).getDescription())

                .project(ProjectResponse.builder()
                        .id(project.getId())
                        .code(project.getCode())
                        .name(project.getName())
                        .type(ProjectType.fromCode(project.getType()).getDescription())
                        .location(project.getLocation())
                        .coordinates(project.getCoordinates())
                        .status(ProjectStatus.fromCode(project.getStatus()).getDescription())
                        .startDate(project.getStartDate())
                        .finishDate(project.getFinishDate())
                        .build())

                .worker(WorkerResponse.builder()
                        .id(worker.getId())
                        .name(worker.getName())
                        .nip(worker.getNip())
                        .recruitDate(worker.getRecruitDate())
                        .position(worker.getPosition())
                        .wage(worker.getWage())
                        .build())

                .build();
    }

    public void delete(String attendanceId){
        Attendance attendance = attendanceRepository.findFirstById(attendanceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Attendance is not found !"));

        attendanceRepository.delete(attendance);
    }

}
