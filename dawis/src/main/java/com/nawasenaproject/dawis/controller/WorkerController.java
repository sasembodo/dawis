package com.nawasenaproject.dawis.controller;

import com.nawasenaproject.dawis.dto.CreateWorkerRequest;
import com.nawasenaproject.dawis.dto.UpdateWorkerRequest;
import com.nawasenaproject.dawis.dto.WebResponse;
import com.nawasenaproject.dawis.dto.WorkerResponse;
import com.nawasenaproject.dawis.entity.User;
import com.nawasenaproject.dawis.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class WorkerController {

    private WorkerService workerService;

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
