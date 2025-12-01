package com.nawasenaproject.dawis.controller;

import com.nawasenaproject.dawis.dto.*;
import com.nawasenaproject.dawis.entity.User;
import com.nawasenaproject.dawis.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProjectController {

    private final ProjectService projectService;

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

    @GetMapping(
            path = "/api/projects/search",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<ProjectResponse>> search(
            User user,
            @RequestParam(name = "code", required = false) String code,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "location", required = false) String location,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "begin_start_date", required = false) String beginStartDate,
            @RequestParam(name = "end_start_date", required = false) String endStartDate,
            @RequestParam(name = "begin_finish_date", required = false) String beginFinishDate,
            @RequestParam(name = "end_finish_date", required = false) String endFinishDate,
            @RequestParam(name = "sort_by", required = false) String sortBy,
            @RequestParam(name = "sort_dir", required = false) String sortDir,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {

        SearchProjectRequest request = SearchProjectRequest.builder()
                .code(code)
                .name(name)
                .type(type)
                .location(location)
                .status(status)
                .beginStartDate(beginStartDate)
                .endStartDate(endStartDate)
                .beginFinishDate(beginFinishDate)
                .endFinishDate(endFinishDate)
                .sortBy(sortBy)
                .sortDir(sortDir)
                .page(page)
                .size(size)
                .build();

        Page<ProjectResponse> projectResponses = projectService.search(user, request);

        return WebResponse.<List<ProjectResponse>>builder()
                .rc(200)
                .messages("OK")
                .data(projectResponses.getContent())
                .paging(PagingResponse.builder()
                        .currentPage(0)
                        .totalPage(2)
                        .size(10)
                        .build()
                )
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
