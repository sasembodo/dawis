package com.nawasenaproject.dawis.controller;

import com.nawasenaproject.dawis.dto.CreateWorkerRequest;
import com.nawasenaproject.dawis.dto.WebResponse;
import com.nawasenaproject.dawis.dto.WorkerResponse;
import com.nawasenaproject.dawis.entity.User;
import com.nawasenaproject.dawis.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
