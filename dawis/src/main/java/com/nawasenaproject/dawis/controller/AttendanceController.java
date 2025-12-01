package com.nawasenaproject.dawis.controller;

import com.nawasenaproject.dawis.dto.AttendanceResponse;
import com.nawasenaproject.dawis.dto.CreateAttendanceRequest;
import com.nawasenaproject.dawis.dto.WebResponse;
import com.nawasenaproject.dawis.entity.User;
import com.nawasenaproject.dawis.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AttendanceController {

    private final AttendanceService attendanceService;

    @Autowired
    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping(
            path = "/api/attendances",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<AttendanceResponse> create(User user, @RequestBody CreateAttendanceRequest request) {
        AttendanceResponse response = attendanceService.create(user, request);
        return WebResponse.<AttendanceResponse>builder()
                .rc(200)
                .messages("OK")
                .data(response)
                .build();
    }
}
