package com.example.demo.controllers;

import com.example.demo.configs.V1APIController;

import org.springframework.web.bind.annotation.GetMapping;

@V1APIController
public class MainController {
    @GetMapping("/")
    public String index() {
        return "sdsdsdsds";
    }
}
