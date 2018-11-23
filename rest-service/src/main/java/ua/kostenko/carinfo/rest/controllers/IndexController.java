package ua.kostenko.carinfo.rest.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index() {
        return "html/index";
    }

    @GetMapping("/search")
    public String search(){
        return "html/search";
    }

    @GetMapping("/statistics")
    public String statistics(){
        return "html/statistics";
    }

    @GetMapping("/login")
    public String login(){
        return "html/login";
    }
}
