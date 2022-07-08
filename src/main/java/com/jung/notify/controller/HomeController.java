package com.jung.notify.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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
    public String loginError(Model model){
        model.addAttribute("error","true");
        return "login";
    }
}
