package com.example.twodatasources.controller;


import com.example.twodatasources.service.FirstService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class Controller {

    @Autowired
    FirstService firstService;

    @RequestMapping(value = "/send")
    public String sendData() {
       return firstService.add();
    }
}
