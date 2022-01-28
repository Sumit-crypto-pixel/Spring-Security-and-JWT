package com.example.DemoSecurity.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class TemplateController {

    @GetMapping("login")
    public String getLoginView(){
        return "login";  // this will bring login.html page
    }
    @GetMapping("courses")
    public String getCourses(){
        return "courses";  // this will bring login.html page
    }
}
