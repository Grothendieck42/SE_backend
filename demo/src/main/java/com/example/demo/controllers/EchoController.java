package com.example.demo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.annotations.session.*;
import com.example.demo.entities.UserEntity;

@RestController
@RequestMapping(path = "/echo")
public class EchoController {
    @GetMapping("")
    public String echo() {
        return "The TSS is running.";
    }

    @GetMapping("/auth")
    @Authorization
    public String auth(@CurrentUser UserEntity user) {
        return "Login as " + user.getUid();
    }
}
