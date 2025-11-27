package com.nawasenaproject.dawis.service;

import com.nawasenaproject.dawis.dto.CreateProjectRequest;
import com.nawasenaproject.dawis.dto.ProjectResponse;
import com.nawasenaproject.dawis.dto.UpdateProjectRequest;
import com.nawasenaproject.dawis.entity.Project;
import com.nawasenaproject.dawis.entity.User;
import com.nawasenaproject.dawis.entity.Worker;
import com.nawasenaproject.dawis.enums.ProjectStatus;
import com.nawasenaproject.dawis.enums.ProjectType;
import com.nawasenaproject.dawis.repository.ProjectRepository;
import com.nawasenaproject.dawis.util.GenerateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ProjectService {

    private ProjectRepository projectRepository;
    private ValidationService validationService;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, ValidationService validationService){
        this.projectRepository = projectRepository;
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

    @Transactional
    public ProjectResponse update(User user, UpdateProjectRequest request){
        validationService.validate(request);

//        ProjectStatus statusEnum = ProjectStatus.fromCode(request.getStatus());
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
}
