package com.nawasenaproject.dawis.controller;

import com.nawasenaproject.dawis.dto.*;
import com.nawasenaproject.dawis.entity.User;
import com.nawasenaproject.dawis.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class WorkerController {

    private final WorkerService workerService;

    @Autowired
    public WorkerController(WorkerService workerService){
        this.workerService = workerService;
    }

    @PostMapping(
            path = "/api/workers",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<WorkerResponse> create(User user, @RequestBody CreateWorkerRequest request){
        WorkerResponse workerResponse = workerService.create(user, request);
        return WebResponse.<WorkerResponse>builder()
                .rc(200)
                .messages("OK")
                .data(workerResponse)
                .build();
    }

    @GetMapping(
            path = "/api/workers/{workerId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<WorkerResponse> get(User user, @PathVariable("workerId") String workerId){
        WorkerResponse workerResponse = workerService.get(user, workerId);
        return WebResponse.<WorkerResponse>builder()
                .rc(200)
                .messages("OK")
                .data(workerResponse)
                .build();
    }

    @GetMapping(
            path = "/api/workers/search",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<WorkerResponse>> search(
            User user,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "nip", required = false) String nip,
            @RequestParam(name = "start_date", required = false) String startDate,
            @RequestParam(name = "end_date", required = false) String endDate,
            @RequestParam(name = "position", required = false) String position,
            @RequestParam(name = "min_wage", required = false) String minWage,
            @RequestParam(name = "max_wage", required = false) String maxWage,
            @RequestParam(name = "sort_by", required = false) String sortBy,
            @RequestParam(name = "sort_dir", required = false) String sortDir,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {

        SearchWorkerRequest request = SearchWorkerRequest.builder()
                .name(name)
                .nip(nip)
                .startDate(startDate)
                .endDate(endDate)
                .position(position)
                .minWage(minWage)
                .maxWage(maxWage)
                .sortBy(sortBy)
                .sortDir(sortDir)
                .page(page)
                .size(size)
                .build();

        Page<WorkerResponse> workerResponses = workerService.search(user, request);

        return WebResponse.<List<WorkerResponse>>builder()
                .rc(200)
                .messages("OK")
                .data(workerResponses.getContent())
                .paging(PagingResponse.builder()
                        .currentPage(0)
                        .totalPage(2)
                        .size(10)
                        .build()
                )
                .build();
    }

    @PatchMapping(
            path = "/api/workers/{workerId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<WorkerResponse> update(User user,
                                               @RequestBody UpdateWorkerRequest request,
                                               @PathVariable("workerId") String workerId){
        request.setId(workerId);

        WorkerResponse workerResponse = workerService.update(user, request);
        return WebResponse.<WorkerResponse>builder()
                .rc(200)
                .messages("OK")
                .data(workerResponse)
                .build();
    }

    @DeleteMapping(
            path = "/api/workers/{workerId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> delete(User user, @PathVariable("workerId") String workerId) {
        workerService.delete(user, workerId);
        return WebResponse.<String>builder()
                .rc(200)
                .messages("OK")
                .build();
    }

}
