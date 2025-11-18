package com.nawasenaproject.dawis.controller;

import com.nawasenaproject.dawis.dto.RegisterUserRequest;
import com.nawasenaproject.dawis.dto.UpdateUserRequest;
import com.nawasenaproject.dawis.dto.UserResponse;
import com.nawasenaproject.dawis.dto.WebResponse;
import com.nawasenaproject.dawis.entity.User;
import com.nawasenaproject.dawis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping(
            path = "/api/users",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<Map<String, Object>> register(@RequestBody RegisterUserRequest request){
        userService.register(request);
        Map<String, Object> response = new HashMap<>();
        response.put("username", request.getUsername());
        return WebResponse.<Map<String, Object>>builder()
                .rc(200)
                .data(response)
                .messages("OK")
                .build();
    }

    @GetMapping(
            path = "/api/users/current",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserResponse> get(User user){
        UserResponse userResponse = userService.get(user);
        return WebResponse.<UserResponse>builder()
                .rc(200)
                .messages("OK")
                .data(userResponse)
                .build();
    }

    @PatchMapping(
            path = "/api/users/current",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserResponse> update(User user, @RequestBody UpdateUserRequest request) {
        UserResponse userResponse = userService.update(user, request);
        return WebResponse.<UserResponse>builder()
                .rc(200)
                .messages("OK")
                .data(userResponse)
                .build();
    }
}
