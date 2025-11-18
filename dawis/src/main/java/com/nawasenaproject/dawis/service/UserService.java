package com.nawasenaproject.dawis.service;


import com.nawasenaproject.dawis.dto.RegisterUserRequest;
import com.nawasenaproject.dawis.dto.UpdateUserRequest;
import com.nawasenaproject.dawis.dto.UserResponse;
import com.nawasenaproject.dawis.entity.User;
import com.nawasenaproject.dawis.repository.UserRepository;
import com.nawasenaproject.dawis.security.BCrypt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Objects;

@Slf4j
@Service
public class UserService {

    private UserRepository userRepository;
    private ValidationService validationService;

    @Autowired
    public UserService(UserRepository userRepository, ValidationService validationService){
        this.userRepository = userRepository;
        this.validationService = validationService;
    }

    @Transactional
    public void register(RegisterUserRequest request){
        validationService.validate(request);

        if(userRepository.existsById(request.getUsername())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exist!");
        }

        User user = new User();
        Date timestamp = new Date(System.currentTimeMillis());
        user.setUsername(request.getUsername());
        user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        user.setCreatedBy(request.getUsername());
        user.setCreatedAt(timestamp.toString());

        userRepository.save(user);
    }

    public UserResponse get(User user){
        return UserResponse.builder()
                .username(user.getUsername())
                .build();
    }

    @Transactional
    public UserResponse update(User user, UpdateUserRequest request) {
        validationService.validate(request);

        if (Objects.nonNull(request.getPassword())) {
            user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        }

        Date timestamp = new Date(System.currentTimeMillis());
        user.setModifiedBy(user.getModifiedBy());
        user.setModifiedAt(timestamp.toString());

        userRepository.save(user);

        log.info("USERRRRR : {}", user.getUsername());

        return UserResponse.builder()
                .username(user.getUsername())
                .build();
    }


}

