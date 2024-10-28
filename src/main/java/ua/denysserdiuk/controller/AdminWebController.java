package ua.denysserdiuk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminWebController {

    @GetMapping("/admin")
    public String adminPage(){
        return "admin";
    }
}
