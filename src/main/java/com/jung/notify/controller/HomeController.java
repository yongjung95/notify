package com.jung.notify.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.naming.AuthenticationException;

@Controller
@RequiredArgsConstructor
public class HomeController {

    @GetMapping("/loginForm")
    public String login(){
        return "login";
    }

    @GetMapping("/home")
    public String home(){
        return "home";
    }

    @PostMapping("/loginError")
    public String loginError(Model model, AuthenticationException exception){

        System.out.println("@@@@@@" + exception.getMessage());
        model.addAttribute("error","true");
        return "login";
    }
}
