package com.nawasenaproject.dawis.service;

import com.nawasenaproject.dawis.dto.*;
import com.nawasenaproject.dawis.entity.Attendance;
import com.nawasenaproject.dawis.entity.Project;
import com.nawasenaproject.dawis.entity.User;
import com.nawasenaproject.dawis.entity.Worker;
import com.nawasenaproject.dawis.enums.PaidStatus;
import com.nawasenaproject.dawis.enums.ProjectStatus;
import com.nawasenaproject.dawis.enums.ProjectType;
import com.nawasenaproject.dawis.repository.AttendanceRepository;
import com.nawasenaproject.dawis.repository.ProjectRepository;
import com.nawasenaproject.dawis.specification.ProjectSpecification;
import com.nawasenaproject.dawis.specification.ReportSpecification;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final AttendanceRepository attendanceRepository;
    private final ValidationService validationService;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, AttendanceRepository attendanceRepository, ValidationService validationService){
        this.projectRepository = projectRepository;
        this.attendanceRepository = attendanceRepository;
        this.validationService = validationService;
    }

    @Transactional
    public ProjectResponse create(User user, CreateProjectRequest request){
        validationService.validate(request);

        ProjectType type = ProjectType.fromCode(request.getType());
        LocalDate currentDate = LocalDate.now();
        Integer lastIndex = projectRepository.findLastIndex(
                type.getCode(),
                String.format("%02d", currentDate.getYear() % 100),
                String.format("%02d", currentDate.getMonthValue())
        );
        String generatedCode = GenerateUtil.projectCodeGenerator(type.getCode(), lastIndex);

        Project project = new Project();
        project.setId(UUID.randomUUID().toString());
        project.setCode(generatedCode);
        project.setName(request.getName());
        project.setType(type.getCode());
        project.setLocation(request.getLocation());
        project.setCoordinates(request.getCoordinates());
        project.setStatus(ProjectStatus.WORK_IN_PROGRESS.getCode());
        project.setStartDate(currentDate);
        project.setUser(user);
        project.setIsActive(true);

        project.setCreatedBy(user.getUsername());
        project.setCreatedAt(LocalDateTime.now());

        projectRepository.save(project);

        return ProjectResponse.builder()
                .id(project.getId())
                .code(project.getCode())
                .name(project.getName())
                .type(type.getDescription())
                .location(project.getLocation())
                .coordinates(project.getCoordinates())
                .status(ProjectStatus.WORK_IN_PROGRESS.getDescription())
                .startDate(project.getStartDate())
                .build();
    }

    @Transactional(readOnly = true)
    public ProjectResponse get(User user, String id){
        Project project = projectRepository.findFirstByUserAndId(user, id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project is not found !"));

        ProjectType typeEnum = ProjectType.fromCode(project.getType());
        ProjectStatus statusEnum = ProjectStatus.fromCode(project.getStatus());

        return ProjectResponse.builder()
                .id(project.getId())
                .code(project.getCode())
                .name(project.getName())
                .type(typeEnum.getDescription())
                .location(project.getLocation())
                .coordinates(project.getCoordinates())
                .status(statusEnum.getDescription())
                .startDate(project.getStartDate())
                .finishDate(project.getFinishDate())
                .build();
    }

    public Page<ProjectResponse> search(User user, SearchProjectRequest request){

        Specification<Project> spec = Specification.allOf(ProjectSpecification.belongsToUser(user.getUsername()));

        spec = spec
                .and(ProjectSpecification.nameEquals(request.getName()))
                .and(ProjectSpecification.codeEquals(request.getCode().toUpperCase()))
                .and(ProjectSpecification.typeEquals(request.getType().toUpperCase()))
                .and(ProjectSpecification.locationEquals(request.getLocation()))
                .and(ProjectSpecification.statusEquals(request.getStatus()))
                .and(ProjectSpecification.startDateBetween(request.getBeginStartDate(), request.getEndStartDate()))
                .and(ProjectSpecification.finishDateBetween(request.getBeginFinishDate(), request.getBeginFinishDate()));

        Sort sort = Sort.unsorted();

        if (!Objects.equals(request.getSortBy(), "") && !Objects.equals(request.getSortDir(), "")) {
            sort = request.getSortDir().equalsIgnoreCase("desc") ?
                    Sort.by(request.getSortBy()).descending() :
                    Sort.by(request.getSortBy()).ascending();
        }

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);
        Page<Project> projects = projectRepository.findAll(spec, pageable);
        List<ProjectResponse> projectResponses = projects.getContent()
                .stream()
                .map(project -> ProjectResponse.builder()
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
                .toList();

        return new PageImpl<>(projectResponses, pageable, projects.getTotalElements());

    }

    @Transactional
    public ProjectResponse update(User user, UpdateProjectRequest request){
        validationService.validate(request);

        LocalDate currentDate = LocalDate.now();

        Project project = projectRepository.findFirstByUserAndId(user, request.getId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project is not found !"));

        ProjectType typeEnum = ProjectType.fromCode(project.getType());

        if (request.getName() != null) {
            project.setName(request.getName());
        }

        if (request.getLocation() != null) {
            project.setLocation(request.getLocation());
        }

        if (request.getCoordinates() != null) {
            project.setCoordinates(request.getCoordinates());
        }

        if (request.getStatus() != null) {
            ProjectStatus statusEnum = ProjectStatus.fromCode(request.getStatus());
            project.setStatus(statusEnum.getCode());

            if (statusEnum == ProjectStatus.FINISH) {
                project.setFinishDate(currentDate);
            } else {
                project.setFinishDate(null);
            }
        }

        project.setUser(user);

        project.setModifiedBy(user.getUsername());
        project.setModifiedAt(LocalDateTime.now());

        projectRepository.save(project);

        return ProjectResponse.builder()
                .id(project.getId())
                .code(project.getCode())
                .name(project.getName())
                .type(typeEnum.getDescription())
                .location(project.getLocation())
                .coordinates(project.getCoordinates())
                .status(ProjectStatus.fromCode(project.getStatus()).getDescription())
                .startDate(project.getStartDate())
                .finishDate(project.getFinishDate())
                .build();
    }

    @Transactional
    public void delete(User user, String projectId) {
        Project project = projectRepository.findFirstByUserAndId(user, projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project is not found !"));

        projectRepository.delete(project);
    }

    @Transactional(readOnly = true)
    public ProjectReportResponse report(User user, String projectId, ProjectReportRequest request) {

        Project project = projectRepository.findFirstByUserAndId(user, projectId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Project is not found"));

        LocalDate start = DateUtil.stringToLocalDate(request.getStartDate());
        LocalDate end = DateUtil.stringToLocalDate(request.getEndDate());

        Specification<Attendance> spec = Specification.allOf(
                ReportSpecification.belongsToUser(user.getUsername()),
                ReportSpecification.belongsToProject(projectId),
                ReportSpecification.dateBetween(start, end),
                ReportSpecification.workerNipEquals(request.getWorkerNip())
        );

        List<Attendance> attendances = attendanceRepository.findAll(spec);

        String workerNip = request.getWorkerNip();
        if (workerNip != null && workerNip.isBlank()) {
            workerNip = null;
        }

        Map<String, Object[]> aggregateMap =
                attendanceRepository.aggregateByWorker(
                                projectId,
                                user.getUsername(),
                                workerNip,
                                start,
                                end,
                                PaidStatus.UNPAID.getCode()
                        )
                        .stream()
                        .collect(Collectors.toMap(
                                r -> (String) r[0],
                                r -> r
                        ));


        Map<Worker, List<Attendance>> byWorker =
                attendances.stream()
                        .collect(Collectors.groupingBy(
                                att -> att.getProjectWorker().getWorker()
                        ));

        List<ProjectReportWorkersResponse> workers = new ArrayList<>();
        int projectTotalUnpaid = 0;

        for (Map.Entry<Worker, List<Attendance>> entry : byWorker.entrySet()) {

            Worker worker = entry.getKey();
            List<Attendance> workerAttendances = entry.getValue();

            Object[] agg = aggregateMap.get(worker.getId());

            double totalManday = agg != null ? ((Number) agg[1]).doubleValue() : 0;
            int totalBonus = agg != null ? ((Number) agg[2]).intValue() : 0;
            int totalAdvances = agg != null ? ((Number) agg[3]).intValue() : 0;
            int totalUnpaid = agg != null ? ((Number) agg[4]).intValue() : 0;

            int totalSpecialWage = workerAttendances.stream()
                    .mapToInt(a -> a.getSpecialWage() != null ? a.getSpecialWage() : 0)
                    .sum();

            int totalWage = (int) Math.round(worker.getWage() * totalManday)
                    + totalSpecialWage;

            projectTotalUnpaid += totalUnpaid;

            List<ProjectReportWorkersAttendancesResponse> attendanceResponses =
                    workerAttendances.stream()
                            .map(a -> ProjectReportWorkersAttendancesResponse.builder()
                                    .id(a.getId())
                                    .date(a.getDate())
                                    .specialWage(a.getSpecialWage())
                                    .mandays(a.getMandays())
                                    .bonuses(a.getBonuses())
                                    .advances(a.getAdvances())
                                    .paidStatus(PaidStatus.fromCode(a.getPaidStatus()).getDescription())
                                    .build()
                            )
                            .toList();

            workers.add(ProjectReportWorkersResponse.builder()
                    .id(worker.getId())
                    .name(worker.getName())
                    .nip(worker.getNip())
                    .recruitDate(worker.getRecruitDate())
                    .position(worker.getPosition())
                    .wage(worker.getWage())
                    .totalManday(totalManday)
                    .totalBonus(totalBonus)
                    .totalAdvances(totalAdvances)
                    .totalWage(totalWage)
                    .totalUnpaid(totalUnpaid)
                    .attendances(attendanceResponses)
                    .build());
        }

        return ProjectReportResponse.builder()
                .id(project.getId())
                .code(project.getCode())
                .name(project.getName())
                .type(project.getType())
                .location(project.getLocation())
                .coordinates(project.getCoordinates())
                .status(ProjectStatus.fromCode(project.getStatus()).getDescription())
                .startDate(project.getStartDate())
                .finishDate(project.getFinishDate())
                .totalUnpaid(projectTotalUnpaid)
                .workers(workers)
                .build();
    }

}
