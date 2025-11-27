package com.nawasenaproject.dawis.controller;

import com.nawasenaproject.dawis.dto.*;
import com.nawasenaproject.dawis.entity.User;
import com.nawasenaproject.dawis.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProjectController {

    private ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService){
        this.projectService = projectService;
    }

    @PostMapping(
            path = "/api/projects",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ProjectResponse> create(User user, @RequestBody CreateProjectRequest request){
        ProjectResponse projectResponse = projectService.create(user, request);
        return WebResponse.<ProjectResponse>builder()
                .rc(200)
                .messages("OK")
                .data(projectResponse)
                .build();
    }

    @GetMapping(
            path = "/api/projects/{projectId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ProjectResponse> get(User user, @PathVariable("projectId") String projectId){
        ProjectResponse projectResponse = projectService.get(user, projectId);
        return WebResponse.<ProjectResponse>builder()
                .rc(200)
                .messages("OK")
                .data(projectResponse)
                .build();
    }

    @PatchMapping(
            path = "/api/projects/{projectId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ProjectResponse> update(User user,
                                              @RequestBody UpdateProjectRequest request,
                                              @PathVariable("projectId") String projectId){
        request.setId(projectId);

        ProjectResponse projectResponse = projectService.update(user, request);
        return WebResponse.<ProjectResponse>builder()
                .rc(200)
                .messages("OK")
                .data(projectResponse)
                .build();
    }

    @DeleteMapping(
            path = "/api/projects/{projectId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> delete(User user, @PathVariable("projectId") String projectId) {
        projectService.delete(user, projectId);
        return WebResponse.<String>builder()
                .rc(200)
                .messages("OK")
                .build();
    }

}
