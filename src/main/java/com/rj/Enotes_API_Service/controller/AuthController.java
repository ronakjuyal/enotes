package com.rj.Enotes_API_Service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rj.Enotes_API_Service.dto.UserDto;
import com.rj.Enotes_API_Service.service.UserService;
import com.rj.Enotes_API_Service.util.CommonUtil;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/")
    public ResponseEntity<?>registerUser(@RequestBody UserDto userDto) throws Exception{

        Boolean register=userService.register(userDto);

        if (register) {
            return CommonUtil.createBuildResponseMessage("Register successful", HttpStatus.CREATED);
        }
        return CommonUtil.createErrorResponseMessage("Registeration failed", HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
